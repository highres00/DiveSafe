package ca.mcgill.ecse.divesafe.javafx.fxml.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.TOAssignment;

public class AssignmentsPageController {
    @FXML
    private Button assignmentButton;
    
    @FXML
    private ListView assignmentsView;
    
    /**
     * @author Andre Munduruca
     * @param event is the action to start assigning
     */
    @FXML
    public void startAssigning(ActionEvent event) {
	String error = AssignmentController.initiateAssignment();
	
	if (!error.equals("")) {
	    ViewUtils.makePopupWindow("Error", error);
	}
	
	List<TOAssignment> listOfAssignments = AssignmentController.getAssignments();
	List<String> listOfStrings = new ArrayList<String>();
	
	
	for (int i = 0; i < listOfAssignments.size(); i++) {
	    var currentAssignment = listOfAssignments.get(i);
	    
	    listOfStrings.add("Member " + currentAssignment.getMemberName() + " - Guide " + currentAssignment.getGuideName() 
	    	+ " StartDate " + currentAssignment.getStartDay());
	}
	
	ObservableList<String> originalList = assignmentsView.getItems();
	originalList.removeAll();
	originalList.addAll(listOfStrings);
    }
    
}
