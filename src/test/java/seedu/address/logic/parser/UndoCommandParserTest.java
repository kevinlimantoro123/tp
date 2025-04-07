package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoCommand;

public class UndoCommandParserTest {
    private UndoCommandParser parser = new UndoCommandParser();

    @Test
    public void parse_withNum_success() {
        String userInput = "123";
        UndoCommand expectedCommand = new UndoCommand(123);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_withoutNum_success() {
        String userInput = "";
        UndoCommand expectedCommand = new UndoCommand();
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_zeroNum_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE);

        String userInput = "0";
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_negativeNum_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE);

        String userInput = "-1";
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_notANum_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE);

        String userInput = "asd";
        assertParseFailure(parser, userInput, expectedMessage);
    }
}
