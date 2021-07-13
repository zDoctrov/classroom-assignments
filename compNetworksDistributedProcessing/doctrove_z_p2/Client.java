/* 
 * Client.java
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

		//Used to signify a shutdown of both the input and output threads at the same time
		AtomicBoolean disconnectClient = new AtomicBoolean(false);

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
				//***The output stream is only sent, so that it can be shut down. output stream isn't used in serverResponses
				Thread serverResponses = new Thread(new ReceiveServerResponses2(clientSocket, disconnectClient, os));
				serverResponses.start();

				//Begin taking client commands
				userInput = stdInput.readLine();

				while (userInput != null)
				{
					os.println(userInput);

					userInput = stdInput.readLine();
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
	PrintStream os;
	AtomicBoolean disconnectClient;

	ReceiveServerResponses2(Socket clientSocket, AtomicBoolean disconnectClient, PrintStream os)
	{
		this.clientSocket = clientSocket;
		this.disconnectClient = disconnectClient;
		this.os = os;
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

				if(serverInput.equals("200 OK: QUIT") || serverInput.equals("210 the server is about to shutdown...")	)
				{
					//close the input/output streams when a client sends the "QUIT" / "SHUTDOWN" message
					// and the server sends back the "200 OK" confirmation message
					is.close();
					os.close();

					System.out.println("You Have Disconnected from the Server");

					disconnectClient.set(true);	//Set AtomicBoolean flag to true

					System.exit(0);
					break;
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("IOException:  " + e);
		}
	}//(The thread ends in the "Terminated" state once the run() method comes to an end)
}
