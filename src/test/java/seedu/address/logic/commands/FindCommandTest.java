package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.EmailIsKeywordPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneIsKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // stub for unit test
    private final Predicate<Person> alwaysTruePersonPredicateStub = person -> true;

    // stub for unit test
    private final Predicate<Person> alwaysFalsePersonPredicateStub = person -> false;

    @Test
    public void equals() {
        FindCommand findFirstCommand = new FindCommand(alwaysTruePersonPredicateStub);
        FindCommand findSecondCommand = new FindCommand(alwaysFalsePersonPredicateStub);

        // same object -> returns true
        assertEquals(findFirstCommand, findFirstCommand);

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(alwaysTruePersonPredicateStub);
        assertEquals(findFirstCommand, findFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, findFirstCommand);

        // null -> returns false
        assertNotEquals(null, findFirstCommand);

        // different person -> returns false
        assertNotEquals(findFirstCommand, findSecondCommand);
    }

    @Test
    public void execute_alwaysFalsePredicate_everyoneFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindCommand command = new FindCommand(alwaysFalsePersonPredicateStub);

        expectedModel.updateFilteredPersonList(alwaysFalsePersonPredicateStub);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_alwaysTruePredicate_everyoneFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        FindCommand command = new FindCommand(alwaysTruePersonPredicateStub);

        expectedModel.updateFilteredPersonList(alwaysTruePersonPredicateStub);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE), model.getFilteredPersonList());
    }

    // integration test - email
    @Test
    public void execute_nonExistentEmailPredicate_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        EmailIsKeywordPredicate predicate = new EmailIsKeywordPredicate("ilovecraftconnect@gmail.com");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_existentEmailPredicate_onlyOnePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        EmailIsKeywordPredicate predicate = new EmailIsKeywordPredicate("alice@example.com");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    // integration test - phone
    @Test
    public void execute_nonExistentPhonePredicate_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        PhoneIsKeywordPredicate predicate = new PhoneIsKeywordPredicate("92424353");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_existentPhonePredicate_onlyOnePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        PhoneIsKeywordPredicate predicate = new PhoneIsKeywordPredicate("94351253");
        FindCommand command = new FindCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        // unit test
        FindCommand findCommand1 = new FindCommand(alwaysTruePersonPredicateStub);
        String expected1 = FindCommand.class.getCanonicalName() + "{predicate=" + alwaysTruePersonPredicateStub + "}";
        assertEquals(expected1, findCommand1.toString());

        // integration test - email
        EmailIsKeywordPredicate emailPredicate = new EmailIsKeywordPredicate("ilovecraftconnect@gmail.com");
        FindCommand findCommand2 = new FindCommand(emailPredicate);
        String expected2 = FindCommand.class.getCanonicalName() + "{predicate=" + emailPredicate + "}";
        assertEquals(expected2, findCommand2.toString());

        // integration test - phone
        PhoneIsKeywordPredicate phonePredicate = new PhoneIsKeywordPredicate("12340987");
        FindCommand findCommand3 = new FindCommand(phonePredicate);
        String expected3 = FindCommand.class.getCanonicalName() + "{predicate=" + phonePredicate + "}";
        assertEquals(expected3, findCommand3.toString());
    }
}
