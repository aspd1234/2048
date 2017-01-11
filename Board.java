/*
 * Name: Seonghyeon (Sean) Kim
 * Login: csb8wapp
 * Date: February 4, 2016
 * File: Board.java
 * Source of help: None for PSA4
 *
 * This program includes the Board class and all its methods. The class is
 * used to implement the board to play the game 2048 in GameManager class.
 * With this class, you can make a board, load a board, save a board, add a
 * tile to a board, or rotate the board in the direction you desire. With
 * the addition of PSA4 materials, this class also has the ability to
 * perform the moves in the four directions (up, down, right, or left), 
 * check if the moves are possible, and check whether the game is over or
 * not.
 */
//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

public class Board {
   public final int NUM_START_TILES = 2;
   public final int TWO_PROBABILITY = 90;
   public final int GRID_SIZE;


   private final Random random;
   private int[][] grid;
   private int score;

   /*
    * Name: Board(int boardSize, Random random)
    * Purpose: This is a Board class constructor that makes a new board
    * with two random tiles.
    * Parameter: "int boardSize": Determines the side length of the grid;
    * "Random random": Used for determining the probability of the location
    * of the new tile and its number;
    * Return: void
    */

   public Board(int boardSize, Random random) {

      this.random = random;

      if(boardSize <= 2)

         GRID_SIZE = 4;
      else

         GRID_SIZE = boardSize;

      grid = new int[GRID_SIZE][GRID_SIZE];

      for(int i = 0; i < NUM_START_TILES; i++) {

         addRandomTile();
      }
   }

   /*
    * Name: Board(String inputBoard, Random random)
    * Purpose: This is a Board class constructor that constructs a board
    * based off of an input file from inputBoard.
    * Parameters: "String inputBoard": Leads to the file name that has the
    * board to construct; "Random random": Used for determining the 
    * probability of the location of the new tile and its number.
    * Return: void
    */
   public Board(String inputBoard, Random random) throws IOException {

      this.random = random;
      Scanner board = new Scanner(new File(inputBoard));
      GRID_SIZE = board.nextInt();
      score = board.nextInt();
      grid = new int[GRID_SIZE][GRID_SIZE];

      for(int i = 0; i < GRID_SIZE; i++) {

         for(int j = 0; j < GRID_SIZE; j++) {

            grid[i][j] = board.nextInt();
         }
      }
   }

   /*
    * Name: saveBoard(String outputBoard)
    * Purpose: This method saves the current board into the file that
    * outputBoard directs to.
    * Parameter: "String outputBoard": Contains the string that leads to
    * the file that this method will save to;
    * Return: void
    */

   public void saveBoard(String outputBoard) throws IOException {

      FileWriter fileName = new FileWriter(outputBoard);
      PrintWriter board = new PrintWriter(fileName);
      board.println(GRID_SIZE);
      board.println(score);

      //Print the grid of the board in its format
      for(int i = 0; i < GRID_SIZE; i++) {

         for(int j = 0; j < GRID_SIZE; j++) {

            board.print(grid[i][j] + " ");
         }

         board.println();
      }

      board.close();
   }

   /*
    * Name: addRandomTile()
    * Purpose: This method adds a random tile of value 2 or 4 to a random
    * empty space on the board if there's empty space.
    * Parameter: None
    * Return: void
    */

   public void addRandomTile() {

      int count = 0;

      //Count the number of empty spaces on the grid
      for(int i = 0; i < grid.length; i++) {

         for(int j = 0; j < grid[0].length; j++) {

            if(grid[i][j] == 0)

               count++;

         }
      }

      if(count == 0)

         return;

      //Used for random location of new tile
      int location = random.nextInt(count);

      //newCount needs to start at 0 when used
      int newCount = -1;

      int row = 0;
      int column = 0;

      //Used to figure out which empty space corresponds to the location
      for(int i = 0; i < grid.length; i++) {

         for(int j = 0; j < grid[0].length; j++) {

            if(grid[i][j] == 0) {

               newCount++;

               if(newCount == location) {

                  row = i;
                  column = j;
               }
            }
         }
      }

      int numProb = 100;

      //Probability of the number being 2 or 4
      if(random.nextInt(numProb) < TWO_PROBABILITY) 

         grid[row][column] = 2;

      else

         grid[row][column] = 4;
   }

   /*
    * Name: rotate(boolean rotateClockwise)
    * Purpose: This method rotates the board by 90 degrees clockwise or 90
    * degrees counter-clockwise
    * Parameter: "boolean rotateClockwise": If this is true, it rotates
    * the board 90 degrees clockwise; if false, it rotates the board 90
    * degrees counter-clockwise;
    * Return: void
    */

   public void rotate(boolean rotateClockwise) {

      int rows = grid.length;
      int columns = grid[0].length;

      //temporary grid to copy the original grid on
      int[][] tempGrid = new int[rows][columns];

      for(int i = 0; i < rows; i++) {

         for(int j = 0; j < columns; j++) {

            tempGrid[i][j] = grid[i][j];
         }
      }

      if(rotateClockwise == false) {

         for(int i = 0; i < rows; i++) {

            for(int j = 0; j < columns; j++) {

               grid[columns - 1 - j][i] = tempGrid[i][j];
            }
         }
      }

      if(rotateClockwise == true) {

         for(int i = 0; i < rows; i++) {

            for(int j = 0; j < columns; j++) {

               grid[j][rows - 1 - i] = tempGrid[i][j];
            }
         }
      }
   }

   //Complete this method ONLY if you want to attempt at getting the extra credit
   //Returns true if the file to be read is in the correct format, else return
   //false
   public static boolean isInputFileCorrectFormat(String inputFile) {
      //The try and catch block are used to handle any exceptions
      //Do not worry about the details, just write all your conditions inside the
      //try block
      try {
         //write your code to check for all conditions and return true if it satisfies
         //all conditions else return false
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   /*
    * Name: move(Direction direction)
    * Purpose: The purpose of this method is to move the numbers on the 
    * grid in the direction stated in the parameter.
    * Parameters: "Direction direction": This determines the direction
    * which the numbers will move to.
    * Return: boolean: It will return true if the move was successful, and
    * false if the move was unsuccessful.
    */

   public boolean move(Direction direction) {

      if(direction.equals(Direction.UP)) {

         if(canMove(direction)) {

            for(int j = 0; j < grid[0].length; j++) {

               for(int i = 1, addInc = -1; i < grid.length; i++) {

                  if(i != 0 && grid[i][j] != 0) {

                     if(grid[i - 1][j] == 0) {

                        grid[i - 1][j] = grid[i][j];
                        grid[i][j] = 0;
                        i -= 2;
                     }

                     else if(grid[i - 1][j] == grid[i][j] && addInc != i - 1) {

                        grid[i - 1][j] *= 2;
                        grid[i][j] = 0;
                        score += grid[i - 1][j];
                        addInc = i - 1;
                     }
                  }
               }
            }

            return true;
         }
      }


      if(direction.equals(Direction.DOWN)) {

         if(canMove(direction)) {

            for(int j = 0; j < grid[0].length; j++) {

               for(int i = grid.length - 2, addInc = -1; i > -1; i--) {

                  if( i != grid.length - 1 && grid[i][j] != 0) {

                     if(grid[i + 1][j] == 0) {

                        grid[i + 1][j] = grid[i][j];
                        grid[i][j] = 0;
                        i += 2;
                     }

                     else if(grid[i + 1][j] == grid[i][j] && addInc != i + 1) {

                        grid[i + 1][j] *= 2;
                        grid[i][j] = 0;
                        score += grid[i + 1][j];
                        addInc = i + 1;
                     }
                  }
               }
            }

            return true;
         }
      }


      if(direction.equals(Direction.LEFT)) {

         if(canMove(direction)) {

            for(int i = 0; i < grid.length; i ++) {

               for(int j = 1, addInc = -1; j < grid[0].length; j++) {

                  if(j != 0 && grid[i][j] != 0) {

                     if(grid[i][j - 1] == 0) {

                        grid[i][j - 1] = grid[i][j];
                        grid[i][j] = 0;
                        j -= 2;
                     }

                     else if(grid[i][j - 1] == grid[i][j] && addInc != j - 1) {

                        grid[i][j - 1] *= 2;
                        grid[i][j] = 0;
                        score += grid[i][j - 1];
                        addInc = j - 1;
                     }
                  }
               }
            }

            return true;
         }
      }


      if(direction.equals(Direction.RIGHT)) {

         if(canMove(direction)) {

            for(int i = 0; i < grid.length; i++) {

               for(int j = grid[0].length - 2, addInc = -1; j > -1; j--) {

                  if(j != grid[0].length - 1 && grid[i][j] != 0) {

                     if(grid[i][j + 1] == 0) {

                        grid[i][j + 1] = grid[i][j];
                        grid[i][j] = 0;
                        j += 2;
                     }

                     else if(grid[i][j + 1] == grid[i][j] && addInc != j + 1) {

                        grid[i][j + 1] *= 2;
                        grid[i][j] = 0;
                        score += grid[i][j + 1];
                        addInc = j + 1;
                     }
                  }
               }
            }

            return true;
         }
      }

      return false;
   }

   /*
    * Name: isGameOver()
    * Purpose: The purpose of this method is to check of the game is over
    * or not, depending on if the grid can move in any direction or not.
    * Parameters: None
    * Return: boolean: It will return true if the game is over, and false
    * if it was not.
    */

   public boolean isGameOver() {

      Direction up = Direction.UP;
      Direction down = Direction.DOWN;
      Direction left = Direction.LEFT;
      Direction right = Direction.RIGHT;

      if(canMove(up))
         return false;

      else if(canMove(down))
         return false;

      else if(canMove(left))
         return false;

      else if(canMove(right))
         return false;

      else
         return true;
   }

   /*
    * Name: canMove(Direction direction)
    * Purpose: The purpose of this method is to check of the grid can move
    * in the direction provided in the parameter.
    * Parameters: "Direction direction": This determines the direction
    * that the method will check to see if it is possible to move in that
    * direction.
    * Return: boolean: It will return true if it is possible to move in the
    * given direction, and false if it cannot.
    */
   public boolean canMove(Direction direction) {

      if(direction.equals(Direction.UP)) {

         for(int i = 1; i < grid.length; i++) {

            for(int j = 0; j < grid[0].length; j++) {

               if(grid[i - 1][j] == 0 && grid[i][j] != 0) 

                  return true; 

               else if(grid[i - 1][j] == grid[i][j] && grid[i][j] != 0)
                  return true;
            }
         }
      }


      else if(direction.equals(Direction.DOWN)) {

         for(int i = grid.length - 2; i > -1; i--) {

            for(int j = 0; j < grid[0].length; j++) {

               if(grid[i + 1][j] == 0 && grid[i][j] != 0)
                  return true;

               else if(grid[i + 1][j] == grid[i][j] && grid[i][j] != 0)
                  return true;
            }
         }
      }


      else if(direction.equals(Direction.LEFT)) {

         for(int i = 0; i < grid.length; i++) {

            for(int j = 1; j < grid[0].length; j++) {

               if(grid[i][j - 1] == 0 && grid[i][j] != 0)
                  return true;

               else if(grid[i][j - 1] == grid[i][j] && grid[i][j] != 0)
                  return true;
            }
         }
      }


      else if(direction.equals(Direction.RIGHT)) {

         for(int i = 0; i < grid.length; i++) {

            for(int j = grid[0].length - 2; j > -1; j--) {

               if(grid[i][j + 1] == 0 && grid[i][j] != 0)
                  return true;

               else if(grid[i][j + 1] == grid[i][j] && grid[i][j] != 0)
                  return true;
            }
         }
      }

      return false;
   }

   // Return the reference to the 2048 Grid
   public int[][] getGrid() {
      return grid;
   }

   // Return the score
   public int getScore() {
      return score;
   }

   @Override
   public String toString() {
      StringBuilder outputString = new StringBuilder();
      outputString.append(String.format("Score: %d\n", score));
      for (int row = 0; row < GRID_SIZE; row++) {
         for (int column = 0; column < GRID_SIZE; column++)
            outputString.append(grid[row][column] == 0 ? "    -" :
                  String.format("%5d", grid[row][column]));

         outputString.append("\n");
      }
      return outputString.toString();
   }
}
