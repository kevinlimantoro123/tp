package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

public class ExportCommandTest {
    private static Model model;

    private static Path tempDir;
    private static Path nonExistentDir;
    private static Path toCreateDir;
    private static Path existentDirDoNotUse;
    private static Path targetExistentDir;
    private static Storage storage;

    private static File defaultJsonFile;
    private static File defaultUserPrefsFile;

    /**
     * Create a temporary directory and its stub files.
     *
     * @throws Exception any exception from reading, loading files... A correct setup would not throw Exceptions.
     */
    @BeforeAll
    public static void setUp() throws Exception {
        // set up a temporary directory
        tempDir = Files.createTempDirectory("importTest");

        // create paths to sub-folders
        nonExistentDir = tempDir.resolve("nonExistentFolder");
        toCreateDir = tempDir.resolve("toCreateFolder");

        // create a sub-folder for the existent sub-folder path
        existentDirDoNotUse = Files.createTempDirectory("existentFolder");
        targetExistentDir = tempDir.resolve(existentDirDoNotUse.getFileName());
        Files.move(existentDirDoNotUse, targetExistentDir, StandardCopyOption.REPLACE_EXISTING);

        // set up the test files
        defaultJsonFile = new File(tempDir.toFile(), "addressbook.json");
        defaultUserPrefsFile = new File(tempDir.toFile(), "preferences.json");

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
    }

    // Invalid folder names are OS-dependent, so not everything can be tested.
    @Test
    @EnabledOnOs(OS.WINDOWS) // Run this test only on Windows
    public void execute_invalidFolderNamesWindows_throwsCommandException() {
        String[] invalidNames = {"folder?", "folder*", "folder|", "C:\\invalid<name>"};
        for (String name : invalidNames) {
            assertCommandFailure(
                    new ExportCommand(name, false),
                    model,
                    ExportCommand.generateErrorMessage(name, ExportCommand.MESSAGE_NOT_A_FOLDER)
            );
        }
    }

    @Test
    @EnabledOnOs(OS.LINUX) // Run this test only on Linux
    public void execute_invalidFolderNamesLinux_throwsCommandException() {
        String[] invalidNames = {"valid\0invalid"};
        for (String name : invalidNames) {
            assertCommandFailure(
                    new ExportCommand(name, false),
                    model,
                    ExportCommand.generateErrorMessage(name, ExportCommand.MESSAGE_NOT_A_FOLDER)
            );
        }
    }

    @Test
    public void execute_nonExistentFolder_throwsCommandException() {
        String nonExistentFolderString = nonExistentDir.toString();
        assertCommandFailure(
                new ExportCommand(nonExistentFolderString, false),
                model,
                ExportCommand.generateErrorMessage(nonExistentFolderString, ExportCommand.MESSAGE_FOLDER_DOES_NOT_EXIST)
        );

        String toCreateDirString = toCreateDir.toString();
        assertCommandFailure(
                new ExportCommand(toCreateDirString, false),
                model,
                ExportCommand.generateErrorMessage(toCreateDirString, ExportCommand.MESSAGE_FOLDER_DOES_NOT_EXIST)
        );
    }

    @Test
    public void execute_existentFolderNoCreateFolder_success() throws IOException {
        String targetExistentDirString = targetExistentDir.toString();
        Path exportedDataFilePath = targetExistentDir.resolve(ExportCommand.DEFAULT_EXPORT_FILE_NAME);

        // successful invocation should not alter model
        assertCommandSuccess(
                new ExportCommand(targetExistentDirString, false),
                model,
                String.format(ExportCommand.MESSAGE_SUCCESS, exportedDataFilePath),
                model
        );

        // exported data file exists
        assertTrue(Files.exists(exportedDataFilePath));

        // copy is successful (by comparing content)
        assertArrayEquals(
                Files.readAllLines(defaultJsonFile.toPath()).toArray(),
                Files.readAllLines(exportedDataFilePath).toArray()
        );
    }

    @Test
    public void execute_nonExistentFolderWithCreateFolder_success() throws IOException {
        Path exportedDataFilePath = toCreateDir.resolve(ExportCommand.DEFAULT_EXPORT_FILE_NAME);

        // successful invocation should not alter model
        assertCommandSuccess(
                new ExportCommand(toCreateDir.toString(), true),
                model,
                String.format(ExportCommand.MESSAGE_SUCCESS, exportedDataFilePath),
                model
        );

        // exported data file exists
        assertTrue(Files.exists(exportedDataFilePath));

        // copy is successful (by comparing content)
        assertArrayEquals(
                Files.readAllLines(defaultJsonFile.toPath()).toArray(),
                Files.readAllLines(exportedDataFilePath).toArray()
        );
    }

    @Test
    public void equals() {
        String targetFolder = "???";
        String alsoTargetFolder = "???";
        String targetFolder2 = "craftconnect";

        // same object -> returns true
        ExportCommand command = new ExportCommand("hello", false);
        assertEquals(command, command);

        // same target folder and create directory flag, should be equal
        assertEquals(
                new ExportCommand(targetFolder, true),
                new ExportCommand(targetFolder, true)
        );

        // same arguments -> returns true
        assertEquals(
                new ExportCommand(targetFolder, true),
                new ExportCommand(alsoTargetFolder, true)
        );

        assertEquals(
                new ExportCommand(alsoTargetFolder, false),
                new ExportCommand(targetFolder, false)
        );

        // different argument(s) -> returns false
        assertNotEquals(
                new ExportCommand(targetFolder, false),
                new ExportCommand(targetFolder2, false)
        );

        assertNotEquals(
                new ExportCommand(targetFolder, true),
                new ExportCommand(targetFolder2, true)
        );

        assertNotEquals(
                new ExportCommand(alsoTargetFolder, true),
                new ExportCommand(alsoTargetFolder, false)
        );

        // incompatible types -> returns false
        assertNotEquals(
                new ExportCommand(alsoTargetFolder, true),
                1
        );

        // null -> returns false
        assertNotEquals(
                new ExportCommand(targetFolder2, true),
                null
        );
    }

    @Test
    public void toStringMethod() {
        ExportCommand exportCommand = new ExportCommand(targetExistentDir.toString(), true);
        String expected = ExportCommand.class.getCanonicalName()
                + "{folderPath=" + targetExistentDir
                + ", createsDirectory=" + true
                + "}";
        assertEquals(expected, exportCommand.toString());
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(targetExistentDir.resolve(ExportCommand.DEFAULT_EXPORT_FILE_NAME));
        Files.deleteIfExists(targetExistentDir);
        Files.deleteIfExists(defaultJsonFile.toPath());
        Files.deleteIfExists(defaultUserPrefsFile.toPath());
        Files.deleteIfExists(existentDirDoNotUse);
        Files.deleteIfExists(toCreateDir.resolve(ExportCommand.DEFAULT_EXPORT_FILE_NAME));
        Files.deleteIfExists(toCreateDir);
        Files.deleteIfExists(nonExistentDir);
        Files.delete(tempDir);
    }
}
