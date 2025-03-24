package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class ImportCommandTest {
    private static Model model;

    private static Path tempDir;
    private static File defaultJsonFile, defaultUserPrefsFile;
    private static File validJsonFile, invalidJsonFile, notJsonFile, nonExistentFile, emptyJsonFile;
    private static Storage storage;

    /**
     * Create a temporary directory and its stub files.
     * @throws Exception any exception from reading, loading files... A correct setup would not throw Exceptions.
     */
    @BeforeAll
    static void setUp() throws Exception {
        // set up a temporary directory
        tempDir = Files.createTempDirectory("importTest");

        // set up the test files
        validJsonFile = new File(tempDir.toFile(), "valid_contacts.json");
        invalidJsonFile = new File(tempDir.toFile(), "invalid_contacts.json");
        notJsonFile = new File(tempDir.toFile(), "not_json.txt");
        nonExistentFile = new File(tempDir.toFile(), "non_existent_file.json");
        defaultJsonFile = new File(tempDir.toFile(), "addressbook.json");
        defaultUserPrefsFile = new File(tempDir.toFile(), "preferences.json");
        emptyJsonFile = new File(tempDir.toFile(), "empty.json");

        // set up stub files for address book data and user preferences data
        String defaultJsonContent = """
{
  "persons" : [ {
    "name" : "Alex Yeoh",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "customer" ]
  }, {
    "name" : "Bernice Yu",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "bulkbuyer", "customer" ]
  }, {
    "name" : "Charlotte Oliveiro",
    "phone" : "93210283",
    "email" : "charlotte@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "boothRentalCraftFes" ]
  }, {
    "name" : "David Li",
    "phone" : "91031282",
    "email" : "lidavid@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "tags" : [ "keychainManufacturer" ]
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ]
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ]
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(defaultJsonFile)) {
            fileWriter.write(defaultJsonContent);
        }

        String defaultUserPrefsContent = String.format("""
{
  "guiSettings" : {
    "windowWidth" : 719.3333129882812,
    "windowHeight" : 668.6666870117188,
    "windowCoordinates" : {
      "x" : 424,
      "y" : 171
    }
  },
  "addressBookFilePath" : "%s"
}
                """, defaultJsonFile.getAbsolutePath().replace("\\", "\\\\"));

        try (FileWriter fileWriter = new FileWriter(defaultUserPrefsFile)) {
            fileWriter.write(defaultUserPrefsContent);
        }

        storage = new StorageManager(
                new JsonAddressBookStorage(defaultJsonFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        FileBasedCommand.bindStorage(storage);

        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            model = new ModelManager(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        } else {
            throw new Exception("Stub files empty!");
        }

        // write to valid JSON file
        String validJsonContent = """
{
  "persons" : [ {
    "name" : "Dickson Tan",
    "phone" : "92343827",
    "email" : "dicksontan@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "boothPartner" ]
  }, {
    "name" : "Mirai Sasaki",
    "phone" : "83082531",
    "email" : "miraisasaki@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "customer" ]
  }, {
    "name" : "Ismail bin Mail",
    "phone" : "99273461",
    "email" : "ismailbinmail@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "keychainManufacturer", "paintSupplier" ]
  } ]
}
                """;
        try (FileWriter fileWriter = new FileWriter(validJsonFile)) {
            fileWriter.write(validJsonContent);
        }

        // write to invalid JSON file
        String invalidJsonContent = """
{
  "something_wrong" : [ {
    "not_name" : "Dickson Tan",
    "not_phone" : "92343827",
    "not_email" : "dicksontan@example.com",
    "not_address" : "Blk 30 Geylang Street 29, #06-40",
    "not_tags" : [ "boothPartner" ]
  } ]
}
                """;
        try (FileWriter fileWriter = new FileWriter(invalidJsonFile)) {
            fileWriter.write(invalidJsonContent);
        }

        // not a JSON file
        try (FileWriter fileWriter = new FileWriter(notJsonFile)) {
            fileWriter.write("NAHHHHH!");
        }

        // empty JSON file
        try (FileWriter fileWriter = new FileWriter(emptyJsonFile)) {
            fileWriter.write("");
        }
    }

    @Test
    public void execute_nonExistentFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(nonExistentFile.getAbsolutePath());
        String expectedMessage = String.format(ImportCommand.MESSAGE_ERROR, ImportCommand.MESSAGE_FILE_DOES_NOT_EXIST);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(nonExistentFile.getPath());
        assertCommandFailure(relativePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_notJsonFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(notJsonFile.getAbsolutePath());
        String expectedMessage = String.format(ImportCommand.MESSAGE_ERROR, ImportCommand.MESSAGE_NOT_JSON_FILE);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(notJsonFile.getPath());
        assertCommandFailure(relativePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidJsonSchemaFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(invalidJsonFile.getAbsolutePath());
        String expectedMessage = String.format(ImportCommand.MESSAGE_ERROR, ImportCommand.MESSAGE_INCOMPATIBLE_SCHEMA);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(invalidJsonFile.getPath());
        assertCommandFailure(relativePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_emptyJsonFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(emptyJsonFile.getAbsolutePath());
        String expectedMessage = String.format(ImportCommand.MESSAGE_ERROR, ImportCommand.MESSAGE_INCOMPATIBLE_SCHEMA);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_validJsonSchemaFile_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validJsonFile.getAbsolutePath());

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(validJsonFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelManager();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelManager(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(validJsonFile.toPath());
        Files.deleteIfExists(invalidJsonFile.toPath());
        Files.deleteIfExists(nonExistentFile.toPath());
        Files.deleteIfExists(notJsonFile.toPath());
        Files.deleteIfExists(defaultJsonFile.toPath());
        Files.deleteIfExists(defaultUserPrefsFile.toPath());
        Files.deleteIfExists(emptyJsonFile.toPath());
        Files.delete(tempDir);
    }
}
