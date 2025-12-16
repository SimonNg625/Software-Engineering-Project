package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;


import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.rules.ExpectedException;

import sportapp.FacilityBookingControl;
import sportapp.User;
import sportapp.UserCollection;
import sportapp.manager.ConfirmedBookManager;
import sportapp.manager.EquipmentBookManager;
import sportapp.manager.EquipmentManager;
import sportapp.manager.FacilityBookManager;
import sportapp.manager.SportFacilityManager;
import sportapp.model.EquipmentBookRecord;
import sportapp.model.BookingStatus;
import sportapp.model.FacilityBookRecord;
import sportapp.model.SportFacility;
import sportapp.ui.ViewConfirmedBookingUI;
import sportapp.util.DataInit;
import sportapp.Route;
import sportapp.model.Equipment;

public class TestViewConfirmedBookings {
	
	public enum TEST_PART {
		BASIC_FUNCTION,
		UPDATE_DATETIME,
		UPDATE_FACILITY,
		CANCEL_BOOKING	
	}
	
	public static boolean setUpOnce = false;
	//initialize
	public static Scanner scanner;
    public static ViewConfirmedBookingUI confirmedBookingUI;
	public static FacilityBookManager fbm;
	public static EquipmentBookManager ebm;
	public static ConfirmedBookManager cbm;
	public static UserCollection uc;
	public static FacilityBookRecord facilityRecord;
	public static EquipmentBookRecord equipmentRecord;
	public static ArrayList<EquipmentBookRecord> testEquipRecords;
	public static ArrayList<FacilityBookRecord> testFaciRecords;
	public static SportFacilityManager sfm;
	public static EquipmentManager em ;
    public static FacilityBookingControl fbc;
	public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


	@BeforeEach
	public void setUp() throws Exception {
		if (!setUpOnce) {
			System.out.println("Set up!");
			setUpOnce = true;
			DataInit.initDefaultData("false");
			uc = UserCollection.getInstance();

			uc.addUser("Tom","1234", null);
			uc.addUser("Jerry","1234", null);
			uc.addUser("Marry","1234", null);

			fbm = FacilityBookManager.getInstance();
			ebm = EquipmentBookManager.getInstance();
			cbm = ConfirmedBookManager.getInstance();
			sfm = SportFacilityManager.getInstance();
			em = EquipmentManager.getInstance();


			fbc = new FacilityBookingControl();
			confirmedBookingUI = new ViewConfirmedBookingUI();

			//FacilityBookRecord(SportFacility sportFacility, User user, int startHour, int endHour, LocalDate date)
			//sportFacility: "Room Bad101", "Room Bas201", "Room TT301"
			SportFacility sf1 = sfm.getSportFacilityByName("SF-001"); //look at DataInit for facility names

			System.out.println("SportFac name:" + sf1.getName());
			try {
				fbm.addBooking(new FacilityBookRecord(sf1, uc.findUserByName("Tom") , LocalDate.parse("22/10/2025",formatter), 9, 10, BookingStatus.CONFIRMED));
				fbm.addBooking(new FacilityBookRecord(sf1, uc.findUserByName("Tom") , LocalDate.now().plusDays(2), 9, 10, BookingStatus.CONFIRMED));
				fbm.addBooking(new FacilityBookRecord(sf1, uc.findUserByName("Tom"), LocalDate.now().plusDays(2), 12, 13, BookingStatus.CONFIRMED));
				fbm.addBooking(new FacilityBookRecord(sf1, uc.findUserByName("Tom"), LocalDate.now().plusDays(3), 9, 10, BookingStatus.CONFIRMED));
				
				fbm.addBooking(new FacilityBookRecord(sfm.getSportFacilityByName("SF-002"), uc.findUserByName("Tom"), LocalDate.now().plusDays(4), 9, 10, BookingStatus.CONFIRMED));
				fbm.addBooking(new FacilityBookRecord(sfm.getSportFacilityByName("SF-002"), uc.findUserByName("Tom"), LocalDate.now().plusDays(4), 20, 21, BookingStatus.CONFIRMED));
				fbm.addBooking(new FacilityBookRecord(sfm.getSportFacilityByName("SF-002"), uc.findUserByName("Tom"), LocalDate.now().plusDays(4), 20, 21, BookingStatus.CONFIRMED));
				
				fbm.addBooking(new FacilityBookRecord(sf1, uc.findUserByName("Jerry"), LocalDate.now().plusDays(3), 9, 10, BookingStatus.CONFIRMED));
				//EquipmentBookRecord(Equipment bookingEquipment, User bookingUser, LocalDate date,

				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.parse("20/01/2025", formatter), 9, 10, BookingStatus.CONFIRMED, 1));
				//sellable equipment below
				ebm.addBookRecord(new EquipmentBookRecord(em.getSellableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(2), 9, 10, BookingStatus.CONFIRMED, 4));
				ebm.addBookRecord(new EquipmentBookRecord(em.getSellableCollection().get(0) , uc.findUserByName("Jerry") , LocalDate.now().plusDays(2), 9, 10, BookingStatus.CONFIRMED, 4));
				
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(2), 9, 10, BookingStatus.CONFIRMED, 1));
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(2) , uc.findUserByName("Tom") , LocalDate.now().plusDays(2), 9, 10, BookingStatus.CONFIRMED, 1));
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(12), 9, 10, BookingStatus.CONFIRMED, 1));
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(14), 12, 13, BookingStatus.CONFIRMED, 1));
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(14), 17, 20, BookingStatus.CONFIRMED, 1));
				
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(2) , uc.findUserByName("Tom") , LocalDate.now().plusDays(25), 9, 10, BookingStatus.CONFIRMED, 1));
				ebm.addBookRecord(new EquipmentBookRecord(em.getBorrowableCollection().get(0) , uc.findUserByName("Tom") , LocalDate.now().plusDays(26), 9, 21, BookingStatus.CONFIRMED, 1));
				// cbm.setUpUserCollection(uc.findUserByName("Tom"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} 

    }
	

 	private Object invokeSelectFunctionWithInput(String input, User user, TEST_PART testPart ) throws Exception {
 		cbm.resetUserCollection(uc.findUserByName(user.getUsername()));
 		
		Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
		if (testPart.equals(TEST_PART.BASIC_FUNCTION)) {
			return confirmedBookingUI.display(scanner, user);
		} else {
			confirmedBookingUI.setUpTest(scanner, user);
			if (testPart.equals(TEST_PART.UPDATE_DATETIME)) {
				confirmedBookingUI.updateDateTime();
			} else if (testPart.equals(TEST_PART.UPDATE_FACILITY)) {
				confirmedBookingUI.updateSportFacility();
			} else {
				confirmedBookingUI.cancelBooking();
			}
		}
		
		return null;
		
	}


/* input position test:
 * 1st -> choose function
 * 1: updateDateTime; 2: updateSportFacility; 3: cancelBooking; 4: exist;
 * 
 *      updateDateTime:
 *      2nd -> Update Type: 1 or 2
 *      3rd -> Choose Equipment/Facilty Record (int)
 *      4th -> Choose Update Date
 *      5th -> Start Time
 *      6th -> End Time
 * 
 *      updateSportFacility:
 *      2nd -> Choose Facility Record (int)
 *      3rd -> Choose Facility Venue
 *      
 *      cancelBooking:
 *      2nd -> Choose Type: 1 or 2
 *      3rd -> Choose Equipment/Facility Record (int)
 *      4th -> Confirm Delete? (y/n)
 *      
 *      Exit: Exit Screen
 */

//	@Test
//	public void UpdateEquip_Normal() {
//		// Test displaying confirmed bookings for a user
//		confirmedBookingUI.display(new Scanner(System.in), uc.findUserByName("Tom"));
//		confirmedBookingUI.display(new Scanner(System.in), uc.findUserByName("Tom"));
//        
//	}
//
 	@Test
 	public void testEmpty() {
 		try {	//empty booking
 			assertEquals(Route.HOME, invokeSelectFunctionWithInput("1", uc.findUserByName("Marry"), TEST_PART.BASIC_FUNCTION) );
 		} catch (Exception e) {
 			
 		}
 	}
 	
    @Test
    public void invalidChooseFunc_1() {

        //invokeSelectFunctionWithInput("a\n1\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"));
        
    	try {
			assertEquals(Route.CONFIRMED_BOOKINGS, invokeSelectFunctionWithInput("a\n1\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.BASIC_FUNCTION));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
    }
    
//
    @Test
    public void invalidChooseFunc_2() {

    	try {
			assertEquals(Route.CONFIRMED_BOOKINGS, invokeSelectFunctionWithInput("-1\n1\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.BASIC_FUNCTION));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
    }

    @Test
    public void invalidChooseFunc_3() {

    	try {
			assertEquals(Route.CONFIRMED_BOOKINGS, invokeSelectFunctionWithInput("6\n1\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.BASIC_FUNCTION));
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
    }
    
    @Test
    public void UpdateEquip_InvalidChooseType_1() {

    	try {
    		Exception exception = assertThrows( Exception.class, () ->
          invokeSelectFunctionWithInput("3\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME));
          
          assertEquals("Invalid Input", exception.getMessage());
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
    }
 
    @Test
    public void UpdateEquip_InvalidChooseType_2() {

       try {
    	   Exception exception = assertThrows( Exception.class, () ->
		   invokeSelectFunctionWithInput("a\n1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
		    );
		    
		    assertEquals(null, exception.getMessage());
		} catch (Exception e) {
			//System.out.println("Error: " + e.getMessage());
		}
    }
//
    @Test
    public void UpdateEquip_InvalidChooseEquip_1() {
       try {
    	   Exception exception = assertThrows( Exception.class, () ->
    	   invokeSelectFunctionWithInput("1\na\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
    	   );
    
    	   assertEquals(null, exception.getMessage());
       } catch (Exception e) {
    	//   System.out.println("Error: " + e.getMessage());
       }
       
    }

    @Test
    public void UpdateEquip_InvalidChooseEquip_2() {

    	try {
     	   Exception exception = assertThrows( Exception.class, () ->
     	   invokeSelectFunctionWithInput("1\n-1\n22/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
     	   );
     
     	   assertEquals("No Chosen Record with Input Found!", exception.getMessage());
        } catch (Exception e) {
     	//   System.out.println("Error: " + e.getMessage());
        }
    }    

    @Test
    public void UpdateDateTime_InvalidDateInput_1() {

    	try {
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("2\n1\n1\n9\n10\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      
      	   
         } catch (Exception e) {
      	  //System.out.println("############Error HERE: " + e.getMessage());
         }    
    }

    @Test
    public void UpdateDateTime_InvalidDateInput_2() {

    	try {
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n4\n32/12/2026\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      
      	   assertEquals("Text '32/12/2026' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 32",
      	   		exception.getMessage());
         } catch (Exception e) {
      	   System.out.println("Error: " + e.getMessage());
         }
    }
//
    @Test
    public void UpdateDateTime_OutDatedDateInput() {

    	try {
       	   Exception exception = assertThrows( Exception.class, () ->
       	   invokeSelectFunctionWithInput("1\n3\n10/10/1995\n9\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
       	   );
       
       	   assertEquals("Input Invalid New Date update", exception.getMessage());
          } catch (Exception e) {
       	//System.out.println("############Error: " + e.getMessage());
          }    
    }

    @Test
    public void UpdateDateTime_InvalidTimeInput_1() {

    	try {
				LocalDate bookDate = LocalDate.now().plusDays(10);
				String bookDateString = bookDate.format(formatter);
    		
        	   Exception exception = assertThrows( Exception.class, () ->
        	   invokeSelectFunctionWithInput("1\n3\n" + bookDateString +"\n8\n10\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
        	   );
        
        	   assertEquals("This Equipment with this Date/Time can not be found.", exception.getMessage());
           } catch (Exception e) {
        	//System.out.println("############Error: " + e.getMessage());
           }    
    }


     @Test
    public void UpdateDateTime_InvalidTimeInput_2() {

    	 try {
 			LocalDate bookDate = LocalDate.now().plusDays(10);
 			String bookDateString = bookDate.format(formatter);
    		 
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n3\n" + bookDateString + "\n9\n22\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      
      	   assertEquals("This Equipment with this Date/Time can not be found.", exception.getMessage());
         } catch (Exception e) {
      	//System.out.println("############Error: " + e.getMessage());
         }    
    }

	@Test
    public void UpdateDateTime_InvalidTimeInput_3() {

    	 try {
 			LocalDate bookDate = LocalDate.now().plusDays(11);
 			String bookDateString = bookDate.format(formatter);
    		 
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n3\n" + bookDateString + "\n12\n12\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      
      	   assertEquals("Invalid Input", exception.getMessage());
         } catch (Exception e) {
      	//System.out.println("############Error: " + e.getMessage());
         }    
    }

	@Test
    public void UpdateDateTime_InvalidTimeInput_4() {

    	 try {
 			LocalDate bookDate = LocalDate.now().plusDays(11);
 			String bookDateString = bookDate.format(formatter);
    		 
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n3\n" + bookDateString + "\n12\n11\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      
      	   assertEquals("Invalid Input", exception.getMessage());
         } catch (Exception e) {
      	//System.out.println("############Error: " + e.getMessage());
         }    
    }
    
	@Test
    public void UpdateDateTime_DoneEquipment() { 
    	//not allowed
    	try {
    		//System.out.println("!!!!!!!!!!!!#####THIS IS EQUIPMENTSELLABLE: " + cbm.getEquipmentList().get(2-1).getBookingEquipment().get(0).getEquipmentName() + "Sellable: " + cbm.getEquipmentList().get(3-1).isSellable());
			LocalDate bookDate = LocalDate.now().plusDays(4);
			String bookDateString = bookDate.format(formatter);
    		
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n1\n" + bookDateString + "\n9\n10\ny", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      	   
      	   assertEquals("You can not update the booking.", exception.getMessage());
         } catch (Exception e) {
      	//System.out.println("############Error: " + e.getMessage());
         }        
    }
    
    @Test
    public void UpdateDateTime_EquipSellable() { 
    	//not allowed
    	try {
    		//System.out.println("!!!!!!!!!!!!#####THIS IS EQUIPMENTSELLABLE: " + cbm.getEquipmentList().get(2-1).getBookingEquipment().get(0).getEquipmentName() + "Sellable: " + cbm.getEquipmentList().get(3-1).isSellable());
			LocalDate bookDate = LocalDate.now().plusDays(2);
			String bookDateString = bookDate.format(formatter);
    		
      	   Exception exception = assertThrows( Exception.class, () ->
      	   invokeSelectFunctionWithInput("1\n2\n" + bookDateString + "\n9\n10\ny", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME)
      	   );
      	   
      	   assertEquals(true, cbm.getEquipmentList().get(2-1).isSellable());
      	   
      	   assertEquals("Cannot update Equipment which is sellable!", exception.getMessage());
         } catch (Exception e) {
      	//System.out.println("############Error: " + e.getMessage());
         }        
    }

     @Test
    public void UpdateDateTime_CheckOutput_1() {
    	 //Date 30/12/2025
    	 try {
			LocalDate bookDate = LocalDate.now().plusDays(2);
			String bookDateString = bookDate.format(formatter);
            //assertDoesNotThrow( ()-> invokeSelectFunctionWithInput("1\n1\n30/12/2025\n9\n10\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME));
    		 invokeSelectFunctionWithInput("1\n4\n"+ bookDateString +"\n9\n10\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME);
    		 assertEquals(bookDate, cbm.getEquipmentList().get(4-1).getDate());
            //cbm.displayConfirmedRecord(0);
    	 } catch (Exception e) {
    		 
    	 }
    }

	@Test
    public void UpdateDateTime_CheckOutput_2() {
    	 //Date 30/12/2025
    	 try {
            //assertDoesNotThrow( ()-> invokeSelectFunctionWithInput("1\n1\n30/12/2025\n9\n10\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME));
			LocalDate bookDate = LocalDate.now().plusDays(2);
			String bookDateString = bookDate.format(formatter);
    		 invokeSelectFunctionWithInput("2\n6\n" + bookDateString + "\n9\n10\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME);
    		 assertEquals(bookDate, cbm.getFacilityList().get(6-1).getDate());
    		 
    		 //invokeSelectFunctionWithInput("2\n6\n4/12/2025\n20\n21\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_DATETIME);
            //cbm.displayConfirmedRecord(0);
    	 } catch (Exception e) {
    		 
    	 }
    }

//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Test Update Facility
     
     @Test
    public void UpdateFacility_InvalidChooseFac_1() {
    	 try {
        	   Exception exception = assertThrows( Exception.class, () ->
        	   invokeSelectFunctionWithInput("22\n1\nn", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY)
        	   );
        
        	   assertEquals("No Chosen Record with Input Found!", exception.getMessage());
           } catch (Exception e) {
        	//System.out.println("############ Error: " + e.getMessage());
           }        
    }
     
     @Test
     public void UpdateFacility_InvalidChooseFac_2() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("-1\n1\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY)
         	   );
         
         	   assertEquals("No Chosen Record with Input Found!", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void UpdateFacility_InvalidChooseVenue_1() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   	invokeSelectFunctionWithInput("2\n11\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY)
         	   );
         
         	   assertEquals("Invalid Input", exception.getMessage());
            } catch (Exception e) {
            	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void UpdateFacility_InvalidChooseVenue_2() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("2\n-1\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY)
         	   );
         
         	   assertEquals("Invalid Input", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void UpdateFacility_outdatedRecord() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("1\n2\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY)
         	   );
         
         	   assertEquals("You can not update the booking.", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void UpdateFacility_ValidUpdate() {
     	 try {
     		//Exception exception = assertThrows( Exception.class, () ->
         	 invokeSelectFunctionWithInput("6\n3\n", uc.findUserByName("Tom"), TEST_PART.UPDATE_FACILITY);
         	 //);
     		
         	 System.out.print("!$@#Q$@#EFASRFRQ#$RFQWEFAS: "+cbm.getFacilityList().get(6-1).getSportFacility().getName());
         	 assertEquals(sfm.getSportFacilityByName("SF-003"), cbm.getFacilityList().get(6-1).getSportFacility());
         	   //get method.?
          } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
          }        
     }
     
     //~~~~~~~~~~~~~~~~~~~~~~~Cancel booking
     @Test
     public void Cancel_InvalidChooseType_1() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("-1\n1\nn", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("Invalid Input", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     
     @Test
     public void Cancel_InvalidChooseType_2() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("10\n1\nn", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("Invalid Input", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     
     @Test
     public void CancelEquip_InvalidChooseEquip_1() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("1\n10\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("No Chosen Record with Input Found!", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void CancelEquip_InvalidChooseEquip_2() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("1\n-1\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("No Chosen Record with Input Found!", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("########### Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void CancelEquip_Outdated() {
     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("1\n1\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("You can not cancel started booking.", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("########### Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void CancelFacility_Outdated() {

     	 try {
         	   Exception exception = assertThrows( Exception.class, () ->
         	   invokeSelectFunctionWithInput("2\n1\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
         	   );
         
         	   assertEquals("You can not cancel started booking.", exception.getMessage());
            } catch (Exception e) {
         	//System.out.println("########### Error: " + e.getMessage());
            }        
     }
     
     
     @Test
     public void CancelEquipCheck_CheckOutput_1() {
     	 try {
     		 boolean foundRecordCBM = false;
     		 boolean foundRecordEBM = false;
     		 	EquipmentBookRecord testSubject = cbm.getEquipmentRecord(6);
     		 	
         	   invokeSelectFunctionWithInput("1\n7\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING);
         	   
         	   
         	   for ( EquipmentBookRecord i : cbm.getEquipmentList()) {
         		   if (i.equals(testSubject))
         			   foundRecordCBM = true;
         	   }
         	   for (EquipmentBookRecord i : ebm.getBookRecords()) {
         		   if (i.equals(testSubject))
         			   foundRecordEBM = true;
         		   
         	   }
         	   assertEquals(false, foundRecordCBM);
         	   assertEquals(false, foundRecordEBM);
         	   
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void CancelEquipCheck_CheckSellable() {
     	 try {
     		 boolean foundRecordCBM = false;
     		 boolean foundRecordEBM = false;
     		 EquipmentBookRecord testSubject = cbm.getEquipmentRecord(2-1);
     		 //System.out.println("!!!#New Sellable equipment: " + testSubject.getBookingEquipment().get(0).getEquipmentName());
     		 	
         	Exception exception = assertThrows(Exception.class, () -> invokeSelectFunctionWithInput("1\n2\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING));
         	assertEquals("Cannot update Equipment which is sellable!", exception.getMessage());
         	   
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }        
     }
     
     @Test
     public void CancelFacilityCheck_CheckOutput_1() { 
     	 try {
     		 boolean foundRecordCBM = false;
     		 boolean foundRecordFBM = false;
     		 	FacilityBookRecord testSubject = cbm.getFacilityRecord(6-1);
         	   
     		 	assertDoesNotThrow( //for testing this, testing time also matter.
     		 		() -> invokeSelectFunctionWithInput("2\n6\ny", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
          	   );
         	   
         	   for ( FacilityBookRecord i : cbm.getFacilityList()) {
         		   if (i.equals(testSubject))
         			   foundRecordCBM = true;
         	   }
         	   for (FacilityBookRecord i : fbm.getBookingRecords()) {
         		   if (i.equals(testSubject))
         			   foundRecordFBM = true;
         	   }
         	   assertEquals(false, foundRecordCBM);
         	   assertEquals(false, foundRecordFBM);
         	   
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }   
     }
     
     @Test
     public void Cancel_Cancel() { 
     	 try {
     		 boolean foundRecordCBM = false;
     		 boolean foundRecordFBM = false;
     		 	FacilityBookRecord testSubject = cbm.getFacilityRecord(6-1);
         	   
     		 	assertDoesNotThrow( //for testing this, testing time also matter.
     		 		() -> invokeSelectFunctionWithInput("2\n6\nn", uc.findUserByName("Tom"), TEST_PART.CANCEL_BOOKING)
          	   );
         	   
         	   for ( FacilityBookRecord i : cbm.getFacilityList()) {
         		   if (i.equals(testSubject))
         			   foundRecordCBM = true;
         	   }
         	   for (FacilityBookRecord i : fbm.getBookingRecords()) {
         		   if (i.equals(testSubject))
         			   foundRecordFBM = true;
         	   }
         	   assertEquals(true, foundRecordCBM);
         	   assertEquals(true, foundRecordFBM);
         	   
            } catch (Exception e) {
         	//System.out.println("############ Error: " + e.getMessage());
            }   
     }
     
}