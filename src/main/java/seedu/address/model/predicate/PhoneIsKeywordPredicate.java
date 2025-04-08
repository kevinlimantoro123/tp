package seedu.address.model.predicate;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Phone} <b>exactly</b> matches the phone given.
 */
public class PhoneIsKeywordPredicate implements Predicate<Person> {
    private final String phoneKeyword;

    public PhoneIsKeywordPredicate(String phoneKeyword) {
        this.phoneKeyword = phoneKeyword;
    }

    @Override
    public boolean test(Person person) {
        return person.getPhone().value.equals(phoneKeyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PhoneIsKeywordPredicate otherPhoneIsKeywordPredicate)) {
            return false;
        }

        return this.phoneKeyword.equals(otherPhoneIsKeywordPredicate.phoneKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("phone", phoneKeyword).toString();
    }
}
