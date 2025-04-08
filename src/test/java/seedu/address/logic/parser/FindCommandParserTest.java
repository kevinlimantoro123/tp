package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.predicate.EmailIsKeywordPredicate;
import seedu.address.model.predicate.PhoneIsKeywordPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser,
                "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argMoreThanOneAttribute_throwsParseException() {
        assertParseFailure(parser,
                String.format(" %s91237483 %silovecraftconnect@gmail.com", PREFIX_PHONE, PREFIX_EMAIL),
                String.format(FindCommand.MESSAGE_TOO_MANY_IDENTIFIERS_SPECIFIED, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                String.format(" %sneil deGrease Tyson %sastrophysicist@nasa.gov %steacher",
                        PREFIX_NAME, PREFIX_EMAIL, PREFIX_TAG),
                String.format(FindCommand.MESSAGE_TOO_MANY_IDENTIFIERS_SPECIFIED, FindCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                String.format(" %scolleagues %s59 Dummy Street, Gotham %s83582957 %sfriend",
                        PREFIX_TAG, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_TAG),
                String.format(FindCommand.MESSAGE_TOO_MANY_IDENTIFIERS_SPECIFIED, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsName_throwsParseExceptionForNonUniqueAttribute() {
        assertParseFailure(parser,
                " " + PREFIX_NAME + "alex yeoh",
                String.format(FindCommand.MESSAGE_NOT_UNIQUE_ATTRIBUTE_DETECTED, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsAddress_throwsParseExceptionForNonUniqueAttribute() {
        assertParseFailure(parser,
                " " + PREFIX_ADDRESS + "420 Craft Avenue, Singapore 572847",
                String.format(FindCommand.MESSAGE_NOT_UNIQUE_ATTRIBUTE_DETECTED, FindCommand.MESSAGE_USAGE));
    }

    // integration test
    @Test
    public void parse_emailArg_returnsFindCommand() {
        // no whitespace
        FindCommand expectedFindCommand =
                new FindCommand(new EmailIsKeywordPredicate("ilovecraftconnect@gmail.com"));
        assertParseSuccess(parser, " " + PREFIX_EMAIL + "ilovecraftconnect@gmail.com", expectedFindCommand);

        // trailing whitespace
        expectedFindCommand =
                new FindCommand(new EmailIsKeywordPredicate("ilovecraftconnect@gmail.com"));
        assertParseSuccess(parser, "      " + PREFIX_EMAIL + "ilovecraftconnect@gmail.com", expectedFindCommand);
    }

    @Test
    public void parse_phoneArg_returnsFindCommand() {
        // no whitespace
        FindCommand expectedFindCommand =
                new FindCommand(new PhoneIsKeywordPredicate("93424353"));
        assertParseSuccess(parser, " " + PREFIX_PHONE + "93424353", expectedFindCommand);


        // trailing whitespace
        expectedFindCommand =
                new FindCommand(new PhoneIsKeywordPredicate("93424353"));
        assertParseSuccess(parser, "   " + PREFIX_PHONE + "93424353", expectedFindCommand);
    }
}
