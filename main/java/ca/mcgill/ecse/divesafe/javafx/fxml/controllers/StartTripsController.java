package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import javafx.event.ActionEvent;
import static ca.mcgill.ecse.divesafe.javafx.fxml.controllers.ViewUtils.callController;

import javafx.scene.control.DatePicker;

public class StartTripsController {
	@FXML
	private Button startTripButton;
	@FXML
	private DatePicker selectedDate;

	/**
	 * @author James Kingsmill
	 * @param event is the action of starting trips
	 */
	// Event Listener on Button[#startTripButton].onAction
	@FXML
	public void startTripClicked(ActionEvent event) {
		LocalDate dateSelected = selectedDate.getValue();
		LocalDate seasonStartDate = convertDate(AssignmentController.getStartDate());
		int daysOfSeason = (int) ChronoUnit.DAYS.between(seasonStartDate, dateSelected) + 1;
		AssignmentController.startTripsForDay(daysOfSeason); 
		if (dateSelected == null); {
		  ViewUtils.showError("Select a valid date");
		}
		callController(AssignmentController.startTripsForDay(daysOfSeason));
	}
	
	
	/**
	 * @author James Kingsmill
	 * @param date is the date that will change to a local date
	 * @return
	 */
	public LocalDate convertDate(Date date) {
	    return new Date(date.getTime()).toLocalDate();
	}
}
