---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# CraftConnect User Guide

CraftConnect is a simple desktop app that makes managing your contacts **faster and easier**. It combines the quick typing of a command-line tool with the familiar look of a regular app. Whether you’re keeping track of suppliers or customers, CraftConnect helps you stay organized and get things done in less time.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `filter n/John Doe` : Filters all contacts with the name `John Doe`.
   
   * `find p/98765432` : Finds the contact with the phone number `98765432`.
   
   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/CATEGORY]…​`

* Can only add a person with a unique phone number and email address.

<box type="tip" seamless>

**Tip:** A person can have any number of categories (including 0)
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/supplier`

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* Can only edit the phone number and email if they are unique (No duplicates in the contact book). 
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by unique identifier: `find`

Finds persons by searching for a unique attribute

Format: `find UNIQUE_IDENTIFIER` 

<box type="tip" seamless>

**Tip:** Only 1 parameter can be inputted at a time.
</box>

* There are 2 unique identifiers that can be used to search for a person:
  * `p/PHONE_NUMBER`
  * `e/EMAIL`
* This search will always return at most 1 contact when a valid attribute is provided.
* The inputs are case-sensitive e.g. `JOHN` will not match `john`.
* Only full words or numbers will be matched e.g. `123` will not match `1234`.

Examples:
* `find p/123` returns the contact with the phone number `123`
* `find e/johnd@example.com` returns the contact with the email `johnd@example.com`
  ![result for 'find alex david'](images/findp123.png)

### Filtering persons by common identifier: `filter`

Filters persons by searching for a common attribute

Format: `filter COMMON_IDENTIFIER`

<box type="tip" seamless>

**Tip:** Only 1 parameter TYPE can be inputted at a time.
</box>

* There are 3 common identifiers that can be used to filter for a person:
    * `n/NAME`
    * `a/ADDRESS`
    * `t/TAG`
* Note that for tags, multiple tags can be specified for filtering eg. `filter t/friend colleague`.
* This search will return all contacts that match the common attribute provided.
* The inputs are case-insensitive e.g. `JOHN` will match `john`.
* Only full words will be matched e.g. `han` will not match `hans`.

Examples:
* `filter n/alex` returns all contacts with the name `alex`
* `filter a/123 street` returns all contacts with the address `123 street`
* `filter t/friend` returns all contacts with the tag `friend`

### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete UNIQUE_IDENTIFIER`

<box type="tip" seamless>

**Tip:** Only 1 parameter can be inputted at a time.
</box>

* There are 3 unique identifiers that can be used to delete a person:
    * `INDEX` 
    * `p/PHONE_NUMBER`
    * `e/EMAIL`
* When using filter or find, the current list will be updated to a filtered list. When deletion by index is used, the index refers to the index number shown in the filtered persons list.
* A valid email or phone number will delete the corresponding contact regardless of any applied filters.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `filter n/Betsy` followed by `delete 1` deletes the 1st person in the results of the `filter` command.
* `delete p/1234567` deletes the person with the phone number `1234567` (even if not shown in current list).

### Export data : `export`

Exports the current contacts from CraftConnect into a JSON file named `craftconnect.json`, 
and puts the file into a folder whose absolute path is specified.

Format: `export ABSOLUTE_PATH_TO_FOLDER [--create-dir]`

- The absolute path refers to the full location of the file starting from the root of the system, for example,
  - `C:\Users\JohnDoe\Documents\Data` (Windows)
  - `/home/johndoe/Documents/Data` (Linux)
- When the path to the folder contains spaces, do not include quotation marks. Leave the path as it is.
- When there is no folder at the specified path, the `-–create-dir` flag will tell CraftConnect to create that folder 
before inserting the JSON file into it. If the flag is not specified, there **must be** an existing folder at that path.
- The –create-dir flag can be put anywhere after the export command, so
  - `export -–create-dir C:\Users\John\Data`
  - `export C:\Users\John\Data -–create-dir`

  are all valid commands.
- It is highly recommended to use an absolute path to ensure that the correct file is imported. 
Using a relative path (e.g. `Documents/Data/`) may cause unexpected behaviour because the system would not 
know your current location.
- If the following conditions are met:
  - There is an existing folder, for example `C:\Users\John\Data`
  - There does not exist a specific file within that folder, for example `C:\Users\John\Data\data.json`
  - An accidental call to import with the path to that file
  - The `-–create-dir` flag is enabled, for example, `export C:\Users\John\Data\data.json –-create-dir`.
  
  Then, CraftConnect will create the new folder, for example with name `data.json`, and put the exported data file 
  into this folder, resulting in the data file being located in `C:\Users\John\Data\data.json\craftconnect.json`. 
  This is to allow for folder names that contain the ‘.’ character.
- Accidentally calling import with a path to an existing file will return an error.

Examples
- `export C:\Users\John\Data` will export all data into a file located at `C:\Users\John\Data\craftconnect.json` if the 
folder exists, and returns an error otherwise.
- `export C:\Users\John\My Data –create-dir` will create a new folder located at `C:\Users\John\My Data` if the folder 
has not existed, and export all data into a file located at `C:\Users\John\Data\craftconnect.json`.

### Import data : `import`

Overwrites the current contacts in CraftConnect by new data from a JSON file in the specified path.

Format: `import ABSOLUTE_PATH_TO_JSON_FILE`

- The absolute path refers to the full location of the file starting from the root of the system, for example,
  - `C:\Users\JohnDoe\Documents\Data\addressbook.json` (Windows)
  - `/home/johndoe/Documents/Data/addressbook.json` (Linux)
- When the path to the file contains spaces, do not include quotation marks. Leave the path as it is.
- It is highly recommended to use an absolute path to ensure that the correct file is imported. 
Using a relative path (e.g. `Documents/Data/addressbook.json`) may cause unexpected behaviour because the system would 
not know your current location.
- The file to be imported must exist, have the `.json` extension, and follow the data schema of CraftConnect. 
It is best for non-technical users to pair the `import` functionality with `export`, to carry data from one 
CraftConnect address book to another CraftConnect address book.

Examples:
- `import C:\Users\JohnDoe\Documents\My Data\addressbook.json` will overwrite the current contacts in CraftConnect 
with new data from a JSON file located at `C:\Users\JohnDoe\Documents\My Data\addressbook.json`.


### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action      | Format, Examples                                                                                                                                                      |
|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**     | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague` |
| **Clear**   | `clear`                                                                                                                                                               |
| **Delete**  | `delete UNIQUE_IDENTIFIER`<br> e.g., `delete 3` / `delete p/98765432`                                                                                                 |                                                                                                  
| **Edit**    | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                           |                                          
| **Export**  | `export ABSOLUTE_PATH_TO_FOLDER [--create-dir]` <br> e.g., `export C:\Users\John\Data --create-dir`                                                                   |                                      
| **Find**    | `find UNIQUE_IDENTIFIER`<br> e.g., `find p/86253723`                                                                                                                  |                                     
| **Filter**  | `filter COMMON_IDENTIFIER`<br> e.g., `filter t/friend`                                                                                                                |                                    
| **Import**  | `import ABSOLUTE_PATH_TO_JSON_FILE` <br> e.g., `export C:\Users\John\Data\data.json`                                                                                  |                                   
| **List**    | `list`                                                                                                                                                                |                                  
| **Help**    | `help`                                                                                                                                                                |                                 
