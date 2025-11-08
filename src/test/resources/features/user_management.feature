Feature: User Management in OrangeHRM
  As an admin user
  I want to update user information
  So that I can manage user accounts effectively

  Scenario: Update username for existing user
    Given Maria navigates to OrangeHRM login page
    When she logs in with username "Admin" and password "admin123"
    And she navigates to Admin User Management section
    And she searches for user "Jasmine.Morgan"
    And she updates the username to "Jasmine.Morgan.Test"
    Then she should see a success message
