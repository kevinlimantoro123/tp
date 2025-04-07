package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Filter and display contacts whose non-unique identifier is exactly the same as the specified keyword.<br>
 * Currently, the non-unique identifiers supported are:
 * <ul>
 *     <li>Name</li>
 *     <li>Address</li>
 *     <li>Tags</li>
 * </ul>
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters all persons based on their "
            + "non-unique identifiers and displays the person's full information.\n"
            + "Non-unique identifiers include name, address and tags.\n"
            + "Parameters: [" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_TAG + "TAGS]\n"
            + "Example: " + COMMAND_WORD + " t/friends";

    public static final String TOO_MANY_IDENTIFIERS_SPECIFIED = "Too many attributes specified!\n%1$s";
    public static final String UNIQUE_ATTRIBUTE_DETECTED = "A unique attribute detected!\n"
            + "For unique attributes, use 'find'.\n%1$s";

    private final Predicate<Person> predicate;

    public FilterCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
