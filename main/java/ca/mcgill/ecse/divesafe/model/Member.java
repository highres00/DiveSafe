/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.model;
import java.sql.Date;
import java.util.*;

// line 1 "../../../../../DiveSafeStateMachine.ump"
// line 134 "../../../../../DiveSafe.ump"
// line 272 "../../../../../DiveSafe.ump"
public class Member extends NamedUser
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Member Attributes
  private boolean hasPaid;
  private Date startDateOfTrip;
  private Date endDateOfTrip;
  private int refund;
  private int numDays;
  private boolean guideRequired;
  private boolean hotelRequired;

  //Member State Machines
  public enum MemberAssignment { Unassigned, Assigned, Started, Banned, Cancelled, Finished }
  public enum MemberAssignmentAssigned { Null, Unpaid, Paid }
  private MemberAssignment memberAssignment;
  private MemberAssignmentAssigned memberAssignmentAssigned;

  //Member Associations
  private DiveSafe diveSafe;
  private Assignment assignment;
  private List<ItemBooking> itemBookings;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Member(String aEmail, String aPassword, String aName, String aEmergencyContact, Date aStartDateOfTrip, Date aEndDateOfTrip, int aNumDays, boolean aGuideRequired, boolean aHotelRequired, DiveSafe aDiveSafe)
  {
    super(aEmail, aPassword, aName, aEmergencyContact);
    hasPaid = false;
    startDateOfTrip = aStartDateOfTrip;
    endDateOfTrip = aEndDateOfTrip;
    refund = 0;
    numDays = aNumDays;
    guideRequired = aGuideRequired;
    hotelRequired = aHotelRequired;
    boolean didAddDiveSafe = setDiveSafe(aDiveSafe);
    if (!didAddDiveSafe)
    {
      throw new RuntimeException("Unable to create member due to diveSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    itemBookings = new ArrayList<ItemBooking>();
    setMemberAssignmentAssigned(MemberAssignmentAssigned.Null);
    setMemberAssignment(MemberAssignment.Unassigned);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setHasPaid(boolean aHasPaid)
  {
    boolean wasSet = false;
    hasPaid = aHasPaid;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDateOfTrip(Date aStartDateOfTrip)
  {
    boolean wasSet = false;
    startDateOfTrip = aStartDateOfTrip;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDateOfTrip(Date aEndDateOfTrip)
  {
    boolean wasSet = false;
    endDateOfTrip = aEndDateOfTrip;
    wasSet = true;
    return wasSet;
  }

  public boolean setRefund(int aRefund)
  {
    boolean wasSet = false;
    refund = aRefund;
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

  public boolean getHasPaid()
  {
    return hasPaid;
  }

  public Date getStartDateOfTrip()
  {
    return startDateOfTrip;
  }

  public Date getEndDateOfTrip()
  {
    return endDateOfTrip;
  }

  public int getRefund()
  {
    return refund;
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
  /* Code from template attribute_IsBoolean */
  public boolean isGuideRequired()
  {
    return guideRequired;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isHotelRequired()
  {
    return hotelRequired;
  }

  public String getMemberAssignmentFullName()
  {
    String answer = memberAssignment.toString();
    if (memberAssignmentAssigned != MemberAssignmentAssigned.Null) { answer += "." + memberAssignmentAssigned.toString(); }
    return answer;
  }

  public MemberAssignment getMemberAssignment()
  {
    return memberAssignment;
  }

  public MemberAssignmentAssigned getMemberAssignmentAssigned()
  {
    return memberAssignmentAssigned;
  }

  public boolean assign()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignment aMemberAssignment = memberAssignment;
    switch (aMemberAssignment)
    {
      case Unassigned:
        if (diveSafe.getStartAssign())
        {
          setMemberAssignment(MemberAssignment.Assigned);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean toggleBan()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignment aMemberAssignment = memberAssignment;
    switch (aMemberAssignment)
    {
      case Unassigned:
        setMemberAssignment(MemberAssignment.Banned);
        wasEventProcessed = true;
        break;
      case Assigned:
        exitMemberAssignment();
        setMemberAssignment(MemberAssignment.Banned);
        wasEventProcessed = true;
        break;
      case Started:
        setMemberAssignment(MemberAssignment.Banned);
        wasEventProcessed = true;
        break;
      case Banned:
        setMemberAssignment(MemberAssignment.Unassigned);
        wasEventProcessed = true;
        break;
      case Cancelled:
        setMemberAssignment(MemberAssignment.Banned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean endTrip()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignment aMemberAssignment = memberAssignment;
    switch (aMemberAssignment)
    {
      case Started:
        setMemberAssignment(MemberAssignment.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancel()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignment aMemberAssignment = memberAssignment;
    MemberAssignmentAssigned aMemberAssignmentAssigned = memberAssignmentAssigned;
    switch (aMemberAssignment)
    {
      case Started:
        // line 34 "../../../../../DiveSafeStateMachine.ump"
        refund = 10;
        setMemberAssignment(MemberAssignment.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    switch (aMemberAssignmentAssigned)
    {
      case Unpaid:
        exitMemberAssignment();
        // line 22 "../../../../../DiveSafeStateMachine.ump"
        startDateOfTrip = null; endDateOfTrip = null;
        setMemberAssignment(MemberAssignment.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        exitMemberAssignment();
        // line 27 "../../../../../DiveSafeStateMachine.ump"
        startDateOfTrip = null; endDateOfTrip = null; refund = 50;
        setMemberAssignment(MemberAssignment.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pay()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignmentAssigned aMemberAssignmentAssigned = memberAssignmentAssigned;
    switch (aMemberAssignmentAssigned)
    {
      case Unpaid:
        exitMemberAssignmentAssigned();
        // line 20 "../../../../../DiveSafeStateMachine.ump"
        refund = 0; hasPaid = true;
        setMemberAssignmentAssigned(MemberAssignmentAssigned.Paid);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startTrip()
  {
    boolean wasEventProcessed = false;
    
    MemberAssignmentAssigned aMemberAssignmentAssigned = memberAssignmentAssigned;
    switch (aMemberAssignmentAssigned)
    {
      case Unpaid:
        exitMemberAssignment();
        setMemberAssignment(MemberAssignment.Banned);
        wasEventProcessed = true;
        break;
      case Paid:
        exitMemberAssignment();
        setMemberAssignment(MemberAssignment.Started);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitMemberAssignment()
  {
    switch(memberAssignment)
    {
      case Assigned:
        exitMemberAssignmentAssigned();
        break;
    }
  }

  private void setMemberAssignment(MemberAssignment aMemberAssignment)
  {
    memberAssignment = aMemberAssignment;

    // entry actions and do activities
    switch(memberAssignment)
    {
      case Assigned:
        if (memberAssignmentAssigned == MemberAssignmentAssigned.Null) { setMemberAssignmentAssigned(MemberAssignmentAssigned.Unpaid); }
        break;
      case Cancelled:
        // line 43 "../../../../../DiveSafeStateMachine.ump"
        hasPaid = false;
        break;
    }
  }

  private void exitMemberAssignmentAssigned()
  {
    switch(memberAssignmentAssigned)
    {
      case Unpaid:
        setMemberAssignmentAssigned(MemberAssignmentAssigned.Null);
        break;
      case Paid:
        setMemberAssignmentAssigned(MemberAssignmentAssigned.Null);
        break;
    }
  }

  private void setMemberAssignmentAssigned(MemberAssignmentAssigned aMemberAssignmentAssigned)
  {
    memberAssignmentAssigned = aMemberAssignmentAssigned;
    if (memberAssignment != MemberAssignment.Assigned && aMemberAssignmentAssigned != MemberAssignmentAssigned.Null) { setMemberAssignment(MemberAssignment.Assigned); }
  }
  /* Code from template association_GetOne */
  public DiveSafe getDiveSafe()
  {
    return diveSafe;
  }
  /* Code from template association_GetOne */
  public Assignment getAssignment()
  {
    return assignment;
  }

  public boolean hasAssignment()
  {
    boolean has = assignment != null;
    return has;
  }
  /* Code from template association_GetMany */
  public ItemBooking getItemBooking(int index)
  {
    ItemBooking aItemBooking = itemBookings.get(index);
    return aItemBooking;
  }

  public List<ItemBooking> getItemBookings()
  {
    List<ItemBooking> newItemBookings = Collections.unmodifiableList(itemBookings);
    return newItemBookings;
  }

  public int numberOfItemBookings()
  {
    int number = itemBookings.size();
    return number;
  }

  public boolean hasItemBookings()
  {
    boolean has = itemBookings.size() > 0;
    return has;
  }

  public int indexOfItemBooking(ItemBooking aItemBooking)
  {
    int index = itemBookings.indexOf(aItemBooking);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setDiveSafe(DiveSafe aDiveSafe)
  {
    boolean wasSet = false;
    if (aDiveSafe == null)
    {
      return wasSet;
    }

    DiveSafe existingDiveSafe = diveSafe;
    diveSafe = aDiveSafe;
    if (existingDiveSafe != null && !existingDiveSafe.equals(aDiveSafe))
    {
      existingDiveSafe.removeMember(this);
    }
    diveSafe.addMember(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setAssignment(Assignment aNewAssignment)
  {
    boolean wasSet = false;
    if (assignment != null && !assignment.equals(aNewAssignment) && equals(assignment.getMember()))
    {
      //Unable to setAssignment, as existing assignment would become an orphan
      return wasSet;
    }

    assignment = aNewAssignment;
    Member anOldMember = aNewAssignment != null ? aNewAssignment.getMember() : null;

    if (!this.equals(anOldMember))
    {
      if (anOldMember != null)
      {
        anOldMember.assignment = null;
      }
      if (assignment != null)
      {
        assignment.setMember(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfItemBookings()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ItemBooking addItemBooking(int aQuantity, DiveSafe aDiveSafe, Item aItem)
  {
    return new ItemBooking(aQuantity, aDiveSafe, this, aItem);
  }

  public boolean addItemBooking(ItemBooking aItemBooking)
  {
    boolean wasAdded = false;
    if (itemBookings.contains(aItemBooking)) { return false; }
    Member existingMember = aItemBooking.getMember();
    boolean isNewMember = existingMember != null && !this.equals(existingMember);
    if (isNewMember)
    {
      aItemBooking.setMember(this);
    }
    else
    {
      itemBookings.add(aItemBooking);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeItemBooking(ItemBooking aItemBooking)
  {
    boolean wasRemoved = false;
    //Unable to remove aItemBooking, as it must always have a member
    if (!this.equals(aItemBooking.getMember()))
    {
      itemBookings.remove(aItemBooking);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addItemBookingAt(ItemBooking aItemBooking, int index)
  {  
    boolean wasAdded = false;
    if(addItemBooking(aItemBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemBookings()) { index = numberOfItemBookings() - 1; }
      itemBookings.remove(aItemBooking);
      itemBookings.add(index, aItemBooking);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveItemBookingAt(ItemBooking aItemBooking, int index)
  {
    boolean wasAdded = false;
    if(itemBookings.contains(aItemBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfItemBookings()) { index = numberOfItemBookings() - 1; }
      itemBookings.remove(aItemBooking);
      itemBookings.add(index, aItemBooking);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addItemBookingAt(aItemBooking, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    DiveSafe placeholderDiveSafe = diveSafe;
    this.diveSafe = null;
    if(placeholderDiveSafe != null)
    {
      placeholderDiveSafe.removeMember(this);
    }
    Assignment existingAssignment = assignment;
    assignment = null;
    if (existingAssignment != null)
    {
      existingAssignment.delete();
    }
    for(int i=itemBookings.size(); i > 0; i--)
    {
      ItemBooking aItemBooking = itemBookings.get(i - 1);
      aItemBooking.delete();
    }
    super.delete();
  }

  // line 141 "../../../../../DiveSafe.ump"
   public static  Member getWithEmail(String email){
    if (User.getWithEmail(email) instanceof Member member) {
      return member;
    }
    return null;
  }

  // line 148 "../../../../../DiveSafe.ump"
   public static  boolean hasWithEmail(String email){
    return getWithEmail(email) != null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "hasPaid" + ":" + getHasPaid()+ "," +
            "refund" + ":" + getRefund()+ "," +
            "numDays" + ":" + getNumDays()+ "," +
            "guideRequired" + ":" + getGuideRequired()+ "," +
            "hotelRequired" + ":" + getHotelRequired()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDateOfTrip" + "=" + (getStartDateOfTrip() != null ? !getStartDateOfTrip().equals(this)  ? getStartDateOfTrip().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDateOfTrip" + "=" + (getEndDateOfTrip() != null ? !getEndDateOfTrip().equals(this)  ? getEndDateOfTrip().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "diveSafe = "+(getDiveSafe()!=null?Integer.toHexString(System.identityHashCode(getDiveSafe())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "assignment = "+(getAssignment()!=null?Integer.toHexString(System.identityHashCode(getAssignment())):"null");
  }
}