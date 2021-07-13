package network_slides;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
Three threads are running on this file:

Thread #1: The application thread being used to run the GUI
Thread #2: Used to allow the server to continuously search for new clients    (Collective clients)
Thread #3: Used to give each client a input/output stream to the server       (Individual clients)
 */

public class MultiThreadServer extends Application {
    // Text area for displaying contents
    private TextArea ta = new TextArea();

    // Number a client
    private int clientNo = 0;

    //Server organizes connected clients in a ArrayList
    private static ArrayList<HandleAClient> clients = new ArrayList<>();

    //ExecutorService can execute Runnable tasks
    private static ExecutorService pool =  Executors.newFixedThreadPool(4);    //Thread pool of 4 clients at a time
                                                            /** The Executor interface only has the single "execute" method */

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("MultiThreadServer");         // Set the stage title
        primaryStage.setScene(scene);                       // Place the scene in the stage
        primaryStage.show();                                // Display the stage

        ta.setEditable(false);

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);

                //Need to use "runLater" to safely update application thread with data
                Platform.runLater( () -> {
                    ta.appendText("MultiThreadServer started at " + new Date() + '\n');
                });                                         /** The original file from the book didn't include
                                                                "Platform.runLater", causing unneeded stress */

                while (true) {                              /** The loop will be stuck at "serverSocket.accept" until a client connects */
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();  /** The "socket" variable is client's lifeline to the server.
                                                                Its the same socket from the "Client" file ported to the server file */
                    // Increment clientNo
                    clientNo++;                             /** Clients numbered in the order in which they connect */

                    // Create, pool, and start a new thread for the new client connecting
                    HandleAClient clientThread = new HandleAClient(socket, clients, clientNo);
                    clients.add(clientThread);
                    pool.execute(clientThread);
                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket socket;                              //A connected socket
        private ArrayList<HandleAClient> clients;           //All client instances
        private int clientNumber;                           //Number of client

        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;

        /** Constructor*/
        public HandleAClient(Socket socket, ArrayList<HandleAClient> clients, int clientNumber) throws IOException {
            this.socket = socket;
            this.clients = clients;
            this.clientNumber = clientNumber;

            inputFromClient = new DataInputStream(socket.getInputStream());     /** Data inputted by clients is then inputted into the
                                                                                    the server to be processed */
            outputToClient = new DataOutputStream(socket.getOutputStream());    /** Data that the server contains is then outputted to
                                                                                    the client */
        }

        /** Run a thread */
        public void run() {
            try {
                //Announce if a client has joined the server
                Platform.runLater(() -> { ta.appendText("Client #" + clientNumber + " has joined the server" + '\n'); });

                // Continuously serve the client
                while (true) {
                    // Receive message from the client
                    String receivedMessage = inputFromClient.readUTF();

                    outToAll(receivedMessage);                          //Outputs client commands to other clients

                    //ALWAYS REMEMBER: "runLater" waits for an opening in the Application thread to apply its changes
                    Platform.runLater(() -> {
                        ta.appendText("Message from Client: " + receivedMessage + '\n');
                    });
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }

        //Method that uses a for loop to send a message to each running client.
        private void outToAll(String msg) throws IOException {
            //For loop that goes through each client
            for (HandleAClient aClient : clients)
            {
                if(aClient.clientNumber == clientNumber)
                {
                    aClient.outputToClient.writeUTF("YOU: " + msg);
                }
                else
                {
                    aClient.outputToClient.writeUTF("Friend " + clientNumber + ": " + msg);
                }
            }
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}