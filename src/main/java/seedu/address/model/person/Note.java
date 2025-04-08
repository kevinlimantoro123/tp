package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's note in the address book.
 * Guarantees: immutable; is always valid
 */
public class Note {
    public static final String MESSAGE_CONSTRAINTS =
            "Notes can take any values, and it should be 255 characters or less.";


    public final String value;

    /**
     * Constructs a {@code Note}.
     *
     * @param note A valid note.
     */
    public Note(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_CONSTRAINTS);
        this.value = note;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidNote(String test) {
        return test.length() <= 255;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Note)) {
            return false;
        }
        Note otherNote = (Note) other;
        String thisStr = String.join(" ", value.split("\\s+"));
        String otherStr = String.join(" ", otherNote.value.split("\\s+"));
        return thisStr.equalsIgnoreCase(otherStr);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return String.join(" ", value.split("\\s+")).toLowerCase().hashCode();
    }
}
