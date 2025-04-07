package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.modifications.Modification;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.CannotRedoException;
import seedu.address.model.person.exceptions.CannotUndoException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBookStateManager addressBookStateManager;
    private final UserPrefs userPrefs;
    private FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBookStateManager = new AddressBookStateManager(new AddressBook(addressBook));
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.getAddressBook().getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    private AddressBook getInternalAddressBook() {
        return this.addressBookStateManager.getCurrentAddressBook();
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.getInternalAddressBook().resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return this.addressBookStateManager.getCurrentAddressBook();
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return this.getInternalAddressBook().hasPerson(person);
    }

    @Override
    public Person findPersonWithSameEmail(Person person) {
        requireNonNull(person);
        return this.getInternalAddressBook().findPersonWithSameEmail(person);
    }

    @Override
    public Person findPersonWithSamePhoneNumber(Person person) {
        requireNonNull(person);
        return this.getInternalAddressBook().findPersonWithSamePhoneNumber(person);
    }

    @Override
    public void deletePerson(Person target) {
        this.getInternalAddressBook().removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        this.getInternalAddressBook().addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        this.getInternalAddressBook().setPerson(target, editedPerson);
    }
    //=========== addressBookStateHistory ===================================================================

    @Override
    public void commitAddressBook(Modification modification) {
        this.addressBookStateManager.commit(modification);
    }

    @Override
    public Modification undoAddressBook() throws CannotUndoException {
        Modification undoneMod = this.addressBookStateManager.undo();
        return undoneMod;
    }
    @Override
    public Modification redoAddressBook() throws CannotRedoException {
        Modification restoredMod = this.addressBookStateManager.redo();
        return restoredMod;
    }

    @Override
    public List<Modification> undoAddressBookMultiple(int numberOfTimes) {
        return this.addressBookStateManager.undoMultiple(numberOfTimes);
    }

    @Override
    public List<Modification> redoAddressBookMultiple(int numberOfTimes) {
        return this.addressBookStateManager.redoMultiple(numberOfTimes);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBookStateManager.equals(otherModelManager.addressBookStateManager)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
