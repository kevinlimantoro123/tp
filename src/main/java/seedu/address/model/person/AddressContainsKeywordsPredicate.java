package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Address} <b>exactly</b> matches the address given.
 */
public class AddressContainsKeywordsPredicate implements Predicate<Person> {
    private final String addressKeyword;

    public AddressContainsKeywordsPredicate(String addressKeyword) {
        this.addressKeyword = addressKeyword;
    }

    @Override
    public boolean test(Person person) {
        return person.getAddress().value.equals(addressKeyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressContainsKeywordsPredicate)) {
            return false;
        }

        AddressContainsKeywordsPredicate otherTagContainsKeywordsPredicate = (AddressContainsKeywordsPredicate) other;
        return addressKeyword.equals(otherTagContainsKeywordsPredicate.addressKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("address", addressKeyword).toString();
    }
}
