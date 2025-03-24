package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Helper functions for handling strings.
 */
public class StringUtil {

    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     *   Ignores case, but a full word match is required.
     *   <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     * @param sentence cannot be null
     * @param word cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        return Arrays.stream(wordsInPreppedSentence)
                .anyMatch(preppedWord::equalsIgnoreCase);
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Calculates recursively the edit distance between the substrings [0, i1) of word 1 and [0, i2) of word 2.
     * This is a helper method to the dynamic programming approach of the getEditDistance method.
     *
     * @param word1 The first word
     * @param word2 The second word
     * @param i1 The end index of the [0, i1) substring of word 1
     * @param i2 The end index of the [0, i2) substring of word 2
     * @param dp The 2-dimensional array for dynamic programming
     * @return The edit distance for the substrings [0, i1) of word and [0, i2) of word2
     */
    private static int getEditDistanceHelper(String word1, String word2, int i1, int i2, int[][] dp) {
        if (i1 < 0 || i2 < 0) {
            return word1.length() + word2.length();
        }

        if (dp[i1][i2] != -1) {
            return dp[i1][i2];
        }

        if (word1.charAt(i1) == word2.charAt(i2)) {
            dp[i1][i2] = getEditDistanceHelper(word1, word2, i1 - 1, i2 - 1, dp);
            return dp[i1][i2];
        }

        int distance = Math.min(getEditDistanceHelper(word1, word2, i1 - 1, i2, dp) + 1,
                Math.min(getEditDistanceHelper(word1, word2, i1, i2 - 1, dp) + 1,
                        getEditDistanceHelper(word1, word2, i1 - 1, i2 - 1, dp) + 1
                )
        );

        dp[i1][i2] = distance;
        return distance;
    }

    /**
     * Returns the edit distance between two words.<br><br>
     *
     * The <b>edit distance</b> between two words is the smallest number of
     * operations on the first word to get to the second word. The operations include:<br>
     * <ul>
     *     <li>Replace a letter of the original word with another letter.</li>
     *     <li>Remove a letter from the original word.</li>
     *     <li>Add a letter to the original word.</li>
     * </ul>
     *
     * @param word1 The first word
     * @param word2 The second word
     * @return The edit distance between two words
     */
    private static double getEditDistance(String word1, String word2) {
        int l1 = word1.length();
        int l2 = word2.length();

        word1 = " " + word1;
        word2 = " " + word2;

        int[][] dp = new int[l1 + 1][l2 + 1];

        for (int r = 0; r <= l1; r++) {
            for (int c = 0; c <= l2; c++) {
                dp[r][c] = r == 0
                        ? c
                        : c == 0
                        ? r
                        : -1;
            }
        }

        return getEditDistanceHelper(word1, word2, l1, l2, dp);
    }

    /**
     * Computes the closeness of the task's description to the user's keyword.<br>
     * This returns 0 if the keyword is contained within the task's description. Else, split the keywords and
     * description into two String arrays and perform matching for each keyword word.
     *
     * @param stringToCompareTo The String to compare the keywords to
     * @param keyword The user's keyword(s), which can be one word or multiple words separated by whitespace
     * @return The closeness of the task's description to the user's keyword
     */
    public static double computeCloseness(String stringToCompareTo, String keyword) {
        String[] keywordWords = keyword.split("\\s+");
        String[] descriptionWords = stringToCompareTo.split("\\s+");

        double distance = 0;

        // for each keyword word, compute the smallest edit distance to any of the description word
        for (String kw : keywordWords) {
            double minDistanceToKw = Double.MAX_VALUE;

            for (String dw : descriptionWords) {
                if (dw.contains(kw)) {
                    minDistanceToKw = 0;
                    break;
                }

                minDistanceToKw = Math.min(minDistanceToKw, getEditDistance(dw, kw));
            }

            // sum the smallest edit distances for each keyword word up
            distance += minDistanceToKw;
        }

        return distance;
    }
}
