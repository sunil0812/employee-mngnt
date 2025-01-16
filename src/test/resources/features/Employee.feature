Feature: Employee Api
  As a user
  I want to have Employee details in our system
  So that admin can access employee data


  Scenario: Save Employee Details
    When  passing valid employee request for role 'MANAGER'
    Then  Store the employee details in DB

  Scenario: Save Employee Details
    When  passing valid employee request for role 'SOFTWARE_ENGINEER'
    Then  Store the employee details in DB
