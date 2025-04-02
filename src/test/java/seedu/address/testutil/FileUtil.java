package seedu.address.testutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A utility class that contains methods to populate file data used for address book testing
 * relevant to files.
 */
public class FileUtil {
    /**
     * Populates a given file with valid JSON address book data.
     * This file is to be used to initialise <b>default</b> data for a test model.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateDefaultJsonFile(File f) throws IOException {
        String defaultJsonContent = """
{
  "persons" : [ {
    "name" : "Alex Yeoh",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "customer" ],
    "note" : "Note 1"
  }, {
    "name" : "Bernice Yu",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "bulkbuyer", "customer" ],
    "note" : ""
  }, {
    "name" : "Charlotte Oliveiro",
    "phone" : "93210283",
    "email" : "charlotte@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "boothRentalCraftFes" ],
    "note" : ""
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(defaultJsonContent);
        }
    }

    /**
     * Populates a given file with JSON data that does not conform with address book schema.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateInvalidJsonFile(File f) throws IOException {
        String invalidJsonContent = """
{
  "something_wrong" : [ {
    "not_name" : "Dickson Tan",
    "not_phone" : "92343827",
    "not_email" : "dicksontan@example.com",
    "not_address" : "Blk 30 Geylang Street 29, #06-40",
    "not_tags" : [ "boothPartner" ],
    "not_note" : ""
  } ]
}
                """;
        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(invalidJsonContent);
        }
    }

    /**
     * Populates a given non-JSON file with data that is not JSON.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateNotJsonFile(File f) throws IOException {
        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write("NAHHHHH!");
        }
    }

    /**
     * Writes an empty string into the given JSON file.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateEmptyJsonFile(File f) throws IOException {
        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write("");
        }
    }

    /**
     * Populates a given file with valid JSON user preference data.
     * This file is to be used to initialise <b>default</b> user preference for a test model.
     *
     * @param userPref the path to the user preference file
     * @param dataFile the path to the data file to bind to this user preference file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateDefaultUserPrefsFile(File userPref, File dataFile) throws IOException {
        String defaultUserPrefsContent = String.format("""
{
  "guiSettings" : {
    "windowWidth" : 719.3333129882812,
    "windowHeight" : 668.6666870117188,
    "windowCoordinates" : {
      "x" : 424,
      "y" : 171
    }
  },
  "addressBookFilePath" : "%s"
}
                """, dataFile.getAbsolutePath().replace("\\", "\\\\"));

        try (FileWriter fileWriter = new FileWriter(userPref)) {
            fileWriter.write(defaultUserPrefsContent);
        }
    }

    /**
     * Populates a given file with valid JSON address book data that are not duplicates of the default file.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateValidNoDupesJsonFile(File f) throws IOException {
        String noDupeJsonContent = """
{
  "persons" : [ {
    "name" : "David Li",
    "phone" : "91031282",
    "email" : "lidavid@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "tags" : [ "keychainManufacturer" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(noDupeJsonContent);
        }
    }

    /**
     * Populates a given file with valid JSON address book data that contains 1 phone duplicate of the default file.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateValidDupePhoneJsonFile(File f) throws IOException {
        String dupePhoneJsonContent = """
{
  "persons" : [ {
    "name" : "David Li",
    "phone" : "87438807",
    "email" : "lidavid@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "tags" : [ "keychainManufacturer" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(dupePhoneJsonContent);
        }
    }

    /**
     * Populates a given file with valid JSON address book data that contains 1 email duplicate of the default file.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateValidDupeEmailJsonFile(File f) throws IOException {
        String dupeEmailJsonContent = """
{
  "persons" : [ {
    "name" : "David Li",
    "phone" : "91031282",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "tags" : [ "keychainManufacturer" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(dupeEmailJsonContent);
        }
    }

    /**
     * Populates a given file with valid JSON address book data after appending valid-no-dupe data to default data.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateExpectedAfterAppendNoDupe(File f) throws IOException {
        String expectedJsonContentAfterAppendNoDupe = """
{
  "persons" : [ {
    "name" : "Alex Yeoh",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "customer" ],
    "note" : "Note 1"
  }, {
    "name" : "Bernice Yu",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "bulkbuyer", "customer" ],
    "note" : ""
  }, {
    "name" : "Charlotte Oliveiro",
    "phone" : "93210283",
    "email" : "charlotte@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "boothRentalCraftFes" ],
    "note" : ""
  }, {
    "name" : "David Li",
    "phone" : "91031282",
    "email" : "lidavid@example.com",
    "address" : "Blk 436 Serangoon Gardens Street 26, #16-43",
    "tags" : [ "keychainManufacturer" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(expectedJsonContentAfterAppendNoDupe);
        }
    }

    /**
     * Populates a given file with valid JSON address book data after appending valid-with-1-phone-dupe
     * data to default data.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateExpectedAfterAppendDupePhone(File f) throws IOException {
        String expectedJsonContentAfterAppendDupePhone = """
{
  "persons" : [ {
    "name" : "Alex Yeoh",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "customer" ],
    "note" : "Note 1"
  }, {
    "name" : "Bernice Yu",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "bulkbuyer", "customer" ],
    "note" : ""
  }, {
    "name" : "Charlotte Oliveiro",
    "phone" : "93210283",
    "email" : "charlotte@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "boothRentalCraftFes" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(expectedJsonContentAfterAppendDupePhone);
        }
    }

    /**
     * Populates a given file with valid JSON address book data after appending valid-with-1-email-dupe
     * data to default data.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateExpectedAfterAppendDupeEmail(File f) throws IOException {
        String expectedJsonContentAfterAppendDupeEmail = """
{
  "persons" : [ {
    "name" : "Alex Yeoh",
    "phone" : "87438807",
    "email" : "alexyeoh@example.com",
    "address" : "Blk 30 Geylang Street 29, #06-40",
    "tags" : [ "customer" ],
    "note" : "Note 1"
  }, {
    "name" : "Bernice Yu",
    "phone" : "99272758",
    "email" : "berniceyu@example.com",
    "address" : "Blk 30 Lorong 3 Serangoon Gardens, #07-18",
    "tags" : [ "bulkbuyer", "customer" ],
    "note" : ""
  }, {
    "name" : "Charlotte Oliveiro",
    "phone" : "93210283",
    "email" : "charlotte@example.com",
    "address" : "Blk 11 Ang Mo Kio Street 74, #11-04",
    "tags" : [ "boothRentalCraftFes" ],
    "note" : ""
  }, {
    "name" : "Irfan Ibrahim",
    "phone" : "92492021",
    "email" : "irfan@example.com",
    "address" : "Blk 47 Tampines Street 20, #17-35",
    "tags" : [ "stickerMaterialSupplier" ],
    "note" : "Frequent buyer who always comes every week"
  }, {
    "name" : "Roy Balakrishnan",
    "phone" : "92624417",
    "email" : "royb@example.com",
    "address" : "Blk 45 Aljunied Street 85, #11-31",
    "tags" : [ "boothRentalConnectiCon" ],
    "note" : "Consistent supplier with good quality materials"
  } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(expectedJsonContentAfterAppendDupeEmail);
        }
    }

    /**
     * Populates a given file with duplicated entries within itself.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateDuplicatedPersonFile(File f) throws IOException {
        String duplicatedPersonsContent = """
{
   "persons": [ {
     "name": "Alice Pauline",
     "phone": "94351253",
     "email": "alice@example.com",
     "address": "123, Jurong West Ave 6, #08-111",
     "tags": [ "friends" ],
     "note": ""
   }, {
     "name": "Alice Pauline",
     "phone": "94351253",
     "email": "pauline@example.com",
     "address": "4th street",
     "note": ""
   } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(duplicatedPersonsContent);
        }
    }

    /**
     * Populates a given file with valid JSON address book data after appending valid-with-1-email-dupe
     * data to default data.
     *
     * @param f the path to the file
     * @throws IOException if there are I/O exceptions
     */
    public static void populateExpectedAfterOverwriteDupe(File f) throws IOException {
        String expectedJsonContentAfterOverwriteDupe = """
{
   "persons": [ {
     "name": "Alice Pauline",
     "phone": "94351253",
     "email": "alice@example.com",
     "address": "123, Jurong West Ave 6, #08-111",
     "tags": [ "friends" ],
     "note": ""
   } ]
}
                """;

        try (FileWriter fileWriter = new FileWriter(f)) {
            fileWriter.write(expectedJsonContentAfterOverwriteDupe);
        }
    }
}
