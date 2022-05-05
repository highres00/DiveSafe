package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;


import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.successful;
import java.sql.Date;
import java.util.List;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.ChoiceBox;

/**
 * 
 * @author IDK WHO DID THIS
 *
 */
public class ConfirmPaymentPageController {
	@FXML
	private TextField userEmailTextField;
	
	@FXML
	private TextField authorizationCodeTextField;
	
	@FXML
	private Button confirmPaymentButton;
	
	/**
	 * @author Andre Munduruca
	 * @param Event is the action to confirm payment
	 */
	@FXML
	public void confirmPaymentClicked(ActionEvent Event) {
		var userEmail = (String) userEmailTextField.getText();
		var authorizationCode = (String) authorizationCodeTextField.getText();
		var error = AssignmentController.confirmPayment(userEmail, authorizationCode);
		if(authorizationCode.equals(error)) {
			userEmailTextField.setText("");
			authorizationCodeTextField.setText("");
			ViewUtils.makePopupWindow("Payment Confirmed","Payment confirmed \n for user: " + userEmail);
		}else {
			ViewUtils.makePopupWindow("Error", error);
		}
	}
}
