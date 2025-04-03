package seedu.address.logic.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ImportCommandParser;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * Import data from a JSON file with the specified path to the application.
 * This operation will overwrite existing data.
 */
public class ImportCommand extends FileBasedCommand {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Populates CraftConnect using the contacts from the "
            + "specified JSON file. It's recommended to only use this to transfer data from another CraftConnect to "
            + "minimise errors. "
            + "Parameters: PATH_TO_JSON_FILE ["
            + ImportCommandParser.IS_OVERWRITE_FLAG + "] ["
            + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + "]\n"
            + "- The " + ImportCommandParser.IS_OVERWRITE_FLAG + " tells CraftConnect to overwrite existing data with "
            + "data in the new JSON file. Else, CraftConnect will append new data to the existing data by default.\n"
            + "- The " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + " tells CraftConnect to ignore all "
            + "messages about duplicated contacts (duplication is when there are more than one people with the same "
            + "email addresses or the same phone number). "
            + "Else, if there are duplicated contacts, CraftConnect will abort the command and tell the user the "
            + "first instance of duplicated contact detected in the specified JSON file.\n"
            + "All flags and the file path can be specified in any order as long as they are after the export "
            + "command.\n"
            + "Example: " + COMMAND_WORD + " C:/Users/Dummy/data.json\n"
            + "or: " + COMMAND_WORD + " C:/Users/Dummy/data.json " + ImportCommandParser.IS_OVERWRITE_FLAG + "\n"
            + "or: " + COMMAND_WORD + " " + ImportCommandParser.IS_OVERWRITE_FLAG + " C:/Users/Dummy/data.json\n"
            + "or: " + COMMAND_WORD + " C:/Users/Dummy/data.json "
            + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + "\n"
            + "or: " + COMMAND_WORD + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
            + " C:/Users/Dummy/data.json\n"
            + "or: " + COMMAND_WORD + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
            + " " + ImportCommandParser.IS_OVERWRITE_FLAG + " C:/Users/Dummy/data.json\n"
            + "or: " + COMMAND_WORD + " " + ImportCommandParser.IS_OVERWRITE_FLAG
            + " C:/Users/Dummy/data.json " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + "\n"
            + "Do not put your file path inside quotation marks.\n";

    public static final String MESSAGE_SUCCESS = "Data successfully imported! Enjoy using CraftConnect!";
    public static final String MESSAGE_ERROR = "Command aborted due to failure to import data.\n%s";
    public static final String MESSAGE_USER_PATH = "Your file path: %s\n";
    public static final String MESSAGE_FILE_DOES_NOT_EXIST = "The specified file does not exist. "
            + "Please check if the path is correct.";
    public static final String MESSAGE_NOT_JSON_FILE = "The specified file is not a JSON file. "
            + "Please ensure the correct file format.";

    public static final String MESSAGE_INCOMPATIBLE_SCHEMA = "The JSON file is either empty or does not follow "
            + "CraftConnect's schema. If duplicates are not ignored, it may also be because of duplicate contacts "
            + "in your JSON file.\n\n"
            + "Please ensure that your JSON data file follows the following convention: \n"
            + "{\n"
            + "  \"persons\" : [ {\n"
            + "    \"name\" : (type: string),\n"
            + "    \"phone\" : (type: string, 3-16 numerical digits),\n"
            + "    \"email\" : (type: string, conforms to the email format),\n"
            + "    \"address\" : (type: string),\n"
            + "    \"tags\" : [ (type: string),... ],\n"
            + "    \"notes\" : (type: string) "
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
            + "    \"notes\" : \"ordered 200 flower stickers\"\n"
            + "  }, {\n"
            + "    \"name\" : \"Bernice Yu\",\n"
            + "    \"phone\" : \"99272758\",\n"
            + "    \"email\" : \"berniceyu@example.com\",\n"
            + "    \"address\" : \"Blk 30 Lorong 3 Serangoon Gardens, #07-18\",\n"
            + "    \"tags\" : [ \"boothRental\" ],\n"
            + "    \"notes\" : \"$280 for 2 days\""
            + "  } ]\n"
            + "}";

    public static final String MESSAGE_DUPLICATE_PHONE = "A contact in your JSON file has the same phone number "
            + "as an existing contact! The second duplicated contact:\n%s\n"
            + "Please check your JSON file.";

    public static final String MESSAGE_DUPLICATE_EMAIL = "A contact in your JSON file has the same email address "
            + "as an existing contact! The second duplicated contact:\n%s\n"
            + "Please check your JSON file.";

    public static final String MESSAGE_TOO_MANY_ARGUMENTS = "Too many arguments specified!\n"
            + "Please make sure that you only supply ONE file path "
            + "(and optionally, " + ImportCommandParser.IS_OVERWRITE_FLAG + ")"
            + "(and also optionally, " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + ")\n";

    private final boolean isOverwrite;
    private final boolean suppressesDuplicateErrors;

    /**
     * Initialises a new ImportCommand instance.<br><br>
     *
     * By default, ImportCommand will attempt to append data from the specified JSON file to the existing address book
     * data. To overwrite existing data instead, set <code>isOverwrite</code> to <code>true</code>.
     * If overwrite is enabled, suppression of duplicate errors will not take effect.<br><br>
     *
     * When appending data, there may be cases of duplicate people (as in same email, phone number, any other unique
     * identifier...), and by default, if there is a duplicate person, this throws an exception alongside the first
     * instance of a duplicated person. To suppress all duplication exceptions,
     * set <code>suppressesDuplicateErrors</code> to <code>true</code>.
     *
     * @param filePath The path to the JSON file
     * @param isOverwrite If set to true, overwrites existing data with data from the specified JSON file.
     * @param suppressesDuplicateErrors If set to true, the system will silently remove duplicated entries from the
     *                                  new data list (duplicate as in same unique identifiers).
     */
    public ImportCommand(String filePath, boolean isOverwrite, boolean suppressesDuplicateErrors) {
        super(filePath);

        this.isOverwrite = isOverwrite;
        this.suppressesDuplicateErrors = suppressesDuplicateErrors;
    }

    /**
     * Generates an error message that caters to the user's file path and the type of error message.
     * @param userFile The path to the user's specified file
     * @param errorInformation The specific error message
     * @return The full message containing the user's file path and the specific error message
     */
    public static String generateErrorMessage(String userFile, String errorInformation) {
        assert userFile != null;
        return String.format(MESSAGE_ERROR,
                String.format(MESSAGE_USER_PATH, userFile) + errorInformation);
    }

    /**
     * Appends (iteratively) new contacts from a ReadOnlyAddressBook into the model.
     *
     * @param model the Model containing the existing contacts
     * @param src the address book containing new data from the user-supplied JSON file
     * @throws CommandException if there are duplicated entries and duplicate suppression is not enabled
     */
    private void appendContacts(Model model, ReadOnlyAddressBook src) throws CommandException {
        for (Person newPerson : src.getPersonList()) {
            Person duplicatedPerson = model.findPersonWithSamePhoneNumber(newPerson);
            if (duplicatedPerson != null) {
                if (this.suppressesDuplicateErrors) {
                    continue;
                }
                throw new CommandException(String.format(MESSAGE_DUPLICATE_PHONE, Messages.format(newPerson)));
            }

            duplicatedPerson = model.findPersonWithSameEmail(newPerson);
            if (duplicatedPerson != null) {
                if (this.suppressesDuplicateErrors) {
                    continue;
                }
                throw new CommandException(String.format(MESSAGE_DUPLICATE_EMAIL, Messages.format(newPerson)));
            }

            model.addPerson(newPerson);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        ReadOnlyAddressBook originalAddressBook = model.getAddressBook();

        if (!(new File(this.path).exists())) {
            throw new CommandException(generateErrorMessage(this.path, MESSAGE_FILE_DOES_NOT_EXIST));
        }

        if (!this.path.toLowerCase().endsWith(".json")) {
            throw new CommandException(generateErrorMessage(this.path, MESSAGE_NOT_JSON_FILE));
        }

        try {
            Path path = Paths.get(this.path);
            Optional<ReadOnlyAddressBook> addressBook;

            if (this.suppressesDuplicateErrors) {
                addressBook = storage.readAddressBookIgnoreDuplicates(path);
            } else {
                addressBook = storage.readAddressBook(path);
            }

            if (this.isOverwrite) {
                model.setAddressBook(addressBook.orElse(originalAddressBook));
            } else if (addressBook.isPresent()) {
                this.appendContacts(model, addressBook.get());
            }

            return new CommandResult(MESSAGE_SUCCESS);
        } catch (DataLoadingException e) {
            throw new CommandException(generateErrorMessage(this.path, MESSAGE_INCOMPATIBLE_SCHEMA));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return this.path.equals(otherImportCommand.path)
                && this.isOverwrite == otherImportCommand.isOverwrite
                && this.suppressesDuplicateErrors == otherImportCommand.suppressesDuplicateErrors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("path", this.path)
                .add("overwrite", this.isOverwrite)
                .add("suppressDupeErrors", this.suppressesDuplicateErrors)
                .toString();
    }
}
