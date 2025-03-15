package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;

import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s {@code Email} <b>exactly</b> matches the email given.
 */
public class EmailIsKeywordPredicate implements Predicate<Person> {
    private final String emailKeyword;

    public EmailIsKeywordPredicate(String emailKeyword) {
        this.emailKeyword = emailKeyword;
    }

    @Override
    public boolean test(Person person) {
        return person.getEmail().value.equals(emailKeyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailIsKeywordPredicate)) {
            return false;
        }

        EmailIsKeywordPredicate otherEmailIsKeywordPredicate = (EmailIsKeywordPredicate) other;
        return this.emailKeyword.equals(otherEmailIsKeywordPredicate.emailKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("email", emailKeyword).toString();
    }
}
