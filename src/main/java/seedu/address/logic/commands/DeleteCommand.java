package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.modifications.DeleteMod;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmailIsKeywordPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhoneIsKeywordPredicate;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by his/her unique identifier.\n"
            + "Currently, unique identifiers include email, phone number and index.\n"
            + "- Deletion by index is based on the currently filtered model. "
            + "For instance, suppose after a call to 'find' or 'filter', person A is displayed with index 1. "
            + "Deletion by index 1 will remove A.\n"
            + "- Deletion by email or phone number is regardless of what filter is applied and what is displayed. "
            + "For example, suppose after a call to 'find' or 'filter', person A is not displayed. "
            + "Deletion by person A's email or phone number is still allowed.\n"
            + "NOTE: Only supply exactly ONE attribute for deletion.\n"
            + "Parameters: [INDEX] (must be a positive integer) ["
            + PREFIX_PHONE + "PHONE_NUMBER] ["
            + PREFIX_EMAIL + "EMAIL]\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "or: " + COMMAND_WORD + " " + PREFIX_PHONE + "98761234\n"
            + "or: " + COMMAND_WORD + " " + PREFIX_EMAIL + "ilovecraftconnect@gmail.com";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_TOO_MANY_ATTRIBUTES_SPECIFIED = "Too many attributes specified!\n%1$s";
    public static final String MESSAGE_NOT_UNIQUE_ATTRIBUTE_DETECTED = "A non-unique attribute is detected!\n%1s";
    public static final String MESSAGE_NO_PERSON_WITH_MATCHING_EMAIL = "No person with matching email address.";
    public static final String MESSAGE_NO_PERSON_WITH_MATCHING_PHONE = "No person with matching phone number.";

    private enum DeletionType {
        BY_INDEX, BY_EMAIL, BY_PHONE
    }

    private final Object target;
    private final DeletionType deletionAttribute;

    /**
     * Initialises a DeleteCommand with a target index.
     * @param targetIndex The index of the person to delete.
     */
    public DeleteCommand(Index targetIndex) {
        this.target = targetIndex;
        this.deletionAttribute = DeletionType.BY_INDEX;
    }

    /**
     * Initialises a DeleteCommand with a target email address.
     * @param email The email address of the person to delete.
     */
    public DeleteCommand(Email email) {
        this.target = email;
        this.deletionAttribute = DeletionType.BY_EMAIL;
    }

    /**
     * Initialises a DeleteCommand with a target phone number.
     * @param phone The phone number of the person to delete.
     */
    public DeleteCommand(Phone phone) {
        this.target = phone;
        this.deletionAttribute = DeletionType.BY_PHONE;
    }

    /**
     * Returns the first person whose information matches the given predicate.<br>
     * Ideally, this is used for searching a unique person, so the supplied predicate should
     * be related to a person's unique attribute, such as phone number and email address.
     *
     * @param people The list of Person
     * @param predicate The predicate that acts on a Person object
     * @return The first person whose information matches the predicate.
     */
    private Person find(List<Person> people, Predicate<Person> predicate) {
        for (Person person : people) {
            if (predicate.test(person)) {
                return person;
            }
        }
        return null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        switch (this.deletionAttribute) {
        case BY_INDEX -> {
            Index targetIndex = (Index) target;

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
            model.deletePerson(personToDelete);
            model.commitAddressBook(new DeleteMod(personToDelete));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }
        case BY_EMAIL -> {
            // deletion by email works even if the filtered list does not have the matching email
            // because each email is uniquely bounded to one person
            // thus, restore the unfiltered address book before checking email existence
            model.updateFilteredPersonList(person -> true);
            Email targetEmail = (Email) target;
            Person personToDelete = find(lastShownList, new EmailIsKeywordPredicate(targetEmail.value));

            if (personToDelete == null) {
                throw new CommandException(DeleteCommand.MESSAGE_NO_PERSON_WITH_MATCHING_EMAIL);
            }

            model.deletePerson(personToDelete);
            model.commitAddressBook(new DeleteMod(personToDelete));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }
        case BY_PHONE -> {
            // deletion by phone number works even if the filtered list does not have the matching phone number
            // because each phone number is uniquely bounded to one person
            // thus, restore the unfiltered address book before checking phone number existence
            model.updateFilteredPersonList(person -> true);
            Phone targetPhone = (Phone) target;
            Person personToDelete = find(lastShownList, new PhoneIsKeywordPredicate(targetPhone.value));

            if (personToDelete == null) {
                throw new CommandException(DeleteCommand.MESSAGE_NO_PERSON_WITH_MATCHING_PHONE);
            }

            model.deletePerson(personToDelete);
            model.commitAddressBook(new DeleteMod(personToDelete));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }
        default -> {
            // nothing happens here, all DeletionType cases have been handled
        }
        }

        throw new CommandException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return target.equals(otherDeleteCommand.target);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("target", target)
                .toString();
    }
}
