/* 
 * Client.java
 */

import java.io.*;
import java.net.*;

public class Client 
{
    public static final int SERVER_PORT = 5432;

    //Clients only have sockets
    public static void main(String[] args)
    {
		Socket clientSocket = null;		//Client Socket

		//Streams to and from the server
		PrintStream os = null;

		//Intermediary variables to contain a single line command
		String userInput;

		BufferedReader stdInput = null;	//"Standard Input" that uses the Intellij terminal

		//Check the number of command line parameters
		//	//*Go to Run--> Edit Configurations--> Add "127.0.0.1" to program arguments
			//This shows that the server is on the same machine that you're on
			//(If you don't include this, the client will always output "Usage..." and exit immediately)
		if (args.length < 1)
		{
		    System.out.println("Usage: client <Server IP Address>");
		    System.exit(1);
		}

		// Try to open a socket on SERVER_PORT
		// Try to open input and output streams
		try
		{
	    	clientSocket = new Socket(args[0], SERVER_PORT);		//arg[0] = the server's ip address
	    	os = new PrintStream(clientSocket.getOutputStream());
	    	stdInput = new BufferedReader(new InputStreamReader(System.in));
		}
		catch (UnknownHostException e)
		{
	    	System.err.println("Don't know about host: hostname");
		}
		catch (IOException e)
		{
	    	System.err.println("Couldn't get I/O for the connection to: hostname");
		}

		if (clientSocket != null && os != null)
		{
	    	try
	    	{
	    		System.out.println("Type \"HELP\" for list of commands");

	    		//Create a new thread for receiving server responses independent from client commands, and then start it
				//(Thread "serverResponses" begins in the "New" state)
				Thread serverResponses = new Thread(new ReceiveServerResponses2(clientSocket));
				serverResponses.start();

				//Begin taking client commands
				userInput = stdInput.readLine();

				while (userInput != null)
				{
					//The client program only ever ends once "serverResponses" thread ends, due to "200 OK: QUIT"
					if (!serverResponses.isAlive())
					{
						System.out.println("You Have Disconnected from the Server");
						break;
					}
					else
					{
						os.println(userInput);
					}

					//Need to give the client input thread some room to shutdown by restarting the loop once
					// as to allow one last "200 OK" message to be received by the receiving thread
					if (userInput.equals("QUIT") || userInput.equals("SHUTDOWN"))
					{
						continue;
					}
					else
					{
						//If a "QUIT" or "SHUTDOWN" command weren't sent, continue receiving messages
						userInput = stdInput.readLine();
					}
				}

				stdInput.close(); 		// close keyboard stream
				os.close();				// close the output stream
				clientSocket.close();	// close the socket
				return;					// Terminate Client program
	    	}
	    	catch (IOException e)
			{
				System.err.println("IOException:  " + e);
			}
		}
    }//Main ends
}//Client Class ends

//Class placed in separate thread to output server responses to client terminal
//(Thread enters the "Runnable" state)
class ReceiveServerResponses2 implements Runnable
{
	private Socket clientSocket;
	BufferedReader is = null;
	String serverInput = null;

	ReceiveServerResponses2(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}

	//The thread enters the "Running" state
	@Override
	public void run()
	{
		try
		{
			is = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

			while (	(serverInput = is.readLine()) != null	)
			{
				System.out.println(serverInput);		//Server responses are echoed here

				if(serverInput.equals("200 OK: QUIT")	)
				{
					break;
				}
			}

			// close the input stream when user inputs "QUIT" and the server sends the "200 OK" confirmation message
			is.close();
		}
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
		}
	}//(The thread ends in the "Terminated" state once the run() method comes to an end)
}
