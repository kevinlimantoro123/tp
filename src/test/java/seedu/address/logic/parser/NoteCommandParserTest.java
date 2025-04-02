package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.person.Note;

public class NoteCommandParserTest {
    private NoteCommandParser parser = new NoteCommandParser();
    private final String note = "Sample note";

    @Test
    public void parse_allFieldsSpecified_success() {
        // Note content present
        String userInputWithNote = "1 " + PREFIX_NOTE + note;
        NoteCommand expectedCommandWithNote = new NoteCommand(INDEX_FIRST_PERSON, new Note(note));
        assertParseSuccess(parser, userInputWithNote, expectedCommandWithNote);

        // Note content not present
        String userInputWithoutNote = "1 " + PREFIX_NOTE;
        NoteCommand expectedCommandWithoutNote = new NoteCommand(INDEX_FIRST_PERSON, new Note(""));
        assertParseSuccess(parser, userInputWithoutNote, expectedCommandWithoutNote);
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);

        // non-empty preamble
        assertParseFailure(parser, "random words" + INDEX_FIRST_PERSON + PREFIX_NOTE + note, expectedMessage);

        // no params
        assertParseFailure(parser, "", expectedMessage);

        // duplicate prefixes
        assertParseFailure(parser, "1 2" + PREFIX_NOTE + note, expectedMessage);

        // no index
        assertParseFailure(parser, PREFIX_NOTE + note, expectedMessage);

        // no note
        assertParseFailure(parser, "1 ", expectedMessage);
    }
}
