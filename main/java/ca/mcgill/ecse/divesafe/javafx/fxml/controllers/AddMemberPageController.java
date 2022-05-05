package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;
import java.sql.Date;
import java.util.List;
import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.ChoiceBox;

public class AddMemberPageController {
	@FXML
	private TextField memberNameTextField;
	@FXML
	private TextField memberEmailAddressTextField;
	@FXML
	private TextField memberPasswordTextField;
	@FXML
	private TextField memberEmergencyContactTextField;
	@FXML
	private TextField memberNumberOfDaysTextField;
	@FXML
	private ChoiceBox<String> guideChoiceBox;
	@FXML
	private ChoiceBox<String> hotelChoiceBox;
	@FXML
	private Button addMemberButton;
	

	/**
	 * @author James Kingsmill
	 */
	@FXML
	public void initialize() {
	  // set guide choice box
	  ObservableList<String> guideChoiceList = FXCollections.observableArrayList("yes", "no");
	  guideChoiceBox.setItems(guideChoiceList);
	  
	  // set hotel choice box
      ObservableList<String> hotelChoiceList = FXCollections.observableArrayList("yes", "no");
      hotelChoiceBox.setItems(hotelChoiceList);
	  
	}
	
	/**
	 * @author James Kingsmill
	 * @param event is the action of adding a member
	 */
	// Event Listener on Button[#addMemberButton].onAction
	@FXML
	public void addMemberClicked(ActionEvent event) {
	  var error = "";
	  
	  // get member name
	  String name = memberNameTextField.getText();
	  
	  // get member email address
	  String email = memberEmailAddressTextField.getText();
	  
	  // get member password
	  String password = memberPasswordTextField.getText();
	  
	  // get emergency contact
	  String emergencyContact = memberEmergencyContactTextField.getText();
	  
	  
	  // get number of days
	  int numberDays = 0;
	  try {
        numberDays = Integer.parseInt(memberNumberOfDaysTextField.getText());
      } catch (NumberFormatException e) {
        ViewUtils.showError("Please input a valid number of days");
      }
	  
	  // get guide choice
	  String guideChoice = guideChoiceBox.getValue();
	  boolean guideRequired = false;
	  if (guideChoice == null) {
        ViewUtils.showError("Please select if a guide is required");
      }
	  else if (guideChoice.equals("yes")) {
	    guideRequired = true;
	  } 
	  
	  // get hotel choice
	  String hotelChoice = hotelChoiceBox.getValue();
	  boolean hotelRequired = false; // initialize variable
	  if (hotelChoice == null) {
	    ViewUtils.showError("Please select if a hotel is required");
	  }
	  else if (hotelChoice.equals("yes")) {
        hotelRequired = true;
      } 
      // check to make sure name, email, password and emergency contact are valid
      if (name == null || name.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid name");
      }
      if (email == null || email.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid email"); 
      } 
      if (password == null || password.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid password");
      } 
      if (emergencyContact == null || emergencyContact.trim().isEmpty()) {
        ViewUtils.showError("Please input a valid phone number");
      }
      // make sure phone number is all numbers
      try {
        int phoneNumber = Integer.parseInt(memberEmergencyContactTextField.getText());
      } catch (NumberFormatException e) {
        ViewUtils.showError("Please input a valid phone number");
      }
      
      /*if (!error.isEmpty()) {
        ViewUtils.showError(error);
      }*/
      
      if (successful(MemberController.registerMember(email, password, name, emergencyContact, null, null, numberDays, guideRequired, hotelRequired, null, null))) {
        memberNameTextField.setText("");
        memberEmailAddressTextField.setText("");
        memberPasswordTextField.setText("");
        memberEmergencyContactTextField.setText("");
        memberNumberOfDaysTextField.setText("");
        guideChoiceBox.setValue(null);
        hotelChoiceBox.setValue(null);
      }
      
	}
}
