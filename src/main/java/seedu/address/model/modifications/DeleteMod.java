package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.person.Person;

/**
 * Record of a deletion of a contact from the address book.
 */
public class DeleteMod extends ModificationWithTarget {
    public static final String MOD_TYPE = "Delete person";

    public DeleteMod(Person targetPerson) {
        super(targetPerson);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPerson", this.getTargetPerson())
                .toString();
    }

    @Override
    public String getUserDescription() {
        return MOD_TYPE + " " + Messages.format(this.getTargetPerson());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteMod)) {
            return false;
        }

        DeleteMod otherDeleteMod = (DeleteMod) other;
        return ((ModificationWithTarget) this).equals((ModificationWithTarget) otherDeleteMod);
    }
}
