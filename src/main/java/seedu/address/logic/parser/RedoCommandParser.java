package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new {@code RedoCommand} object
 */
public class RedoCommandParser implements Parser<RedoCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RedoCommand}
     * and returns a {@code RedoCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public RedoCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
        int numberOfTimes;

        if (argMultimap.getPreamble().isEmpty()) {
            return new RedoCommand();
        }

        try {
            numberOfTimes = Integer.parseInt(argMultimap.getPreamble());
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
        }

        if (numberOfTimes <= 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
        }

        return new RedoCommand(numberOfTimes);
    }
}
