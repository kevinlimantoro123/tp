package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.Storage;

/**
 * An abstract command that has to deal with files and storages.
 */
public abstract class FileBasedCommand extends Command {
    protected static Storage storage;
    protected final String filePath;

    public FileBasedCommand(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Binds the storage manager object to all file-based commands.
     * This bind call is invoked after the storage manager has been instantiated.
     * @param storage The storage to bind.
     */
    public static void bindStorage(Storage storage) {
        FileBasedCommand.storage = storage;
    }


    @Override
    public abstract CommandResult execute(Model model) throws CommandException;
}
