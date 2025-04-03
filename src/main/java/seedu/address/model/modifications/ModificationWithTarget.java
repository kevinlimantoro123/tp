package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Abstract class representing a record of a modification of a person in the address book.
 */
public abstract class ModificationWithTarget extends Modification {
    public static final String MOD_TYPE = "Generic modification with target person: ";
    private final Person targetPerson;

    public ModificationWithTarget(Person targetPerson) {
        super();
        this.targetPerson = targetPerson;
    }

    public Person getTargetPerson() {
        return this.targetPerson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPerson", this.getTargetPerson())
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
        if (!(other instanceof ModificationWithTarget)) {
            return false;
        }

        ModificationWithTarget otherMod = (ModificationWithTarget) other;
        return this.targetPerson.equals(otherMod.targetPerson);
    }
}
