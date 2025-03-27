package seedu.address.model;

import seedu.address.logic.commands.Command;

/**
 * Representation a historical state of the model's address book.
 */
public class AddressBookStateNode {
    private ReadOnlyAddressBook state;
    private Command command;

    /**
     * Constructs an AddressBookStateNode.
     */
    public AddressBookStateNode(ReadOnlyAddressBook state, Command command) {
        this.state = state;
        this.command = command;
    }

    /**
     * Returns the state of the address book at this node.
     */
    public ReadOnlyAddressBook getState() {
        return this.state;
    }

    /**
     * Returns the command that resulted in this state of the address book.
     */
    public Command getCommand() {
        return this.command;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBookStateNode)) {
            return false;
        }

        AddressBookStateNode otherModelManager = (AddressBookStateNode) other;
        return state.equals(otherModelManager.state)
                && ((command == null && otherModelManager.command == null)
                || (command != null && otherModelManager.command != null && command.equals(otherModelManager.command)));
    }
}
