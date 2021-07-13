Zachary Doctrove's Project 2

The Word document "SAMPLE_PICTURES" contains the sample outputs of doctrove_z_p2

1) All Commands were Implemented by Zachary Doctrove (In order or implementation): ADD, LIST, DELETE, SHUTDOWN, QUIT, (HELP), LOGIN, LOGOUT, WHO, LOOK


2) In order to begin running this program, you'll need to open it in your preferred IDE (such as IntelliJ or Netbeans). 
These instructions will refer to the steps taken in IntelliJ, my IDE of Choice 

Setting up the program folder
	a) Download the zip file from canvas
	b) unzip it and place the unzipped folder into your IDE's project location
	c) right click your project and select "Open Folder as IntelliJ IDEA Community Edition Project"

Making the program run without prompting a command line parameter for the IP address
	a) Open the program package containing both "Server" and "Client" on IDE
	b) Select the "Run" tab at the top of the screen
	c) Mouse down to "Edit Configurations..."
	d) Click the "Client" tab under "Applications"
	e) Under the "Configurations" tab to the right, enter "127.0.0.1" in "Program arguments" to tell 
	the "Client" program that you are running the "Server" program on the same machine (localhost)
	f) Select "Allow parallel run" to make it so the client class can have multiple instances
	g) Click "Apply" and exit out of the configurations menu

In order to read to and from the "record.txt" AND the "userData" file, it is necessary that you change
the Server program's file path for "record.txt" and "userData" in the code. 

The four spots in particular that need to be changed are

For "record.txt"
	Change line 514 in the method "writeToRecord"
	Change line 492 in the method "readFromRecord"

For "userData.txt"
	Change line 562 in the method "writeToDataFile"
	Change line 540 in the method "readFromDataFile" 

(You can easily find these file reference with the comment "//***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM" above them)

3) Known problems or bugs:
If you shut down the server through a integrated development environment,
like IntelliJ, instead of sending a "SHUTDOWN" command then the server will NOT save
the changes you made to the address book AND the program will not logout accounts
that were logged in before.

(*Sample outputs in "SAMPLE_PICTURES")

