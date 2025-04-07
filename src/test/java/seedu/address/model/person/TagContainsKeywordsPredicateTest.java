package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;


public class TagContainsKeywordsPredicateTest {
    private String oneWordTag = "friends";
    private String multiWordTag = "friends and neighbours";
    private TagContainsKeywordsPredicate oneWordTagPredicate = new TagContainsKeywordsPredicate(oneWordTag);
    private TagContainsKeywordsPredicate otherTagPredicate = new TagContainsKeywordsPredicate("dummy");

    @Test
    public void equals() {
        // same object -> returns true
        assertTrue(oneWordTagPredicate.equals(oneWordTagPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate oneWordTagPredicateCopy = new TagContainsKeywordsPredicate(oneWordTag);
        assertTrue(oneWordTagPredicate.equals(oneWordTagPredicateCopy));

        // different types -> returns false
        assertFalse(oneWordTagPredicate.equals(1));

        // null -> returns false
        assertFalse(oneWordTagPredicate.equals(null));

        // different person -> returns false
        assertFalse(oneWordTagPredicate.equals(otherTagPredicate));
    }

    @Test
    public void test_tagContainsKeyword_returnsTrue() {
        // Exact keyword
        assertTrue(oneWordTagPredicate.test(new PersonBuilder().withTags(oneWordTag).build()));

        // Two letters or fewer keyword
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate("e");
        assertTrue(predicate.test(new PersonBuilder().withTags("e waste disposal").build()));

        // Keyword matches part of tag
        assertTrue(oneWordTagPredicate.test(new PersonBuilder().withTags(multiWordTag).build()));

        // Less than 3 typos
        assertTrue(oneWordTagPredicate.test(new PersonBuilder().withTags("frinds").build()));

        // Matches for person with multiple tags
        assertTrue(oneWordTagPredicate.test(new PersonBuilder().withTags(multiWordTag).withTags(multiWordTag).build()));
    }

    @Test
    public void test_tagDoesNotContainsKeyword_returnsFalse() {
        // Non-matching keyword
        assertFalse(oneWordTagPredicate.test(new PersonBuilder().withTags("family").build()));

        // Two letters or fewer keyword
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate("o");
        assertFalse(predicate.test(new PersonBuilder().withTags("e waste disposal").build()));

        // 3 or more typos
        assertFalse(oneWordTagPredicate.test(new PersonBuilder().withTags("freid").build()));

        // Keyword has more than what is available
        assertFalse(new AddressContainsKeywordsPredicate(multiWordTag)
                .test(new PersonBuilder().withTags("friends").build()));
    }


    @Test
    public void toStringMethod() {
        String keyword = "keyword";
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(keyword);

        String expected = TagContainsKeywordsPredicate.class.getCanonicalName() + "{tags=" + keyword + "}";
        assertEquals(expected, predicate.toString());
    }
}
