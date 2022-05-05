package ca.mcgill.ecse.divesafe.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Date;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.Member.MemberAssignment;
import ca.mcgill.ecse.divesafe.model.Member.MemberAssignmentAssigned;
import ca.mcgill.ecse.divesafe.model.Guide;

public class AssignmentController {
  private static DiveSafe diveSafe = DiveSafeApplication.getDiveSafe();
  private static DiveSafePersistence persist = new DiveSafePersistence();

  private AssignmentController() {}

  public static List<TOAssignment> getAssignments() {
    List<TOAssignment> assignments = new ArrayList<>();

    for (var assignment : diveSafe.getAssignments()) {
      var newTOAssignment = wrapAssignment(assignment);
      assignments.add(newTOAssignment);
    }

    return assignments;
  }

  /**
   * @author André A. Munduruca
   * A method which initiates the assignment procedure.
   * @return A String if an error occurred.
   */
  public static String initiateAssignment() {
    int currentGuideDays = 0;

    //try {
      diveSafe.setStartAssign(true);
      for (Guide guide : diveSafe.getGuides()) {
        //if (guide.getGuideAssignment() != Guide.GuideAssignment.Full) {
        if (guide.getDaysLeft()) {
          for (Member member : diveSafe.getMembers()) {
            if (member.isGuideRequired() && member.getMemberAssignment() == MemberAssignment.Unassigned) {
              if (diveSafe.getNumDays() - currentGuideDays >= member.getNumDays()) {
                createAssignment(currentGuideDays, currentGuideDays + member.getNumDays(), member, guide, diveSafe);
                currentGuideDays += member.getNumDays();
                guide.setDaysLeft(diveSafe.getNumDays() - currentGuideDays > 0);

              } else {
                // Go to the next guide
                break;
              }

            } else if (!member.isGuideRequired() && member.getMemberAssignment() == MemberAssignment.Unassigned) {
        	createAssignment(diveSafe.getNumDays(), diveSafe.getNumDays() + member.getNumDays(), member, null, diveSafe);
            }
          }
        }
      }

      DiveSafePersistence.save();

    /*} catch (Error e) {
      return e.getMessage();
    }**/

    return "";
  }

  /**
   * A method which simplifies the process of creating an assignment between a member and a guide. It also makes the method AssignmentController.initiateAssignment() simpler to read.
   * @param startDate the starting date of the member's trip
   * @param endDate the end date of the member's trip
   * @param member the member whose trip the assignment describes 
   * @param guide the guide who will accompany the member
   * @param diveSafe the divesafe system
   * @return an Assignment instance which describes the relation between these elements
   */
  private static Assignment createAssignment(int startDate, int endDate, Member member, Guide guide, DiveSafe diveSafe) {
    // Creates the model assignment
    var out = new Assignment(startDate + 1, endDate, "", member, diveSafe);
    if (guide != null) out.setGuide(guide);
    diveSafe.addAssignment(out);

    // Creates controller/statemachine assigment
    member.setStartDateOfTrip(addDates(diveSafe.getStartDate(), startDate));
    member.setEndDateOfTrip(addDates(diveSafe.getStartDate(), endDate));


    if (member.assign() && guide != null) {
      guide.assignMember();
    }

    return out; // should this method be in the model? Maybe? I treat it moreso as a helper method, but IDK to be honest. For me if it works it works lol
  }

  /** 
   * @author André A. Munduruca
   * A method which returns a date offset by a specified amount.
   * @param date a date which will be offset by a given amount
   * @param offset an amount to offset the previous date by
   * @return a new date which is equal to the date given plus the amount of days specified.
   */
  private static Date addDates(Date date, int offset) {
    // This is a HELPER METHOD it doesn't need to return a string or have any persistance
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, offset);
    return new Date(cal.getTime().getTime());
  }

  /**
   * @author James Kingsmill
   * @param userEmail is the email of the users whose trip is cancelled
   * @return error string
   */
  public static String cancelTrip(String userEmail) {
    
    // Persistence 
    try {
      diveSafe.cancelTrip(userEmail); // need method in model to cancel trip
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return ""; // empty string means operation was successful (no error)
    //return null;
  }

  /**
   * @author James Kingsmill
   * @param userEmail is the email of the user whose trip is finished
   * @return error string
   */
  public static String finishTrip(String userEmail) {
    
 // Persistence 
    try {
      diveSafe.finishTrip(userEmail); // need method in model to finish trip
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return ""; // empty string means operation was successful (no error)
    //return null;
  }

  /**
   * @author James Kingsmill
   * @param day is the day that the trips are starting on
   * @return error string
   */
  public static String startTripsForDay(int day) {
    
    // Persistence 
    try {
      diveSafe.startTripsForDay(day); // need method in model to start trips for day
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return ""; // empty string means operation was successful (no error)
    //return null;
  }
  
  /**
   * @author James Kingsmill
   * @return start date of season from model
   */
  public static Date getStartDate() {
    return diveSafe.getStartDate();
  }

  
  /**
   * @author André A. Munduruca
   * @param userEmail email of the user who's payment is being confirmed
   * @param authorizationCode authorization code of the payment handled externally
   * @return and string containing any possible errors that may have occured
   */
  public static String confirmPayment(String userEmail, String authorizationCode) {
    Member member = Member.getWithEmail(userEmail);
    
    //user with specified email does not exist
    if (member == null) {
    	return "Member with email address " + userEmail + " does not exist";
    }
    
    Assignment assignment = member.getAssignment();
    
    if(assignment == null) {
    	return "There is no assignment for this user";
    }
    
    if (member.getMemberAssignment() == MemberAssignment.Banned) {
	return "Cannot pay for the trip due to a ban";
    } else if (member.getMemberAssignment() == MemberAssignment.Cancelled) {
	return "Cannot pay for a trip which has been cancelled";
    } else if (member.getMemberAssignment() == MemberAssignment.Finished) {
	return "Cannot pay for a trip which has finished";
    } else if (member.getMemberAssignmentAssigned() == MemberAssignmentAssigned.Paid) {
	return "Trip has already been paid for";
    }
    
    //authorization code is invalid string
    if (authorizationCode.equals("")) {
    	return "Invalid authorization code";
    }
    
    //if the payment has already been confirmed
    if(!assignment.getAuthorizationCode().equals("")) {
    	return "Trip has already been paid for";
    }
    
    assignment.setAuthorizationCode(authorizationCode);
    member.pay();
    DiveSafePersistence.save();
    return authorizationCode;
	  
	
  }

  /** 
   * @author André A. Munduruca
   * A method which toggles a user from banned to unbanned and vice-versa.
   * @param userEmail the email of the user to ban/unban
   * @return a String containing any errors which may have occurred
   */
  public static String toggleBan(String userEmail) {
    //    try {
//      for (Member member : diveSafe.getMembers()) {
//        if (member.getEmail().equals(userEmail)) {
//          member.toggleBan();
//          DiveSafePersistence.save();
//        }
//      }
//    } catch (Error e) {
//      return e.getMessage();
//    }

    try {
      List<Member> members = diveSafe.getMembers();
      int i = 0, membersSize = members.size();
      while (i < membersSize) {
        Member member = members.get(i);
        if (member.getEmail().equals(userEmail)) {
          member.toggleBan();
          DiveSafePersistence.save();
        }
        i++;
      }
    } catch (Error e) {
      return e.getMessage();
    }



    return "";
  }
  
  /**
   * Helper method used to wrap the information in an <code>Assignment</code> instance in an
   * instance of <code>TOAssignment</code>.
   *
   * @author Harrison Wang Oct 19, 2021
   * @param assignment - The <code>Assignment</code> instance to transfer the information from.
   * @return A <code>TOAssignment</code> instance containing the information in the
   *         <code>Assignment</code> parameter.
   */
  private static TOAssignment wrapAssignment(Assignment assignment) {
    var member = assignment.getMember();

    // Initialize values for all necessary parameters.
    String memberEmail = member.getEmail();
    String memberName = member.getName();
    String guideEmail = assignment.hasGuide() ? assignment.getGuide().getEmail() : "";
    String guideName = assignment.hasGuide() ? assignment.getGuide().getName() : "";
    String hotelName = assignment.hasHotel() ? assignment.getHotel().getName() : "";

    int numDays = member.getNumDays();
    int startDay = assignment.getStartDay();
    int endDay = assignment.getEndDay();
    int totalCostForGuide = assignment.hasGuide() ? numDays * diveSafe.getPriceOfGuidePerDay() : 0;
    /*
     * Calculate the totalCostForEquipment.
     *
     * Sum the costs of all booked items depending on if they are an Equipment or EquipmentBundle
     * instance to get the equipmentCostPerDay for this assignment.
     *
     * Multiply equipmentCostPerDay by nrOfDays to get totalCostForEquipment.
     */
    int equipmentCostPerDay = 0;
    for (var bookedItem : member.getItemBookings()) {
      Item item = bookedItem.getItem();
      if (item instanceof Equipment equipment) {
        equipmentCostPerDay += equipment.getPricePerDay() * bookedItem.getQuantity();
      } else if (item instanceof EquipmentBundle bundle) {
        int bundleCost = 0;
        for (var bundledItem : bundle.getBundleItems()) {
          bundleCost += bundledItem.getEquipment().getPricePerDay() * bundledItem.getQuantity();
        }
        // Discount only applicable if assignment includes guide, so check for that before applying discount
        if (assignment.hasGuide()) {
          bundleCost = (int) (bundleCost * ((100.0 - bundle.getDiscount()) / 100.0));
        }
        equipmentCostPerDay += bundleCost * bookedItem.getQuantity();
      }
    }
    int totalCostForEquipment = equipmentCostPerDay * numDays;

    return new TOAssignment(memberEmail, memberName, guideEmail, guideName, hotelName, startDay,
        endDay, totalCostForGuide, totalCostForEquipment);
  }

}
