package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's note in the address book.
 * Guaranatees: immutable; is always valid
 */
public class Note {
    public final String value;

    /**
     * Constructs a {@code Note}.
     *
     * @param note A valid note.
     */
    public Note(String note) {
        requireNonNull(note);
        this.value = note;
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
