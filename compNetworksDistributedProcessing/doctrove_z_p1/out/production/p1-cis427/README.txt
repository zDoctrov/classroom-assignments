Zachary Doctrove's Project 1

Command Implemented (In order or implementation): ADD, LIST, DELETE, SHUTDOWN, QUIT, (HELP)

How to compile and run this program:
1) In order to read to and from the "record.txt" file, it is necessary that you change
the Server program's file path for "record.txt" in the code (Lines --- and --- need to be changed)

2) In order to begin running this program, you'll need to open it in your preferred IDE (such as IntelliJ or Netbeans). 
These instructions will refer to the steps taken in IntelliJ, my IDE of Choice
	a) Open the program package containing both "Server" and "Client" on IDE
	b) Select the "Run" tab at the top of the screen
	c) Mouse down to "Edit Configurations..."
	d) Click the "Client" tab under "Applications"
	e) Under the "Configurations" tab to the right, enter "127.0.0.1" in "Program arguments" to tell 
	the "Client" program that you are running the "Server" program on the same machine (localhost)
	f) Click "Apply" and exit out of the configurations menu


Known problems or bugs:
1) If you shut down the server through a integrated development environment,
like IntelliJ, instead of sending a "SHUTDOWN" command then the server will NOT save
the changes you made to the address book



Sample output (at the client side) of each command

ADD COMMAND:
ADD Zachary Doctrove 111-222-3333
200 OK
The new Record ID is 1001

/LIST COMMAND/:
LIST
200 OK
1001      Zachary Doctrove   111-222-3333 


/DELETE COMMAND/:
DELETE 1001
200 OK

(After Deleting the only record)
LIST
200 OK
The record is empty


/SHUTDOWN COMMAND/:
SHUTDOWN
200 OK
You Have Disconnected from the Server

Process finished with exit code 0


/QUIT COMMAND/:
QUIT
200 OK: QUIT
You Have Disconnected from the Server

Process finished with exit code 0


(/HELP COMMAND/):
Type "HELP" for list of commands	<-- (This sentence always appears when the client connects)
HELP
1."ADD" First_Name Last_Name Phone_Number
2."DELETE" ID_Number
3."LIST"   4."QUIT"   5."SHUTDOWN"

