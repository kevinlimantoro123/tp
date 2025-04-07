package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "first";
        String secondPredicateKeyword = "second";

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeyword);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeyword);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // Exact match
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate("Alice");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // One typo
        predicate = new NameContainsKeywordsPredicate("Alixe");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-capitalised name
        predicate = new NameContainsKeywordsPredicate("alice");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Keyword matches part of a name
        predicate = new NameContainsKeywordsPredicate("alice");
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Yeo").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate("Carol");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // More than one typo
        predicate = new NameContainsKeywordsPredicate("Alese");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Part of keyword matches but not the other part
        predicate = new NameContainsKeywordsPredicate("Alice Tan");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Yeo").build()));

        // Keyword has more than what is available
        predicate = new NameContainsKeywordsPredicate("Alice Pauline Tan");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Tan").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keyword);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{name=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
