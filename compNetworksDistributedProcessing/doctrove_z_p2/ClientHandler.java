
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


//Runnable class allows us to create a task
//to be run on a thread
public class ClientHandler implements Runnable {
    private Socket serviceSocket;               //connected socket
    private ServerSocket serverSocket;          //server's socket
    private int clientNumber;
    private AtomicBoolean shutDownServer;

    private ArrayList<ClientHandler> clients;   //All client instances, allowing the
                                                // server to message all running clients

    BufferedReader inputFromClient;
    PrintStream outputToClient;

    //create an instance
    public ClientHandler(Socket serviceSocket, ServerSocket serverSocket, int clientNumber,
                         AtomicBoolean shutDownServer, ArrayList<ClientHandler> clients) throws IOException {
        this.serviceSocket = serviceSocket;     //The client socket we're using to communicate
        this.serverSocket = serverSocket;       //So that the client can issue a shutdown command
        this.clientNumber = clientNumber;       //Know what the server uses to identify this particular client
        this.shutDownServer = shutDownServer;

        this.clients = clients;

        inputFromClient = new                   //Open input stream
                BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
        outputToClient = new                    //Open output stream
                PrintStream(serviceSocket.getOutputStream());
    }//constructor ends
    
    
    //run() method is required by all
    //Runnable implementers
    @Override
    public void run() {
        //run the thread in here

        outToAll("Client #" + clientNumber + " has joined the server");

        //Use a local ArrayList Data structure to temporarily store the contents of file and make changes to it
        ArrayList<String> allRecords = new ArrayList<>();
        readFromRecord(allRecords);

        //Use a local ArrayList to keep track of other users that are currently logged in using data from a file
        ArrayList<String> userData = new ArrayList<>();
        readFromDataFile(userData);

        //All clients that join the server are not logged into a user at first (NULL = not logged in)
        String currentAccount = new String("NULL");

        try {
            String line;                        //Each line of user input is temporarily stored here

            //continuously serve the client, independently from the main() thread
            //As long as we receive data, echo that data back to the client.
            while ((line = inputFromClient.readLine()) != null)
            {
                System.out.println(line);       //The server should print out all messages received from clients on the screen

                //COMMAND RECOGNITION STARTS HERE
                String[] splitCommand = line.trim().split(" ");

                if(splitCommand[0].equals("ADD") && splitCommand.length == 4)
                {
                    if(currentAccount.equals("NULL"))
                    {
                        outputToClient.println("401 You are not currently logged in, login first");
                    }
                    else
                    {
                        //Use the most up to date version of the address book
                        readFromRecord(allRecords);

                        if (allRecords.size() < 20)
                        {
                            Integer incrementID = 1001;    //Used to make sure ID's all have a value from 1001 to 1020
                            boolean recordAdded = false;

                            String[] splitPhoneNumber = splitCommand[3].trim().split("-");

                            //Make sure that the user's first/last name are under 8 characters
                            // and their phone number is exactly 12 characters
                            if (splitCommand[1].length() <= 8 && splitCommand[2].length() <= 8 &&
                                    splitCommand[3].length() == 12 &&

                                    //Make sure phone number contains ONLY numbers and dashes at specific locations
                                    splitCommand[3].charAt(3) == '-' && splitCommand[3].charAt(7) == '-' &&
                                    splitPhoneNumber[0].matches("[0-9]+") && splitPhoneNumber[1].matches("[0-9]+") &&
                                    splitPhoneNumber[2].matches("[0-9]+"))
                            {

                                for (int i = 0; i < allRecords.size(); i++)    //Need to check if there are any spaces to fill before creating a new ID
                                {
                                    //Retrieve the record ID of the given record (The first item stored in the string, before a whitespace is detected)
                                    String currentID = allRecords.get(i).substring(0, allRecords.get(i).indexOf(" "));

                                    //Check to make sure an ID number wasn't previously deleted
                                    if (currentID.equals(incrementID.toString()))
                                    {
                                        incrementID++;
                                    }
                                    else
                                    {
                                        allRecords.add(i, incrementID.toString() + " "
                                                + splitCommand[1] + " " + splitCommand[2] + " " + splitCommand[3]);

                                        outputToClient.println("200 OK");
                                        outputToClient.println("The new Record ID is " + incrementID.toString());
                                        recordAdded = true;
                                        break;
                                    }
                                }

                                if ((incrementID - 1001) == allRecords.size())
                                {
                                    //If all ID's are currently numerically ordered, add to the bottom of the ARRAYLIST
                                    allRecords.add(incrementID.toString() + " "
                                            + splitCommand[1] + " " + splitCommand[2] + " " + splitCommand[3]);

                                    outputToClient.println("200 OK");
                                    outputToClient.println("The new Record ID is " + incrementID.toString());
                                    recordAdded = true;
                                }

                                //If a record was successfully added, update the record file
                                if (recordAdded == true)
                                {
                                    writeToRecord(allRecords);
                                }
                            }
                            else
                            {
                                outputToClient.println("301 message format error");
                            }
                        }
                        else
                        {
                            outputToClient.println("302 Address Book Exceeds 20 records");
                        }
                    }
                }

                //User enters "DELETE" command to remove a specific phone ID
                else if(splitCommand[0].equals("DELETE") && splitCommand.length == 2)
                {
                    if(currentAccount.equals("NULL"))
                    {
                        outputToClient.println("401 You are not currently logged in, login first");
                    }
                    else
                    {
                        //Use the most up to date version of the address book
                        readFromRecord(allRecords);

                        boolean removeSuccess = false;

                        if (allRecords.isEmpty())
                        {
                            //403 Error will pop up if the for loop is never entered
                        }
                        else
                        {
                            for (int i = 0; i < allRecords.size(); i++)
                            {
                                //Retrieve the record ID of the given record (The first item stored in the string)
                                String currentID = allRecords.get(i).substring(0, allRecords.get(i).indexOf(" "));

                                if (currentID.equals(splitCommand[1]))
                                {
                                    allRecords.remove(i);

                                    removeSuccess = true;

                                    writeToRecord(allRecords);      //If a record was deleted, update the record file
                                    break;                          //Leave for loop once the record is found and removed
                                }
                            }
                        }

                        if (removeSuccess == true)
                        {
                            outputToClient.println("200 OK");
                        }
                        else
                        {
                            outputToClient.println("403 The Record ID does not exist");
                        }
                    }
                }

                //User enters "LIST" command to show all stored phone records
                else if(splitCommand[0].equals("LIST") && splitCommand.length == 1)
                {
                    outputToClient.println("200 OK");

                    if(allRecords.isEmpty())
                    {
                        outputToClient.println("The record is empty");
                    }
                    else {
                        //Use the most up to date version of the address book
                        readFromRecord(allRecords);

                        //List out all records in internal server structure (ArrayList)
                        for (int i = 0; i < allRecords.size(); i++) {
                            //Reformatting the output by first separating record items
                            String tmp = allRecords.get(i).trim();
                            String[] splitOutput = tmp.trim().split(" ");
                            String fullName = splitOutput[1] + " " + splitOutput[2];

                            //%Left-Justify Width String
                            outputToClient.printf("%-10s", splitOutput[0]);			//Print ID
                            outputToClient.printf("%-17s", fullName);				//Print Full Name
                            outputToClient.printf("%14s \n", splitOutput[3]);		//Print Phone Number
                        }
                    }
                }

                //User enters "SHUTDOWN" command to save the changes made by clients and turn off the server
                else if(splitCommand[0].equals("SHUTDOWN") && splitCommand.length == 1)
                {
                    if(currentAccount.equals("root"))
                    {
                        //Before anything else, set all users to offline
                        for (int i = 0; i < userData.size(); i++)
                        {
                            String[] splitData = userData.get(i).trim().split(" ");

                            //Retrieve Logged in status from the ArrayList
                            String currentStatus = splitData[2];

                            if (currentStatus.equals("1"))
                            {
                                userData.set(i, splitData[0] + " " + splitData[1] + " 0 " + "NULL");
                            }
                        }
                        writeToDataFile(userData);              //Save the forced account logoff

                        outputToClient.println("200 OK");     //Added a second end line, for more space
                        outToAll("\n210 the server is about to shutdown...");

                        //Close all open SOCKETS and FILES
                        inputFromClient.close();                //Close input stream
                        outputToClient.close();                 //Close output stream

                        //Set flag to true, so that all threads knows to begin shutting down
                        shutDownServer.set(true);

                        //In order to immediately get the server to shutdown, a "poison pill" client
                        // connects to the server and then immediately terminates itself.
                        new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort()).close();

                        serviceSocket.close();                  //Close client socket
                        serverSocket.close();                   //Close server socket

                        //Terminate the program
                        return;
                    }
                    else
                    {
                        outputToClient.println("402 User not allowed to execute this command");
                    }
                }

                //User enters "QUIT" command to disconnect from the server, but the server keeps running
                else if(splitCommand[0].equals("QUIT") && splitCommand.length == 1)
                {
                    outputToClient.println("200 OK: QUIT"); //The confirmation message is sent back to the client
                    // and recognized by the client as the message to shut down the client's program

                    //Log client, if they're logged in still
                    currentAccount = logOut(userData, currentAccount);

                    //Close all open SOCKETS and FILES, except the server socket
                    inputFromClient.close();//Close input stream
                    outputToClient.close();	//Close output stream
                    serviceSocket.close();	//Close client socket
                }
                else if(splitCommand[0].equals("HELP") && splitCommand.length == 1)
                {
                    //List out all the functional commands and their general formatting
                    outputToClient.println("1.\"ADD\" First_Name Last_Name Phone_Number");
                    outputToClient.println("2.\"DELETE\" ID_Number");
                    outputToClient.println("3.\"LIST\"   4.\"QUIT\"   5.\"SHUTDOWN\"");
                    outputToClient.println("6.\"LOGIN\" UserID Password");
                    outputToClient.println("7.\"LOGOUT\"   8.\"WHO\"");
                    outputToClient.println("9.\"LOOK\" command_num compare_string");

                }
                //User enters "LOGIN" command to gain access to "ADD", "DELETE", or "SHUTDOWN" commands
                else if(splitCommand[0].equals("LOGIN") && splitCommand.length == 3)
                {
                    readFromDataFile(userData);

                    boolean userInUse = false;
                    boolean successfulLogin = false;

                    if(userData.isEmpty())
                    {
                        //410 Error will pop up if the for loop is never entered
                    }
                    else
                    {
                        //Need all four to be true to successfully login
                        // 1. UserID Exists, 2. Password Matches UserID,
                        // 3. User is not currently logged in already
                        // (0 = logged out, 1 = logged in already)
                        // 4. User is not logged in another account
                        for(int i = 0; i < userData.size(); i++)
                        {
                            String[] splitData = userData.get(i).trim().split(" ");

                            //Retrieve the UserID, Password, and Logged in status from the ArrayList
                            String currentUser = splitData[0];
                            String currentPassword = splitData[1];
                            String currentStatus = splitData[2];

                            if(currentUser.equals(splitCommand[1]) && currentPassword.equals(splitCommand[2])
                                    && currentStatus.equals("1"))
                            {
                                userInUse = true;
                                break;
                            }

                            // 1. Does the UserID Exist? // 2. Does the Password match? // 3. Is the user not in use?
                            if(currentUser.equals(splitCommand[1]) && currentPassword.equals(splitCommand[2])
                                && currentStatus.equals("0") && currentAccount.equals("NULL"))
                            {
                                successfulLogin = true;

                                //Get host IP address
                                InetAddress inetAddress = serviceSocket.getInetAddress();

                                //Change currentAccount from NULL
                                currentAccount = currentUser;

                                //Set user to online in DataFile
                                userData.set(i, currentUser + " " +currentPassword + " 1 " + inetAddress.getHostAddress());

                                writeToDataFile(userData);
                            }
                        }
                    }

                    if(successfulLogin == true)             //Successful Login
                    {
                        outputToClient.println("200 OK");
                    }
                    else if(!currentAccount.equals("NULL")) //Client is already logged in
                    {
                        outputToClient.println("412 You are Logged in as " + currentAccount);
                    }
                    else if(userInUse == true)              //Another client is logged in as this user
                    {
                        outputToClient.println("411 User Already Logged in");
                    }
                    else                                    //Account / Password failure
                    {
                        outputToClient.println("410 Wrong UserID or Password");
                    }

                }
                //User enters "LOGOUT" command to lose access to "ADD", "DELETE", or "SHUTDOWN" commands
                else if(splitCommand[0].equals("LOGOUT") && splitCommand.length == 1)
                {
                    //All the logic is stored in a method, so that the
                    // "QUIT" command can also log the user out
                    currentAccount = logOut(userData, currentAccount);
                }
                //User enters "WHO" to see all active users
                else if(splitCommand[0].equals("WHO") && splitCommand.length == 1)
                {
                    readFromDataFile(userData);
                    boolean noActiveUsers = true;

                    outputToClient.println("The list of the active users");

                    for (int i = 0; i < userData.size(); i++)
                    {
                        String[] splitData = userData.get(i).trim().split(" ");

                        //Retrieve UserID's and IP Addresses
                        String currentUser = splitData[0];
                        String currentIP = splitData[3];

                        if (!currentIP.equals("NULL"))
                        {
                            noActiveUsers = false;
                            outputToClient.printf(String.format("%-12s %s \n", currentUser, currentIP));
                        }
                    }

                    if(noActiveUsers == true)
                    {
                        outputToClient.println("----NONE----");
                    }
                }
                //User enters "LOOK" command to search the address book for any of the following:
                // 1 - First Name, 2 - Last Name, 3 - Phone Number
                else if(splitCommand[0].equals("LOOK") && splitCommand.length == 3)
                {
                    ArrayList<String> matchingRecords = new ArrayList<String>();
                    int numMatches = 0;

                    //Turn String command number into an integer
                    // to choose which part of the record to compare
                    int recordPart = Integer.parseInt(splitCommand[1]);

                    //Search First Names, Last Names, and Phone Numbers
                    if(splitCommand[1].equals("1") || splitCommand[1].equals("2") || splitCommand[1].equals("3"))
                    {
                        for(int i = 0; i < allRecords.size(); i++)
                        {
                            //splitRecord[0] = recordID, (...)[1] = fName, (...)[2] = lName, (...)[3] = phoneNumber
                            String[] splitRecord = allRecords.get(i).trim().split(" ");

                            if (splitRecord[recordPart].equals( splitCommand[2] ))
                            {
                                matchingRecords.add(allRecords.get(i));
                                numMatches++;
                            }
                        }

                        if(numMatches != 0)
                        {
                            outputToClient.println("Found " + numMatches + " Match(es)");
                            for(int i = 0; i < numMatches; i++)
                            {
                                outputToClient.println(matchingRecords.get(i));
                            }
                        }
                        else
                        {
                            outputToClient.println("404 Your search did not match any records");
                        }
                    }
                    else
                    {
                        outputToClient.println("303 not a valid \"LOOK\" command number"); //A "LOOK" command, but improper formatting
                    }
                }
                else	//ERROR MESSAGES HERE
                {
                    if(!splitCommand[0].equals("ADD") 	&& !splitCommand[0].equals("DELETE")	&&
                            !splitCommand[0].equals("LIST") && !splitCommand[0].equals("SHUTDOWN")	&&
                            !splitCommand[0].equals("QUIT") && !splitCommand[0].equals("HELP") &&
                            !splitCommand[0].equals("LOGIN") && !splitCommand[0].equals("LOGOUT") &&
                            !splitCommand[0].equals("WHO") && !splitCommand[0].equals("LOOK"))
                    {
                        outputToClient.println("300 invalid command");      //Not even a command
                    }
                    else
                    {
                        outputToClient.println("301 message format error"); //A command, but improper formatting
                    }
                }
                //All successful commands should return a "200 OK" message
            }	//inner while loop ends here. once left, messages can no longer be received and sent to client

            //close ALL SOCKETS and FILES
            inputFromClient.close();//Close input stream
            outputToClient.close();	//Close output stream
            serviceSocket.close();	//Close client socket
            serverSocket.close();	//Close server socket
        }//Try block ends
        catch (IOException e)
        {
            System.out.println(e);
        }
        
    }//end run

    //Whenever invoked, this method takes the file data and reads it into the "allRecords" ArrayList
    public synchronized void readFromRecord(ArrayList<String> allRecords)
    {
        //Clear old Arraylist, to thoroughly overwrite previous data
        allRecords.clear();

        //Retrieve the contents of the record database and save them in an internal server data structure (ArrayList)
        try
        {   //***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
            BufferedReader fileInput = new BufferedReader(
                    new FileReader("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p2\\record.txt"));
            String s;

            while((s = fileInput.readLine()	) != null)	//Retrieval
            {
                allRecords.add(s);                        //Saved in internal data structure
            }
            fileInput.close();							//Always close your buffered readers
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Whenever invoked, this method takes the Arraylist of records and writes that info. to the file
    public synchronized void writeToRecord(ArrayList<String> allRecords)
    {
        //the server saves all of the client's changes back to the file
        try
        {   //***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
            BufferedWriter fileOutput = new BufferedWriter(
                    new FileWriter("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p2\\record.txt"));

            for(int i = 0; i < allRecords.size(); i++)
            {
                fileOutput.write(allRecords.get(i) + "\n");
            }

            fileOutput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Whenever invoked, this method takes the file data and reads it into the "userData" ArrayList
    public synchronized void readFromDataFile(ArrayList<String> userData)
    {
        //Clear old Arraylist, to thoroughly overwrite previous data
        userData.clear();

        //Retrieve the contents of the user database (userData.txt) and
        // save them in an internal server data structure (ArrayList userData)
        try
        {   //***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
            BufferedReader fileInput = new BufferedReader(
                    new FileReader("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p2\\userData.txt"));
            String s;

            while((s = fileInput.readLine()	) != null)	//Retrieval
            {
                userData.add(s);                        //Saved in internal data structure
            }
            fileInput.close();							//Always close your buffered readers
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Whenever invoked, this method takes the Arraylist of records and writes that info. to the file
    public synchronized void writeToDataFile(ArrayList<String> userData)
    {
        //the server saves all of the client's changes back to the file
        try
        {   //***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
            BufferedWriter fileOutput = new BufferedWriter(
                    new FileWriter("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p2\\userData.txt"));

            for(int i = 0; i < userData.size(); i++)
            {
                fileOutput.write(userData.get(i) + "\n");
            }

            fileOutput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Method that uses a for loop to send a message to each running client.
    private void outToAll(String msg)
    {
        //For loop that goes through each client
        for (ClientHandler aClient : clients)
        {
            aClient.outputToClient.println(msg);
        }
    }

    //Used in both "LOGOUT" and "QUIT" commands.
    public synchronized String logOut(ArrayList<String> userData, String currentAccount)
    {
        readFromDataFile(userData);

        //Don't bother looking for the data in the ArrayList if its empty
        // or the user isn't even logged in
        if(userData.isEmpty() || currentAccount.equals("NULL"))
        {
            outputToClient.println("413 You are not logged in yet");
        }
        else
        {
            //As long as the user has already logged in before
            // they should be able to log out
            for(int i = 0; i < userData.size(); i++)
            {
                String[] splitData = userData.get(i).trim().split(" ");

                //Retrieve the UserID from the ArrayList (First item stored in string)
                String currentUser = splitData[0];
                String currentStatus = splitData[2];

                // 1. Select the proper account  // 2. Is the User in use? // 3. Is the client logged in as someone else?
                if(currentUser.equals(currentAccount) && currentStatus.equals("1")
                        && !currentAccount.equals("NULL"))
                {
                    //Change currentAccount to NULL
                    currentAccount = "NULL";

                    //Set user to offline in DataFile
                    userData.set(i, currentUser + " " + splitData[1]  + " 0 " + "NULL");

                    writeToDataFile(userData);

                    outputToClient.println("200 OK");
                    return currentAccount;
                }
            }//for loop ends
        }//else ends

        return "NULL";
    }

}//end ClientHandler