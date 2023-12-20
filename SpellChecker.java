
import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;

/**
 * SpellChecker class to check and correct spelling in a text file.
 */
public class SpellChecker {

  /**
   * Main method to run the spell checker.
   */
  public static void main(String[] args) {
    HashSet<String> dictionary = new HashSet<>();
    HashSet<String> misspelledWords = new HashSet<>();

    // Loading Dictionary
    try {
      Scanner filein = new Scanner(new File("./words.txt")); // I saved word in the same directory
      while (filein.hasNext()) {
        dictionary.add(filein.next().toLowerCase());
      }
      filein.close();
      System.out.println("Dictionary loaded. Word count: " + dictionary.size());
      List<String> commonWords = Arrays.asList("the", "be", "of", "and");
      for (String word : commonWords) {
        if (dictionary.contains(word)) {
          System.out.println("The word '" + word + "' is in the dictionary.");
        } else {
          System.out.println("The word '" + word + "' is NOT in the dictionary.");
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("Error: Unable to Load.");
      return;
    }

    // Get input file from user
    File inputFile = getInputFileNameFromUser();
    if (inputFile == null) {
      System.out.println("No file selected.");
      return;
    }

    // Check the file
    try {
      Scanner in = new Scanner(inputFile);
      // This command was provided in the lab
      in.useDelimiter("[^a-zA-Z]+");

      while (in.hasNext()) {
        String word = in.next().toLowerCase();
        if (!dictionary.contains(word) && !misspelledWords.contains(word)) {
          misspelledWords.add(word);
          TreeSet<String> corrections = corrections(word, dictionary);
          System.out.print(word + ": ");
          if (corrections.isEmpty()) {
            System.out.println("(no suggestions)");
          } else {
            for (String corr : corrections) {
              System.out.print(corr + ", ");
            }
            System.out.println();
          }
        }
      }
      in.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found.");
    }
  }

  static File getInputFileNameFromUser() {
    JFileChooser fileDialog = new JFileChooser();
    fileDialog.setDialogTitle("Select File for Input");
    int option = fileDialog.showOpenDialog(null);
    if (option != JFileChooser.APPROVE_OPTION)
      return null;
    else
      return fileDialog.getSelectedFile();
  }

  /**
   * Generates possible corrections for a misspelled word.
   * 
   * @param badWord    The misspelled word.
   * @param dictionary The set of words to use as a dictionary.
   * @return A TreeSet containing possible corrections.
   */
  static TreeSet<String> corrections(String badWord, HashSet<String> dictionary) {
    TreeSet<String> result = new TreeSet<>();

    // Delete any one letter
    for (int i = 0; i < badWord.length(); i++) {
      String s = badWord.substring(0, i) + badWord.substring(i + 1);
      if (dictionary.contains(s)) {
        result.add(s);
      }
    }

    // Change any letter to another letter
    for (int i = 0; i < badWord.length(); i++) {
      for (char ch = 'a'; ch <= 'z'; ch++) {
        String s = badWord.substring(0, i) + ch + badWord.substring(i + 1);
        if (dictionary.contains(s)) {
          result.add(s);
        }
      }
    }

    // Insert any letter at any point
    for (int i = 0; i <= badWord.length(); i++) {
      for (char ch = 'a'; ch <= 'z'; ch++) {
        String s = badWord.substring(0, i) + ch + badWord.substring(i);
        if (dictionary.contains(s)) {
          result.add(s);
        }
      }
    }

    // Swap any two neighboring characters
    for (int i = 0; i < badWord.length() - 1; i++) {
      String s = badWord.substring(0, i) + badWord.charAt(i + 1) + badWord.charAt(i) + badWord.substring(i + 2);
      if (dictionary.contains(s)) {
        result.add(s);
      }
    }

    // Insert a space at any point and check both words
    for (int i = 1; i < badWord.length(); i++) {
      String part1 = badWord.substring(0, i);
      String part2 = badWord.substring(i);
      if (dictionary.contains(part1) && dictionary.contains(part2)) {
        result.add(part1 + " " + part2);
      }
    }

    return result;
  } // correction()

}// SpellChecker()