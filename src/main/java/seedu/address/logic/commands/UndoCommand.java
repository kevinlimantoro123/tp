package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.modifications.Modification;
import seedu.address.model.person.exceptions.CannotUndoException;

/**
 * Restores the state of the address book before the last command that modifies it.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "The following change has been undone: \n%s";
    public static final String MESSAGE_SUCCESS_MULTIPLE =
            "The last %d change(s) have been undone! (Requested: %d changes)";
    public static final String MESSAGE_CANNOT_UNDO = "There are no more changes to undo!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the latest changes to the person list.\n"
        + "Format: " + COMMAND_WORD + " [NUMBER_OF_CHANGES]\n"
        + "Parameters: NUMBER_OF_CHANGES (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 3";

    private final int numberOfTimes;

    public UndoCommand() {
        this.numberOfTimes = 1;
    }

    public UndoCommand(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert(this.numberOfTimes >= 1);

        if (this.numberOfTimes == 1) {
            Modification undoneMod;
            try {
                undoneMod = model.undoAddressBook();
            } catch (CannotUndoException e) {
                throw new CommandException(MESSAGE_CANNOT_UNDO);
            }
            return new CommandResult(
                String.format(MESSAGE_SUCCESS, undoneMod.getUserDescription()));
        } else {
            List<Modification> undoneMods = model.undoAddressBookMultiple(this.numberOfTimes);
            if (undoneMods.size() == 0) {
                throw new CommandException(MESSAGE_CANNOT_UNDO);
            } else {
                return new CommandResult(
                    String.format(MESSAGE_SUCCESS_MULTIPLE, undoneMods.size(), this.numberOfTimes));
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UndoCommand)) {
            return false;
        }
        UndoCommand otherCommand = (UndoCommand) other;
        return numberOfTimes == (otherCommand.numberOfTimes);
    }
}
