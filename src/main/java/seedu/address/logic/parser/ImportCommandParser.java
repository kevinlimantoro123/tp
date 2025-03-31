package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input argument and returns a new ImportCommand object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {
    public static final String IS_OVERWRITE_FLAG = "--overwrite";
    public static final String SUPPRESSES_DUPLICATE_ERROR_FLAG = "--ignore-duplicates";

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ImportCommand parse(String userInput) throws ParseException {
        String filePath = userInput.trim();

        if (filePath.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        return new ImportCommand(filePath, true, false);
    }
}
