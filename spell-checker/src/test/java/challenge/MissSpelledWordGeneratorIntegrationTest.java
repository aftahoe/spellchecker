package challenge;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by annawang on 11/29/16.
 */
public class MissSpelledWordGeneratorIntegrationTest {
    private MisSpelledWordGenerator generator = new MisSpelledWordGenerator();
    private SpellChecker checker = new SpellChecker();

    @Test
    public void generateMissSpelledWord_AlwaysWithSuggestions() throws Exception {

        int count = 0;
        // run a lot of times and make sure there is always a recommend list
        while (count < generator.getWordsListSize() * 2) {
            String missSpelledWord = generator.generateMisSpelledWord();

            Assert.assertNotEquals(0, checker.getRecommendWordsList(missSpelledWord).size());
            count++;
        }
    }
}
