package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.predicate.PhoneIsKeywordPredicate;
import seedu.address.testutil.PersonBuilder;

public class PhoneIsKeywordPredicateTest {
    @Test
    public void equals() {
        String firstPhone = "93424353";
        String secondPhone = "0910671358";

        PhoneIsKeywordPredicate firstPhonePredicate = new PhoneIsKeywordPredicate(firstPhone);
        PhoneIsKeywordPredicate secondPhonePredicate = new PhoneIsKeywordPredicate(secondPhone);

        // same object -> returns true
        assertEquals(firstPhonePredicate, firstPhonePredicate);

        // same values -> returns true
        PhoneIsKeywordPredicate firstPhonePredicateCopy = new PhoneIsKeywordPredicate(firstPhone);
        assertEquals(firstPhonePredicate, firstPhonePredicateCopy);

        // different types -> returns false
        assertNotEquals(1, firstPhonePredicate);

        // null -> returns false
        assertNotEquals(null, firstPhonePredicate);

        // different person -> returns false
        assertNotEquals(firstPhonePredicate, secondPhonePredicate);
    }

    @Test
    public void test_phoneIsKeyword_returnsTrue() {
        String firstPhone = "93424353";
        String secondPhone = "0910671358";

        PhoneIsKeywordPredicate firstPhonePredicate = new PhoneIsKeywordPredicate(firstPhone);
        PhoneIsKeywordPredicate secondPhonePredicate = new PhoneIsKeywordPredicate(secondPhone);

        assertTrue(firstPhonePredicate.test(new PersonBuilder().withPhone(firstPhone).build()));
        assertTrue(firstPhonePredicate.test(new PersonBuilder().withPhone("93424353").build()));

        assertTrue(secondPhonePredicate.test(new PersonBuilder().withPhone(secondPhone).build()));
        assertTrue(secondPhonePredicate.test(new PersonBuilder().withPhone("0910671358").build()));
    }

    @Test
    public void test_phoneIsNotKeyword_returnsFalse() {
        String firstPhone = "93424353";
        String secondPhone = "0910671358";

        PhoneIsKeywordPredicate firstPhonePredicate = new PhoneIsKeywordPredicate(firstPhone);
        PhoneIsKeywordPredicate secondPhonePredicate = new PhoneIsKeywordPredicate(secondPhone);

        // unmatched keyword
        assertFalse(firstPhonePredicate.test(new PersonBuilder().withPhone(secondPhone).build()));
        assertFalse(firstPhonePredicate.test(new PersonBuilder().withPhone("0912345678").build()));

        assertFalse(secondPhonePredicate.test(new PersonBuilder().withPhone(firstPhone).build()));
        assertFalse(secondPhonePredicate.test(new PersonBuilder().withPhone("91238172").build()));

        // null phone string
        PhoneIsKeywordPredicate nullPhonePredicate = new PhoneIsKeywordPredicate(null);
        assertFalse(nullPhonePredicate.test(new PersonBuilder().withPhone("91238172").build()));

        // empty phone string
        PhoneIsKeywordPredicate emptyPhonePredicate = new PhoneIsKeywordPredicate("");
        assertFalse(emptyPhonePredicate.test(new PersonBuilder().withPhone("0123847684").build()));
    }

    @Test
    public void toStringMethod() {
        String phone = "85630193";
        PhoneIsKeywordPredicate predicate = new PhoneIsKeywordPredicate(phone);

        String expected = PhoneIsKeywordPredicate.class.getCanonicalName() + "{phone=" + phone + "}";
        assertEquals(expected, predicate.toString());
    }
}
