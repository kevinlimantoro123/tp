package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ExportCommandParser;
import seedu.address.model.Model;

/**
 * Exports the address book data into a JSON file in a specified folder.
 */
public class ExportCommand extends FileBasedCommand {
    public static final String COMMAND_WORD = "export";
    public static final String DEFAULT_EXPORT_FILE_NAME = "craftconnect.json";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports CraftConnect's data into the specified folder."
            + " The default file's name will be " + DEFAULT_EXPORT_FILE_NAME + ".\n"
            + "Parameters: PATH_TO_FOLDER [" + ExportCommandParser.CREATES_DIRECTORY_FLAG + "]\n"
            + "- The " + ExportCommandParser.CREATES_DIRECTORY_FLAG + " flag tells CraftConnect to create the relevant "
            + "folders to store the data file, and it can be placed before or after the folder path.\n"
            + "Example: " + COMMAND_WORD + " C:/Users/DummyUser\n"
            + "or: " + COMMAND_WORD + " C:/Users/DummyUser " + ExportCommandParser.CREATES_DIRECTORY_FLAG + "\n"
            + "or: " + COMMAND_WORD + " " + ExportCommandParser.CREATES_DIRECTORY_FLAG + " C:/Users/DummyUser\n\n"
            + "Do not put your file path inside quotation marks.\n\n"
            + "Note that if you accidentally specify the folder path to look like a file, such as: \n"
            + ">> export C:/Users/DummyUser/Documents/CraftConnect/data.json\n"
            + "and your desired intention is to put a 'data.json' file in the folder\n"
            + "C:/Users/DummyUser/Documents/CraftConnect\n, and the " + ExportCommandParser.CREATES_DIRECTORY_FLAG
            + " flag is enabled, "
            + "CraftConnect will create a data.json folder on top of your desired folder and put the "
            + "data export file in that folder.\n"
            + "This is to allow for cases when you intentionally want to create "
            + "a folder whose name contains the '.' symbol.\n"
            + "However, if there is an existing file at that path, an error message will be displayed.\n\n"
            + "We also recommend using the absolute folder path so that you know where the file will be.\n";

    public static final String MESSAGE_SUCCESS = "Data successfully exported at \n%s\n!";
    public static final String MESSAGE_ERROR = "Command aborted due to failure to export data.\n%s";
    public static final String MESSAGE_USER_PATH = "Your file path: %s\n";
    public static final String MESSAGE_FOLDER_DOES_NOT_EXIST = "The specified folder does not exist. "
            + "Please check if the path is correct, or use the flag "
            + ExportCommandParser.CREATES_DIRECTORY_FLAG + ".";
    public static final String MESSAGE_NOT_A_FOLDER = "The specified path is not a valid folder. "
            + "Please ensure the correct folder path (which may be dependent on your OS).";
    public static final String MESSAGE_CREATE_FILE_ERROR = "The directory cannot be created. "
            + "Please check if you have enough space, or choose another path.";
    public static final String MESSAGE_ERROR_COPYING_DATA = "Cannot copy CraftConnect data to export file. "
            + "Please check if you have enough space, or choose another path.";
    public static final String MESSAGE_TOO_MANY_ARGUMENTS = "Too many arguments specified!\n"
            + "Please make sure that you only supply the folder path "
            + "(and optionally, " + ExportCommandParser.CREATES_DIRECTORY_FLAG + ")\n";

    private final boolean createsDirectory;

    /**
     * Initialises a new ExportCommand instance.<br>
     * When the user's specified path to folder does not exist,
     * <ul>
     *     <li>Setting <code>createsDirectory</code> to <code>true</code> will silently create the folder
     *     before inserting the exported data file into this newly created folder.</li>
     *     <li>Otherwise, throws an error to inform the user that the folder with the given path does not exist.</li>
     * </ul>
     *
     * @param folderPath The path of the folder to store the exported data file
     * @param createsDirectory Whether to create a new folder if the user's folder path does not exist
     */
    public ExportCommand(String folderPath, boolean createsDirectory) {
        super(folderPath);
        this.createsDirectory = createsDirectory;
    }

    /**
     * Generates an error message that caters to the user's folder path and the type of error message.
     * @param userFolder The path to the user's specified folder
     * @param errorInformation The specific error message
     * @return The full message containing the user's folder path and the specific error message
     */
    public static String generateErrorMessage(String userFolder, String errorInformation) {
        assert userFolder != null;
        return String.format(MESSAGE_ERROR,
                String.format(MESSAGE_USER_PATH, userFolder) + errorInformation);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Path userDirectory;

        // check for valid path to a folder
        try {
            userDirectory = Paths.get(this.path);
        } catch (InvalidPathException e) {
            throw new CommandException(generateErrorMessage(this.path, MESSAGE_NOT_A_FOLDER));
        }

        // check if folder exists
        if (!Files.exists(userDirectory)) {
            if (!createsDirectory) {
                throw new CommandException(generateErrorMessage(this.path, MESSAGE_FOLDER_DOES_NOT_EXIST));
            }

            try {
                Files.createDirectories(userDirectory);
            } catch (IOException e) {
                throw new CommandException(generateErrorMessage(this.path, MESSAGE_CREATE_FILE_ERROR));
            }
        }

        // do the rest
        Path exportedDataFile = userDirectory.resolve(DEFAULT_EXPORT_FILE_NAME);

        try {
            storage.saveAddressBook(model.getAddressBook(), exportedDataFile);
            return new CommandResult(String.format(MESSAGE_SUCCESS, exportedDataFile));
        } catch (IOException e) {
            throw new CommandException(generateErrorMessage(this.path, MESSAGE_ERROR_COPYING_DATA));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return this.createsDirectory == otherExportCommand.createsDirectory
                && this.path.equals(otherExportCommand.path);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("folderPath", this.path)
                .add("createsDirectory", this.createsDirectory)
                .toString();
    }
}
