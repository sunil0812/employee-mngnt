Feature: Employee Api
  As a user
  I want to have Employee details in our system
  So that admin can access employee data


  Scenario: Save Employee Details Role MANAGER
    When  passing valid employee request for role 'MANAGER'
    Then  Store the employee details in DB

  Scenario: Save Employee Details Role SOFTWARE_ENGINEER
    When  passing valid employee request for role 'SOFTWARE_ENGINEER'
    Then  Store the employee details in DB

  Scenario: Get Employee Details Role MANAGER
    Given id 'PK0' should available in database
    When get details for employee id 'PK0'
    Then validate the id 'PK0' details with 'team 2' for role 'Manager'

  Scenario: Get Employee Details Role Software_Engineer
    Given check id 'JK0' already presented in database
    When get details for employee id 'JK0'
    Then validate the id 'JK0' details with 'team 2' for role 'Software_Engineer'

  Scenario: Remove Employee from a team and later update it
    Given check id 'JK0' already presented in database
    When employee id 'JK0' removed from a team with status 'remove'
    Then validate the id 'JK0' details with removed team

  Scenario: Update Employee Details after joining new team
    Given id 'JK0' should available in database
    When update details for employee id 'JK0' with new team
    Then validate the id 'JK0' details with email as 'john2742@gmail.com' and team


