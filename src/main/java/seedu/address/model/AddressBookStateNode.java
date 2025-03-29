package seedu.address.model;

import seedu.address.model.modifications.Modification;

/**
 * Representation a historical state of the model's address book.
 */
public class AddressBookStateNode {
    private ReadOnlyAddressBook state;
    private Modification modification;

    /**
     * Constructs an AddressBookStateNode.
     */
    public AddressBookStateNode(ReadOnlyAddressBook state, Modification modification) {
        this.state = state;
        this.modification = modification;
    }

    /**
     * Returns the state of the address book at this node.
     */
    public ReadOnlyAddressBook getState() {
        return this.state;
    }

    /**
     * Returns the modification that resulted in this state of the address book.
     */
    public Modification getModification() {
        return this.modification;
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
                && ((modification == null && otherModelManager.modification == null)
                || (modification != null && otherModelManager.modification != null
                && modification.equals(otherModelManager.modification)));
    }
}
