# Software-Engineering-Project
SportApp is a modular platform for campus/community sports venue booking and equipment rental. It offers secure login, time-slot management, conflict resolution, instant borrowing, payment via Octopus, booking updates/cancellations, tiered discounts, undo/redo notifications, and transaction logs—boosting efficiency and user experience.
CS3343 Sport Facility & Equipment Booking System (Group 5)

=====================================================================================================

Sport Facility & Equipment Booking System

=====================================================================================================

<<Description>>
Sport Facility & Equipment Booking System is a integrated sports management platform for user to book 
facility and equipment. it provides automate conflict detection, stock validation and streamlined user
journeys from login to payment for diverse consumer groups.

<Aims and goal>
We aims to digitalize the sport management and booking flow and simplified the booking procedure. We 
hope it can reduce the human error, enhance the efficiency of processing massive booking and 
provide seamless and fluent user experience to client.

<<Configuration>>
The version control of our project is conducted at GitHub. All programming language we used is only 
Java with version 21. The IDE we choose is VS code and Eclipse. System has been well tested under 
multi-layer and wide range of testing using the tools like Junit5 and Eclemma.

*remark: Minimum Java version requirement is Java Version 21.

=====================================================================================================

<<Installation>>

1. install the latest Java SE Development Kit (JDK) for Windows user
   https://www.oracle.com/java/technologies/downloads/
2. Download the our JAR file online
3. Double click the file

Alternative if not work:

4. Open command prompt(Windows) or terminal (macOS/Linux)
5. navigate to the directory where the JAR file is located using cd command 
   e.g. cd C:\Users\chan_tommy\Downloads
6. running the program using "java -jar Release.jar" command

=====================================================================================================

<<User Guideline>>
Total of six features can be found at system. Follow the guideline below to learn how to book facility and equipment.

1. Portal
At the beginning, you should locate at Portal page with 4 options: 

	1. Register:
		1. If you are a new user, please enter 2 in portal page for register a new account. 
		2. During the registration, you will need to set a username, password and security question with answer.

	*remind: password should at least 8 characters long and includes at least 1 digit, lowercase, uppercase and symbol. 

	please follow the text guide display in terminal.

	2. Reset Password
		1. please enter 3 in portal page for resetting password. 
		2. During the resetting, you will need to provide your username and answer security question correctly.

	please follow the text guide display in terminal.

	3. Login
		1. please enter 1 in portal page for login.
		2. You will need to enter your username and password.

	4. Exit

After login, you should now locate at Home page with 6 options


1. Sport Facility Booking:
	1. you can input corresponding function number to book the desired facility.
	2. You will need to input booking date using the format dd/MM/yyyy
	3. Available time slot will be displayed on that day
	4. You will need to input start time and end time of the booking.
	5. Confirmed message will be displayed if book successfully
	6. Input y if continue to book. Input n if no. 

*remind: Failed to book if input booking date before today or booking time not within the available time slot

2. Sport Equipment Booking:
	1. Choose the facility you have booked by enter number
	2. Choose book equipment or buy equipment by enter number
	3. Select the equipment you desired to book/buy by enter number
	4. Input the quantity of the equipment.

*remind: you should book facility before book equipment. Book equipment only is not allowed
*remind: Only the corresponding and available for book/purchase equipment will be displayed for the booked facility. 
e.g. Basketball court --> can borrow basketball instead of badminton racket

3. View Pending booking
	1. you can view your pending booking here with a table
	2. you have 3 options for revise booking details:
		1. update booking date and time before booking date
		2. update booking facility/Equipment before booking date
		3. cancel booking before booking time
	3. Once confirm to pay, please enter 4 to proceed payment.

*remind: updated booking date and time should not be the past.
*remind: Once cancelled, no refund will be returned

4. Payment
	1. Total price that includes membership discount will be displayed (Basic: no discount, Gold: 20% off, platinum: 40% off)
	2. Support the payment method: Octopus, payme
	3. message will be returned if success to pay
	4. Items in pending booking table will be clear. All booking status change to confirmed and store at confirmed booking table

5. View Confirmed booking  
	1. you can view your confirmed booking here with a table
	2. you have 3 options for revise booking details:
		1. update booking date and time before booking date
		2. update booking facility/Equipment before booking date
		3. cancel booking before booking time

*remind: updated booking date and time should not be the past.
*remind: Once cancelled, no refund will be returned

=====================================================================================================

<<Version History>>
16-Sep-2025 v1.0 initial version
22-Oct-2025 v2.0 UI features enhancement
16-Nov-2025 v3.0 Fixed bugs after testing
03-Dec-2025 v4.0 Reorganised structure for project report submission 
