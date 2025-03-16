package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // integration test by index
    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // integration test by email
    @Test
    public void execute_validEmailUnfilteredList_success() {
        Email validEmail = ALICE.getEmail();
        Person personToDelete = ALICE;
        DeleteCommand deleteCommand = new DeleteCommand(validEmail);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidEmailUnfilteredList_throwsCommandException() {
        Email validEmail = AMY.getEmail();
        DeleteCommand deleteCommand = new DeleteCommand(validEmail);

        String expectedMessage = DeleteCommand.NO_PERSON_WITH_MATCHING_EMAIL;

        assertCommandFailure(deleteCommand, model, expectedMessage);
    }

    // email is in filtered list
    @Test
    public void execute_validEmailFilteredList_success() {
        Person personToDelete = ALICE;
        Email emailOfPtd = personToDelete.getEmail();
        DeleteCommand deleteCommand = new DeleteCommand(emailOfPtd);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        // expect the end-result model to show the ORIGINAL list
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        // thus, copy the original model to expectedModel before we filter
        model.updateFilteredPersonList(person -> person.equals(ALICE));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // email is not in filtered list, but exists in unfiltered list
    @Test
    public void execute_validEmailNotInFilteredList_success() {
        Person personToDelete = BENSON;
        Email emailOfPtd = personToDelete.getEmail();
        DeleteCommand deleteCommand = new DeleteCommand(emailOfPtd);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        // expect the end-result model to show the ORIGINAL list
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        // thus, copy the original model to expectedModel before we filter
        model.updateFilteredPersonList(person -> person.equals(ALICE));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // integration test by phone
    @Test
    public void execute_validPhoneUnfilteredList_success() {
        Phone validEmail = BENSON.getPhone();
        Person personToDelete = BENSON;
        DeleteCommand deleteCommand = new DeleteCommand(validEmail);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPhoneUnfilteredList_throwsCommandException() {
        Phone validEmail = BOB.getPhone();
        DeleteCommand deleteCommand = new DeleteCommand(validEmail);

        String expectedMessage = DeleteCommand.NO_PERSON_WITH_MATCHING_PHONE;

        assertCommandFailure(deleteCommand, model, expectedMessage);
    }

    // phone is in filtered list
    @Test
    public void execute_validPhoneFilteredList_success() {
        Person personToDelete = ALICE;
        Phone phoneOfPtd = personToDelete.getPhone();
        DeleteCommand deleteCommand = new DeleteCommand(phoneOfPtd);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        // expect the end-result model to show the ORIGINAL list
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        // thus, copy the original model to expectedModel before we filter
        model.updateFilteredPersonList(person -> person.equals(ALICE));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // phone is not in filtered list, but exists in unfiltered list
    @Test
    public void execute_validPhoneNotInFilteredList_success() {
        Person personToDelete = BENSON;
        Phone phoneOfPtd = personToDelete.getPhone();
        DeleteCommand deleteCommand = new DeleteCommand(phoneOfPtd);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        // expect the end-result model to show the ORIGINAL list
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        // thus, copy the original model to expectedModel before we filter
        model.updateFilteredPersonList(person -> person.equals(ALICE));

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    // other methods
    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        DeleteCommand deleteEmailCommand = new DeleteCommand(CARL.getEmail());

        DeleteCommand deletePhoneCommand = new DeleteCommand(DANIEL.getPhone());

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertEquals(deleteFirstCommand, deleteFirstCommandCopy);

        DeleteCommand deleteEmailCommandCopy = new DeleteCommand(CARL.getEmail());
        assertEquals(deleteEmailCommand, deleteEmailCommandCopy);

        DeleteCommand deletePhoneCommandCopy = new DeleteCommand(DANIEL.getPhone());
        assertEquals(deletePhoneCommand, deletePhoneCommandCopy);

        // different types -> returns false
        assertNotEquals(1, deleteFirstCommand);
        assertNotEquals("Hello, world!", deletePhoneCommand);

        // null -> returns false
        assertNotEquals(null, deleteFirstCommand);

        // different person -> returns false
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);
        assertNotEquals(deleteEmailCommand, deletePhoneCommand);
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteIndexCommand = new DeleteCommand(targetIndex);
        String expectedForIndex = DeleteCommand.class.getCanonicalName() + "{target=" + targetIndex + "}";
        assertEquals(expectedForIndex, deleteIndexCommand.toString());

        Phone targetPhone = new Phone("98765432");
        DeleteCommand deletePhoneCommand = new DeleteCommand(targetPhone);
        String expectedForPhone = DeleteCommand.class.getCanonicalName() + "{target=" + targetPhone + "}";
        assertEquals(expectedForPhone, deletePhoneCommand.toString());

        Email targetEmail = new Email("ilovecraftconnect@example.com");
        DeleteCommand deleteEmailCommand = new DeleteCommand(targetEmail);
        String expectedForEmail = DeleteCommand.class.getCanonicalName() + "{target=" + targetEmail + "}";
        assertEquals(expectedForEmail, deleteEmailCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
