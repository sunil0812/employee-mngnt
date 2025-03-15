Feature: Test Attendance details api


  Scenario: save attendance details of employee
    When employee "CK0" attendance not already stored
    Given employee id "CK0" and status "wfo" of attendance
    Then validate the attendance of employee "CK0"

  Scenario: employee attendance already stored in DB on current day throw exception
    Given employee id "MK0" and status "wfo" of attendance
    Then validate the employee "MK0" attendance of already stored in db with 500