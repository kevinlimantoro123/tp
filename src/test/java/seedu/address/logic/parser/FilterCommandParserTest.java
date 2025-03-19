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

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyFilterArg_throwsParseException() {
        assertParseFailure(parser, FilterCommand.COMMAND_WORD + " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argMoreThanOneAttribute_throwsParseException() {
        assertParseFailure(parser, FilterCommand.COMMAND_WORD +
                String.format(" %sBlk 123 %sAlex", PREFIX_ADDRESS, PREFIX_NAME),
                String.format(FilterCommand.TOO_MANY_IDENTIFIERS_SPECIFIED, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsEmail_throwsParseExceptionForUniqueAttribute() {
        assertParseFailure(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_EMAIL + "this@gmail.com",
                String.format(FilterCommand.UNIQUE_ATTRIBUTE_DETECTED, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsPhone_throwsParseExceptionForUniqueAttribute() {
        assertParseFailure(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_PHONE + "99898998",
                String.format(FilterCommand.UNIQUE_ATTRIBUTE_DETECTED, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nameArg_returnsFilterCommand() {
        assertParseSuccess(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_NAME + "Alex",
                new FilterCommand(new NameContainsKeywordsPredicate("Alex")));
    }

    @Test
    public void parse_addressArg_returnsFilterCommand() {
        assertParseSuccess(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_ADDRESS + "Blk 123",
                new FilterCommand(new AddressContainsKeywordsPredicate("Blk 123")));
    }

    @Test
    public void parse_oneTagArg_returnsFilterCommand() {
        assertParseSuccess(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_TAG + "friends",
                new FilterCommand(new TagContainsKeywordsPredicate("friends")));
    }

    @Test
    public void parse_multipleTagsArg_returnsFilterCommand() {
        assertParseSuccess(parser, FilterCommand.COMMAND_WORD + " " + PREFIX_TAG + "friends family",
                new FilterCommand(new TagContainsKeywordsPredicate("friends family")));
    }
}
