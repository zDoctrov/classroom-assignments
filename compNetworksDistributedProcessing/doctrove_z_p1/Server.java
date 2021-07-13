/*
 * Server.java
 */

//YOU WILL ALSO NEED TO CREATE A "README" FILE TO GO WITH THIS PROJECT: IT WILL CONTAIN THE FOLLOWING INFORMATION
	//1.The commands you have implemented
	//2.The instructions about how to compile and run your program
	//3.Any known problems or bugs
	//4.The output at the client side of a sample run of all commands you implemented
	// (your "Makefile" doesn't need to be edited because you kept the file names of the sample code)

import java.io.*;
import java.net.*;
import java.util.*;			//Using linked lists to organize the 20 records

public class Server
{
    public static final int SERVER_PORT = 5432;

    //Servers have both a ServerSocket and sockets to reference the clients
    public static void main(String args[]){
		ServerSocket myServerice = null;
		String line;
		BufferedReader is;
		PrintStream os;
		Socket serviceSocket;

		//Use an ArrayList Data structure to temporarily store the contents of file and make changes to it
		ArrayList<String> allRecords = new ArrayList<>();

		//Retrieve the contents of the record database and save them in an internal server data structure (ArrayList)
		try
		{																			//***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
			BufferedReader fileInput = new BufferedReader(new FileReader("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p1\\record.txt"));
			String s;

			while((s = fileInput.readLine()	) != null)	//Retrieval
			{
				allRecords.add(s);						//Saved in internal data structure
			}
			fileInput.close();							//Always close your buffered readers
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Try to open a server socket
		try
		{
	    	myServerice = new ServerSocket(SERVER_PORT);
		}
		catch (IOException e)
		{
	    	System.out.println(e);
		}

		//While loop only activates once a client connects
		while (true)
		{
	    	try
	    	{
				serviceSocket = myServerice.accept();												//Create a socket object from the ServerSocket
				is = new BufferedReader (new InputStreamReader(serviceSocket.getInputStream()));	// to listen and accept connections.
				os = new PrintStream(serviceSocket.getOutputStream());								//Open input and output streams

				//As long as we receive data, echo that data back to the client.
				while ((line = is.readLine()) != null)
				{
		    		System.out.println(line);		//The server should print out all messages received from clients on the screen

		    		//COMMAND RECOGNITION STARTS HERE
					String[] splitCommand = line.trim().split(" ");

					if(splitCommand[0].equals("ADD") && splitCommand.length == 4)
					{
						if (allRecords.size() < 20)
						{
							Integer incrementID = 1001;					//Used to make sure ID's all have a value from 1001 to 1020

							String[] splitPhoneNumber = splitCommand[3].trim().split("-");

							//Make sure that the user's first/last name are under 8 characters
							// and their phone number is exactly 12 characters
							if(splitCommand[1].length() <= 8 && splitCommand[2].length() <= 8 &&
									splitCommand[3].length() == 12 &&

									//Make sure phone number contains ONLY numbers and dashes at specific locations
									splitCommand[3].charAt(3) == '-' && splitCommand[3].charAt(7) == '-' &&
									splitPhoneNumber[0].matches("[0-9]+") && splitPhoneNumber[1].matches("[0-9]+") &&
									splitPhoneNumber[2].matches("[0-9]+")  )
							{

								for(int i = 0; i < allRecords.size(); i++)	//Need to check if there are any spaces to fill before creating a new ID
								{
									//Retrieve the record ID of the given record (The first item stored in the string, before a whitespace is detected)
									String currentID = allRecords.get(i).substring(0, allRecords.get(i).indexOf(" "));

									if (currentID.equals(incrementID.toString()))
									{
										incrementID++;
									}
									else
									{
										allRecords.add(i, incrementID.toString() + " "
												+ splitCommand[1] + " " + splitCommand[2] + " " + splitCommand[3]);

										os.println("200 OK");
										os.println("The new Record ID is " + incrementID.toString());
										break;
									}
								}

								if((incrementID - 1001) == allRecords.size())
								{
									//If all ID's are currently numerically ordered, add to the bottom of the ARRAYLIST
									allRecords.add(incrementID.toString() + " "
											+ splitCommand[1] + " " + splitCommand[2] + " " + splitCommand[3]);

									os.println("200 OK");
									os.println("The new Record ID is " + incrementID.toString());
								}
							}
							else
							{
								os.println("301 message format error");
							}

						}
						else
						{
							os.println("302 Address Book Exceeds 20 records");
						}
					}

					//User enters "DELETE" command to remove a specific phone ID
					else if(splitCommand[0].equals("DELETE") && splitCommand.length == 2)
					{
						boolean removeSuccess = false;

						if(allRecords.isEmpty())
						{
							//403 Error will pop up if the for loop is never entered
						}
						else
						{
							for (int i = 0; i < allRecords.size(); i++) {
								//Retrieve the record ID of the given record (The first item stored in the string)
								String currentID = allRecords.get(i).substring(0, allRecords.get(i).indexOf(" "));

								if (currentID.equals(splitCommand[1])) {
									allRecords.remove(i);

									removeSuccess = true;
									break;                        //Leave for loop once the record is found and removed
								}
							}
						}

						if(removeSuccess == true)
						{
							os.println("200 OK");
						}
						else
						{
							os.println("403 The Record ID does not exist");
						}
					}

					//User enters "LIST" command to show all stored phone records
					else if(splitCommand[0].equals("LIST") && splitCommand.length == 1)
					{
						os.println("200 OK");

						if(allRecords.isEmpty())
						{
							os.println("The record is empty");
						}
						else {

							//List out all records in internal server structure (ArrayList)
							for (int i = 0; i < allRecords.size(); i++) {
								//Reformatting the output by first separating record items
								String tmp = allRecords.get(i).trim();
								String[] splitOutput = tmp.trim().split(" ");
								String fullName = splitOutput[1] + " " + splitOutput[2];

								//%Left-Justify Width String
								os.printf("%-10s", splitOutput[0]);			//Print ID
								os.printf("%-17s", fullName);				//Print Full Name
								os.printf("%14s \n", splitOutput[3]);		//Print Phone Number
							}
						}
					}

					//User enters "SHUTDOWN" command to save the changes made by clients and turn off the server
					else if(splitCommand[0].equals("SHUTDOWN") && splitCommand.length == 1)
					{
						os.println("200 OK");

						//Before shutting down, the server should save all of the client's changes back to the file
						try
						{									  //***CHANGE THIS FILE NAME TO SUITE YOUR FILE SYSTEM
							BufferedWriter fileOutput = new BufferedWriter(
									new FileWriter("C:\\Users\\zacdo\\IdeaProjects\\doctrove_z_p1\\record.txt"));

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

						//Close all open SOCKETS and FILES
						is.close();				//Close input stream
						os.close();				//Close output stream
						serviceSocket.close();	//Close client socket
						myServerice.close();	//Close server socket


						//Terminate the program
						return;
					}

					//User enters "QUIT" command to disconnect from the server, but the server keeps running
					else if(splitCommand[0].equals("QUIT") && splitCommand.length == 1)
					{
						os.println("200 OK: QUIT"); //The confirmation message is sent back to the client
													// and recognized as the message to shut down the client's program

						//Close all open SOCKETS and FILES, except the server socket
						is.close();				//Close input stream
						os.close();				//Close output stream
						serviceSocket.close();	//Close client socket
					}
					else if(splitCommand[0].equals("HELP") && splitCommand.length == 1)
					{
						//List out all the functional commands and their general formatting
						os.println("1.\"ADD\" First_Name Last_Name Phone_Number");
						os.println("2.\"DELETE\" ID_Number");
						os.println("3.\"LIST\"   4.\"QUIT\"   5.\"SHUTDOWN\"");
					}
					else	//ERROR MESSAGES HERE
					{
						if(!splitCommand[0].equals("ADD") 	&& !splitCommand[0].equals("DELETE")	&&
							!splitCommand[0].equals("LIST") && !splitCommand[0].equals("SHUTDOWN")	&&
							!splitCommand[0].equals("QUIT") && !splitCommand[0].equals("HELP")	)
						{
							os.println("300 invalid command");
						}
						else
						{
							os.println("301 message format error");
						}
					}
					//All successful commands should return a "200 OK" message
				}	//inner while loop ends here. once left, messages can no longer be received and sent to client

			//close ALL SOCKETS and FILES
			is.close();				//Close input stream
			os.close();				//Close output stream
			serviceSocket.close();	//Close client socket
			myServerice.close();	//Close server socket
	    	}//Try block ends
			catch (IOException e)
			{
			System.out.println(e);
	    	}
		} //outer while loop ends here. once left, the server will shutdown (Use of "break" statement)
    }//main() ends here
}