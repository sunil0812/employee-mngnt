Feature: Test Team Api
  AS a user
  i want to test team api


  Scenario: save team details
    Given team name "team 4" should be save in db
    Then validate the team details with 200

  Scenario: throw exception already team exists
    Given team name "team 1" should be save in db
    Then validate already exist with 404

  Scenario: get team name with manager id
    When team details with manager id in a list
    Then validate the list can be null or not null with 200

  Scenario: get team details
    When team details are available
    Then validate the team details list with 200