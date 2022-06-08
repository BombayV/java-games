import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List
import java.lang.Math;

public class TicTacToe {
    // Fields
    private final char playerSymbol;
    private final char aiSymbol;
    private final int[][] drawPositions = {{0,0},{300,0},{600,0},{0,300},{300,300},{600,300},{0,600},{300,600},{600,600}};
    private final int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    private int turns;
    private int maxTurns;
    private char[] currentBoard;
    private boolean hasEnded;
    private Picture boardMap;


      /**
   * Constructor that initiates a Tic Tac Toe game
   */
    public TicTacToe() {
        boardMap = new Picture(900, 900);
        hasEnded = false;
        turns = 0;
        maxTurns = 10;
        currentBoard = new char[9];

        // Show a JOptionPane dialog box to pick a player symbol
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(null, "Pick the symbol you want to use", "Player Symbol", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            playerSymbol = 'X';
            aiSymbol = 'O';
        } else if (choice == 1) {
            playerSymbol = 'O';
            aiSymbol = 'X';
        } else {
            playerSymbol = ' ';
            aiSymbol = ' ';
            System.exit(0);
        }
        drawBoard();
        startGame();
    }
    
      /**
   * Constructor that allows the ai to start first
   * @param aiStarts boolean - AI starts first or not   */
    public TicTacToe(boolean aiStarts) {
        boardMap = new Picture(900, 900);
        hasEnded = false;
        if (aiStarts) {
            turns = 1;
            maxTurns = 11;
        } else {
            turns = 0;
            maxTurns = 10;
        }
        currentBoard = new char[9];

        // Show a JOptionPane dialog box to pick a player symbol
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(null, "Pick the symbol you want to use", "Player Symbol", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            playerSymbol = 'X';
            aiSymbol = 'O';
        } else if (choice == 1) {
            playerSymbol = 'O';
            aiSymbol = 'X';
        } else {
            playerSymbol = ' ';
            aiSymbol = ' ';
            System.exit(0);
        }
        drawBoard();
        startGame();
    }

    // Principal method of game that handles all the data validation from the ai and player
    public void startGame() {
        do {
            turns++;
            // Check if the game has reached 9 turns
            if (turns == maxTurns) {
                hasEnded = true;
                // Show a JOptionPane dialog box to show the game has ended in a draw
                JOptionPane.showMessageDialog(null, "The game has ended in a draw");
                playAgain();
            }

            // Alternate between the AI and the player
            if (turns % 2 == 0) {
                // AI's turn
                // Randomly pick a position between 1 and 9
                int position = (int) (Math.random() * 9) + 1;
                boolean validate = newPosition(position, aiSymbol);
                // If the position is not valid, pick another position
                while (!validate) {
                    position = (int) (Math.random() * 9) + 1;
                    validate = newPosition(position, aiSymbol);
                }

               checkForWin();
            } else {
                // Player's turn
                try {
                      int nextMove = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the position you want to place your symbol (1-9)\nIt must be empty."));
                      boolean validate = newPosition(nextMove, playerSymbol);
                      // Check if the move is valid, if not, keep asking for a valid move
                      while (!validate) {
                          nextMove = Integer.parseInt(JOptionPane.showInputDialog(null, "Invalid position.\n Try again with a number from (1-9)"));
                          validate = newPosition(nextMove, playerSymbol);
                      }
                      checkForWin();
                } catch (Exception ex) {
                     hasEnded = true;
                     System.exit(0);
                }
            }
        } while (!hasEnded);
    }

    // Setters
    public boolean newPosition(int pos, char symbol) {
        // Data validation
        if (pos < 0 || pos > 9) return false;
        if (currentBoard[pos - 1] == 'X' || currentBoard[pos - 1] == 'O') return false;
        
        // Symbols check
        if (symbol == 'X') {
            drawX(drawPositions[pos - 1][0], drawPositions[pos - 1][1]);
            currentBoard[pos - 1] = 'X';
        } else {
            drawO(drawPositions[pos - 1][0], drawPositions[pos - 1][1]);
            currentBoard[pos - 1] = 'O';
        }
        currentBoard[pos - 1] = symbol;
        return true;
    }
    
    // Getters
    public String getBoardState() {
      String xBoard = "";
      String oBoard = "";
      for (int i = 0; i < currentBoard.length; i++) {
         if (currentBoard[i] == 'X') {
            xBoard = xBoard + i + ", ";
         } else if (currentBoard[i] == 'O'){
            oBoard = oBoard +  i + ", ";
         }
      }
      return "X: " + xBoard + "\nO: " + oBoard;
    }
    
    public Picture getPicture() {
      return boardMap;
    }
    
    // Returns current turns[0] and maxTurns[1] number
    public int[] getTurnsData() {
      int[] data = {turns, maxTurns};
      return data;
    }
    
    // Returns player[0] and ai[1] symbol
    public char[] getPlayersData() {
      char[] data = {playerSymbol, aiSymbol};
      return data;
    }

    // Methods
    
    // Game methods
   public void checkForWin() {
        // Check for a win
        for (int i = 0; i < winningPositions.length; i++) {
            if (currentBoard[winningPositions[i][0]] == currentBoard[winningPositions[i][1]] && currentBoard[winningPositions[i][1]] == currentBoard[winningPositions[i][2]]) {
               gameEnded(currentBoard[winningPositions[i][0]]);
               break;
            }
        }
    }

    public void restartGame() {
        // Clean picture
        hasEnded = false;
        turns = 0;
        currentBoard = new char[9];
       // boardMap.hide();
        newWhiteBoard();
        drawBoard();
        startGame();
    }

    public void gameEnded(char winningChar) {
        // Check if winning char is empty
        if (winningChar == 'X' || winningChar == 'O') {
              hasEnded = false;
              if (winningChar == playerSymbol) {
                  // Show a JOptionPane dialog box to show the player has won
                  JOptionPane.showMessageDialog(null, "The player has won");
                  playAgain();
              } else {
                  // Show a JOptionPane dialog box to show the AI has won
                  JOptionPane.showMessageDialog(null, "The AI has won");
                  playAgain();
              }
        }
    }
    
    public void playAgain() {
          // Show a JOptionPane dialog box to ask if the user wants to play again
         int choice = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again?", JOptionPane.YES_NO_OPTION);
         if (choice == JOptionPane.YES_OPTION) {
             // Reset the game
             restartGame();
         } else {
             System.exit(0);
         }
    }

    // Drawing methods
    public void drawBoard() {
        Graphics g = boardMap.getGraphics();
        g.setColor(Color.BLACK);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g.drawRect(i * 300, j * 300, 300, 300);
            }
        }
        boardMap.show();
    }
    
    public void newWhiteBoard() {
      for (int x = 0; x < boardMap.getWidth(); x++) {
         for (int y = 0; y < boardMap.getHeight(); y++) {
            boardMap.getPixel(x, y).setColor(Color.WHITE);
         }
      }
      boardMap.repaint();
    }
    
    public void drawX(int x, int y) {
        Graphics g = boardMap.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.BLUE);
        g2.setStroke(new BasicStroke(6));
        g2.draw(new Line2D.Double(x, y, x + 300, y + 300));
        g2.draw(new Line2D.Double(x + 300, y, x, y + 300));
        boardMap.repaint();
    }

    public void drawO(int x, int y) {
        Graphics g = boardMap.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.RED);
        g2.setStroke(new BasicStroke(6));
        g2.draw(new Ellipse2D.Double(x, y, 300, 300));
        boardMap.repaint();
    }

    // Main Method
    // Basically just runs the game. At the moment it is
    // using the boolean param to represent that we
    // want the AI to go first.
    public static void main(String[] args) {
        new TicTacToe(true);
    }
}
