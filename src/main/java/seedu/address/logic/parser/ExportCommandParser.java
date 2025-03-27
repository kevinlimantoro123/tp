package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input argument and returns a new ExportCommand object.
 */
public class ExportCommandParser implements Parser<ExportCommand> {
    public static final String CREATES_DIRECTORY_FLAG = "--create-dir";

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ExportCommand parse(String userInput) throws ParseException {
        String trimmedArgs = userInput.trim();

        boolean createsDir = false;
        StringBuilder folderBuilder = new StringBuilder();

        String[] tokens = trimmedArgs.split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals(CREATES_DIRECTORY_FLAG)) {
                if (i == 0 || i == tokens.length - 1) {
                    createsDir = true;
                } else {
                    throw new ParseException(ExportCommand.MESSAGE_TOO_MANY_ARGUMENTS);
                }
            } else {
                if (!folderBuilder.isEmpty()) {
                    folderBuilder.append(" ");
                }
                folderBuilder.append(token);
            }
        }

        if (folderBuilder.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        return new ExportCommand(folderBuilder.toString(), createsDir);
    }
}
