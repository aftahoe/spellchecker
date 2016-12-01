package challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static challenge.SpellChecker.DICT_DISK_LOCATION;
import static challenge.SpellChecker.isVowel;
import static challenge.SpellChecker.vowels;

/**
 * Created by annawang on 11/29/16.
 */
public class MisSpelledWordGenerator {
    // randomly repeat characters but the max repeat times is 4
    private static final int MAX_REPEAT_TIMES = 4;
    private static Random random = new Random(System.currentTimeMillis());

    private List<String> wordsList;

    public MisSpelledWordGenerator() {
        wordsList = new ArrayList<String>();
        loadDictWords(DICT_DISK_LOCATION);
    }

    /**
     * Given a word, randomly select the characters to repeat with randomly times. (max repeat time is 4)
     *
     * @param word
     * @return
     */
    public String repeatCharacters(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            sb.append(c);
            if (random.nextFloat() <= 0.5) {
                for (int j = 0; j < random.nextInt(MAX_REPEAT_TIMES); j++) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }


    public String changeUpperCase(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (random.nextFloat() <= 0.5) {
                sb.append(Character.toString(c).toLowerCase());
            } else {
                sb.append(Character.toString(c).toUpperCase());
            }
        }
        return sb.toString();
    }

    public String changeVowels(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (isVowel(c) && random.nextFloat() <= 0.5) {
                char randomVowel = vowels[random.nextInt(vowels.length)];
                // If this vowel has the same previous character or next character, don't change the vowel
                // EX: ee    -> don't change it
                // If changing the vowel to the random vowel makes different consecutive vowels become the same, don't change it.
                // EX: ea  -> ee    don't change it.
                if (hasConsecutiveSameVowels(word, i, c) || becomesConsecutiveSameVowelsAfterChanging(word, sb, i, c, randomVowel)) {
                    sb.append(c);
                } else {
                    sb.append(randomVowel);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public int getWordsListSize() {
        return wordsList.size();
    }


    public String generateMisSpelledWord() {
        String randomWord = wordsList.get(random.nextInt(wordsList.size()));

        return changeUpperCase(repeatCharacters(changeVowels(randomWord)));
    }

    private boolean becomesConsecutiveSameVowelsAfterChanging
            (String word, StringBuilder sb, int i, char c, char randomVowel) {
        // After change character "c" at index "i" to the new vowel "randomVowel", 2 different consecutive vowels become the same
        return
                // check the next character
                (i + 1 < word.length() && isVowel(word.charAt(i + 1)) &&
                        word.charAt(i + 1) != c && randomVowel == word.charAt(i + 1)) ||
                        // check the previous character
                        (i - 1 >= 0 && isVowel(word.charAt(i - 1)) &&
                                word.charAt(i - 1) != c && randomVowel == sb.charAt(i - 1));
    }

    private boolean hasConsecutiveSameVowels(String word, int index, char c) {
        // At character index, check does the previous character or next character have the same vowel
        return (index + 1 < word.length() && isVowel(word.charAt(index + 1)) && c == word.charAt(index + 1)) ||
                (index - 1 >= 0 && isVowel(word.charAt(index - 1)) && c == word.charAt(index - 1));
    }
    /**
     * Load words in the dictionary file line by line to a array list
     *
     * @param dictFile
     */
    private void loadDictWords(String dictFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dictFile));

            String word = reader.readLine();
            while (word != null) {
                word = word.trim().toLowerCase();
                wordsList.add(word);

                word = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot find dictionary file at location: " + dictFile);
        } catch (IOException e) {
            System.out.println("Error: Failed to open and read dictionary file at location: " + dictFile);
        }
    }

    public static void main(String[] args) {
        MisSpelledWordGenerator generator = new MisSpelledWordGenerator();
        SpellChecker checker = new SpellChecker();
        System.out.print("> Press Enter to Generate the Word with Spelling Mistakes and See the Suggestion Word");
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String word = in.nextLine();
            word = word.trim().toLowerCase();
            if (word.length() < 0) {
                continue;
            }

            String result = generator.generateMisSpelledWord();
            System.out.println("> spelling mistake word: " + result);
            List<String> suggestions = checker.getRecommendWordsList(result);
            System.out.println("> Suggestion:            " + (suggestions.size() == 0 ? "NO SUGGESTION " : suggestions.get(0)));
            System.out.println("> ");
            System.out.print("> Press Enter to Generate the Word with Spelling Mistakes and See the Suggestion Word");
        }
    }

}
