package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;

public class ImportCommandParserTest {
    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " ", expectedMessage);
        assertParseFailure(parser, "    ", expectedMessage);
    }

    @Test
    public void parse_nonEmptyArgs_success() {
        String validArgs1 = "C:/Users/DummyUser/data.json";
        String validArgs2 = "../../data.json";
        String validArgs3 = "C:/Users/FalseUser/data.json randomWord";
        String validArgs4 = "the parser faithfully parse the argument, the command checks the path validity";

        assertParseSuccess(parser, validArgs1, new ImportCommand(validArgs1));
        assertParseSuccess(parser, validArgs2, new ImportCommand(validArgs2));
        assertParseSuccess(parser, validArgs3, new ImportCommand(validArgs3));
        assertParseSuccess(parser, validArgs4, new ImportCommand(validArgs4));
    }
}
