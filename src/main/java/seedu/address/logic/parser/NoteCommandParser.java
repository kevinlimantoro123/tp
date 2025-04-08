package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;


/**
 * Parses input arguments and creates a new {@code NoteCommand} object
 */
public class NoteCommandParser implements Parser<NoteCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code NoteCommand}
     * and returns a {@code NoteCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public NoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTE);
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE), pe);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTE);
        if (!argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
        }

        String noteContent = argMultimap.getValue(PREFIX_NOTE).get();
        Note note = ParserUtil.parseNote(noteContent);

        return new NoteCommand(index, note);

    }
}
