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
     * Checks if a flag is present in the user's argument.
     * A flag is present if it is present in the string and stands alone:
     * <ul>
     *     For example, if we are trying to detect a flag named "--my-flag":
     *     <li>"--my-flag /Documents/Data/file.json" will detect the '--my-flag' flag.</li>
     *     <li>"/Documents/Data/file.json --my-flag" will also detect the '--my-flag' flag.</li>
     *     <li>"/Documents/Data/--my-flag/file.json" will not detect the "--my-flag" flag because it is not alone.</li>
     * </ul>
     *
     * If there are multiple flags with the same name detected, this will still return true.
     *
     * @param arg The user's argument
     * @param flag The flag to detect
     * @return true if the flag is detected in the user's argument
     */
    private boolean isFlagPresent(String arg, String flag) {
        String[] parts = arg.split("\\s+");
        for (String part : parts) {
            if (part.equals(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extracts a file path from the user's argument.
     *
     * @param args The user's argument
     * @return The file path
     * @throws ParseException if more than 1 file path is detected
     */
    private String extractFilePath(String args) throws ParseException {
        String[] parts = args.split("\\s+");
        StringBuilder filePathBuilder = new StringBuilder();

        boolean foundFile = false;

        for (String part : parts) {
            // this part is a flag, if the path builder is non-empty, a file path is done
            if (part.equals(IS_OVERWRITE_FLAG) || part.equals(SUPPRESSES_DUPLICATE_ERROR_FLAG)) {
                if (!filePathBuilder.isEmpty()) {
                    foundFile = true;
                }
                continue;
            }

            // do not accept more than 1 file path
            if (foundFile) {
                throw new ParseException(ImportCommand.MESSAGE_TOO_MANY_ARGUMENTS);
            }

            // build file path
            if (!filePathBuilder.isEmpty()) {
                filePathBuilder.append(" ");
            }
            filePathBuilder.append(part);
        }

        if (filePathBuilder.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        return filePathBuilder.toString();
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ImportCommand parse(String userInput) throws ParseException {
        String trimmedArgs = userInput.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        boolean isOverwrite = isFlagPresent(trimmedArgs, IS_OVERWRITE_FLAG);
        boolean suppressDuplicateErrors = isFlagPresent(trimmedArgs, SUPPRESSES_DUPLICATE_ERROR_FLAG);
        String filePath = extractFilePath(trimmedArgs);

        return new ImportCommand(filePath, isOverwrite, suppressDuplicateErrors);
    }
}
