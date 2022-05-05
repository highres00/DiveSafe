/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.divesafe.application;

import java.sql.Date;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.application.Application;

public class DiveSafeApplication {
  private static DiveSafe diveSafe;
  
  public static final boolean DARK_MODE = true;

  public static void main(String[] args) {
    // Launch UI here (For Iteration 4)
    Application.launch(DiveSafeFxmlView.class, args);
        
  }
  
  public static DiveSafe getDiveSafe() {
      return new DiveSafe(new Date(System.currentTimeMillis()), 10, 50);
  }
}