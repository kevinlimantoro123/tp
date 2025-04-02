package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.CannotRedoException;
import seedu.address.model.person.exceptions.CannotUndoException;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.FileUtil;
import seedu.address.testutil.PersonBuilder;

public class ImportCommandTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "FileBasedCommandTest")
                    .toAbsolutePath();

    private static Model model;

    private static File defaultJsonFile;
    private static File defaultUserPrefsFile;
    private static File invalidJsonFile;
    private static File notJsonFile;
    private static File nonExistentFile;
    private static File emptyJsonFile;

    private static File validNoDupesJsonFile;
    private static File validJsonDupePhoneFile;
    private static File validJsonDupeEmailFile;

    private static File expectedJsonAfterAppendNoDupeFile;
    private static File expectedJsonAfterAppendDupePhoneFile;
    private static File expectedJsonAfterAppendDupeEmailFile;

    private static File duplicatedPersonFile;
    private static File expectedJsonAfterOverwriteDupeFile;

    private static Storage storage;

    /**
     * Create a temporary directory and its stub files.
     * @throws Exception any exception from reading, loading files... A correct setup would not throw Exceptions.
     */
    @BeforeAll
    public static void setUp() throws Exception {
        // set up a temporary directory

        // set up the test files
        invalidJsonFile = new File(TEST_DATA_FOLDER.toFile(), "invalid_contacts.json");
        notJsonFile = new File(TEST_DATA_FOLDER.toFile(), "not_json.txt");
        nonExistentFile = new File(TEST_DATA_FOLDER.toFile(), "non_existent_file.json");
        defaultJsonFile = new File(TEST_DATA_FOLDER.toFile(), "addressbook.json");
        defaultUserPrefsFile = new File(TEST_DATA_FOLDER.toFile(), "preferences.json");
        emptyJsonFile = new File(TEST_DATA_FOLDER.toFile(), "empty.json");

        validNoDupesJsonFile = new File(TEST_DATA_FOLDER.toFile(), "valid_contacts.json");
        validJsonDupePhoneFile = new File(TEST_DATA_FOLDER.toFile(), "valid_json_dupe_phone.json");
        validJsonDupeEmailFile = new File(TEST_DATA_FOLDER.toFile(), "valid_json_dupe_email.json");

        expectedJsonAfterAppendNoDupeFile = new File(TEST_DATA_FOLDER.toFile(), "expected_json_append_no_dupe.json");
        expectedJsonAfterAppendDupeEmailFile =
                new File(TEST_DATA_FOLDER.toFile(), "expected_json_append_dupe_email.json");
        expectedJsonAfterAppendDupePhoneFile =
                new File(TEST_DATA_FOLDER.toFile(), "expected_json_append_dupe_phone.json");

        duplicatedPersonFile = new File(TEST_DATA_FOLDER.toFile(), "duplicate_person_address_book.json");
        expectedJsonAfterOverwriteDupeFile =
                new File(TEST_DATA_FOLDER.toFile(), "expected_json_overwrite_dupe.json");

        // set up stub files for address book data and user preferences data
        // the files are already populated, but re-population is performed just in case
        FileUtil.populateDefaultJsonFile(defaultJsonFile);
        FileUtil.populateInvalidJsonFile(invalidJsonFile);
        FileUtil.populateNotJsonFile(notJsonFile);
        FileUtil.populateEmptyJsonFile(emptyJsonFile);
        FileUtil.populateDefaultUserPrefsFile(defaultUserPrefsFile, defaultJsonFile);

        FileUtil.populateValidNoDupesJsonFile(validNoDupesJsonFile);
        FileUtil.populateValidDupePhoneJsonFile(validJsonDupePhoneFile);
        FileUtil.populateValidDupeEmailJsonFile(validJsonDupeEmailFile);

        FileUtil.populateExpectedAfterAppendNoDupe(expectedJsonAfterAppendNoDupeFile);
        FileUtil.populateExpectedAfterAppendDupePhone(expectedJsonAfterAppendDupePhoneFile);
        FileUtil.populateExpectedAfterAppendDupeEmail(expectedJsonAfterAppendDupeEmailFile);

        FileUtil.populateDuplicatedPersonFile(duplicatedPersonFile);
        FileUtil.populateExpectedAfterOverwriteDupe(expectedJsonAfterOverwriteDupeFile);
    }

    /**
     * Resets the model for every test.
     * @throws Exception if any exception from loading, reading files...
     */
    @BeforeEach
    public void resetModel() throws Exception {
        FileUtil.populateDefaultJsonFile(defaultJsonFile);

        storage = new StorageManager(
                new JsonAddressBookStorage(defaultJsonFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath())
        );

        FileBasedCommand.bindStorage(storage);

        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            model = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get()
            );
        } else {
            throw new Exception("Stub files empty!");
        }
    }

    @Test
    public void execute_nonExistentFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(nonExistentFile.getAbsolutePath(), true, false);
        String expectedMessage = ImportCommand.generateErrorMessage(
                nonExistentFile.getPath(),
                ImportCommand.MESSAGE_FILE_DOES_NOT_EXIST
        );
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(nonExistentFile.getPath(), true, false);
        assertCommandFailure(relativePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(nonExistentFile.getAbsolutePath(), true, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(nonExistentFile.getAbsolutePath(), false, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(nonExistentFile.getAbsolutePath(), false, false);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_notJsonFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(notJsonFile.getAbsolutePath(), true, false);
        String expectedMessage = ImportCommand.generateErrorMessage(
                notJsonFile.getPath(),
                ImportCommand.MESSAGE_NOT_JSON_FILE
        );
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(notJsonFile.getPath(), true, false);
        assertCommandFailure(relativePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(notJsonFile.getAbsolutePath(), true, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(notJsonFile.getAbsolutePath(), false, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(notJsonFile.getAbsolutePath(), false, false);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidJsonSchemaFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(invalidJsonFile.getAbsolutePath(), true, false);
        String expectedMessage = ImportCommand.generateErrorMessage(
                invalidJsonFile.getPath(),
                ImportCommand.MESSAGE_INCOMPATIBLE_SCHEMA
        );
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        ImportCommand relativePathCommand = new ImportCommand(invalidJsonFile.getPath(), true, false);
        assertCommandFailure(relativePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(invalidJsonFile.getAbsolutePath(), true, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(invalidJsonFile.getAbsolutePath(), false, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(invalidJsonFile.getAbsolutePath(), false, false);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void execute_emptyJsonFile_throwsCommandException() {
        ImportCommand absolutePathCommand = new ImportCommand(emptyJsonFile.getAbsolutePath(), true, false);
        String expectedMessage = ImportCommand.generateErrorMessage(
                emptyJsonFile.getPath(),
                ImportCommand.MESSAGE_INCOMPATIBLE_SCHEMA
        );
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(emptyJsonFile.getAbsolutePath(), true, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(emptyJsonFile.getAbsolutePath(), false, true);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);

        absolutePathCommand = new ImportCommand(emptyJsonFile.getAbsolutePath(), false, false);
        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    // __________
    // The following tests enable overwrite
    // __________
    @Test
    public void executeOverwriteNoSuppressDupeErrors_validJsonSchemaFile_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validNoDupesJsonFile.getAbsolutePath(), true, false);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(validNoDupesJsonFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeOverwriteSuppressDupeErrors_validJsonSchemaFile_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validNoDupesJsonFile.getAbsolutePath(), true, true);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(validNoDupesJsonFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // __________
    // The following tests enable appending mode and suppression of duplicate errors
    // __________
    @Test
    public void executeAppendSuppressDupeErrors_validJsonSchemaFileNoDupes_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validNoDupesJsonFile.getAbsolutePath(), false, true);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(expectedJsonAfterAppendNoDupeFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeAppendSuppressDupeErrors_validJsonSchemaFileDupePhone_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validJsonDupePhoneFile.getAbsolutePath(), false, true);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(expectedJsonAfterAppendDupePhoneFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeAppendSuppressDupeErrors_validJsonSchemaFileDupeEmail_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validJsonDupeEmailFile.getAbsolutePath(), false, true);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(expectedJsonAfterAppendDupeEmailFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // __________
    // The following tests enable appending mode and throwing of duplicate errors
    // __________
    @Test
    public void executeAppendThrowDupeErrors_validJsonSchemaFileNoDupes_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(validNoDupesJsonFile.getAbsolutePath(), false, false);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(expectedJsonAfterAppendNoDupeFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeAppendThrowDupeErrors_validJsonSchemaFileDupePhone_throwCommandError() {
        ImportCommand absolutePathCommand = new ImportCommand(validJsonDupePhoneFile.getAbsolutePath(), false, false);

        Person duplicatedPerson = (new PersonBuilder())
                .withName("David Li")
                .withPhone("87438807")
                .withEmail("lidavid@example.com")
                .withAddress("Blk 436 Serangoon Gardens Street 26, #16-43")
                .withTags("keychainManufacturer")
                .build();

        String expectedMessage = String.format(
                ImportCommand.MESSAGE_DUPLICATE_PHONE,
                Messages.format(duplicatedPerson));

        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void executeAppendThrowDupeErrors_validJsonSchemaFileDupeEmail_throwCommandError() {
        ImportCommand absolutePathCommand = new ImportCommand(validJsonDupeEmailFile.getAbsolutePath(), false, false);

        Person duplicatedPerson = (new PersonBuilder())
                .withName("David Li")
                .withPhone("91031282")
                .withEmail("alexyeoh@example.com")
                .withAddress("Blk 436 Serangoon Gardens Street 26, #16-43")
                .withTags("keychainManufacturer")
                .build();

        String expectedMessage = String.format(
                ImportCommand.MESSAGE_DUPLICATE_EMAIL,
                Messages.format(duplicatedPerson));

        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void executeOverwriteThrowDupeErrors_selfDupedFile_throwCommandError() {
        ImportCommand absolutePathCommand = new ImportCommand(duplicatedPersonFile.getAbsolutePath(), true, false);

        String expectedMessage = ImportCommand.generateErrorMessage(
                duplicatedPersonFile.getPath(),
                ImportCommand.MESSAGE_INCOMPATIBLE_SCHEMA
        );

        assertCommandFailure(absolutePathCommand, model, expectedMessage);
    }

    @Test
    public void executeOverwriteSuppressDupeErrors_selfDupedFile_success() throws DataLoadingException {
        ImportCommand absolutePathCommand = new ImportCommand(duplicatedPersonFile.getAbsolutePath(), true, true);

        Storage expectedStorage = new StorageManager(
                new JsonAddressBookStorage(expectedJsonAfterOverwriteDupeFile.toPath()),
                new JsonUserPrefsStorage(defaultUserPrefsFile.toPath()));

        Model expectedModel = new ModelStub();
        if (storage.readAddressBook().isPresent() && storage.readUserPrefs().isPresent()) {
            expectedModel = new ModelStub(
                    storage.readAddressBook().get(),
                    storage.readUserPrefs().get());
        }

        if (expectedStorage.readAddressBook().isPresent()) {
            expectedModel.setAddressBook(expectedStorage.readAddressBook().get());
        }

        assertCommandSuccess(absolutePathCommand, model, ImportCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // __________
    // The following tests are about other utility methods
    // __________
    @Test
    public void equals() {
        String notEvenAPath = "???";
        String alsoNotEvenAPath = "???";

        // same object -> returns true
        ImportCommand command = new ImportCommand("hello world", true, false);
        assertEquals(command, command);

        // same arguments -> returns true
        assertEquals(
                new ImportCommand(notEvenAPath, true, false),
                new ImportCommand(notEvenAPath, true, false)
        );

        assertEquals(
                new ImportCommand(notEvenAPath, true, false),
                new ImportCommand(alsoNotEvenAPath, true, false)
        );

        // different argument(s) -> returns false
        assertNotEquals(
                new ImportCommand(notEvenAPath, true, false),
                new ImportCommand(notJsonFile.getPath(), true, false)
        );

        assertNotEquals(
                new ImportCommand(notEvenAPath, true, true),
                new ImportCommand(notEvenAPath, true, false)
        );

        assertNotEquals(
                new ImportCommand(notEvenAPath, true, true),
                new ImportCommand(notEvenAPath, false, true)
        );

        assertNotEquals(
                new ImportCommand(invalidJsonFile.getPath(), true, false),
                new ImportCommand(validNoDupesJsonFile.getPath(), true, false)
        );

        // incompatible types -> return false
        assertNotEquals(
                new ImportCommand(invalidJsonFile.getPath(), true, false),
                1
        );

        // null -> returns false
        assertNotEquals(
                null,
                new ImportCommand(emptyJsonFile.getPath(), true, false)
        );
    }

    @Test
    public void toStringMethod() {
        ImportCommand importCommand = new ImportCommand(validNoDupesJsonFile.getPath(), true, false);
        String expected = ImportCommand.class.getCanonicalName()
                + "{path=" + validNoDupesJsonFile.getPath()
                + ", overwrite=" + true
                + ", suppressDupeErrors=" + false
                + "}";
        assertEquals(expected, importCommand.toString());
    }

    @AfterAll
    static void tearDown() throws IOException {
        FileUtil.populateDefaultJsonFile(defaultJsonFile);
    }

    private static class ModelStub implements Model {
        private AddressBook addressBook;
        private UserPrefs userPrefs;
        private FilteredList<Person> filteredPersons;

        public ModelStub(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
            this.addressBook = new AddressBook(addressBook);
            this.userPrefs = new UserPrefs(userPrefs);
            filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        }

        public ModelStub() {
            this(new AddressBook(), new UserPrefs());
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {

        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {

        }

        @Override
        public Path getAddressBookFilePath() {
            return this.userPrefs.getAddressBookFilePath();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            this.userPrefs.setAddressBookFilePath(addressBookFilePath);
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            this.addressBook.resetData(addressBook);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return this.addressBook;
        }

        @Override
        public boolean hasPerson(Person person) {
            return this.addressBook.hasPerson(person);
        }

        @Override
        public Person findPersonWithSamePhoneNumber(Person person) {
            return this.addressBook.findPersonWithSamePhoneNumber(person);
        }

        @Override
        public Person findPersonWithSameEmail(Person person) {
            return this.addressBook.findPersonWithSameEmail(person);
        }

        @Override
        public void deletePerson(Person target) {
            this.addressBook.removePerson(target);
        }

        @Override
        public void addPerson(Person person) {
            this.addressBook.addPerson(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            this.addressBook.setPerson(target, editedPerson);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return this.filteredPersons;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            this.filteredPersons.setPredicate(predicate);
        }

        @Override
        public void commitAddressBook(Command command) {

        }

        @Override
        public Command undoAddressBook() {
            return null;
        }

        @Override
        public Command redoAddressBook() {
            return null;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof ModelStub)) {
                return false;
            }

            ModelStub otherModelStub = (ModelStub) other;
            return userPrefs.equals(otherModelStub.userPrefs)
                    && filteredPersons.equals(otherModelStub.filteredPersons);
        }
    }
}
