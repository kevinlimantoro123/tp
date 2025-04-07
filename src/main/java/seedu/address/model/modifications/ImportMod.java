package seedu.address.model.modifications;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Record of importing data using import command.
 */
public class ImportMod extends Modification {
    public static final String MOD_TYPE = "Import data from %s (overwrite: %b)";

    private final String location;
    private final boolean isOverwrite;

    /**
     * Constructs an ImportMod.
     */
    public ImportMod(String location, boolean isOverwrite) {
        super();
        this.location = location;
        this.isOverwrite = isOverwrite;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("location", this.location)
                .add("isOverwrite", this.isOverwrite)
                .toString();
    }

    @Override
    public String getUserDescription() {
        return String.format(MOD_TYPE, this.location, this.isOverwrite);
    }

    public String getLocation() {
        return this.location;
    }

    public boolean isOverwrite() {
        return this.isOverwrite;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ImportMod)) {
            return false;
        }

        ImportMod otherImportMod = (ImportMod) other;

        return ((Modification) this).equals((Modification) otherImportMod)
                && this.location.equals(otherImportMod.location)
                && this.isOverwrite == otherImportMod.isOverwrite;
    }
}
