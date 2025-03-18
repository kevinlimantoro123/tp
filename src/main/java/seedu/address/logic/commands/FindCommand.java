package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;

import seedu.address.model.person.Person;

/**
 * Finds and displays the person whose unique identifier is exactly the same as the specified keyword.<br>
 * Currently, the unique identifiers supported are:
 * <ul>
 *     <li>Email</li>
 *     <li>Phone number</li>
 * </ul>
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds a person based on his/her unique identifier "
            + "and displays the person's full information.\n"
            + "Unique identifiers currently contain email address and phone number.\n"
            + "Because this looks for the exact person, the exact phone number or email must be specified.\n"
            + "NOTE: Only supply exactly ONE attribute for finding.\n"
            + "Parameters: [" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE + "91234578\n"
            + "or: " + COMMAND_WORD + " " + PREFIX_EMAIL + "ilovecraftconnect@gmail.com";

    public static final String TOO_MANY_IDENTIFIERS_SPECIFIED = "Too many attributes specified!\n%1$s";
    public static final String NOT_UNIQUE_ATTRIBUTE_DETECTED = "A non-unique attribute detected!\n"
            + "For non-unique attributes, use 'filter'.\n%1$s";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
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
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
