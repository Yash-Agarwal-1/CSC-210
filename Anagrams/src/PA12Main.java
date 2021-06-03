
/*
 * Author: Yash Agarwal
 * CS 210, Spring 21
 * Purpose: This file takes in a dictionary with words and a given word
 * along with a word limit and calculates all the anagrams of that given word.
 * An anagram is a word or phrase made by rearranging the letters of another word or
 * phrase. For example, the words ”midterm” and ”trimmed” are anagrams as well as,
 * ”Clint Eastwood” and ”old west action” are anagrams. It first calculates all the
 * words in the dictionary that can be formed using the given word and then calculates
 * the multi word anagrams that can be formed using those words, given that all letters
 * of the original words are used up. It doesn't put a limit on the number of words if the limit
 * is 0. It uses the LetterInventory class to make things easier for the letter operations.
 * 
 * Infile- .txt file with a word on every line
 * 
 * Sample input- PublicTestCases/dict1.txt barbarabush 0
 * 
 * Output: 
 * Phrase to scramble: barbarabush
 * 
 * All words found in barbarabush:
 *[abash, aura, bar, barb, brush, bus, hub, rub, shrub, sub]
 *
 *Anagrams for barbarabush:
 *[abash, bar, rub]
 *[abash, rub, bar]
 *[bar, abash, rub]
 *[bar, rub, abash]
 *[rub, abash, bar]
 *[rub, bar, abash]
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PA12Main {

    public static void main(String[] args) {
        System.out.println("Phrase to scramble: " + args[1]);
        System.out.println();
        List<String> words = readFile(args[0]);
        List<String> validWords = new ArrayList<String>();
        System.out.println("All words found in " + args[1] + ":");
        findValidWords(words, validWords, args[1]);
        System.out.println(validWords);
        System.out.println();
        LetterInventory obj = new LetterInventory(args[1]);
        System.out.println("Anagrams for " + args[1] + ":");
        findAnagrams(validWords, Integer.valueOf(args[2]), obj,
                new ArrayList<String>());
    }

    /*
     * Helper function to read in the dictionary file and return a List of
     * words contained in that dictionary
     * 
     * @param- filename- the location of the file
     * 
     * @return- the list of all the words contained in the dictionary
     */
    public static List<String> readFile(String filename) {
        List<String> words = new ArrayList<String>();
        File fl = new File(filename);
        Scanner sc = null;
        try {
            sc = new Scanner(fl);
        } catch (FileNotFoundException e) {
            System.out.println("Dictionary not found");
        }
        while (sc.hasNext()) {
            words.add(sc.next().trim());
        }
        return words;
    }

    /*
     * Helper function to find all the words contained in the dictionary
     * that can be formed using the inputted word
     * 
     * @param- words- The list of the words in the dictionary
     * soFar- the reference to the list to store the valid words
     * str- the inputted word
     */
    public static void findValidWords(List<String> words, List<String> soFar, String str) {
        LetterInventory originalWord = new LetterInventory(str);
        for (String word : words) {
            LetterInventory obj = new LetterInventory(word);
            if (originalWord.contains(obj))
                soFar.add(word);
            }

        }

        /*
         * Function which finds the Anagrams of the given word from
         * the list of valid words
         * 
         * @param- words- the list of valid words
         * k- the number of max words that the anagram can be contained
         * original- the LetterInventory object of the inputed word
         * soFar- the list to store the possible word combinations
         */
        public static void findAnagrams(List<String> words, int k,
                LetterInventory original, List<String> soFar) {
            // base condition when no words are left
            if (words.size() == 0) {
            }
            // when all the letters of the original word
            // have been used
            if (original.size() == 0) {
                if (soFar.size() <= k || k == 0) {
                System.out.println(soFar);
                }
            } else {
                for (String s : words) {
                    // backtracks as soon as the size of soFar exceeds k and
                    // original still has letters left
                    if (original.size() == 0 || soFar.size() != k || k == 0) {
                    if (original.contains(s)) {
                        // choose
                        soFar.add(s);
                        original.subtract(s);
                        // explore
                        findAnagrams(words, k, original, soFar);
                        // unchoose
                        soFar.remove(s);
                        original.add(s);
                    }
                }

                }
        }
    }

}


