/*
 * Name: Seonghyeon (Sean) Kim
 * Login: csb8wapp
 * Date: March 3, 2016
 * File: Gui2048.java
 * Source of help: Javadocs and piazza
 *
 * This program includes the Gui2048 class and also uses the board class
 * and its components. This program basically uses the same contents as 
 * PSA4, but this program adds the game into a new window and designs the
 * overlay.
 */
/** Gui2048.java */
/** PSA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
   private String outputBoard; // The filename for where to save the Board
   private Board board; // The 2048 Game Board

   private static final int TILE_WIDTH = 106;

   private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
   private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
   //(128, 256, 512)
   private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
   //(1024, 2048, Higher)

   // Fill colors for each of the Tile values
   private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
   private static final Color COLOR_2 = Color.rgb(238, 228, 218);
   private static final Color COLOR_4 = Color.rgb(237, 224, 200);
   private static final Color COLOR_8 = Color.rgb(242, 177, 121);
   private static final Color COLOR_16 = Color.rgb(245, 149, 99);
   private static final Color COLOR_32 = Color.rgb(246, 124, 95);
   private static final Color COLOR_64 = Color.rgb(246, 94, 59);
   private static final Color COLOR_128 = Color.rgb(237, 207, 114);
   private static final Color COLOR_256 = Color.rgb(237, 204, 97);
   private static final Color COLOR_512 = Color.rgb(237, 200, 80);
   private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
   private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
   private static final Color COLOR_OTHER = Color.BLACK;
   private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

   private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
   // For tiles >= 8

   private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
   // For tiles < 8

   private GridPane pane;

   /** Add your own Instance Variables here */
   private static final int NUM_RECTANGLE = 16;
   private int[][] grid;
   private ArrayList<Rectangle> rectList;
   private ArrayList<Text> textList;
   private Text score;
   private int listLength;
   private int minStageHeight;
   private int minStageWidth;
   private StackPane stack;
   private Scene scene;
   private int gameInc;



   /*
    * Name: start(Stage primaryStage)
    * Purpose: This method is where the program is basically run. This is
    * where the board is initialized and everything is set up for the board
    * to show on the new window.
    * Parameters: "Stage primaryStage": This is where the board and the game
    * as a whole will be shown.
    * Return: Void
    */

   @Override
   public void start(Stage primaryStage)
   {
      // Process Arguments and Initialize the Game Board
      processArgs(getParameters().getRaw().toArray(new String[0]));

      // Create the pane that will hold all of the visual objects
      pane = new GridPane();
      pane.setAlignment(Pos.CENTER);
      pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
      pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
      // Set the spacing between the Tiles
      pane.setHgap(15); 
      pane.setVgap(15);

      /** Add your Code for the GUI Here */

      //initialize instance variables
      gameInc = 0;
      minStageHeight = 600;
      minStageWidth = 550;
      grid = board.getGrid();
      scene = new Scene(pane);

      //make general overlay
      primaryStage.setTitle("Gui2048");
      primaryStage.setScene(scene);
      primaryStage.setMinWidth(minStageWidth);
      primaryStage.setMinHeight(minStageHeight);
      primaryStage.show();

      rectList = new ArrayList<Rectangle>();
      textList = new ArrayList<Text>();

      //showing title and score
      Text title = new Text();
      title.setText("2048");
      title.setFont(Font.font("Gill Sans", FontWeight.BOLD, TEXT_SIZE_LOW));
      pane.add(title,0,0,2,1);
      GridPane.setHalignment(title,HPos.CENTER);
      score = new Text();
      score.setText("Score: " + board.getScore());
      score.setFont(Font.font("Gill Sans", FontWeight.NORMAL, TEXT_SIZE_HIGH));
      pane.add(score,2,0,2,1);
      GridPane.setHalignment(score,HPos.CENTER);

      //making the starting board
      listLength = board.GRID_SIZE * board.GRID_SIZE;
      for(int listInc = 0; listInc < listLength;) {
         for(int i = 0; i < board.GRID_SIZE; i++) {
            for(int j = 0; j < board.GRID_SIZE; j++, listInc++) {
               Rectangle rect = new Rectangle();
               rect.setWidth(TILE_WIDTH);
               rect.setHeight(TILE_WIDTH);

               if(grid[i][j] == 4)
                  rect.setFill(COLOR_4);
               else if(grid[i][j] == 2)
                  rect.setFill(COLOR_2);
               else
                  rect.setFill(COLOR_EMPTY);
               rectList.add(listInc,rect);

               Text text = new Text();
               if(grid[i][j] == 0)
                  text.setText(" ");
               else
                  text.setText(Integer.toString(grid[i][j]));
               text.setFont(Font.font("Gill Sans",
                        FontWeight.BOLD, TEXT_SIZE_LOW));
               text.setFill(COLOR_VALUE_DARK);
               textList.add(listInc,text);
            }
         }
      }

      //showing the starting board
      for(int j = 1, listInc = 0; j < board.getGrid().length + 1; j++) {
         for(int i = 0; i < board.getGrid().length; i++, listInc++) {
            pane.add(rectList.get(listInc),i,j);
            pane.add(textList.get(listInc),i,j);
            GridPane.setHalignment(textList.get(listInc), HPos.CENTER);
         }
      }

      //handle arrow input
      scene.setOnKeyPressed(new myKeyHandler());

      //for game over screen
      stack = new StackPane();
      stack.setAlignment(Pos.CENTER);
      GridPane.setHalignment(pane, HPos.CENTER);
      scene = new Scene(stack);

   }

   /** Add your own Instance Methods Here */

   /*
    * Name: myKeyHandler
    * Purpose: This inner class was made so that arrow input can be
    * implemented into the game.
    * Parameters: None
    * Return: Void
    */

   private class myKeyHandler implements EventHandler<KeyEvent> {

      /*
       * Name: handle(KeyEvent e)
       * Purpose: This method is where it tells the program what to do
       * for the keyboard inputs.
       * Parameters: "KeyEvent e": This is where the keyboard input is
       * saved onto.
       * Return: None
       */

      @Override
      public void handle(KeyEvent e) {

         //boolean condition so it can stop when game is over
         if(gameInc < 1) {

            if(e.getCode() == KeyCode.S) {
               try {
                  board.saveBoard(outputBoard);
               } catch (IOException ex) {
                  System.out.println("saveBoard threw an Exception");
               }
               System.out.println("Saving Board to outputBoard");
            }

            else if(e.getCode() == KeyCode.UP) {
               if(board.canMove(Direction.UP)) {
                  board.move(Direction.UP);
                  System.out.println("Moving Up");
                  board.addRandomTile();
                  updateScore();
                  updateBoard();
               }
            }

            else if(e.getCode() == KeyCode.DOWN) {
               if(board.canMove(Direction.DOWN)) {
                  board.move(Direction.DOWN);
                  System.out.println("Moving Down");
                  board.addRandomTile();
                  updateScore();
                  updateBoard();
               }
            }

            else if(e.getCode() == KeyCode.LEFT) {
               if(board.canMove(Direction.LEFT)) {
                  board.move(Direction.LEFT);
                  System.out.println("Moving Left");
                  board.addRandomTile();
                  updateScore();
                  updateBoard();
               }
            }

            else if(e.getCode() == KeyCode.RIGHT) {
               if(board.canMove(Direction.RIGHT)) {
                  board.move(Direction.RIGHT);
                  System.out.println("Moving Right");
                  board.addRandomTile();
                  updateScore();
                  updateBoard();
               }
            }

            if(board.isGameOver()) {
               pane.add(stack,0,0,board.getGrid().length,
                     board.getGrid().length + 1);
               updateGameOver();
               gameInc++;
            }
         }
      }
   }


   /*
    * Name: updateBoard()
    * Purpose: The purpose of this method is to update the board on the
    * new window after the moves to the board have been made.
    * Parameters: None
    * Return: Void
    */

   public void updateBoard() {

      for(int listInc = 0; listInc < listLength; ) {
         for(int i = 0; i < board.GRID_SIZE; i++) {
            for(int j = 0; j < board.GRID_SIZE; j++, listInc++) {

               textList.get(listInc).setText(Integer.toString(grid[i][j]));

               //set number color
               if(grid[i][j] < 8)
                  textList.get(listInc).setFill(COLOR_VALUE_DARK);
               if(grid[i][j] >= 8)
                  textList.get(listInc).setFill(COLOR_VALUE_LIGHT);

               //set number font size
               if(grid[i][j] < 128)
                  textList.get(listInc).setFont(Font.font("Gill Sans",
                           FontWeight.BOLD, TEXT_SIZE_LOW));
               if(grid[i][j] >= 128 && grid[i][j] < 1024)
                  textList.get(listInc).setFont(Font.font("Gill Sans",
                           FontWeight.BOLD, TEXT_SIZE_MID));
               if(grid[i][j] >= 1024)
                  textList.get(listInc).setFont(Font.font("Gill Sans",
                           FontWeight.BOLD, TEXT_SIZE_HIGH));


               //set tile color
               if(grid[i][j] == 0) {
                  rectList.get(listInc).setFill(COLOR_EMPTY);
                  textList.get(listInc).setText("");
               }
               if(grid[i][j] == 2) 
                  rectList.get(listInc).setFill(COLOR_2);
               if(grid[i][j] == 4)
                  rectList.get(listInc).setFill(COLOR_4);
               if(grid[i][j] == 8)
                  rectList.get(listInc).setFill(COLOR_8);
               if(grid[i][j] == 16)
                  rectList.get(listInc).setFill(COLOR_16);
               if(grid[i][j] == 32)
                  rectList.get(listInc).setFill(COLOR_32);
               if(grid[i][j] == 64)
                  rectList.get(listInc).setFill(COLOR_64);
               if(grid[i][j] == 128)
                  rectList.get(listInc).setFill(COLOR_128);
               if(grid[i][j] == 256)
                  rectList.get(listInc).setFill(COLOR_256);
               if(grid[i][j] == 512)
                  rectList.get(listInc).setFill(COLOR_512);
               if(grid[i][j] == 1024)
                  rectList.get(listInc).setFill(COLOR_1024);
               if(grid[i][j] == 2048)
                  rectList.get(listInc).setFill(COLOR_2048);
               if(grid[i][j] > 2048)
                  rectList.get(listInc).setFill(COLOR_OTHER);
            }
         }
      }
   }


   /*
    * Name: updateScore()
    * Purpose: The purpose of this method is to update the score after
    * the move has been made, since the score can possibly be changed.
    * Parameters: None
    * Return: Void
    */

   public void updateScore() {
      score.setText("Score: " + board.getScore());
   }


   /*
    * Name: updateGameOver()
    * Purpose: This method is ran when no more moves can be made, and it
    * shows the game over screen over the top of the layout.
    * Parameters: None
    * Return: Void
    */

   public void updateGameOver() {

      //rectangle for game over
      Rectangle gameOverRect = new Rectangle();
      gameOverRect.setWidth(minStageWidth);
      gameOverRect.setHeight(minStageHeight);
      gameOverRect.setFill(COLOR_GAME_OVER);

      //game over text
      Text gameOverText = new Text();
      gameOverText.setFill(COLOR_VALUE_DARK);
      gameOverText.setText("Game Over!");
      gameOverText.setFont(Font.font("Gill Sans",
               FontWeight.BOLD, 70));

      GridPane.setHalignment(gameOverText, HPos.CENTER);
      stack.getChildren().add(gameOverRect);
      stack.getChildren().add(gameOverText);
      GridPane.setHalignment(gameOverRect, HPos.CENTER);
   }


   /** DO NOT EDIT BELOW */

   // The method used to process the command line arguments
   private void processArgs(String[] args)
   {
      String inputBoard = null;   // The filename for where to load the Board
      int boardSize = 0;          // The Size of the Board

      // Arguments must come in pairs
      if((args.length % 2) != 0)
      {
         printUsage();
         System.exit(-1);
      }

      // Process all the arguments 
      for(int i = 0; i < args.length; i += 2)
      {
         if(args[i].equals("-i"))
         {   // We are processing the argument that specifies
            // the input file to be used to set the board
            inputBoard = args[i + 1];
         }
         else if(args[i].equals("-o"))
         {   // We are processing the argument that specifies
            // the output file to be used to save the board
            outputBoard = args[i + 1];
         }
         else if(args[i].equals("-s"))
         {   // We are processing the argument that specifies
            // the size of the Board
            boardSize = Integer.parseInt(args[i + 1]);
         }
         else
         {   // Incorrect Argument 
            printUsage();
            System.exit(-1);
         }
      }

      // Set the default output file if none specified
      if(outputBoard == null)
         outputBoard = "2048.board";
      // Set the default Board size if none specified or less than 2
      if(boardSize < 2)
         boardSize = 4;

      // Initialize the Game Board
      try{
         if(inputBoard != null)
            board = new Board(inputBoard, new Random());
         else
            board = new Board(boardSize, new Random());
      }
      catch (Exception e)
      {
         System.out.println(e.getClass().getName() + 
               " was thrown while creating a " +
               "Board from file " + inputBoard);
         System.out.println("Either your Board(String, Random) " +
               "Constructor is broken or the file isn't " +
               "formated correctly");
         System.exit(-1);
      }
   }

   // Print the Usage Message 
   private static void printUsage()
   {
      System.out.println("Gui2048");
      System.out.println("Usage:  Gui2048 [-i|o file ...]");
      System.out.println();
      System.out.println("  Command line arguments come in pairs of the "+ 
            "form: <command> <argument>");
      System.out.println();
      System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
            "should be loaded");
      System.out.println();
      System.out.println("  -o [file]  -> Specifies a file that should be " + 
            "used to save the 2048 board");
      System.out.println("                If none specified then the " + 
            "default \"2048.board\" file will be used");  
      System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
            "board if an input file hasn't been"); 
      System.out.println("                specified.  If both -s and -i" + 
            "are used, then the size of the board"); 
      System.out.println("                will be determined by the input" +
            " file. The default size is 4.");
   }
}
