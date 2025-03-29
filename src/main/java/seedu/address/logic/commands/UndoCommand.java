package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.modifications.Modification;
import seedu.address.model.person.exceptions.CannotUndoException;

/**
 * Restores the state of the address book before the last command that modifies it.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "The following change has been undone: ";
    public static final String MESSAGE_CANNOT_UNDO = "There are no more changes to undo!";


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Modification undoneMod;
        try {
            undoneMod = model.undoAddressBook();
        } catch (CannotUndoException e) {
            throw new CommandException(MESSAGE_CANNOT_UNDO);
        }
        return new CommandResult(MESSAGE_SUCCESS + undoneMod.getUserDescription());
    }
}
