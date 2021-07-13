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
import java.util.concurrent.atomic.AtomicBoolean;

public class Server
{
    public static final int SERVER_PORT = 5432;
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
												//Clients organized in Arraylist

    //Servers have both a ServerSocket and sockets to reference the clients
    public static void main(String args[]){
		createMultithreadCommunicationLoop();	//Relocated old server code from main() to the
												// "createMultithreadCommunicationLoop" method called here,
												// to make the transition from a single client to multiple clients
    }//main() ends here

	//This class accepts new clients and accommodates multiple at once by giving them their own threads
	public static void createMultithreadCommunicationLoop()
	{
		int clientNumber = 0;
		AtomicBoolean shutDownServer = new AtomicBoolean(false);

		try
		{
			// Try to open a server socket
			ServerSocket myServer = new ServerSocket(SERVER_PORT);

			ShutDown serverStop = new ShutDown(shutDownServer);
			serverStop.start();

			//Requires a new client to join and send a "SHUTDOWN" command for the server to properly shutdown
			while(shutDownServer.get() == false)
			{
				Socket serviceSocket = myServer.accept();
				clientNumber++;  //increment client num (the first client will always be labeled '1')

				//Find client's host name
				//and IP address
				InetAddress inetAddress = serviceSocket.getInetAddress();
				System.out.println("Connection from client " +
						clientNumber);
				System.out.println("\tHost name: " +
					inetAddress.getHostName());
				System.out.println("\tHost IP address: "+
						inetAddress.getHostAddress());

				//Set up a new ClientHandler instance
				ClientHandler newClient =
							new ClientHandler(serviceSocket, myServer, clientNumber, shutDownServer, clients);

				//Newly added clients are now added to the clients ArrayList
				clients.add(newClient);

				//create and start new thread for the connection
				// Java has it so you create the thread and then specify the thread's task via a class (that's runnable)
				Thread clientThread = new Thread(newClient);

				clientThread.start();   //Java sets up the thread and calls the run() method inside the new ClientHandler instance
			}//end while
		}//Try block ends
		catch (IOException e)
		{
				System.out.println(e);
		}
	}
}

class ShutDown extends Thread
{
	AtomicBoolean shutDownServer;

	ShutDown(AtomicBoolean shutDownServer)
	{
		this.shutDownServer = shutDownServer;
	}

	public void run()
	{
		while (true)
		{
			if(shutDownServer.get() == true)
			{

				System.exit(0);
			}
		}
	}
}