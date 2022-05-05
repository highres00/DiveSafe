package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.controller.TOMember;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.callController;


import javafx.scene.control.ChoiceBox;

public class UpdateMemberPageController {
    @FXML
    private Button updateMemberButton;
    @FXML
    private ChoiceBox<String> hotelChoiceBox;
    @FXML
    private ChoiceBox<String> guideChoiceBox;
    @FXML
    private ChoiceBox<TOMember> memberChoiceBox;
    @FXML
    private TextField memberPasswordTextField;
    @FXML
    private TextField memberEmergencyContactTextField;
    @FXML
    private TextField memberNumberOfDaysTextField;
    @FXML
    private TextField memberNameTextField;
    
    /**
     * @author James Kingsmill
     */
    public void initialize() {
      // the choice boxes listen to the refresh event
      memberChoiceBox.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT, e -> {
        memberChoiceBox.setItems(ViewUtils.getMembers());
        // reset the choice
        memberChoiceBox.setValue(null);
      });
      
      // set guide choice box
      ObservableList<String> guideChoiceList = FXCollections.observableArrayList("yes", "no");
      guideChoiceBox.setItems(guideChoiceList);
      
       // set hotel choice box
      ObservableList<String> hotelChoiceList = FXCollections.observableArrayList("yes", "no");
      hotelChoiceBox.setItems(hotelChoiceList);
      
      // let the application be aware of the refreshable node
      DiveSafeFxmlView.getInstance().registerRefreshEvent(memberChoiceBox);
      
    }
    
    
    /**
     * @author James Kingsmill
     * @param event is the event of updating a member
     */
    // Event Listener on Button[#updateMemberButton].onAction
    @FXML
    public void updateMemberClicked(ActionEvent event) {
      TOMember member = memberChoiceBox.getValue();
      if (member == null) {
        ViewUtils.showError("Select a valid member");
      }
      
      // get member email
      String email = member.toString();
      
      // get member name
      String name = memberNameTextField.getText();
      
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
      // check to make sure password and emergency contact are valid
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
      
      callController(MemberController.updateMember(email, password, name, emergencyContact, numberDays, guideRequired, hotelRequired, null, null));
      memberChoiceBox.setValue(null);
      memberNameTextField.setText("");
      memberPasswordTextField.setText("");
      memberEmergencyContactTextField.setText("");
      memberNumberOfDaysTextField.setText("");
      guideChoiceBox.setValue(null);
      hotelChoiceBox.setValue(null);
    }
    

    /**
     * @author James Kingsmill
     * @param event is the action of deleting a member
     */
	// Event Listener on Button[#deleteMemberButton].onAction
	@FXML
	public void deleteMemberClicked(ActionEvent event) {
		// TODO Autogenerated
	  TOMember member = memberChoiceBox.getValue();
	  if (member == null) {
	    ViewUtils.showError("Select a valid member");
	  } else {
	    //String email = member.toString();
	    callController(MemberController.deleteMember(member.getEmail()));
	  }
	  
	  
	  /*public void deleteDriverClicked(ActionEvent event) {
	    TODriver driver = driverChoiceBox.getValue();
	    if (driver == null) {
	      ViewUtils.showError("Please select a valid driver");
	    } else {
	      callController(BtmsController.deleteDriver(driver.getId()));
	    }
	  }*/
	}
}
