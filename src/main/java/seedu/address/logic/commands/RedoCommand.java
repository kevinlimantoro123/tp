package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.modifications.Modification;
import seedu.address.model.person.exceptions.CannotRedoException;

/**
 * Restores the state of the address book before the last command that modifies it.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_SUCCESS = "The following change has been restored: ";
    public static final String MESSAGE_CANNOT_REDO = "There are no more changes to restore!";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Modification restoredMod;
        try {
            restoredMod = model.redoAddressBook();
        } catch (CannotRedoException e) {
            throw new CommandException(MESSAGE_CANNOT_REDO);
        }
        return new CommandResult(MESSAGE_SUCCESS + restoredMod.getUserDescription());
    }
}
