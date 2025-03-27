package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.model.person.exceptions.CannotRedoException;
import seedu.address.model.person.exceptions.CannotUndoException;

/**
 * Manager for the state history of the address book, supporting undo and redo operations.
 */
public class AddressBookStateManager {
    private AddressBook addressBook;
    private final List<AddressBookStateNode> addressBookStateHistory;
    private int currentStatePointer;

    /**
     * Initializes the state manager.
     * @param addressBook The starting state of the address book.
     */
    public AddressBookStateManager(AddressBook addressBook) {
        this.addressBook = addressBook;
        this.addressBookStateHistory = new ArrayList<AddressBookStateNode>();
        this.addressBookStateHistory.add(new AddressBookStateNode(new AddressBook(this.addressBook), null));
        this.currentStatePointer = 0;
    }

    /**
     * Signals to the state manager that a command that modifies the address book has finished
     * and the state of the working address book should be stored.
     * @param command The Command that just modified the address book.
     */
    public void commit(Command command) {
        assert(command != null);
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (addressBookStateHistory.size() - 1 > currentStatePointer) {
            addressBookStateHistory.subList(currentStatePointer + 1, addressBookStateHistory.size()).clear();
        }
        addressBookStateHistory.add(new AddressBookStateNode(new AddressBook(this.addressBook), command));
        currentStatePointer++;
    }

    /**
     * Restores the state of the working address book to before the last command that modifies it.
     * @returns The Command that was undone.
     */
    public Command undo() throws CannotUndoException {
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (currentStatePointer == 0) {
            throw new CannotUndoException();
        }

        Command undoneCommand = addressBookStateHistory.get(currentStatePointer).getCommand();

        currentStatePointer--;
        AddressBookStateNode node = addressBookStateHistory.get(currentStatePointer);
        this.addressBook = new AddressBook(node.getState());

        assert(node != null);
        assert(node.getState() != null);
        return undoneCommand;
    }

    /**
     * Restores the state of the working address book to before the last command that modifies it.
     * @returns The Command that was undone.
     */
    public Command redo() throws CannotRedoException {
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (currentStatePointer == addressBookStateHistory.size() - 1) {
            throw new CannotRedoException();
        }

        currentStatePointer++;
        AddressBookStateNode node = addressBookStateHistory.get(currentStatePointer);
        this.addressBook = new AddressBook(node.getState());

        assert(node != null);
        assert(node.getState() != null);
        assert(node.getCommand() != null);
        return node.getCommand();
    }

    /**
     * Returns the current state of the working address book.
     */
    public AddressBook getCurrentAddressBook() {
        return this.addressBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBookStateManager)) {
            return false;
        }

        AddressBookStateManager otherModelManager = (AddressBookStateManager) other;
        return addressBook.equals(otherModelManager.addressBook);
    }
}
