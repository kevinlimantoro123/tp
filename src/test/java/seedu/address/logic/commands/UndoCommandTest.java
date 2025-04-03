package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.modifications.AddMod;
import seedu.address.model.modifications.ClearMod;
import seedu.address.model.modifications.DeleteMod;
import seedu.address.model.modifications.EditMod;
import seedu.address.model.modifications.Modification;
import seedu.address.model.modifications.NoteMod;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class UndoCommandTest {

    private List<Modification> sampleModifications;
    private List<Person> samplePersons;

    private List<Model> sampleModels;

    @BeforeEach
    public void setUp() {
        samplePersons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            samplePersons.add(new PersonBuilder()
                .withName(Integer.toString((i + 1)))
                .withEmail(Integer.toString((i + 1)) + "@gmail.com")
                .withPhone(Integer.toString((i + 1) * 1111))
                .build());
        }

        sampleModifications = new ArrayList<>();
        sampleModifications.add(new AddMod(samplePersons.get(0)));
        sampleModifications.add(new DeleteMod(samplePersons.get(1)));
        sampleModifications.add(new EditMod(samplePersons.get(1), samplePersons.get(2)));
        sampleModifications.add(new NoteMod(samplePersons.get(3), new Note("sample note")));
        sampleModifications.add(new ClearMod());

        sampleModels = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
            for (int j = 0; j < i; j++) {
                model.addPerson(samplePersons.get(j));
                model.commitAddressBook(sampleModifications.get(j));
            }
            sampleModels.add(model);
        }
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        for (int j = 0; j < 5; j++) {
            model.addPerson(samplePersons.get(j));
            model.commitAddressBook(sampleModifications.get(j));
        }
        sampleModels.add(model);
    }

    @Test
    public void undoOnce_changesMade_success() {
        Model model = sampleModels.get(5);
        Model expectedModel = sampleModels.get(4);
        UndoCommand undoCommand = new UndoCommand(1);
        assertCommandSuccess(undoCommand, model,
                String.format(UndoCommand.MESSAGE_SUCCESS, sampleModifications.get(4).getUserDescription()),
                expectedModel);
    }

    @Test
    public void undoOnce_changesNotMade_failure() {
        Model model = sampleModels.get(0);
        UndoCommand undoCommand = new UndoCommand(1);
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_CANNOT_UNDO);
    }

    @Test
    public void undoMultiple_changesMade_success() {
        Model model = sampleModels.get(5);
        Model expectedModel = sampleModels.get(2);

        UndoCommand undoCommand = new UndoCommand(3);
        assertCommandSuccess(undoCommand, model,
                String.format(UndoCommand.MESSAGE_SUCCESS_MULTIPLE, 3, 3), expectedModel);
    }

    @Test
    public void undoMoreThanAvailable_changesMade_success() {
        Model model = sampleModels.get(5);
        Model expectedModel = sampleModels.get(0);

        UndoCommand undoCommand = new UndoCommand(100);
        assertCommandSuccess(undoCommand, model,
                String.format(UndoCommand.MESSAGE_SUCCESS_MULTIPLE, 5, 100), expectedModel);
    }
}
