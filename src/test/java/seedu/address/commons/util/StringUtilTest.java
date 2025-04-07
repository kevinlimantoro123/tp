package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: empty strings
        assertFalse(StringUtil.isNonZeroUnsignedInteger("")); // Boundary value
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: not a number
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // EP: zero
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: zero as prefix
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: signed numbers
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: numbers with white space
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 ")); // Leading/trailing spaces
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0")); // Spaces in the middle

        // EP: number larger than Integer.MAX_VALUE
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // EP: valid numbers, should return true
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1")); // Boundary value
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null, false, null));
    }

    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  ", false, null));
    }

    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB", false, null));
    }

    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc", false, null));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *   - one word
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // Empty sentence
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc", false, null)); // Boundary case
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123", false, null));

        // Matches a partial word only
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb", false, null)); // Sentence word bigger than query word
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb", false, null)); // Query word bigger than sentence word

        // Matches exactly or approximately to sentence, one word only
        assertTrue(StringUtil.containsWordIgnoreCase("aaa B ccc", "B", true, 2));
        assertTrue(StringUtil.containsWordIgnoreCase("aaa Bb ccc", "B", true, 2));

        // Matches word in the sentence, different upper/lower case letters
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb", false, null)); // First word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1", false, null)); // Last word (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa", false, null)); // Sentence has extra spaces
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa", false, null)); // Only one word in sentence (boundary case)
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  ", false, null)); // Leading/trailing spaces

        // Matches multiple words in sentence
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB", false, null));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

    @Test
    public void computeCloseness_identicalStrings_return0() {
        assertEquals(0, StringUtil.computeCloseness("hello", "hello"));
    }

    @Test
    public void computeCloseness_slightlyOffVersusCompletelyUnrelated_returnSlightlyOff() {
        // test for one-word sequence
        String toCompareEmail = "ilovecraftconnect@example.com";
        String correctEmail = "ilovecraftconnect@example.com";
        String extraCharEmail = "ilovecraftconnect@examples.com.sg";
        String missingCharEmail = "icraftconnect@example.com";
        String wrongCharEmail = "ilikecraftconnect@example.com";
        String completelyOffEmail = "alexyeoh@dummy.gov.tv";

        double extraCharEmailCloseness = StringUtil.computeCloseness(toCompareEmail, extraCharEmail);
        double correctEmailCloseness = StringUtil.computeCloseness(toCompareEmail, correctEmail);
        double missingCharEmailCloseness = StringUtil.computeCloseness(toCompareEmail, missingCharEmail);
        double wrongCharEmailCloseness = StringUtil.computeCloseness(toCompareEmail, wrongCharEmail);
        double completelyOffEmailCloseness = StringUtil.computeCloseness(toCompareEmail, completelyOffEmail);

        assertEquals(0, correctEmailCloseness);
        assertTrue(extraCharEmailCloseness < completelyOffEmailCloseness);
        assertTrue(wrongCharEmailCloseness < completelyOffEmailCloseness);
        assertTrue(missingCharEmailCloseness < completelyOffEmailCloseness);

        // test for multiple-word sequences
        String toCompareString = "booth rental CraftFes";
        String extraOneChar = "booth rental CraftFest";
        String missingOneChar = "booth renta CraftFes";
        String wrongOneChar = "booth rental CraftFet";
        String completelyUnrelated = "completely unrelated";
        String anotherUnrelated = "boots supplier ConnectiCon";

        double extraOneCharCloseness = StringUtil.computeCloseness(toCompareString, extraOneChar);
        double missingOneCharCloseness = StringUtil.computeCloseness(toCompareString, missingOneChar);
        double wrongOneCharCloseness = StringUtil.computeCloseness(toCompareString, wrongOneChar);
        double completelyUnrelatedCloseness = StringUtil.computeCloseness(toCompareString, completelyUnrelated);
        double anotherUnrelatedCloseness = StringUtil.computeCloseness(toCompareString, anotherUnrelated);

        assertTrue(extraOneCharCloseness < completelyUnrelatedCloseness);
        assertTrue(missingOneCharCloseness < completelyUnrelatedCloseness);
        assertTrue(wrongOneCharCloseness < completelyUnrelatedCloseness);
        assertTrue(extraOneCharCloseness < anotherUnrelatedCloseness);
        assertTrue(missingOneCharCloseness < anotherUnrelatedCloseness);
        assertTrue(wrongOneCharCloseness < anotherUnrelatedCloseness);

        String extraChars = "booths rental CraftFestival";
        String missingChars = "booth rent CraftFes";
        String wrongChars = "stall rental CraftFetival";

        double extraCharsCloseness = StringUtil.computeCloseness(toCompareString, extraChars);
        double missingCharsCloseness = StringUtil.computeCloseness(toCompareString, missingChars);
        double wrongCharsCloseness = StringUtil.computeCloseness(toCompareString, wrongChars);

        assertTrue(extraCharsCloseness < anotherUnrelatedCloseness);
        assertTrue(missingCharsCloseness < anotherUnrelatedCloseness);
        assertTrue(wrongCharsCloseness < anotherUnrelatedCloseness);
        assertTrue(extraCharsCloseness < completelyUnrelatedCloseness);
        assertTrue(missingCharsCloseness < completelyUnrelatedCloseness);
        assertTrue(wrongCharsCloseness < completelyUnrelatedCloseness);
    }

    @Test
    public void computeCloseness_differentCaseStrings() {
        // identical content, different cases -> return 0
        assertEquals(0, StringUtil.computeCloseness("hello", "HELLO"));
        assertEquals(0, StringUtil.computeCloseness("craftConnect", "CraftconnecT"));

        // slightly off content, different case
        String original = "hello world";
        String missingOneChar = "HeLlO wOrL";
        String extraOneChar = "hElLo, WoRlD";
        String wrongOneChar = "HELLA world";

        double missingOneCharCloseness = StringUtil.computeCloseness(original, missingOneChar);
        double extraOneCharCloseness = StringUtil.computeCloseness(original, extraOneChar);
        double wrongOneCharCloseness = StringUtil.computeCloseness(original, wrongOneChar);

        // completely unrelated content, different case
        String notRelated = "Am I high?";
        String alsoNotRelated = "Get some sleep, brother.";

        double notRelatedCloseness = StringUtil.computeCloseness(original, notRelated);
        double alsoNotRelatedCloseness = StringUtil.computeCloseness(original, alsoNotRelated);

        assertTrue(missingOneCharCloseness < notRelatedCloseness);
        assertTrue(extraOneCharCloseness < notRelatedCloseness);
        assertTrue(wrongOneCharCloseness < notRelatedCloseness);

        assertTrue(missingOneCharCloseness < alsoNotRelatedCloseness);
        assertTrue(extraOneCharCloseness < alsoNotRelatedCloseness);
        assertTrue(wrongOneCharCloseness < alsoNotRelatedCloseness);
    }
}
