package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Address} <b>approximately</b> matches the address given.
 */
public class AddressContainsKeywordsPredicate implements Predicate<Person> {
    private final String addressKeyword;

    public AddressContainsKeywordsPredicate(String addressKeyword) {
        this.addressKeyword = addressKeyword;
    }

    @Override
    public boolean test(Person person) {
        String address = person.getAddress().value;
        String[] addressKeywordParts = addressKeyword.split("\\s+");
        for (String addressKeywordPart: addressKeywordParts) {
            if ((StringUtil.isNonZeroUnsignedInteger(addressKeywordPart)
                        && !StringUtil.containsWordIgnoreCase(address, addressKeywordPart, false)
                        && !StringUtil.containsWordIgnoreCase(address, addressKeywordPart + ",", false))
                    || (addressKeywordPart.length() < 3
                        && !StringUtil.containsWordIgnoreCase(address, addressKeywordPart, true))) {
                return false;
            }
        }
        return StringUtil.computeCloseness(address, addressKeyword) < 3;
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
