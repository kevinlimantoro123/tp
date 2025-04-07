package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.FileBasedCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;

public class ImportCommandParserTest {
    private final ImportCommandParser parser = new ImportCommandParser();

    /**
     * Binds all file-based commands to a dummy storage.
     * This is because any file-based commands must operate on an internal storage, but here, no command
     * is executed, so a stub storage is used.
     */
    @BeforeAll
    public static void setUp() {
        FileBasedCommand.bindStorage(new StorageStub());
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " ", expectedMessage);
        assertParseFailure(parser, "    ", expectedMessage);
    }

    @Test
    public void parse_onlyFileArgs_success() {
        String validArgs1 = "C:/Users/DummyUser/data.json";
        String validArgs2 = "../../data.json";
        String validArgs3 = "C:/Users/FalseUser/data.json randomWord";
        String validArgs4 = "the parser faithfully parse the argument, the command checks the path validity";
        String validArgs5 = "/Documents/--overwrite/data.json";
        String validArgs6 = "/Documents/Subfolder/--ignore-duplicates/ data.json";

        // flags should stand alone, so in this case where the flags are accidentally lumped together,
        // the entire thing is coutned as a file.
        String validArgs7 = ImportCommandParser.IS_OVERWRITE_FLAG + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;

        assertParseSuccess(parser, validArgs1,
                new ImportCommand(validArgs1, false, false));
        assertParseSuccess(parser, validArgs2,
                new ImportCommand(validArgs2, false, false));
        assertParseSuccess(parser, validArgs3,
                new ImportCommand(validArgs3, false, false));
        assertParseSuccess(parser, validArgs4,
                new ImportCommand(validArgs4, false, false));
        assertParseSuccess(parser, validArgs5,
                new ImportCommand(validArgs5, false, false));
        assertParseSuccess(parser, validArgs6,
                new ImportCommand(validArgs6, false, false));
        assertParseSuccess(parser, validArgs7,
                new ImportCommand(validArgs7, false, false));
    }

    @Test
    public void parse_onlyOverwrite_success() {
        String file1 = "C:/Users/DummyUser/data.json";
        String file2 = "../../data.json";
        String file3 = "C:/Users/FalseUser/data.json randomWord";
        String file4 = "the parser faithfully parse the argument, the command checks the path validity";
        String file5 = "/Documents/--overwrite/data.json";
        String file6 = "/Documents/Subfolder/--ignore-duplicates/ data.json";

        String validArgs1 = file1 + " " + ImportCommandParser.IS_OVERWRITE_FLAG;
        String validArgs2 = " " + ImportCommandParser.IS_OVERWRITE_FLAG + " " + file2;
        String validArgs3 = " " + file3 + " " + ImportCommandParser.IS_OVERWRITE_FLAG;
        String validArgs4 = file4 + " " + ImportCommandParser.IS_OVERWRITE_FLAG;
        String validArgs5 = "   " + ImportCommandParser.IS_OVERWRITE_FLAG + "   " + file5 + "     ";
        String validArgs6 = "   " + file6 + " " + ImportCommandParser.IS_OVERWRITE_FLAG + "  ";

        String validArgsDupeFlag1 = file1
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;

        String validArgsDupeFlag2 = ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file2
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;

        String validArgsDupe3 = ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file3;

        assertParseSuccess(parser, validArgs1,
                new ImportCommand(file1, true, false));
        assertParseSuccess(parser, validArgs2,
                new ImportCommand(file2, true, false));
        assertParseSuccess(parser, validArgs3,
                new ImportCommand(file3, true, false));
        assertParseSuccess(parser, validArgs4,
                new ImportCommand(file4, true, false));
        assertParseSuccess(parser, validArgs5,
                new ImportCommand(file5, true, false));
        assertParseSuccess(parser, validArgs6,
                new ImportCommand(file6, true, false));

        assertParseSuccess(parser, validArgsDupeFlag1,
                new ImportCommand(file1, true, false));
        assertParseSuccess(parser, validArgsDupeFlag2,
                new ImportCommand(file2, true, false));
        assertParseSuccess(parser, validArgsDupe3,
                new ImportCommand(file3, true, false));
    }

    @Test
    public void parse_onlySuppressDupeErrors_success() {
        String file1 = "C:/Users/DummyUser/data.json";
        String file2 = "../../data.json";
        String file3 = "C:/Users/FalseUser/data.json randomWord";
        String file4 = "the parser faithfully parse the argument, the command checks the path validity";
        String file5 = "/Documents/--overwrite/data.json";
        String file6 = "/Documents/Subfolder/--ignore-duplicates/ data.json";

        String validArgs1 = file1 + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;
        String validArgs2 = " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + " " + file2;
        String validArgs3 = " " + file3 + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;
        String validArgs4 = file4 + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;
        String validArgs5 = "   " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + "   " + file5 + "     ";
        String validArgs6 = "   " + file6 + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG + "  ";

        String validArgsDupeFlag1 = file1
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;

        String validArgsDupeFlag2 = ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file2
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;

        String validArgsDupe3 = ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file3;

        assertParseSuccess(parser, validArgs1,
                new ImportCommand(file1, false, true));
        assertParseSuccess(parser, validArgs2,
                new ImportCommand(file2, false, true));
        assertParseSuccess(parser, validArgs3,
                new ImportCommand(file3, false, true));
        assertParseSuccess(parser, validArgs4,
                new ImportCommand(file4, false, true));
        assertParseSuccess(parser, validArgs5,
                new ImportCommand(file5, false, true));
        assertParseSuccess(parser, validArgs6,
                new ImportCommand(file6, false, true));

        assertParseSuccess(parser, validArgsDupeFlag1,
                new ImportCommand(file1, false, true));
        assertParseSuccess(parser, validArgsDupeFlag2,
                new ImportCommand(file2, false, true));
        assertParseSuccess(parser, validArgsDupe3,
                new ImportCommand(file3, false, true));
    }

    // by right, the presence of the overwrite flag will ignore the suppress-duplicate-error flag,
    // but the parser simply detects any known flag
    @Test
    public void parse_overwriteAndSuppressDupeErrors_success() {
        String file1 = "C:/Users/DummyUser/data.json";
        String file2 = "../../data.json";
        String file3 = "C:/Users/FalseUser/data.json randomWord";
        String file4 = "the parser faithfully parse the argument, the command checks the path validity";
        String file5 = "/Documents/--overwrite/data.json";
        String file6 = "/Documents/Subfolder/--ignore-duplicates/ data.json";

        String validArgs1 = file1
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;
        String validArgs2 = ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file2
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;
        String validArgs3 = ImportCommandParser.IS_OVERWRITE_FLAG
                + "   " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + "  " + file3 + "   ";
        String validArgs4 = " " + file4
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;
        String validArgs5 = "  " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + "   " + file5
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;
        String validArgs6 = "    " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file6 + "   ";

        String validArgsDupeOverwrite1 = file1
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;

        String validArgsDupeOverwrite2 = ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file2
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;

        String validArgsDupeSuppressDupeErrors1 = file1
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG;

        String validArgsDupeSuppressDupeErrors2 = ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file2
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;

        String validArgsDupeAllFlags = ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file1
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG;

        assertParseSuccess(parser, validArgs1,
                new ImportCommand(file1, true, true));
        assertParseSuccess(parser, validArgs2,
                new ImportCommand(file2, true, true));
        assertParseSuccess(parser, validArgs3,
                new ImportCommand(file3, true, true));
        assertParseSuccess(parser, validArgs4,
                new ImportCommand(file4, true, true));
        assertParseSuccess(parser, validArgs5,
                new ImportCommand(file5, true, true));
        assertParseSuccess(parser, validArgs6,
                new ImportCommand(file6, true, true));

        assertParseSuccess(parser, validArgsDupeOverwrite1,
                new ImportCommand(file1, true, true));
        assertParseSuccess(parser, validArgsDupeOverwrite2,
                new ImportCommand(file2, true, true));
        assertParseSuccess(parser, validArgsDupeSuppressDupeErrors1,
                new ImportCommand(file1, true, true));
        assertParseSuccess(parser, validArgsDupeSuppressDupeErrors2,
                new ImportCommand(file2, true, true));

        assertParseSuccess(parser, validArgsDupeAllFlags,
                new ImportCommand(file1, true, true));
    }

    // even though paths that are space-separated are allowed, if there is a
    // flag within the path, also space-separated, such as:
    // "/Documents/Dummy/ --my-flag /CraftConnectData/file.json"
    // this will count as two paths, and return an error
    @Test
    public void parse_moreThanOnePath_throwsParseException() {
        String file1 = "C:/Users/DummyUser/data.json";
        String file2 = "../../data.json";
        String file3 = "C:/Users/FalseUser/data.json randomWord";
        String file4 = "the parser faithfully parse the argument, the command checks the path validity";
        String file5 = "/Documents/--overwrite/data.json";
        String file6 = "/Documents/Subfolder/--ignore-duplicates/ data.json";

        String invalidArgs1 = file1
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file2;
        String invalidArgs2 = ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file3
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + file4;
        String invalidArgs3 = file5
                + " " + ImportCommandParser.IS_OVERWRITE_FLAG
                + " " + ImportCommandParser.SUPPRESSES_DUPLICATE_ERROR_FLAG
                + " " + file6;

        String expectedMessage = ImportCommand.MESSAGE_TOO_MANY_ARGUMENTS;

        assertParseFailure(parser, invalidArgs1, expectedMessage);
        assertParseFailure(parser, invalidArgs2, expectedMessage);
        assertParseFailure(parser, invalidArgs3, expectedMessage);
    }

    private static class StorageStub implements Storage {
        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBookIgnoreDuplicates(Path filePath) {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {

        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {

        }

        @Override
        public Path getUserPrefsFilePath() {
            return null;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {

        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }
    }
}
