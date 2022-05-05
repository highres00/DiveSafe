package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.callController;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.controller.TOMember;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.event.ActionEvent;

import javafx.scene.control.ChoiceBox;

public class FinishCancelTripsPageController {
	@FXML
	private Button finishTripButton;
	@FXML
	private Button cancelTripButton;
	@FXML
	private ChoiceBox<TOMember> memberChoiceBox;
	
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
      
   // let the application be aware of the refreshable node
      DiveSafeFxmlView.getInstance().registerRefreshEvent(memberChoiceBox);
      
    }

    /**
     * @author James Kingsmill
     * @param event is the action of finishing a trip
     */
	// Event Listener on Button[#finishTripButton].onAction
	@FXML
	public void finishTripClicked(ActionEvent event) {
	  TOMember member = memberChoiceBox.getValue();
      if (member == null) {
        ViewUtils.showError("Select a valid member");
      }
      
      callController(AssignmentController.finishTrip(member.getEmail()));
       
	}
	
	/**
	 * @author James
	 * @param event is the action of canceling a trip
	 */
	// Event Listener on Button[#cancelTripButton].onAction
	@FXML
	public void cancelTripClicked(ActionEvent event) {
	  TOMember member = memberChoiceBox.getValue();
      if (member == null) {
        ViewUtils.showError("Select a valid member");
      }
      
      callController(AssignmentController.cancelTrip(member.getEmail()));
      
      
	}
}
