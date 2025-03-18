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
        // One keyword
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate("Blk 123");
        assertTrue(predicate.test(new PersonBuilder().withAddress("Blk 123").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate("");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123").build()));

        // Non-matching keyword
        predicate = new AddressContainsKeywordsPredicate("Blk 321");
        assertFalse(predicate.test(new PersonBuilder().withAddress("Blk 123").build()));

    }

    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        AddressContainsKeywordsPredicate predicate = new AddressContainsKeywordsPredicate(keyword);

        String expected = AddressContainsKeywordsPredicate.class.getCanonicalName() + "{address=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
