package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Abstract class representing a record of a modification to the address book.
 */
public abstract class Modification {
    public static final String MOD_TYPE = "Generic modification";

    public Modification() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }

    /**
     * Returns an user-friendly description of the record.
     * @return An user-friendly description of the record.
     */
    public abstract String getUserDescription();

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Modification)) {
            return false;
        }

        return true;
    }
}
