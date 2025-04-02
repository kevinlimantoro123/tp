package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class NoteCommandTest {
    private static final String NOTE_TEST = "This is a test note";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addNote_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withNote(NOTE_TEST).build();

        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(editedPerson.getNote().value));
        String expectedMessage = String.format(NoteCommand.MESSAGE_ADD_NOTE_SUCCESS, Messages.format(editedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyNote_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withNote("").build();

        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(editedPerson.getNote().value));
        String expectedMessage = String.format(NoteCommand.MESSAGE_ADD_NOTE_SUCCESS, Messages.format(editedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, new Note(""));
        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, new Note(""));

        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(VALID_NOTE_BOB));

        // same values -> returns true
        NoteCommand commandWithSameValues = new NoteCommand(INDEX_FIRST_PERSON, new Note(VALID_NOTE_BOB));
        assertTrue(noteCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(noteCommand.equals(noteCommand));

        // null -> returns false
        assertFalse(noteCommand.equals(null));

        // different types -> returns false
        assertFalse(noteCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(noteCommand.equals(new NoteCommand(INDEX_SECOND_PERSON, new Note(VALID_NOTE_BOB))));

        // different note -> returns false
        assertFalse(noteCommand.equals(new NoteCommand(INDEX_FIRST_PERSON, new Note("Different note"))));
    }
}
