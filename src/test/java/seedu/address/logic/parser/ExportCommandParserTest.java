package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {
    private final ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, " ", expectedMessage);
        assertParseFailure(parser, "    ", expectedMessage);
    }

    @Test
    public void parse_onlyCreatesDirFlag_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);

        assertParseFailure(parser, ExportCommandParser.CREATES_DIRECTORY_FLAG, expectedMessage);
        assertParseFailure(parser, "  " + ExportCommandParser.CREATES_DIRECTORY_FLAG + "  ", expectedMessage);
        assertParseFailure(parser, "  " + ExportCommandParser.CREATES_DIRECTORY_FLAG, expectedMessage);
        assertParseFailure(parser, ExportCommandParser.CREATES_DIRECTORY_FLAG + "  ", expectedMessage);
    }

    @Test
    public void parse_tooManyArguments_throwsParseException() {
        String expectedMessage = ExportCommand.MESSAGE_TOO_MANY_ARGUMENTS;
        String argument = "a/random/ " + ExportCommandParser.CREATES_DIRECTORY_FLAG + " /folder";

        assertParseFailure(parser, argument, expectedMessage);
    }

    @Test
    public void parse_validFolderPath_success() {
        // no create directory flag
        String validArgNoFlag1 = "C:/Users/DummyUser";
        String validArgNoFlag2 = " C:/Users/DummyUser  ";
        String validArgNoFlag3 = "   hello world";
        String validArgNoFlag4 = "a/random/" + ExportCommandParser.CREATES_DIRECTORY_FLAG + "/folder";

        ExportCommand exportCommandv1 = new ExportCommand("C:/Users/DummyUser", false);
        ExportCommand exportCommandv2 = new ExportCommand("C:/Users/DummyUser", false);
        ExportCommand exportCommandv3 = new ExportCommand("hello world", false);
        ExportCommand exportCommandv4 = new ExportCommand(
                "a/random/" + ExportCommandParser.CREATES_DIRECTORY_FLAG + "/folder", false);

        assertParseSuccess(parser, validArgNoFlag1, exportCommandv1);
        assertParseSuccess(parser, validArgNoFlag2, exportCommandv2);
        assertParseSuccess(parser, validArgNoFlag3, exportCommandv3);
        assertParseSuccess(parser, validArgNoFlag4, exportCommandv4);

        // with create directory flag
        String validArgFlag1 = "C:/Users/DummyUser " + ExportCommandParser.CREATES_DIRECTORY_FLAG;
        String validArgFlag2 = "  " + ExportCommandParser.CREATES_DIRECTORY_FLAG + " C:/Users/DummyUser  ";
        String validArgFlag3 = ExportCommandParser.CREATES_DIRECTORY_FLAG + "   hello world";
        String validArgFlag4 = "hello world " + ExportCommandParser.CREATES_DIRECTORY_FLAG + "  ";

        ExportCommand exportCommandv1f = new ExportCommand("C:/Users/DummyUser", true);
        ExportCommand exportCommandv2f = new ExportCommand("C:/Users/DummyUser", true);
        ExportCommand exportCommandv3f = new ExportCommand("hello world", true);
        ExportCommand exportCommandv4f = new ExportCommand("hello world", true);

        assertParseSuccess(parser, validArgFlag1, exportCommandv1f);
        assertParseSuccess(parser, validArgFlag2, exportCommandv2f);
        assertParseSuccess(parser, validArgFlag3, exportCommandv3f);
        assertParseSuccess(parser, validArgFlag4, exportCommandv4f);
    }
}
