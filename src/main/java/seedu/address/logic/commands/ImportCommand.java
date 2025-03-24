package seedu.address.logic.commands;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;


public class ImportCommand extends FileBasedCommand {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Populates CraftConnect using the contacts from the "
            + "specified JSON file. "
            + "Parameters: PATH_TO_JSON_FILE\n"
            + "Example: " + COMMAND_WORD + " C:/Users/DummyUser/data.json";

    public static final String MESSAGE_SUCCESS = "Data successfully imported! Enjoy using CraftConnect!";
    public static final String MESSAGE_ERROR = "Command aborted due to failure to import data: %s";
    public static final String MESSAGE_FILE_DOES_NOT_EXIST = "The specified file does not exist. "
            + "Please check if the path is correct.";
    public static final String MESSAGE_NOT_JSON_FILE = "The specified file is not a JSON file. "
            + "Please ensure the correct file format.";

    public static final String MESSAGE_INCOMPATIBLE_SCHEMA = "The JSON file does not follow CraftConnect's schema. "
            + "Please ensure that your JSON data file follows the following convention: \n"
            + "{\n"
            + "  \"persons\" : [ {\n"
            + "    \"name\" : (type: string),\n"
            + "    \"phone\" : (type: string, 3-16 numerical digits),\n"
            + "    \"email\" : (type: string, conforms to the email format),\n"
            + "    \"address\" : (type: string),\n"
            + "    \"tags\" : [ (type: string),... ],\n"
            + "  }, ...\n"
            + "}\n"
            + "Example file: \n"
            + "{\n"
            + "  \"persons\" : [ {\n"
            + "    \"name\" : \"Alex Yeoh\",\n"
            + "    \"phone\" : \"87438807\",\n"
            + "    \"email\" : \"alexyeoh@example.com\",\n"
            + "    \"address\" : \"Blk 30 Geylang Street 29, #06-40\",\n"
            + "    \"tags\" : [ \"bulkbuyer\", \"customer\" ],\n"
            + "  }, {\n"
            + "    \"name\" : \"Bernice Yu\",\n"
            + "    \"phone\" : \"99272758\",\n"
            + "    \"email\" : \"berniceyu@example.com\",\n"
            + "    \"address\" : \"Blk 30 Lorong 3 Serangoon Gardens, #07-18\",\n"
            + "    \"tags\" : [ \"boothRental\" ],\n"
            + "  } ]\n"
            + "}";

    public ImportCommand(String filePath) {
        super(filePath);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        ReadOnlyAddressBook originalAddressBook = model.getAddressBook();

        if (!(new File(this.filePath).exists())) {
            throw new CommandException(String.format(MESSAGE_ERROR, MESSAGE_FILE_DOES_NOT_EXIST));
        }

        if (!this.filePath.toLowerCase().endsWith(".json")) {
            throw new CommandException(String.format(MESSAGE_ERROR, MESSAGE_NOT_JSON_FILE));
        }

        try {
            Path path = Path.of(filePath);
            Optional<ReadOnlyAddressBook> addressBook = storage.readAddressBook(path);

            model.setAddressBook(addressBook.orElse(originalAddressBook));
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (DataLoadingException e) {
            throw new CommandException(String.format(MESSAGE_ERROR, MESSAGE_INCOMPATIBLE_SCHEMA));
        }
    }
}
