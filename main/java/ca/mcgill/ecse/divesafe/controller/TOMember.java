/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;
import java.sql.Date;

// line 16 "../../../../../DiveSafeTransferObjects.ump"
public class TOMember
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOMember Attributes
  private String email;
  private String name;
  private String password;
  private String emergencyContact;
  private Date startDate;
  private Date endDate;
  private int numDays;
  private boolean guideRequired;
  private boolean hotelRequired;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOMember(String aEmail, String aName, String aPassword, String aEmergencyContact, Date aStartDate, Date aEndDate, int aNumDays, boolean aGuideRequired, boolean aHotelRequired)
  {
    email = aEmail;
    name = aName;
    password = aPassword;
    emergencyContact = aEmergencyContact;
    startDate = aStartDate;
    endDate = aEndDate;
    numDays = aNumDays;
    guideRequired = aGuideRequired;
    hotelRequired = aHotelRequired;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmergencyContact(String aEmergencyContact)
  {
    boolean wasSet = false;
    emergencyContact = aEmergencyContact;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumDays(int aNumDays)
  {
    boolean wasSet = false;
    numDays = aNumDays;
    wasSet = true;
    return wasSet;
  }

  public boolean setGuideRequired(boolean aGuideRequired)
  {
    boolean wasSet = false;
    guideRequired = aGuideRequired;
    wasSet = true;
    return wasSet;
  }

  public boolean setHotelRequired(boolean aHotelRequired)
  {
    boolean wasSet = false;
    hotelRequired = aHotelRequired;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getName()
  {
    return name;
  }

  public String getPassword()
  {
    return password;
  }

  public String getEmergencyContact()
  {
    return emergencyContact;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public int getNumDays()
  {
    return numDays;
  }

  public boolean getGuideRequired()
  {
    return guideRequired;
  }

  public boolean getHotelRequired()
  {
    return hotelRequired;
  }

  public void delete()
  {}


  @Override
  // line 28 "../../../../../DiveSafeTransferObjects.ump"
   public String toString(){
    return email;
  }

}