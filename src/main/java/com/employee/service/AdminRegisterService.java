package com.employee.service;

import com.employee.client.MSG91WhatsUpClient;
import com.employee.entity.request.AdminRegisterRequest;
import com.employee.entity.request.OtpRequest;
import com.employee.exception.EmployeeExceptions;
import com.employee.model.AdminRegister;
import com.employee.model.Company;
import com.employee.model.CompanyDetails;
import com.employee.model.ValidateDetails;
import com.employee.repository.AdminRegisterRepo;
import com.employee.repository.CompanyDetailsRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.employee.model.ValidateDetails.COMPNAME;
import static com.employee.model.ValidateDetails.EMAIL;
import static com.employee.model.ValidateDetails.NAME;
import static com.employee.model.ValidateDetails.PHONE;


@Service
public class AdminRegisterService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AdminRegisterRepo repo;

    @Autowired
    private CompanyDetailsRepo companyRepo;

    @Autowired
    private EmailService emailService;

    @Value("${msg91.whatsup.key}")
    private String authKey;

    @Autowired
    private MSG91WhatsUpClient whatsUpClient;
    private static final String TEMPLATE = "template";

    @Transactional
    public String register(AdminRegisterRequest adminRegister) {
        try {
            AdminRegister value = AdminRegister.builder()
                    .name(adminRegister.getName().toLowerCase())
                    .gender(adminRegister.getGender().toLowerCase())
                    .email(adminRegister.getEmail().toLowerCase())
                    .phone(adminRegister.getPhone())
                    .dob(adminRegister.getDob())
                    .address(mapper.writeValueAsString(adminRegister.getAddress()))
                    .bankDetails(mapper.writeValueAsString(adminRegister.getBankDetails()))
                    .companyDetails(mapper.writeValueAsString(adminRegister.getCompanyDetails()))
                    .build();
            repo.save(value);
            sendMailToAdminVerify(value.getEmail());
            saveCompanyDetails(adminRegister);
        } catch (Exception exception) {
            throw new EmployeeExceptions(exception.getMessage());
        }
        return "Admin Detail Registered";
    }

    private void sendMailToAdminVerify(String mail) throws IOException, MessagingException {
        ClassPathResource resource = new ClassPathResource("templates/verify-mail.html");
        String content = Files.readString(resource.getFile().toPath());
        String finalValue = content.replace("{{verification_link}}", "https://www.google.com/");
        emailService.sendEmail(mail, "Verify Admin", finalValue);
    }

    private void saveCompanyDetails(AdminRegisterRequest value) throws JsonProcessingException {
        Company comp = value.getCompanyDetails();
        CompanyDetails company = CompanyDetails.builder().name(comp.getName()).phone(comp.getPhone()).email(comp.getEmail()).address(mapper.writeValueAsString(comp.getAddress())).build();
        companyRepo.save(company);
    }

    public String validate(ValidateDetails details) {
        String validated = "Validated ";
        return switch (details.getField()) {
            case "phone" -> {
                if (repo.existsByPhone(details.getValue())) {
                    throw new EmployeeExceptions("Phone Number Already Taken");
                }
                yield PHONE + validated;
            }
            case "email" -> {
                boolean matched = isValidEmail(details.getValue());
                if (repo.existsByEmail(details.getValue()) || !matched) {
                    throw new EmployeeExceptions(matched ? "Email Already Taken" : "InValid Email");
                }
                yield EMAIL + validated;
            }
            case "name" -> {
                if (repo.existsByName(details.getValue())) {
                    throw new EmployeeExceptions("Name Already Taken");
                }
                yield NAME + validated;
            }
            case "companyName" -> {
                if (repo.existsByCompanyName(details.getValue())) {
                    throw new EmployeeExceptions("Company Name Already Taken");
                }
                yield COMPNAME + validated;
            }
            default -> "Given Field not for validation";
        };
    }

    private boolean isValidEmail(String mail) {
        return Pattern.compile("[a-zA-Z0-9]+@[a-z]{3,10}.[a-z]{3}").matcher(mail).matches();
    }

    public boolean sendOtpMsg91WhatsApp(String phone) {
        String otp = String.valueOf((int) (100000 + Math.random() * 900000));

        OtpRequest request = OtpRequest.builder().integratedNumber("919384168225").contentType(TEMPLATE).payload(toHashMap(otp, phone)).build();
        whatsUpClient.sendWhatsAppMessage(authKey, request);

        return true;
    }

    private Map<String, Object> toHashMap(String otp, String phone) {

        var toAndComponents = toComponentValue(otp, phone);

        Map<String, Object> template = Map.of(
                "name", "otp",
                "namespace", "d386422c_3319_4e4a_a6d7_ef5101dd108f",
                "language", Map.of("code", "en", "policy", "deterministic"),
                "to_and_components", List.of(toAndComponents)
        );

        return Map.of(
                "messaging_product", "whatsapp",
                "type", TEMPLATE,
                TEMPLATE, template
        );
    }

    private static Map<String, Object> toComponentValue(String otp, String phone) {
        Map<String, Object> body = Map.of(
                "type", "text",
                "value", otp
        );

        Map<String, Object> button = Map.of(
                "type", "text",
                "subtype", "url",
                "value", otp
        );

        Map<String, Object> components = Map.of(
                "body_1", body,
                "button_1", button
        );

        return Map.of(
                "to", List.of(phone),
                "components", components
        );
    }
}