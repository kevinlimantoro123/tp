package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.modifications.Modification;
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
     * @param modification The Modification done to the address book.
     */
    public void commit(Modification modification) {
        assert(modification != null);
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (addressBookStateHistory.size() - 1 > currentStatePointer) {
            addressBookStateHistory.subList(currentStatePointer + 1, addressBookStateHistory.size()).clear();
        }
        addressBookStateHistory.add(new AddressBookStateNode(new AddressBook(this.addressBook), modification));
        currentStatePointer++;
    }

    /**
     * Restores the state of the working address book to before the last command that modifies it.
     * @returns The Modification that was undone.
     */
    public Modification undo() throws CannotUndoException {
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (currentStatePointer == 0) {
            throw new CannotUndoException();
        }

        Modification undoneMod = addressBookStateHistory.get(currentStatePointer).getModification();

        currentStatePointer--;
        AddressBookStateNode node = addressBookStateHistory.get(currentStatePointer);
        this.addressBook.resetData(node.getState());

        assert(node != null);
        assert(node.getState() != null);
        return undoneMod;
    }

    /**
     * Restores the state of the working address book to before the last command that modifies it.
     * @returns The Modification that was undone.
     */
    public Modification redo() throws CannotRedoException {
        assert(currentStatePointer >= 0 && currentStatePointer < addressBookStateHistory.size());
        if (currentStatePointer == addressBookStateHistory.size() - 1) {
            throw new CannotRedoException();
        }

        currentStatePointer++;
        AddressBookStateNode node = addressBookStateHistory.get(currentStatePointer);
        this.addressBook.resetData(node.getState());

        assert(node != null);
        assert(node.getState() != null);
        assert(node.getModification() != null);
        return node.getModification();
    }
    
    /**
     * Undoes the last n (or all, if there are less than n) modifications to the address book.
     * @param numberOfTimes The value of n.
     * @returns The list of Modifications that were undone.
     */
    List<Modification> undoMultiple(int numberOfTimes) {
        List<Modification> undoneMods = new ArrayList<Modification>();
        for (int i = 0; i < numberOfTimes; i++) {
            try {
                undoneMods.add(this.undo());
            } catch (CannotUndoException e) {
                // Ignore
            }
        }
        return undoneMods;
    }

    /**
     * Restores the last n (or all, if there are less than n) undone modifications to the address book.
     * @param numberOfTimes The value of n.
     * @returns The list of Modifications that were undone.
     */
    List<Modification> redoMultiple(int numberOfTimes) {
        List<Modification> restoredMods = new ArrayList<Modification>();
        for (int i = 0; i < numberOfTimes; i++) {
            try {
                restoredMods.add(this.redo());
            } catch (CannotRedoException e) {
                // Ignore
            }
        }
        return restoredMods;
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
