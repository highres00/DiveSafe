package ca.mcgill.ecse.divesafe.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.Member.MemberAssignment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AssignmentFeatureStepDefinitions {
  
  private DiveSafe diveSafe;
  private static String error = "";
  
  /**
   * @author James Kingsmill
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following DiveSafe system exists:")
  public void the_following_dive_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {
    
    Map<String,String> diveSafeData = dataTable.asMaps().get(0);
    
    diveSafe = DiveSafeApplication.getDiveSafe();
    
    var startDate = Date.valueOf(diveSafeData.get(("startDate")));
    var priceOfGuidePerDay = Integer.valueOf(diveSafeData.get("priceOfGuidePerDay"));
    var numDays = Integer.valueOf(diveSafeData.get("numDays"));
    diveSafe.setStartDate(startDate);
    diveSafe.setPriceOfGuidePerDay(priceOfGuidePerDay);
    diveSafe.setNumDays(numDays);
  }

  /**
   * @author Faiyad Irfan Hares and James Kingmill
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following pieces of equipment exist in the system:")
  public void the_following_pieces_of_equipment_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    
    var rows = dataTable.asMaps();
    for (int i = 0, rowsSize = rows.size(); i < rowsSize; i++){
      var row = rows.get(i);
      var name = row.get("name");
      Integer weight;
      weight = Integer.valueOf(row.get("weight"));
      var pricePerDay = Integer.valueOf(row.get("pricePerDay"));
      diveSafe.addEquipment(name, weight, pricePerDay);
    }
  }

  /**
   * @author James Kingsmill and Faiyad Irfan Hares
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following equipment bundles exist in the system:")
  public void the_following_equipment_bundles_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    
    var rows = dataTable.asMaps();
    int j = 0;
    while (j < rows.size()) {
      Map<String, String> row = rows.get(j);
      String name = row.get("name");
      Integer discount = Integer.parseInt(row.get("discount"));
      System.out.print(discount);
      String[] itemNames = row.get("items").split(",");
      String[] itemQuantities = row.get("quantity").split(",");
      var bundle = diveSafe.addBundle(name, discount);
      int i = 0;
      while (i < itemNames.length) {
        var item = (Equipment) Item.getWithName(itemNames[i]);
        var quantity = Integer.parseInt(itemQuantities[i]);
        bundle.addBundleItem(quantity, diveSafe, item);
        i++;
      }
      j++;
    }
  }

  /**
   * @author James Kingsmill
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following guides exist in the system:")
  public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    
    List<Map<String,String>> rows = dataTable.asMaps();
    
    for (Map<String, String> row : rows) {
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      diveSafe.addGuide(email, password, name, emergencyContact);
    }
  }

  /**
   * @author Faiyad Irfan Hares
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following members exist in the system:")
  public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    
List<Map<String,String>>  rows = dataTable.asMaps();
    for(Map<String,String> row : rows){
      var email = row.get("email");
      var password = row.get("password");
      var name = row.get("name");
      var emergencyContact = row.get("emergencyContact");
      var numDays = Integer.valueOf(row.get("numDays"));
      var guideRequired = Boolean.valueOf(row.get("guideRequired"));
      var hotelRequired = Boolean.valueOf(row.get("hotelRequired"));
      Date startDate = null;
      Date endDate = null;
      var currentMember = diveSafe.addMember(email, password, name, emergencyContact, startDate, endDate, numDays, guideRequired, hotelRequired);
      String[] itemName = row.get("itemBookings").split(",");
      //String[] itemQuantities = row.get("quantity").split(",");

      //for(int i = 0 ; i < itemName.length ; i++) {
       // var Quantity = Integer.parseInt(itemQuantities[i]);
      //  var item = Item.getWithName(itemName[i]);
      //  ItemBooking booking = new ItemBooking(Quantity, diveSafe, currentMember, item);
     // }
    }
  }

  /**
   * @author James Kingsmill
   */
  @When("the administrator attempts to initiate the assignment process")
  public void the_administrator_attempts_to_initiate_the_assignment_process() {
    error += AssignmentController.initiateAssignment();
  }

  /**
   * @author James Kingsmill
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Then("the following assignments shall exist in the system:")
  public void the_following_assignments_shall_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    
    List<Map<String, String>> rows = dataTable.asMaps();
    
    for (Map<String,String> row : rows) {
      String memberEmail = row.get("memberEmail");
      Member member = Member.getWithEmail(memberEmail);
      
      String guideEmail = row.get("guideEmail");
      Guide guide;
      
      if (guideEmail.equals("")) {
	   guide = null;
      } else {
	  guide = Guide.getWithEmail(guideEmail);
      }
      
      int startDay = Integer.parseInt(row.get("startDay"));
      int endDay = Integer.parseInt(row.get("endDay"));
      
      assertTrue(AssignmentExists(member, guide, startDay, endDay, diveSafe));
      
    }
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member
   * @param status shall be marked
   */
  @Then("the assignment for {string} shall be marked as {string}")
  public void the_assignment_for_shall_be_marked_as(String memberEmail, String status) {
    var member = Member.getWithEmail(memberEmail);
    if (status.equals("Paid") || status.equals("Unpaid")) {
	assertEquals(status, member.getMemberAssignmentAssigned().toString());
    } else {
	assertEquals(status, member.getMemberAssignment().toString()); // pretty sure this is wrong
    }
    //     assertEquals(member, member.getAssignment()); 
    
//     var member = Member.getWithEmail(memberEmail);
//     var discount = Integer.parseInt(discountPercent);
//     assertEquals(member, status.getAssignment()); // not sure what to put here
  }

  /**
   * @author James Kingsmill
   * @param numberOfAssignments is the number of assignments in the system
   */
  @Then("the number of assignments in the system shall be {string}")
  public void the_number_of_assignments_in_the_system_shall_be(String numberOfAssignments) {
    List<Assignment> assignment = diveSafe.getAssignments();
    assertEquals(Integer.parseInt(numberOfAssignments), assignment.size());
  }

  /**
   * @author Faiyad Irfan Hares
   * @param errorString is the error
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String errorString) {
    assertTrue(errorString.contains(error));
  }

  /**
   * @author James Kingsmill
   * @param dataTable is the table taken from the Gherkin tests
   */
  @Given("the following assignments exist in the system:")
  public void the_following_assignments_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String,String>> rows = dataTable.asMaps();
    
    for (Map<String,String> row : rows) {
      String memberEmail = row.get("memberEmail");
      Member member = Member.getWithEmail(memberEmail);
      int startDay = Integer.parseInt(row.get("startDay"));
      int endDay = Integer.parseInt(row.get("endDay"));
      
      diveSafe.addAssignment(startDay, endDay, "", member);
      member.assign();
    }
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member being confirmed
   * @param authorizationCode
   */
  @When("the administrator attempts to confirm payment for {string} using authorization code {string}")
  public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(
      String memberEmail, String authorizationCode) {
    error += AssignmentController.confirmPayment(memberEmail, authorizationCode);
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member assigned
   * @param authorizationCode is authorization code for the member
   */
  @Then("the assignment for {string} shall record the authorization code {string}")
  public void the_assignment_for_shall_record_the_authorization_code(String memberEmail,
      String authorizationCode) {
    // Write code here that turns the phrase above into concrete actions
    //throw new io.cucumber.java.PendingException();
    var member = Member.getWithEmail(memberEmail);
    // something about assertEquals(code and something else)
  }

  /**
   * @author James Kingsmill
   * @param email is the supposed email of the member
   */
  @Then("the member account with the email {string} does not exist")
  public void the_member_account_with_the_email_does_not_exist(String email) {
    List<Member> members = diveSafe.getMembers();
    for (int i = 0; i < diveSafe.numberOfMembers(); i++) {
      assertNotEquals(email, members.get(i).getEmail());
    }
  }

  /**
   * @author James Kingsmill
   * @param numberOfMembers is the number of members in the system
   */
  @Then("there are {string} members in the system")
  public void there_are_members_in_the_system(String numberOfMembers) {
    List<Member> member = diveSafe.getMembers();
    assertEquals(Integer.parseInt(numberOfMembers), member.size());
  }

  /**
   * @author James Kingsmill
   * @param errorString is the error
   */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String errorString) {
    assertTrue(error.contains(errorString));
    //assertTrue(error.contains(errorString));
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member whose trip is being cancelled
   */
  @When("the administrator attempts to cancel the trip for {string}")
  public void the_administrator_attempts_to_cancel_the_trip_for(String memberEmail) {
    error += AssignmentController.cancelTrip(memberEmail);
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member who has paid for their trip
   */
  @Given("the member with {string} has paid for their trip")
  public void the_member_with_has_paid_for_their_trip(String memberEmail) {
    var member = Member.getWithEmail(memberEmail);
    member.setHasPaid(true);
    member.pay();
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member receiving the discount
   * @param discountPercent is the discount percent the member is receiving
   */
  @Then("the member with email address {string} shall receive a refund of {string} percent")
  public void the_member_with_email_address_shall_receive_a_refund_of_percent(String memberEmail,
      String discountPercent) {
    // Write code here that turns the phrase above into concrete actions
    //throw new io.cucumber.java.PendingException();
    var member = Member.getWithEmail(memberEmail);
    var discount = Integer.parseInt(discountPercent);

    if (member == null) assertTrue(false);

    assertEquals(discount, member.getRefund());
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member who has started their trip
   */
  @Given("the member with {string} has started their trip")
  public void the_member_with_has_started_their_trip(String memberEmail) {
    var member = Member.getWithEmail(memberEmail);
    member.assign();
    member.pay();
    member.startTrip();
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member whose trip is being finished
   */
  @When("the administrator attempts to finish the trip for the member with email {string}")
  public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(
      String memberEmail) {
    error += AssignmentController.finishTrip(memberEmail);
  }

  /** 
   * @author James Kingsmill
   * @param memberEmail is the email of the member who is banned
   */
  @Given("the member with {string} is banned")
  public void the_member_with_is_banned(String memberEmail) {
    var member = Member.getWithEmail(memberEmail);
    if (member.getMemberAssignment() != MemberAssignment.Banned) AssignmentController.toggleBan(memberEmail);
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member whose status is changed
   * @param bannedStatus is the status of the member
   */
  @Then("the member with email {string} shall be {string}")
  public void the_member_with_email_shall_be(String memberEmail, String bannedStatus) {
    var member = Member.getWithEmail(memberEmail);
    assertEquals(bannedStatus, member.getMemberAssignment().toString());
  }

  /**
   * @author James Kingsmill
   * @param dayNumber is the number of day that the strips are being started on
   */
  @When("the administrator attempts to start the trips for day {string}")
  public void the_administrator_attempts_to_start_the_trips_for_day(String dayNumber) {
    var day = Integer.parseInt(dayNumber);
    AssignmentController.startTripsForDay(day);
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member who has cancelled their trip
   */
  @Given("the member with {string} has cancelled their trip")
  public void the_member_with_has_cancelled_their_trip(String memberEmail) {
    //var member = Member.getWithEmail(memberEmail);
    AssignmentController.cancelTrip(memberEmail); 
  }

  /**
   * @author James Kingsmill
   * @param memberEmail is the email of the member who has finished thier trip
   */
  @Given("the member with {string} has finished their trip")
  public void the_member_with_has_finished_their_trip(String memberEmail) {
    var member = Member.getWithEmail(memberEmail);
    member.pay();
    member.startTrip();
    AssignmentController.finishTrip(memberEmail);
  }
  
/**
   * @author James Kingsmill
   * @param member is the member for the assignment
   * @param guide is the guide for the assignment
   * @param startDay is the start day of the assignment
   * @param endDay is the end day of the assignment
   * @param ds is the instance of the DiveSafe system
   * @return returns true if assignment exists, false if it does not
   */
  private boolean AssignmentExists(Member member, Guide guide, int startDay, int endDay, DiveSafe ds) {
    List<Assignment> assignments = diveSafe.getAssignments();
    for( var assignment : assignments){
      if (assignment.getGuide() == null && guide == null && assignment.getMember().equals(member) && assignment.getStartDay() == startDay && assignment.getEndDay() == endDay) {
	  return true;
      }
      if(assignment.getGuide() != null && assignment.getMember().equals(member) && assignment.getGuide().equals(guide) && assignment.getStartDay() == startDay && assignment.getEndDay() == endDay){
      //if(assignment.getStartDay() == startDay && assignment.getEndDay() == endDay){
        return true;
      }
    }
      return false;
  }
}
