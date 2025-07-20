package com.employee.client;


import com.employee.entity.request.OtpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "msg91Client",
        url = "https://control.msg91.com/api/v5/whatsapp",
        configuration = MSG91WhatsUpConfig.class
)
public interface MSG91WhatsUpClient {

    @PostMapping(value = "/whatsapp-outbound-message/bulk/", consumes = "application/json")
    ResponseEntity<String> sendWhatsAppMessage(
            @RequestHeader("authkey") String authKey,
            @RequestBody OtpRequest request
    );

}
