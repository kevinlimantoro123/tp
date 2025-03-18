package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    // deletion by index
    @Test
    public void parse_validIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, " 1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, " a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_twoAttributes_throwsParseException() {
        assertParseFailure(parser,
                " " + PREFIX_PHONE + "12345678 " + PREFIX_EMAIL + "dummy@example.com",
                String.format(DeleteCommand.TOO_MANY_ATTRIBUTES_SPECIFIED, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_threeAttributes_throwsParseException() {
        assertParseFailure(parser,
                " " + PREFIX_PHONE + "12345678 "
                        + PREFIX_EMAIL + "dummy@example.com "
                        + PREFIX_NAME + "alex yeoh",
                String.format(DeleteCommand.TOO_MANY_ATTRIBUTES_SPECIFIED, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameAttribute_throwsParseException() {
        assertParseFailure(parser,
                " " + PREFIX_NAME + "alex yeoh",
                String.format(DeleteCommand.NOT_UNIQUE_ATTRIBUTE_DETECTED, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_addressAttribute_throwsParseException() {
        assertParseFailure(parser,
                " " + PREFIX_ADDRESS + "420 Craft Avenue, Gotham 123746",
                String.format(DeleteCommand.NOT_UNIQUE_ATTRIBUTE_DETECTED, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tagAttribute_throwsParseException() {
        assertParseFailure(parser,
                " " + PREFIX_TAG + "colleague",
                String.format(DeleteCommand.NOT_UNIQUE_ATTRIBUTE_DETECTED, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validPhone_success() {
        String phone = "12345678";
        assertParseSuccess(parser,
                " " + PREFIX_PHONE + phone,
                new DeleteCommand(new Phone(phone)));
    }

    @Test
    public void parse_validEmail_success() {
        String email = "ilovecraftconnect@example.com";
        assertParseSuccess(parser,
                " " + PREFIX_EMAIL + email,
                new DeleteCommand(new Email(email)));
    }
}
