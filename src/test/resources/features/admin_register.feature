Feature: Admin Register Api
  As a user
  I want to Register Admin details in our system

  Scenario: Register Admin Info
    When "admin_details" of "valid" request to store in our system
    Then store the admin info in our system "sample"

  Scenario: Register InValid Admin Info
    When "admin_details" of "invalid" request to store in our system
    Then validation failed "admin1" and throws "Email Already Taken"


