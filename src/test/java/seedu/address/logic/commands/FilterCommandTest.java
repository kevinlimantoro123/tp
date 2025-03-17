package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import seedu.address.model.person.Person;

public class FilterCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // stub for unit test
    private final Predicate<Person> alwaysTruePersonPredicateStub = person -> true;

    // stub for unit test
    private final Predicate<Person> alwaysFalsePersonPredicateStub = person -> false;

    @Test
    public void equals() {
        FilterCommand filterFirstCommand = new FilterCommand(alwaysTruePersonPredicateStub);
        FilterCommand findSecondCommand = new FilterCommand(alwaysFalsePersonPredicateStub);

        // same object -> returns true
        assertEquals(filterFirstCommand, filterFirstCommand);

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(alwaysTruePersonPredicateStub);
        assertEquals(filterFirstCommand, filterFirstCommandCopy);

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // different person -> returns false
        assertFalse(filterFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_alwaysFalsePredicate_everyoneFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FilterCommand command = new FilterCommand(alwaysFalsePersonPredicateStub);
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

}
