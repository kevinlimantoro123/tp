package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;

public class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser,
                "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyFindArg_throwsParseException() {
        assertParseFailure(parser,
                "find ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argMoreThanOneAttribute_throwsParseException() {
        assertParseFailure(parser,
                String.format("find %sBlk 123 %sAlex", PREFIX_ADDRESS, PREFIX_NAME),
                String.format(FilterCommand.TOO_MANY_IDENTIFIERS_SPECIFIED, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsEmail_throwsParseExceptionForUniqueAttribute() {
        assertParseFailure(parser,
                "filter " + PREFIX_EMAIL + "this@gmail.com",
                String.format(FilterCommand.UNIQUE_ATTRIBUTE_DETECTED, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_argContainsPhone_throwsParseExceptionForUniqueAttribute() {
        assertParseFailure(parser,
                "filter " + PREFIX_PHONE + "99898998",
                String.format(FilterCommand.UNIQUE_ATTRIBUTE_DETECTED, FilterCommand.MESSAGE_USAGE));
    }
}
