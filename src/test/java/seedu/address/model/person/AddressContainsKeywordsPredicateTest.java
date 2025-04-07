package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class AddressContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeyword = "first";
        String secondPredicateKeyword = "second";

        AddressContainsKeywordsPredicate firstPredicate = new AddressContainsKeywordsPredicate(firstPredicateKeyword);
        AddressContainsKeywordsPredicate secondPredicate = new AddressContainsKeywordsPredicate(secondPredicateKeyword);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        AddressContainsKeywordsPredicate firstPredicateCopy =
                new AddressContainsKeywordsPredicate(firstPredicateKeyword);
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
        // Exact keyword
        AddressContainsKeywordsPredicate predicate =
                new AddressContainsKeywordsPredicate("Blk 123 dummy St 31");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31").build()));

        // Less than 3 typos (non-numeric part)
        predicate = new AddressContainsKeywordsPredicate("Blk 123 dummi St 31, #06-41");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31, #06-42").build()));

        // Keyword matches some part of address
        predicate = new AddressContainsKeywordsPredicate("Blk 123");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Non-matching keyword
        AddressContainsKeywordsPredicate predicate =
                new AddressContainsKeywordsPredicate("Blk 321 dummy St 31");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123 example St 31").build()));

        // More than two typos (non-numeric part)
        predicate = new AddressContainsKeywordsPredicate("Blk 123 dunni St 31");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31").build()));

        // One typo (numeric part)
        predicate = new AddressContainsKeywordsPredicate("Blk 124 dummy St 31");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31").build()));

        // Keyword has more than what is available
        predicate = new AddressContainsKeywordsPredicate("Blk 123 dummy St 31, #06-40");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123 dummy St 31").build()));
    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate(keyword);

        String expected = AddressContainsKeywordsPredicate.class.getCanonicalName() + "{address=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
