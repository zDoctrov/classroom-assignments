package network_slides;

import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    private TextField tf = new TextField();
    private TextArea ta = new TextArea();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        //(Lesser Pane) Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");

        //Add button to submit messages
        Button submitMessage = new Button();
        submitMessage.setText("Send");
        paneForTextField.setLeft(submitMessage);

        //TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_LEFT);                   //tf = Text Field (Input Area)
        paneForTextField.setCenter(tf);

        //Text area to display contents
        ta.setPrefHeight(300);
        ta.setEditable(false);
        ScrollPane scrollPane = new ScrollPane(ta);

        //(Greater Pane) Putting all GUI elements together in one main pane
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(scrollPane);                        //ta = Text Area (Output Area)
        mainPane.setBottom(paneForTextField);

        //Create a scene and place it in the stage, adding mainPane to it
        Scene scene = new Scene(mainPane, 450, 340);
        primaryStage.setTitle("Client");                    //Set the stage title
        primaryStage.setScene(scene);                       //Place the scene in the stage
        primaryStage.show();                                //Display the stage

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            Platform.runLater( () -> { ta.appendText("Welcome to the Chat Room!" + "\n"); });

            Task<Void> bg = new Background(socket);
            Thread taskThread = new Thread(bg);
            taskThread.setDaemon(true);
            taskThread.start();
        }
        catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }

        //Action Event Method
        //Button Press: Submit TextField string to server
        submitMessage.setOnAction(e -> {
            try {
                //Get the string message from the text field
                String message = tf.getText();

                //Send the message to the server
                toServer.writeUTF(message);
                toServer.flush();

                //Remove sent message from TextField
                tf.clear();
            }
            catch (IOException ex) {
                System.err.println(ex);
            }
        });//End of Button ActionEvent
    }//End of start()


    class Background extends Task<Void>
    {
        private Socket socket;

        TextArea tempArea;

        Background(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        protected Void call() throws Exception {
            fromServer = new DataInputStream(socket.getInputStream());
            String messageReturn = "Welcome to the Chat Room!" + "\n";
            String tempMessage = null;

           while(true)
           {
               try{
                   tempMessage = fromServer.readUTF() + '\n';
                   messageReturn = messageReturn + tempMessage;
                   System.out.print(tempMessage);                       //Output Conversation to console

                   ta.setText(messageReturn);
               }
               catch(IOException e){
                   e.printStackTrace();
               }
           }
        }
    }//End of Background Task

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }//End of main()
}//End of Client file