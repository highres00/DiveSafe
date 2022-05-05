package ca.mcgill.ecse.divesafe.controller;

import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Member;


public class GuideController {

  private static DiveSafe diveSafe = DiveSafeApplication.getDiveSafe();

  private GuideController() {}

  public static String registerGuide(String email, String password, String name,
      String emergencyContact) {
    String error = checkCommonConditions(password, name, emergencyContact);

    if (email.equals("admin@ad.atl")) {
      error = "Email cannot be admin@ad.atl";
    }

    if (email.contains(" ")) {
      error = "Email must not contain any spaces";
    }

    if (email.indexOf("@") <= 0 || email.indexOf("@") != email.lastIndexOf("@")
        || email.indexOf("@") >= email.lastIndexOf(".") - 1
        || email.lastIndexOf(".") >= email.length() - 1) {
      error = "Invalid email";
    }

    if (email == null || email.equals("")) {
      error = "Email cannot be empty";
    }

    if (Guide.hasWithEmail(email)) {
      error = "Email already linked to a guide account";
    }
    if (Member.hasWithEmail(email)) {
      error = "Email already linked to a member account";
    }

    if (!error.isBlank()) {
      return error.trim();
    }

    diveSafe.addGuide(email, password, name, emergencyContact);

    return "";
  }

  /**
   * (GuideController.updateGuide; implemented by André A. Munduruca)
   * A method which updates the password, name and emergency contact attributes of a guide in the DiveSafe system.
   * @param email corresponds to the email of the guide whose attributes are to be changed.
   * @param newPassword corresponds to a new password for the guide account.
   * @param newName corresponds to a new name to refer to the guide as.
   * @param newEmergencyContact corresponds to a new contact method which can reach someone close to the guide in case of emergency.
   * @return a string which is empty in the case of normal execution, and contains an error message otherwise.
   */
  public static String updateGuide(String email, String newPassword, String newName,
      String newEmergencyContact) {
    String error = ""; // initialize error string

    error = checkCommonConditions(newPassword, newName, newEmergencyContact);

    if (!Guide.hasWithEmail(email)) {
      error = "The Guide does not exist in the system";
    }
    if (!error.isBlank()) {
      return error;
    }

    var guideToUpdate = Guide.getWithEmail(email);
    guideToUpdate.setPassword(newPassword);
    guideToUpdate.setName(newName);
    guideToUpdate.setEmergencyContact(newEmergencyContact);

    return "";
  }

  public static String deleteGuide(String email) {
    Guide guide = Guide.getWithEmail(email);
    if (guide != null) {
      guide.delete();
    }
    return "";
  }

  private static String checkCommonConditions(String password, String name,
      String emergencyContact) {
    String error = "";
    if (password == null || password.equals("")) {
      error = "Password cannot be empty";
    }
    if (name == null || name.equals("")) {
      error = "Name cannot be empty";
    }
    if (emergencyContact == null || emergencyContact.equals("")) {
      error = "Emergency contact cannot be empty";
    }

    return error;
  }

}
