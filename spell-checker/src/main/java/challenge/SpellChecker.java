package challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by annawang on 11/29/16.
 */
public class SpellChecker {
    private static final String NO_SUGGESTION = "NO SUGGESTION";
    private static final String WORD_FREQUENCY = "word_frequency";

    public static final String DICT_DISK_LOCATION = "/usr/share/dict/words";
    public static char[] vowels = {'a', 'e', 'i', 'o', 'u'};

    private Map<String, List<String>> wordsDict;
    private Map<String, Integer> wordFrequencyRank;

    public SpellChecker() {
        wordsDict = new HashMap<String, List<String>>();
        wordFrequencyRank = new HashMap<String, Integer>();
        loadDictWords(DICT_DISK_LOCATION);
        loadWordsFrequency();
    }

    /**
     * Check is character c vowel or not
     *
     * @param c
     * @return
     */
    public static boolean isVowel(char c) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
            return true;
        }
        return false;
    }

    /**
     * Calculate the edit distance of word1 and word2
     *
     * @param word1
     * @param word2
     * @return edit distance
     */
    public int editDistance(String word1, String word2) {

        int length1 = word1.length();
        int length2 = word2.length();

        int dp[][] = new int[length1 + 1][length2 + 1];

        // Use dynamic programming method to find out the edit distance
        for (int i = 0; i <= length1; i++) {
            for (int j = 0; j <= length2; j++) {

                if (i == 0) {// If first string is empty, insert all characters of second string
                    dp[i][j] = j;
                } else if (j == 0) { // If second string is empty, remove all characters of first string
                    dp[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) { // If last characters are same
                    dp[i][j] = dp[i - 1][j - 1];
                } else {// If last character are different, consider all possibilities and find minimum
                    dp[i][j] = 1 + min(
                            dp[i][j - 1],  // Insert
                            dp[i - 1][j],             // Remove
                            dp[i - 1][j - 1]);        // Replace
                }
            }
        }

        return dp[length1][length2];
    }

    /**
     * Given the mis-spelled word, get the sorted recommendation list.
     * Sort by the recommended word with the higher possibility.
     *
     * @param word
     * @return recommendation list
     */
    public List<String> getRecommendWordsList(final String word) {
        String pattern = removeRepeatCharacters(word.trim().toLowerCase());
        pattern = replaceVowels(pattern);

        List<String> candidates = new ArrayList<String>();
        if (wordsDict.get(pattern) != null) {
            if (wordsDict.get(pattern).contains(word)) { // If the word already matches, return directly
                candidates.add(word);
                return candidates;
            } else {
                candidates.addAll(wordsDict.get(pattern));
            }
        }

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '*') {
                String newPattern = pattern.substring(0, i) + '*' + pattern.substring(i, pattern.length());
                if (wordsDict.get(newPattern) != null) {
                    candidates.addAll(wordsDict.get(newPattern));
                }
            }
        }
        // Sort by 1. edit distance 2. word frequency
        Collections.sort(candidates, new Comparator<String>() {
            public int compare(String o1, String o2) {
                int firstDistance = editDistance(o1, word);
                int secondDistance = editDistance(o2, word);
                if (firstDistance - secondDistance != 0) {
                    return firstDistance - secondDistance;
                }

                Integer rank1 = wordFrequencyRank.get(o1) == null ? Integer.MAX_VALUE : wordFrequencyRank.get(o1);
                Integer rank2 = wordFrequencyRank.get(o2) == null ? Integer.MAX_VALUE : wordFrequencyRank.get(o2);
                return rank1.compareTo(rank2);
            }
        });

        return candidates;
    }

    /**
     * remove sequential repeat characters in a string
     *
     * @param word
     * @return string without sequential repeat characters
     */
    public String removeRepeatCharacters(String word) {
        if (word == null || word.length() <= 1) {
            return word;
        }

        StringBuilder newString = new StringBuilder();
        char checkCharacter = word.charAt(0);
        newString.append(checkCharacter);
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) != checkCharacter) {
                newString.append(word.charAt(i));
                checkCharacter = word.charAt(i);
            }
        }
        return newString.toString();
    }

    /**
     * replace vowels with "*" in a string
     *
     * @param word
     * @return
     */
    public String replaceVowels(String word) {
        if (word == null) return word;

        StringBuilder newString = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(newString.charAt(i))) {
                newString.setCharAt(i, '*');
            }
        }
        return newString.toString();
    }

    private int min(int x, int y, int z) {
        if (x < y && x < z) return x;
        if (y < x && y < z) return y;
        else return z;
    }

    /**
     * load the dictionary words line by line from a file.
     * transform each word into the pattern (ex: people -> p**pl*).
     * use the pattern as the key of the hash map.
     * Since multiple words may have the same pattern,
     * all words with the same pattern will be stored in the linked list which is the value of the hash map.
     *
     * @param dictFile
     */
    private void loadDictWords(String dictFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dictFile));

            String word = reader.readLine();
            while (word != null) {
                word = word.trim().toLowerCase();
                String pattern = removeRepeatCharacters(word);
                pattern = replaceVowels(pattern);
                if (wordsDict.get(pattern) != null) {
                    wordsDict.get(pattern).add(word);
                } else {
                    List<String> words = new ArrayList<String>();
                    words.add(word);
                    wordsDict.put(pattern, words);
                }

                word = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot find dictionary file at location: " + dictFile);
        } catch (IOException e) {
            System.out.println("Error: Failed to open and read dictionary file at location: " + dictFile);
        }
    }

    /**
     * Load word frequency to a hash map from the resource file which is downloaded from Wikipedia.
     * key of the hash map is the word. value of the hash map is its frequent rank.
     */
    private void loadWordsFrequency() {
        try {
            InputStream stream = this.getClass().getResourceAsStream(WORD_FREQUENCY);
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line = reader.readLine();

                while (line != null) {
                    String[] strRank = line.split("\t");
                    wordFrequencyRank.put(strRank[1], Integer.parseInt(strRank[0]));
                    line = reader.readLine();
                }
                stream.close();
            }

        } catch (IOException e) {
            System.out.println("Error: Failed to open and read word frequency file");
        }
    }

    public static void main(String[] args) {

        SpellChecker checker = new SpellChecker();
        System.out.print("> ");
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String word = in.nextLine();
            word = word.trim().toLowerCase();
            if (word.length() < 0) {
                continue;
            }

            List<String> result = checker.getRecommendWordsList(word);
            if (result.size() == 0) {
                System.out.println(NO_SUGGESTION);
            } else
                System.out.println(result.get(0));
            System.out.print("> ");
        }
    }
}