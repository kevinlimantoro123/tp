package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Record of an edit of a contact's note in the address book.
 */
public class NoteMod extends ModificationWithTarget {
    public static final String MOD_TYPE = "Edit note of person";
    private final Note note;

    /**
     * Constructs a NoteMod.
     * @param targetPerson The person being edited.
     * @param note The new note.
     */
    public NoteMod(Person targetPerson, Note note) {
        super(targetPerson);
        this.note = note;
    }

    public Note getNote() {
        return this.note;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPerson", this.getTargetPerson())
                .add("note", this.getNote())
                .toString();
    }

    @Override
    public String getUserDescription() {
        return MOD_TYPE + " " + Messages.format(this.getTargetPerson()) + " to " + this.getNote();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteMod)) {
            return false;
        }

        NoteMod otherNoteMod = (NoteMod) other;
        return ((Modification) this).equals((Modification) otherNoteMod)
                && this.note.equals(otherNoteMod.note);
    }
}
