package seedu.address.model.person.exceptions;

/**
 * Signals that no further undos are possible.
 */
public class CannotUndoException extends Exception {
    public CannotUndoException() {
        super("No more undos possible");
    }
}
