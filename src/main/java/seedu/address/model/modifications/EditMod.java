package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.person.Person;

/**
 * Record of an edit of a contact in the address book.
 */
public class EditMod extends Modification {
    public static final String MOD_TYPE = "Edit Person";
    private final Person newPerson;

    /**
     * Constructs an EditMod.
     * @param targetPerson The person being edited.
     * @param newPerson The resulting person, after the edir.
     */
    public EditMod(Person targetPerson, Person newPerson) {
        super(targetPerson);
        this.newPerson = newPerson;
    }

    public Person getNewPerson() {
        return this.newPerson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPerson", this.getTargetPerson())
                .add("newPerson", this.getNewPerson())
                .toString();
    }

    @Override
    public String getUserDescription() {
        return MOD_TYPE + " " + Messages.format(this.getTargetPerson()) + " to " + Messages.format(this.getNewPerson());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditMod)) {
            return false;
        }

        EditMod otherEditMod = (EditMod) other;
        return ((Modification) this).equals((Modification) otherEditMod)
                && this.newPerson.equals(otherEditMod.newPerson);
    }
}
