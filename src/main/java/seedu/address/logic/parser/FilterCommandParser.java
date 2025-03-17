package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FilterCommandParser implements Parser<FilterCommand> {

    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (argMultimap.getNumberOfPrefixes() > 2) {
            throw new ParseException(
                    String.format(FilterCommand.TOO_MANY_IDENTIFIERS_SPECIFIED, FilterCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()
                || argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            throw new ParseException(
                    String.format(FilterCommand.UNIQUE_ATTRIBUTE_DETECTED, FilterCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            return new FilterCommand(new TagContainsKeywordsPredicate(argMultimap.getValue(PREFIX_TAG).get()));
        }

        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            return new FilterCommand(new AddressContainsKeywordsPredicate(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            return new FilterCommand(new NameContainsKeywordsPredicate(argMultimap.getValue(PREFIX_NAME).get()));
        }

        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
