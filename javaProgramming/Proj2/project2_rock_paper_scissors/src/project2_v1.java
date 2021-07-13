import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class project2_v1 extends Application {
     @Override // Override the start method in the Application class
     
     /****
     *Program Structure: 
     * 1) Import needed libraries, put start method in class filename
     * 2) set up main pane (Grid pane) and lesser panes
     * 3) "ComputerClass", which displays player vs. computer outputs
     * 4) All of the event class handlers for each button
     * 5) Designated section for user selection buttons (HBox)
     * 6) Place nodes onto the Grid pane, set up the scene
     * 7) Additional VBox methods & main
     *
     *Images used in software
     *"file:image/rock.png"
     *"file:image/lizard.png"
     *"file:image/spock.png"
     *"file:image/paper.png"
     *"file:image/scissors.png"
     ****/
         
  public void start(Stage primaryStage) 
  {
    //gridPane organizes the overall design of the scene
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);  //Place all nodes in the center
                                        // of window, no matter the size (import...Pos)                                 
    gridPane.setVgap(15);
    gridPane.setHgap(2);
    //******
    
    //contestantChoices organizes the player & computer choices into the middle column
    VBox contestantChoices = new VBox(0);                  //Space between Vertical Objects
    contestantChoices.setPadding(new Insets(0, 0, 0, 0));  //Set the boundaries (top/right/bottom/left)
    contestantChoices.setSpacing(60);
    
    //These Stack Panes are used to overlap images of choices over the two circles
    StackPane computerDecision = new StackPane();
    StackPane playerDecision = new StackPane();
    //******
    
    //contestantResults displays the outcome of the match in the rightmost column
    VBox contestantResults = new VBox(150);                  //Space between Vertical Objects
    contestantResults.setPadding(new Insets(0, 0, 0, 0));    //Set the boundaries (top/right/bottom/left)
    contestantResults.setSpacing(20);
    //******
    
    class ComputerClass //This class contains the computer's choice & game outcomes
    {
        public int computerChoice() //The computer chooses their "weapon"
        {
            int computerChoice;
            Random random = new Random();                //Computer's selection is
            computerChoice = (random.nextInt(5) + 1);    //randomly generated (1 - 5)
            
            //"Rock"=1, "Lizard"=2, "Spock"=3, "Paper"=4, "Scissors"=5
            switch (computerChoice)
            {
                case 1:
                    if(computerDecision.getChildren().size() == 2) //Makes sure only 1
                    {                                              //image appears at once
                        computerDecision.getChildren().remove(1);
                    }
                    
                    Image rockImage = new Image("file:image/rock.png");
         
                    ImageView rockImageView = new ImageView(rockImage);
                    rockImageView.setFitHeight(60);
                    rockImageView.setFitWidth(100);
            
                    ImageView rockImageView2 = new ImageView(rockImage);
                    rockImageView2.setFitHeight(60);
                    rockImageView2.setFitWidth(100);
            
                    computerDecision.getChildren().add(rockImageView);
                    break;
            
                case 2:
                    if(computerDecision.getChildren().size() == 2)  //Makes sure only 1
                    {                                               //image appears at once
                        computerDecision.getChildren().remove(1);
                    }
            
                    Image lizardImage = new Image("file:image/lizard.png");
            
                    ImageView lizardImageView = new ImageView(lizardImage);
                    lizardImageView.setFitHeight(60);
                    lizardImageView.setFitWidth(100);
            
                    computerDecision.getChildren().add(lizardImageView);
                    break;
              
                case 3:
                    if(computerDecision.getChildren().size() == 2)  //Makes sure only 1
                    {                                               //image appears at once
                        computerDecision.getChildren().remove(1);
                    }
           
                    Image spockImage = new Image("file:image/spock.png");
            
                    ImageView spockImageView = new ImageView(spockImage);
                    spockImageView.setFitHeight(100);
                    spockImageView.setFitWidth(100);
            
                    computerDecision.getChildren().add(spockImageView);
                    break;
              
                case 4:            
                    if(computerDecision.getChildren().size() == 2) //Makes sure only 1
                    {                                              //image appears at once
                        computerDecision.getChildren().remove(1);
                    }
           
                    Image paperImage = new Image("file:image/paper.png");
         
                    ImageView paperImageView = new ImageView(paperImage);
                    paperImageView.setFitHeight(100);
                    paperImageView.setFitWidth(100);
            
                    computerDecision.getChildren().add(paperImageView);
                    break;
              
                case 5:
                    if(computerDecision.getChildren().size() == 2) //Makes sure only 1
                    {                                              //image appears at once
                        computerDecision.getChildren().remove(1);
                    }
                   
                    Image scissorsImage = new Image("file:image/scissors.png");
         
                    ImageView scissorsImageView = new ImageView(scissorsImage);
                    scissorsImageView.setFitHeight(60);
                    scissorsImageView.setFitWidth(100);
            
                    computerDecision.getChildren().add(scissorsImageView);
                    break;
              
                default:
                System.out.println("Invalid Input Used");
            }            
            return computerChoice;
        }
        
        //Translates integer choices into the respective string name of the choice  
        public String choiceName(int choice)
        {
            String choiceName = new String();
            
            switch (choice)
            {
                case 1:
                    choiceName = "Rock";
                    break;
                    
                case 2:
                    choiceName = "Lizard";
                    break;    
                    
                case 3:
                    choiceName = "Spock";
                    break;
                    
                case 4:
                    choiceName = "Paper";
                    break;
                    
                case 5:
                    choiceName = "Scissors";
                    break;
                    
                default:
                    choiceName = "N/A";
            }
            
            return choiceName; 
        }
        
        public void gameOutcome(int playerChoice, int computerChoice) //The computer chooses their "weapon"
        {
              contestantResults.getChildren().clear();  //Removes previous
                                                        //match outcome statement
              //Setting up outcome labels
              Label interaction = new Label();
              Label outcome = new Label();              
              interaction.setFont(Font.font("Ties New Roman", FontWeight.EXTRA_BOLD, 20));
              outcome.setFont(Font.font("Ties New Roman", FontWeight.EXTRA_BOLD, 20));
              
              //"Rock"=1, "Lizard"=2, "Spock"=3, "Paper"=4, "Scissors"=5
              if(playerChoice == computerChoice)
              {
                 interaction.setText(choiceName(playerChoice) + " doesn't affect " + choiceName(computerChoice));
                 outcome.setText("Draw!"); 
              }
              else if(playerChoice == 1)
              {//Player chooses "Rock"
                  if(computerChoice == 2 || computerChoice == 5)
                  {//computer loses (Lizard or Scissors)        
                      interaction.setText(choiceName(playerChoice) + " crushes " + choiceName(computerChoice));
                      outcome.setText("You Win!");
                  }
                  else
                  {//computer wins (Spock = 3 or Paper = 4)
                      switch(computerChoice)
                      {
                          case 3:
                              interaction.setText(choiceName(computerChoice) + " vaporizes " + choiceName(playerChoice));
                              break;
                          case 4:
                              interaction.setText(choiceName(computerChoice) + " covers " + choiceName(playerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(computerChoice) + " ??? " + choiceName(playerChoice));
                      }
 
                      outcome.setText("You Lose...");
                  }    
              }
              else if(playerChoice == 2)
              {//Player chooses "Lizard"
                  if(computerChoice == 3 || computerChoice == 4)
                  {//computer loses (Spock or Paper)
                      switch(computerChoice)
                      {
                          case 3:
                              interaction.setText(choiceName(playerChoice) + " poisons " + choiceName(computerChoice));
                              break;
                          case 4:
                              interaction.setText(choiceName(playerChoice) + " eats " + choiceName(computerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(playerChoice) + " ??? " + choiceName(computerChoice));
                      }
                       
                      outcome.setText("You Win!");
                  }
                  else
                  {//computer wins (Rock or Scissors)
                      switch(computerChoice)
                      {
                          case 1:
                              interaction.setText(choiceName(computerChoice) + " crushes " + choiceName(playerChoice));
                              break;
                          case 5:
                              interaction.setText(choiceName(computerChoice) + " decapitates " + choiceName(playerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(computerChoice) + " ??? " + choiceName(playerChoice));
                      }
 
                      outcome.setText("You Lose...");
                  }      
              }
              else if(playerChoice == 3)
              {//Player chooses "Spock"
                  if(computerChoice == 1 || computerChoice == 5)
                  {//computer loses (Rock or Scissors)
                      switch(computerChoice)
                      {
                          case 1:
                              interaction.setText(choiceName(playerChoice) + " vaporizes " + choiceName(computerChoice));
                              break;
                          case 5:
                              interaction.setText(choiceName(playerChoice) + " smashes " + choiceName(computerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(playerChoice) + " ??? " + choiceName(computerChoice));
                      }
                       
                      outcome.setText("You Win!");
                  }
                  else
                  {//computer wins (Lizard or Paper)
                      switch(computerChoice)
                      {
                          case 2:
                              interaction.setText(choiceName(computerChoice) + " poisons " + choiceName(playerChoice));
                              break;
                          case 4:
                              interaction.setText(choiceName(computerChoice) + " disproves " + choiceName(playerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(computerChoice) + " ??? " + choiceName(playerChoice));
                      }
 
                      outcome.setText("You Lose...");
                  }    
              }
              else if(playerChoice == 4)
              {//Player chooses "Paper"
                  if(computerChoice == 1 || computerChoice == 3)
                  {//computer loses (Rock or Spock)
                      switch(computerChoice)
                      {
                          case 1:
                              interaction.setText(choiceName(playerChoice) + " covers " + choiceName(computerChoice));
                              break;
                          case 3:
                              interaction.setText(choiceName(playerChoice) + " disproves " + choiceName(computerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(playerChoice) + " ??? " + choiceName(computerChoice));
                      }
                       
                      outcome.setText("You Win!");
                  }
                  else
                  {//computer wins (Lizard or Scissors)
                        switch(computerChoice)
                        {
                          case 2:
                              interaction.setText(choiceName(computerChoice) + " eats " + choiceName(playerChoice));
                              break;
                          case 5:
                              interaction.setText(choiceName(computerChoice) + " cuts " + choiceName(playerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(computerChoice) + " ??? " + choiceName(playerChoice));
                        }
 
                      outcome.setText("You Lose...");
                  }
              }
              else if(playerChoice == 5)
              {//Player chooses "Scissors"
                  if(computerChoice == 2 || computerChoice == 4)
                  {//computer loses (Lizard or Paper)
                      switch(computerChoice)
                      {
                          case 2:
                              interaction.setText(choiceName(playerChoice) + " decaptitates " + choiceName(computerChoice));
                              break;
                          case 4:
                              interaction.setText(choiceName(playerChoice) + " cuts " + choiceName(computerChoice));
                              break;
                          default:
                              interaction.setText(choiceName(playerChoice) + " ??? " + choiceName(computerChoice));
                      }
                      
                      outcome.setText("You Win!");
                  }
                  else
                  {//computer wins (Rock or Spock)
                      switch(computerChoice)
                      {
                        case 1:
                            interaction.setText(choiceName(computerChoice) + " crushes " + choiceName(playerChoice));
                            break;
                        case 3:
                            interaction.setText(choiceName(computerChoice) + " smashes " + choiceName(playerChoice));
                            break;
                        default:
                            interaction.setText(choiceName(computerChoice) + " ??? " + choiceName(playerChoice));
                      }
 
                        outcome.setText("You Lose...");
                  }
                  
              }
              
              
              //Finally add correct labels for situation
              contestantResults.getChildren().add(interaction);
              contestantResults.getChildren().add(outcome);
        }
    }
    
    //All Event Handler Classes are Inner Classes to the start Class
        //The handler classes represent the outcomes of pressing their
        //respective objects button
    
    //"Rock" is denoted by integer 1
    class RockHandlerClass implements EventHandler<ActionEvent> 
    { //The Event Listener
        @Override
        public void handle(ActionEvent e)
        {//The Event Handler
            if(playerDecision.getChildren().size() == 2) //Makes sure only 1
            {                                            //image appears at once
                playerDecision.getChildren().remove(1);
            }
                    
            System.out.println("Rock button clicked");
            Image rockImage = new Image("file:image/rock.png");
         
            ImageView rockImageView = new ImageView(rockImage);
            rockImageView.setFitHeight(60);
            rockImageView.setFitWidth(100);
            
            playerDecision.getChildren().add(rockImageView);
            
            //The computer makes its choice after the player
            ComputerClass computer = new ComputerClass();
            int computerNum = computer.computerChoice();
            
            //Results
            computer.gameOutcome(1, computerNum);
        }
    }
    
    //"Lizard" is denoted by integer 2
    class LizardHandlerClass implements EventHandler<ActionEvent> 
    { //The Event Listener
        @Override
        public void handle(ActionEvent e) 
        { //The Event Handler                                      
            if(playerDecision.getChildren().size() == 2)    //Makes sure only 1
            {                                               //image appears at once
                playerDecision.getChildren().remove(1);
            }
              
            System.out.println("Lizard button clicked");
            Image lizardImage = new Image("file:image/lizard.png");
            
            ImageView lizardImageView = new ImageView(lizardImage);
            lizardImageView.setFitHeight(60);
            lizardImageView.setFitWidth(100);
            
            playerDecision.getChildren().add(lizardImageView);
            
            //The computer makes its choice after the player
            ComputerClass computer = new ComputerClass();
            int computerNum = computer.computerChoice();
            
            //Results
            computer.gameOutcome(2, computerNum);
        }
    }
    
    //"Spock" is denoted by integer 3
    class SpockHandlerClass implements EventHandler<ActionEvent> 
    { //The Event Listener
        @Override
        public void handle(ActionEvent e) 
        { //The Event Handler
            if(playerDecision.getChildren().size() == 2)    //Makes sure only 1
            {                                               //image appears at once
                playerDecision.getChildren().remove(1);
            }
              
            System.out.println("Spock button clicked"); 
            Image spockImage = new Image("file:image/spock.png");
            
            ImageView spockImageView = new ImageView(spockImage);
            spockImageView.setFitHeight(100);
            spockImageView.setFitWidth(100);
            
            playerDecision.getChildren().add(spockImageView);
            
            //The computer makes its choice after the player
            ComputerClass computer = new ComputerClass();
            int computerNum = computer.computerChoice();
            
            //Results
            computer.gameOutcome(3, computerNum);
        }
    }   
    
    //"Paper" is denoted by integer 4
    class PaperHandlerClass implements EventHandler<ActionEvent> 
    { //The Event Listener
        @Override
        public void handle(ActionEvent e) 
        { //The Event Handler
            
            if(playerDecision.getChildren().size() == 2) //Makes sure only 1
            {                                            //image appears at once
                playerDecision.getChildren().remove(1);
            }
                    
            System.out.println("Paper button clicked");
            Image paperImage = new Image("file:image/paper.png");
         
            ImageView paperImageView = new ImageView(paperImage);
            paperImageView.setFitHeight(100);
            paperImageView.setFitWidth(100);
            
            playerDecision.getChildren().add(paperImageView);
            
            //The computer makes its choice after the player
            ComputerClass computer = new ComputerClass();
            int computerNum = computer.computerChoice();
            
            //Results
            computer.gameOutcome(4, computerNum);
        }
    }
    
    //"Scissors" is denoted as integer 5
    class ScissorsHandlerClass implements EventHandler<ActionEvent> 
    { //The Event Listener
        @Override
        public void handle(ActionEvent e) 
        { //The Event Handler
            if(playerDecision.getChildren().size() == 2) //Makes sure only 1
            {                                            //image appears at once
                playerDecision.getChildren().remove(1);
            }
                    
            System.out.println("Scissor button clicked");
            Image scissorsImage = new Image("file:image/scissors.png");
         
            ImageView scissorsImageView = new ImageView(scissorsImage);
            scissorsImageView.setFitHeight(60);
            scissorsImageView.setFitWidth(100);
            
            playerDecision.getChildren().add(scissorsImageView);
            
            //The computer makes its choice after the player
            ComputerClass computer = new ComputerClass();
            int computerNum = computer.computerChoice();
            
            //Results
            computer.gameOutcome(5, computerNum);
        }
    }
    
    //***********
    //All 5 of the user's selectable buttons are contained here
    HBox userSelection = new HBox(15);                   //The Space between horizontal objects
    userSelection.setPadding(new Insets(0, 0, 0, 0));    //Set the boundaries (top/right/bottom/left)
  
    //Create VBox for Rock 
    //(Note: Paper, Scissors, Lizard, & Spock will copy this format)
    Image rockImage = new Image("file:image/rock.png");     //Image file Location needed
    Button rockButton = new Button();
    rockButton.setPrefSize(80, 80);                         //Set Width & Height of Button
    rockButton.setGraphic(new ImageView(rockImage));        //Put Image ontop of button
    
    RockHandlerClass rockHandler = new RockHandlerClass();  //Event Listener Class Instance Created
    rockButton.setOnAction(rockHandler);                    //registering the event
    
    VBox rockIcon = new VBox(rockButton);                   
    rockIcon.getChildren().add(new Label("Rock"));          //Add Text under Button
    
    userSelection.getChildren().add(rockIcon);              //Add rock to user selection
   
    //Create VBox for Paper
    Image paperImage = new Image("file:image/paper.png");
     
    Button paperButton = new Button();                      //Paper Button
    paperButton.setPrefSize(80, 80);
    paperButton.setGraphic(new ImageView(paperImage));
     
    PaperHandlerClass paperHandler = new PaperHandlerClass();
    paperButton.setOnAction(paperHandler);
     
    VBox paperIcon = new VBox(paperButton);
    paperIcon.getChildren().add(new Label("Paper"));
     
    userSelection.getChildren().add(paperIcon);             //Add Paper to user selection
   
    //Create a VBox for Scissors
    Image scissorsImage = new Image("file:image/scissors.png");
     
    Button scissorsButton = new Button();                   //Scissors Button
    scissorsButton.setPrefSize(80, 80);
    scissorsButton.setGraphic(new ImageView(scissorsImage));
     
    ScissorsHandlerClass scissorsHandler = new ScissorsHandlerClass();
    scissorsButton.setOnAction(scissorsHandler);
     
    VBox scissorsIcon = new VBox(scissorsButton);
    scissorsIcon.getChildren().add(new Label("Scissors"));  //Add Scissors to user selection
     
    userSelection.getChildren().add(scissorsIcon);
     
    //Create VBox for Lizard
    Image lizardImage = new Image("file:image/lizard.png");
    
    Button lizardButton = new Button();                     //Lizard Button
    lizardButton.setPrefSize(80, 80);
    lizardButton.setGraphic(new ImageView(lizardImage));
    
    LizardHandlerClass lizardHandler = new LizardHandlerClass();
    lizardButton.setOnAction(lizardHandler);
    
    VBox lizardIcon = new VBox(lizardButton);               
    lizardIcon.getChildren().add(new Label("Lizard"));
    
    userSelection.getChildren().add(lizardIcon);            //Add Lizard to user selection
    
    //Create VBox for Spock
    Image spockImage = new Image("file:image/spock.png");
    
    Button spockButton = new Button();                      //Spock Button
    spockButton.setPrefSize(80, 80);
    spockButton.setGraphic(new ImageView(spockImage));
     
    SpockHandlerClass spockHandler = new SpockHandlerClass();
    spockButton.setOnAction(spockHandler);
     
    VBox spockIcon = new VBox(spockButton);
    spockIcon.getChildren().add(new Label("Spock"));
     
    userSelection.getChildren().add(spockIcon);             //Add Spock to user selection
    //*********** (userSelection ends)
    
    computerDecision.getChildren().add(getComputerCircle());    //Add circle background
    playerDecision.getChildren().add(getPlayerCircle());        //to differentiate player
    //******                                                    //from computer
   
    contestantChoices.getChildren().add(computerDecision);      //Use Stack Pane to allign
    contestantChoices.getChildren().add(playerDecision);        //Image over contestant circles
    contestantChoices.getChildren().add(getChoiceMessage());    //("Select an option" message)
    //******
    
    //(...).add(node, columnIndex, rowIndex, columnSpan, rowSpan)
    gridPane.add(userSelection, 0, 15, 70, 5);                  //Adds 5 buttons to the bottom row    
    gridPane.add(getContestantLabels(), 0, 0, 1, 5);            //Adds Computer & Player label to left column
    gridPane.add(contestantChoices, 5, 0, 1, 7);                //Adds contestant selection to middle coloumn   
    gridPane.add(contestantResults, 20, 5, 1, 5);               //Match outcome is put on the right column
    
    // Create a scene and place it in the stage
    Scene scene = new Scene(gridPane, 900, 700);
    primaryStage.setTitle("Rock, Paper, Scissors: Additional Weapons"); // Set the stage title
    primaryStage.setScene(scene);                                       // Place the scene in the stage
    primaryStage.show();                                                // Display the stage
  }//Start Ends
  
  //Static Label to differentiate Player from Computer
  private VBox getContestantLabels() 
  {
     VBox contestantLabels = new VBox(150);                  //Space between Vertical Objects
     contestantLabels.setPadding(new Insets(0, 0, 0, 0));    //Set the boundaries (top/right/bottom/left)
     
     contestantLabels.setSpacing(140);
     
     Label computerLabel = new Label("Computer");
     Label playerLabel = new Label("Player");
     
     //Change Font, Embolden, & Change Size
     computerLabel.setFont(Font.font("Ties New Roman", FontWeight.EXTRA_BOLD, 25));
     playerLabel.setFont(Font.font("Ties New Roman", FontWeight.EXTRA_BOLD, 25));
     
     contestantLabels.getChildren().add(computerLabel);
     contestantLabels.getChildren().add(playerLabel);
     
     return contestantLabels;
  }
  
  //Circled Images of computer contestant (used to be getContestantChoices)
  private Circle getComputerCircle() 
  {
     Circle computerCircle = new Circle();
     computerCircle.setStroke(Color.BLACK);
     computerCircle.setFill(Color.GRAY);
     computerCircle.setStrokeWidth(6.0);
     computerCircle.setRadius(60);
     
     return computerCircle;
  }
  
  
  //Circled Images of player contestant (used to be getContestantChoices)
  private Circle getPlayerCircle() 
  {
     Circle playerCircle = new Circle();
     playerCircle.setStroke(Color.BLACK);
     playerCircle.setFill(Color.BLUE);
     playerCircle.setStrokeWidth(6.0);
     playerCircle.setRadius(60);
     
     return playerCircle;
  }
  
  //"Select an option" appears below the 2nd column
  private Label getChoiceMessage()
  {
     Label chooseOne = new Label("Select An Option");
     
     //Change Font, Embolden, & Change Size
     chooseOne.setFont(Font.font("Ties New Roman", FontWeight.EXTRA_BOLD, 20));
     
     return chooseOne;
  }
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) 
  {
    launch(args);
  }//main ends
  
}//project2_v1 ends