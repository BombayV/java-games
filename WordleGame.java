import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
public class WordleGame {
    // Fields
    final private Picture map;
    private String word;
    private int tries;
    private boolean finalized;

    // Constructors
    public WordleGame() throws FileNotFoundException {
        Scanner words = new Scanner(new File(".\\words.txt"));
        ArrayList<String> wordList = new ArrayList();
        while (words.hasNext()) {
            wordList.add(words.next());
        }
        words.close();

        // Pick a random word from the list
        Random rand = new Random();
        int randIndex = rand.nextInt(wordList.size());
        word = wordList.get(randIndex).toLowerCase();
         System.out.println(word);
        tries = 0;
        finalized = false;
        map = new Picture(500, 600);
        drawMap();
        gameHandler();
    }

    public WordleGame(String w) {
        if (w.length() != 5) {
            throw new IllegalArgumentException("Word must be 5 letters long.");
        }
        tries = 0;
        finalized = false;
        word = w.toLowerCase();
        map = new Picture(500, 600);
        drawMap();
        gameHandler();
    }

    public void gameHandler() {
        while (tries < 6 && !finalized) {
            String input = JOptionPane.showInputDialog("Enter a 5 letter word");
            while (input != null && input.length() != 5) {
                input = JOptionPane.showInputDialog("Enter a 5 letter word");
            }
            if (input == null) System.exit(0);
            if (input.toLowerCase().equals(word)) {
                finalized = true;
                drawWord(input.toLowerCase(), tries);
                JOptionPane.showMessageDialog(null, "You guessed the word correctly!");
                System.exit(0);
            } else {
                drawWord(input, tries);
                tries++;
            }
        }
        JOptionPane.showMessageDialog(null, "You didn't guess the word.\n The word was: " + word);
        System.exit(0);
    }

    // Getters
    public String getWord() {
        return word;
    }

    // Setters
    public void setWord(String w) {
        if (w.length() != 5) {
            throw new IllegalArgumentException("Word must be 5 letters long.");
        }
        word = w;
    }

    // Methods
    public String toString() {
        return word;
    }

    public void drawWord(String w, int y) {
        // Loop through the word and check if the letter is in the word or if the letter is in the word but in the wrong place
        for (int i = 0; i < w.length(); i++) {
            if (w.charAt(i) == word.charAt(i)) {
                drawCorrect(i, y);
            } else if (word.contains(w.charAt(i) + "")) {
                drawIncorrect(i, y);
            } else {
                drawMissing(i, y);
            }
            drawLetter(w.charAt(i) + "", i, y);
        }
    }

    // Drawing methods
    public void drawMap() {
        Graphics g = map.getGraphics();
        g.setColor(Color.BLACK);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 6; y++) {
                g.drawRect(x * 100, y * 100, 100, 100);
            }
        }
        map.show();
    }

    public void drawLetter(String letter, int x, int y) {
        Graphics g = map.getGraphics();
        g.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 70));
        g.setColor(Color.WHITE);
        g.drawString(letter.toUpperCase(), x * 100 + 27, y * 100 + 77);
        map.repaint();
    }

    // Square colors
    public void drawCorrect(int x, int y) {
        Graphics g = map.getGraphics();
        g.setColor(new Color(106 ,170, 100));
        g.fillRect((x * 100) + 1, (y * 100) + 1, 99, 99);
        map.repaint();
    }

    public void drawIncorrect(int x, int y) {
        Graphics g = map.getGraphics();
        g.setColor(new Color(201, 180, 88));
        g.fillRect((x * 100) + 1, (y * 100) + 1, 99, 99);
        map.repaint();
    }

    public void drawMissing(int x, int y) {
        Graphics g = map.getGraphics();
        g.setColor(new Color(120, 124, 126));
        g.fillRect((x * 100) + 1, (y * 100) + 1, 99, 99);
        map.repaint();
    }

    // Main method
    // Initializes the wordle game with a random word.
    // If string is passed (< 5 chars),
    // that string will be used as a word.
    // Throws exception due to using file class.
    public static void main(String[] args) throws Exception {
        new WordleGame();
    }
}
