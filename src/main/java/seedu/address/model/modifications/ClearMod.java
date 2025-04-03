package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Record of an edit of a contact's note in the address book.
 */
public class ClearMod extends Modification {
    public static final String MOD_TYPE = "Clear the address book";

    /**
     * Constructs a ClearMod.
     * @param targetPerson The person being edited.
     * @param note The new note.
     */
    public ClearMod() {
        super();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }

    @Override
    public String getUserDescription() {
        return MOD_TYPE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClearMod)) {
            return false;
        }

        return true;
    }
}
