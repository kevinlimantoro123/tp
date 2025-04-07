package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.modifications.Modification;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.CannotRedoException;
import seedu.address.model.person.exceptions.CannotUndoException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns a person with the same phone number as {@code person}, or null with none exists.
     */
    Person findPersonWithSamePhoneNumber(Person person);

    /**
     * Returns a person with the same email as {@code person}, or null with none exists.
     */
    Person findPersonWithSameEmail(Person person);


    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Signals to the model that a command that modifies the address book has finished
     * and the state of the address book should be stored.
     * @param modification A record of the modification done to the address book.
     */
    void commitAddressBook(Modification modification);

    /**
     * Restores the state of the address book before the last command that modifies it.
     * @returns The Modification that was undone.
     * @throws CannotUndoException If no more undos are possible.
     */
    Modification undoAddressBook() throws CannotUndoException;

    /**
     * Restores the state of the address book before the last call to undoAddressBook.
     * @returns The Modification that was restored.
     * @throws CannotRedoException If no more redos are possible.
     */
    Modification redoAddressBook() throws CannotRedoException;

    /**
     * Undoes the last n (or all, if there are less than n) modifications to the address book.
     * @param numberOfTimes The value of n.
     * @returns The list of Modifications that were undone.
     */
    List<Modification> undoAddressBookMultiple(int numberOfTimes);

    /**
     * Restores the last n (or all, if there are less than n) undone modifications to the address book.
     * @param numberOfTimes The value of n.
     * @returns The list of Modifications that were undone.
     */
    List<Modification> redoAddressBookMultiple(int numberOfTimes);
}
