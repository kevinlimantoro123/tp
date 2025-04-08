package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.predicate.EmailIsKeywordPredicate;
import seedu.address.testutil.PersonBuilder;

public class EmailIsKeywordPredicateTest {

    @Test
    public void equals() {
        String firstEmailKeyword = "ilovecraftconnect@gmail.com";
        String secondEmailKeyword = "e1234567@u.nus.edu";

        EmailIsKeywordPredicate firstEmailPredicate = new EmailIsKeywordPredicate(firstEmailKeyword);
        EmailIsKeywordPredicate secondEmailPredicate = new EmailIsKeywordPredicate(secondEmailKeyword);

        // same object -> returns true
        assertEquals(firstEmailPredicate, firstEmailPredicate);

        // same values -> returns true
        EmailIsKeywordPredicate firstEmailPredicateCopy = new EmailIsKeywordPredicate(firstEmailKeyword);
        assertEquals(firstEmailPredicate, firstEmailPredicateCopy);

        // different types -> returns false
        assertNotEquals(1, firstEmailPredicate);

        // null -> returns false
        assertNotEquals(null, firstEmailPredicate);

        // different person -> returns false
        assertNotEquals(firstEmailPredicate, secondEmailPredicate);
    }

    @Test
    public void test_emailIsKeyword_returnsTrue() {
        String firstEmailKeyword = "ilovecraftconnect@gmail.com";
        EmailIsKeywordPredicate firstEmailPredicate = new EmailIsKeywordPredicate(firstEmailKeyword);

        String secondEmailKeyword = "e1234567@u.nus.edu";
        EmailIsKeywordPredicate secondEmailPredicate = new EmailIsKeywordPredicate(secondEmailKeyword);

        assertTrue(firstEmailPredicate.test(new PersonBuilder().withEmail(firstEmailKeyword).build()));
        assertTrue(firstEmailPredicate.test(new PersonBuilder().withEmail("ilovecraftconnect@gmail.com").build()));

        assertTrue(secondEmailPredicate.test(new PersonBuilder().withEmail(secondEmailKeyword).build()));
        assertTrue(secondEmailPredicate.test(new PersonBuilder().withEmail("e1234567@u.nus.edu").build()));
    }

    @Test
    public void test_emailIsNotKeyword_returnsFalse() {
        String firstEmailKeyword = "ilovecraftconnect@gmail.com";
        EmailIsKeywordPredicate firstEmailPredicate = new EmailIsKeywordPredicate(firstEmailKeyword);

        String secondEmailKeyword = "e1234567@u.nus.edu";
        EmailIsKeywordPredicate secondEmailPredicate = new EmailIsKeywordPredicate(secondEmailKeyword);

        // unmatched keyword
        assertFalse(firstEmailPredicate.test(new PersonBuilder().withEmail(secondEmailKeyword).build()));
        assertFalse(firstEmailPredicate.test(new PersonBuilder().withEmail("e1234567@u.nus.edu").build()));
        assertFalse(firstEmailPredicate.test(new PersonBuilder().withEmail("craftconect@gmail.com").build()));

        assertFalse(secondEmailPredicate.test(new PersonBuilder().withEmail(firstEmailKeyword).build()));
        assertFalse(secondEmailPredicate.test(new PersonBuilder().withEmail("ilovecraftconnect@gmail.com").build()));
        assertFalse(secondEmailPredicate.test(new PersonBuilder().withEmail("1234567@u.nus.edu").build()));

        // null email string
        EmailIsKeywordPredicate nullEmailPredicate = new EmailIsKeywordPredicate(null);
        assertFalse(nullEmailPredicate.test(new PersonBuilder().withEmail("e1234567@u.nus.edu").build()));

        // empty email string
        EmailIsKeywordPredicate emptyEmailPredicate = new EmailIsKeywordPredicate("");
        assertFalse(emptyEmailPredicate.test(new PersonBuilder().withEmail("e1234567@u.nus.edu").build()));
    }

    @Test
    public void toStringMethod() {
        String email = "ilovecraftconnect@gmail.com";
        EmailIsKeywordPredicate predicate = new EmailIsKeywordPredicate(email);

        String expected = EmailIsKeywordPredicate.class.getCanonicalName() + "{email=" + email + "}";
        assertEquals(expected, predicate.toString());
    }
}
