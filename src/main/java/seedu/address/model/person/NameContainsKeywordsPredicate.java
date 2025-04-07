package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} <b>approximately</b> matches the name given (case-insensitive).
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final String nameKeyword;

    public NameContainsKeywordsPredicate(String nameKeyword) {
        this.nameKeyword = nameKeyword;
    }

    @Override
    public boolean test(Person person) {
        return StringUtil.computeCloseness(person.getName().fullName, nameKeyword) < 2;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return nameKeyword.equals(otherNameContainsKeywordsPredicate.nameKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("name", nameKeyword).toString();
    }
}
