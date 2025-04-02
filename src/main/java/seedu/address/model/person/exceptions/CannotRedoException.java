package seedu.address.model.person.exceptions;

/**
 * Signals that no further redos are possible.
 */
public class CannotRedoException extends Exception {
    public CannotRedoException() {
        super("No more redos possible");
    }
}
