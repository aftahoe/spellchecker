package challenge;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by annawang on 11/29/16.
 */
public class SpellCheckerTest {

    private SpellChecker checker = new SpellChecker();

    @Test
    public void removeRepeatedCharacters_NullString() throws Exception {
        Assert.assertEquals(null, checker.removeRepeatCharacters(null));
    }

    @Test
    public void removeRepeatedCharacters_EmptyString() throws Exception {
        Assert.assertEquals("", checker.removeRepeatCharacters(""));
    }

    @Test
    public void removeRepeatedCharacters_StringWithoutRepeatedCharacters() throws Exception {
        Assert.assertEquals("abcd", checker.removeRepeatCharacters("abcd"));
    }

    @Test
    public void removeRepeatedCharacters_StringWithCharactersRepeatOnce() throws Exception {
        Assert.assertEquals("a", checker.removeRepeatCharacters("aa"));

    }

    @Test
    public void removeRepeatedCharacters_StringWithCharactersRepeatMultipleTimes() throws Exception {
        Assert.assertEquals("a", checker.removeRepeatCharacters("aaa"));
    }

    @Test
    public void removeRepeatedCharacters_StringWithMultipleRepeatedCharacters() throws Exception {
        Assert.assertEquals("acb", checker.removeRepeatCharacters("aacbb"));
    }

    @Test
    public void replaceVowels_NullString() throws Exception {
        Assert.assertEquals(null, checker.replaceVowels(null));
    }

    @Test
    public void replaceVowels_StringWithOneCharacter() throws Exception {
        Assert.assertEquals("*", checker.replaceVowels("a"));
    }

    @Test
    public void replaceVowels_StringWithoutSequentialVowels() throws Exception {
        Assert.assertEquals("*bc*d", checker.replaceVowels("abced"));
    }

    @Test
    public void replaceVowels_StringWithSequentialVowels() throws Exception {
        Assert.assertEquals("p**pl*", checker.replaceVowels("people"));
    }

    @Test
    public void replaceVowels_StringWithoutVowels() throws Exception {
        Assert.assertEquals("b", checker.replaceVowels("b"));
    }


    @Test
    public void editDistance_TestCase1() throws Exception {
        Assert.assertEquals(1, checker.editDistance("ISLANDER", "SLANDER"));
    }

    @Test
    public void editDistance_TestCase2() throws Exception {
        Assert.assertEquals(3, checker.editDistance("MART", "KARMA"));
    }

    @Test
    public void editDistance_TestCase3() throws Exception {
        Assert.assertEquals(3, checker.editDistance("KITTEN", "SITTING"));
    }

    @Test
    public void editDistance_TestCase4() throws Exception {
        Assert.assertEquals(5, checker.editDistance("INTENTION", "EXECUTION"));
    }

    @Test
    public void getRecommendWordsList_people() throws Exception {
        Assert.assertEquals("people", checker.getRecommendWordsList("people").get(0));
    }

    @Test
    public void getRecommendWordsList_peepple() throws Exception {
        Assert.assertEquals("people", checker.getRecommendWordsList("peepple").get(0));
    }

    @Test
    public void getRecommendWordsList_sheeeeep() throws Exception {
        Assert.assertEquals("sheep", checker.getRecommendWordsList("sheeeeep").get(0));
    }

    @Test
    public void getRecommendWordsList_sheeple() throws Exception {
        Assert.assertEquals(0, checker.getRecommendWordsList("sheeple").size());
    }

    @Test
    public void getRecommendWordsList_inSIDE() throws Exception {
        Assert.assertEquals("inside", checker.getRecommendWordsList("inSIDE").get(0));
    }

    @Test
    public void getRecommendWordsList_jjoobbb() throws Exception {
        Assert.assertEquals("job", checker.getRecommendWordsList("jjoobbb").get(0));
    }

    @Test
    public void getRecommendWordsList_weke() throws Exception {
        Assert.assertEquals("wake", checker.getRecommendWordsList("weke").get(0));
    }

    @Test
    public void getRecommendWordsList_CUNsperrICY() throws Exception {
        Assert.assertEquals("conspiracy", checker.getRecommendWordsList("CUNsperrICY").get(0));
    }
}
