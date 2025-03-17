package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

public class AddressContainsKeywordPredicate implements Predicate<Person> {
    private final String addressKeyword;

    public AddressContainsKeywordPredicate(String addressKeyword) {
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
        if (!(other instanceof AddressContainsKeywordPredicate)) {
            return false;
        }

        AddressContainsKeywordPredicate otherTagContainsKeywordsPredicate = (AddressContainsKeywordPredicate) other;
        return addressKeyword.equals(otherTagContainsKeywordsPredicate.addressKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", addressKeyword).toString();
    }
}
