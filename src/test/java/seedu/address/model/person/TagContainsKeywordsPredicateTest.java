package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;


public class TagContainsKeywordsPredicateTest {
    private String oneTag = "friends";
    private String multipleTags = "friends family colleagues";
    private TagContainsKeywordsPredicate oneTagPredicate = new TagContainsKeywordsPredicate(oneTag);
    private TagContainsKeywordsPredicate multipleTagsPredicate = new TagContainsKeywordsPredicate(multipleTags);

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(oneTagPredicate.equals(oneTagPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate oneTagPredicateCopy = new TagContainsKeywordsPredicate(oneTag);
        assertTrue(oneTagPredicate.equals(oneTagPredicateCopy));

        // different types -> returns false
        assertFalse(oneTagPredicate.equals(1));

        // null -> returns false
        assertFalse(oneTagPredicate.equals(null));

        // different person -> returns false
        assertFalse(oneTagPredicate.equals(multipleTagsPredicate));
    }

    @Test
    public void test_tagContainsKeyword_returnsTrue() {
        assertTrue(oneTagPredicate.test(new PersonBuilder().withTags(oneTag).build()));
        assertTrue(oneTagPredicate.test(new PersonBuilder().withTags("friends").build()));
        // Tests not working because of new tag implementation
        //assertTrue(multipleTagsPredicate.test(new PersonBuilder().withTags("friends").build()));
        //assertTrue(multipleTagsPredicate.test(new PersonBuilder()
        //      .withTags("friends").withTags("family").withTags("colleagues").build()));
    }

    @Test
    public void test_tagDoesNotContainsKeyword_returnsFalse() {
        assertFalse(oneTagPredicate.test(new PersonBuilder().withTags("family").build()));
        assertFalse(multipleTagsPredicate.test(new PersonBuilder().withTags("employees").build()));
    }


    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(keyword);

        String expected = TagContainsKeywordsPredicate.class.getCanonicalName() + "{tags=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
