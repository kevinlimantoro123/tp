package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new {@code UndoCommand} object
 */
public class UndoCommandParser implements Parser<UndoCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code UndoCommand}
     * and returns a {@code UndoCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public UndoCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
        int numberOfTimes;

        try {
            numberOfTimes = Integer.parseInt(argMultimap.getPreamble());
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
        }
        
        if (numberOfTimes <= 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
        }

        return new UndoCommand(numberOfTimes);
    }
}
