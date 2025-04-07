---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# CraftConnect Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a contact).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a business owner, preferably in arts and crafts
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast and/or prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

CraftConnect app will keep track of the many different suppliers and
customers easily and organise them into groups. Since small business owners usually have
a lack of manpower, CraftConnect will provide an efficient solution for handling of vendor and
customer orders. On top of that, business owners can manage contacts faster than a typical
mouse/GUI driven app.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                         | So that I can…​                                                                        |
|----------|--------------------------------------------|--------------------------------------|----------------------------------------------------------------------------------------|
| `* * *`  | new user                                   | see usage instructions               | refer to instructions when I forget how to use the App                                 |
| `* * *`  | user                                       | add a new person                     |                                                                                        |
| `* * *`  | user                                       | delete a person by unique attributes | remove entries that I no longer need just by knowing one piece of unique data          |
| `* * *`  | user                                       | edit a person's contact              | update the person's information quickly without having to delete and re-add the person |
| `* * *`  | user                                       | filter people by common attributes   | locate details of persons without having to go through the entire list                 |
| `* * *`  | user                                       | find people by unique attributes     | locate the full details of a person just by knowing his/her phone number or email      |
| `* *`    | user                                       | clear all contacts                   | start an entirely new instance of the address book                                     |
| `* *`    | user                                       | export my contacts to a file         | back up my contacts or share them with others                                          |
| `* *`    | user                                       | import new data from a file          | restore my address book or merge contacts from another source                          |
| `* *`    | user                                       | add a note to a contact by index      | add specific information about certain contacts                                       |
| `* *`    | user                                       | undo the most recent change to the contact list    | restore an incorrect edit or delete |
| `* *`    | user                                       | restore the most recently undone change to the contact list |  restore an accidentally undone change |

### Use cases

(For all use cases below, the **System** is `CraftConnect` and the **Actor** is the `user`, unless specified otherwise)
<br><br><br>
**Use case: See the usage instruction**

**MSS**

1. User requests to see the usage instructions.
2. CraftConnect displays the usage instructions.

   Use case ends.
   <br><br><br>

**Use case: Add a new contact**

**MSS**

1.  User specifies the new contact information (`Name`, `Email`, `Phone Number`, `Address`, `Category`)
2.  CraftConnect adds the new contact and informs the user of the successful addition.

    Use case ends.

**Extensions**
* 1a. The user fails to specify the contact name, email, phone number or address.

    * 1a1. CraftConnect shows an error message and informs the user of the correct command format.

      Use case resumes at step 1.

* 1b. The phone number (if specified) is not in the correct phone number format.

    * 1b1. CraftConnect shows an error message and informs the user that the phone number is not in the correct format,
      and shows the user the correct format for a phone number.

      Use case resumes at step 1.

* 1c. The email (if specified) is not in the correct email format.

    * 1c1. CraftConnect shows an error message and informs the user that the email is not in the correct format,
      and shows the user the correct format for an email.

      Use case resumes at step 1.

* 1d. The phone number (if specified) is already used by an existing contact.

    * 1d1. CraftConnect shows an error message and informs the user that the phone number is already used by an existing contact, and shows the said contact.

      Use case resumes at step 1.

* 1e. The email (if specified) is already used by an existing contact.

    * 1e1. CraftConnect shows an error message and informs the user that the email is already used by an existing contact, and shows the said contact.

      Use case resumes at step 1.
      <br><br><br>

**Use case: Delete a contact by unique attributes (`Email`, `Index`, `Phone Number`)**

**MSS**

1.  User requests to list contacts.
2.  CraftConnect displays a list of contacts.
3.  User requests to delete a contact by inputting the contact’s unique attribute (`Email`, `Index`, `Phone Number`).
4.  CraftConnect deletes the specified contact and informs the user of the successful deletion.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The input is invalid.

    * 3a1. CraftConnect shows an error message and informs the user on the correct input syntax.
      
      **NOTE**: : a non-positive index or an index outside the range of a Java integer results in an invalid command format
          exception, because these can be detected during the parsing phase. A positive index that is outside the range
          of the contact list can only be detected at command execution phase, so another message will be returned.

      Use case resumes at step 3.

* 3b. A non-unique attribute is supported.

    * 3b1. CraftConnect shows an error message and informs the user that the user's attribute is not a unique attribute.

      Use case resumes at step 3.

* 3c. No contacts matching the user's supported value.

    * 3c1. CraftConnect shows an error message and informs the user that no contact matches their value.

      Use case resumes at step 3.
      <br><br><br>

**Use case: Edit an existing contact**

**MSS**

1.  User requests to list contacts.
2.  CraftConnect displays a list of contacts.
3.  User requests to edit a contact by specifying the index of the contact, the attributes to be changed and the changed attributes.
4.  CraftConnect edits the specified contact accordingly and informs the user of the successful edit.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The inputted index is invalid as it does not correspond to a valid index within CraftConnect.

    * 3a1. CraftConnect shows an error message and informs the user that the inputted index is invalid.

      **NOTE**: : a non-positive index or an index outside the range of a Java integer results in an invalid command format
      exception, because these can be detected during the parsing phase. A positive index that is outside the range
      of the contact list can only be detected at command execution phase, so another message will be returned.

      Use case resumes at step 3.

* 3b. The inputted attributes are invalid as it does not correspond to a valid attribute within CraftConnect.

    * 3b1. CraftConnect shows an error message and informs the user of the constraints of the invalid attributes.

      Use case resumes at step 3.

* 3c. The email (if specified) is not in the correct email format.

    * 3c1. CraftConnect shows an error message and informs the user that the email is not in the correct format, and shows the user how an email should look like.

      Use case resumes at step 3.

* 3d. The email provided (if specified) is a duplicate.

    * 3d1. CraftConnect shows an error message, informing the user of the contact which is already using the email.

      Use case resumes at step 3.

* 3e. The phone number provided (if specified) is a duplicate.

    * 3e1. CraftConnect shows an error message, informing the user of the contact which is already using the phone number.

      Use case resumes at step 3.

* 3f. The inputted attributes are the same as the old attributes of the contact

    * 3f1. CraftConnect shows an error message, informing the user that the inputted attributes are the same as the old attributes of the contact.
    
      Use case resumes at step 3.
      <br><br><br>

**Use case: Find a contact by a unique attribute (e.g., `Email`, `Phone`)**

**MSS**

1.  User requests to list contacts.
2.  CraftConnect displays a list of contacts.
3.  User request to view the contact with the corresponding unique attribute (`Email`, `Phone`).
4.  CraftConnect displays the contact that match the inputted attribute.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The input is invalid.

    * 3a1. CraftConnect shows an error message and informs the user on the correct input syntax.

      Use case resumes at step 3.

* 3b. More than one attribute is specified.

    * 3b1. CraftConnect shows an error message and informs the user that only one and unique attribute should be specified.

      Use case resumes at step 3.

* 3c. The inputted attribute is not unique.

    * 3c1. CraftConnect shows an error message and informs the user that the attribute they specified is not unique, while
      also suggesting the user to use the filter functionality if he/she wants to search using a common attribute.

      Use case resumes at step 3.
      <br><br><br>

**Use case: Filter contacts by common attributes (`Name`, `Address`, `Tag`)**

**MSS**

1.  User requests to list contacts.
2.  CraftConnect displays a list of contacts.
3.  User requests to filter contacts by specifying a common attribute.
4.  CraftConnect displays a list of contacts that match the inputted attribute.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The input is invalid.

    * 3a1. CraftConnect shows an error message and informs the user of the constraints of the invalid attributes.

      Use case resumes at step 3.

* 3b. More than one attribute is specified.

    * 3b1. CraftConnect shows an error message and informs the user that only one common attribute should be specified.

      Use case resumes at step 3.

* 3c. The inputted attribute is not unique.

    * 3c1. CraftConnect shows an error message and informs the user that the attribute they specified is unique, while
      also suggesting the user to use the find functionality if he/she wants to search using a unique attribute.
<br><br><br>

**Use case: Clear all contacts**

**MSS**

1.  User requests to list contacts.
2.  CraftConnect displays a list of contacts.
3.  User requests to clear all contacts.
4.  CraftConnect deletes all contacts and informs the user of the successful deletion.

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.
<br><br><br>

**Use case: Export contacts to a file**

**MSS**

1. User requests to export data by specifying the folder to store the data file, and whether to create
a new folder if their specified folder does not exist.
2. CraftConnect exports the data into a JSON file, stores it in the user's folder and informs the successful export.

   Use case ends.

**Extensions**

* 2a. The entire input is invalid (anything not conformed to 1 create-folder-option and 1 path)

    * 2a1. CraftConnect shows an error message and informs the user of the correct command usage.

      Use case resumes at step 1.

* 2b. The folder path is invalid (OS-dependent)

    * 2b1. CraftConnect shows an error message and asks the user to check for spelling.

      Use case resumes at step 1.

* 2c. The folder does not exist, and the option of creating folder if not exists is not specified

    * 2c1. CraftConnect shows an error message and tells the user to check for spelling or specify 
create-folder-if-not-exist.

      Use case resumes at step 1.

* 2d. Some I/O error has occurred when creating folders or the file.

    * 2d1. CraftConnect shows an error message and tells the user to check disk space or use another path.

      Use case resumes at step 1.
      <br><br><br>

**Use case: Import contacts from a file**

**MSS**

1. User requests to import data by specifying the path to the file, whether to overwrite existing contacts with the
new contacts from the JSON file or to append new contacts into the existing contacts, and whether to ignore duplicated
contacts.
2. CraftConnect replaces the address book contacts with the data from the file and informs the user of the successful 
import.

    Use case ends.

**Extensions**

* 2a. The input is empty

  * 2a1. CraftConnect shows an error message and informs the user of the correct command usage.
  
    Use case resumes at step 1.

* 2b. The path to the file is invalid, or is valid but the file does not exist at that location

  * 2b1. CraftConnect shows an error message and asks the user to check for spelling or choose an existent path.

    Use case resumes at step 1.

* 2c. The file is not a JSON file

    * 2c1. CraftConnect shows an error message and asks the user to specify the path to a JSON file.
      
      Use case resumes at step 1.

* 2d. The JSON file does not follow CraftConnect's schema

    * 2d1. CraftConnect shows an error message and tells the user the schema it uses to store contacts' data.

      Use case resumes at step 1.

* 2e. (Exclusive to when duplicates are not ignored) The JSON file contains duplicated contacts within itself, or it 
contains duplicated contacts with an existing contact in append mode.

    * 2e1. CraftConnect shows an error message about duplicated contacts.

      Use case resumes at step 1.

<br><br><br>

**Use case: Add a note to a contact**

**MSS**

1. User request to list contacts.
2. CraftConnect displays a list of contacts.
3. User requests to update the note of a specific contact by inputting the index and the new note.
4. CraftConnect updates the contact's note and indicates success.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The inputted index is invalid as it does not correspond to a valid index within CraftConnect.

    * 3a1. CraftConnect shows an error message and informs the user that the inputted index is invalid.
      
      **NOTE**: a non-positive index or an index outside the range of a Java integer results in an invalid command format
        exception, because these can be detected during the parsing phase. A positive index that is outside the range
        of the contact list can only be detected at command execution phase, so another message will be returned.

      Use case resumes at step 3.
<br><br><br>

**Use case: Undo the most recent change to the contact list**

**MSS**

1. User requests to undo the most recent change to the contact list
2. CraftConnect restores the state of the contact list to before the most recent change

    Use case ends.

**Extensions**

* 1a. There are no changes to undo

  * 1a1. CraftConnect shows an error message and informs the user that there are no changes to undo.
  
    Use case ends.
<br><br><br>

**Use case: Restore the most recent undone change to the contact list**

**MSS**

1. User requests to restore the most recent undone change to the contact list
2. CraftConnect restores the state of the contact list to after the undone change

    Use case ends.

**Extensions**

* 1a. There are no changes to restore

  * 1a1. CraftConnect shows an error message and informs the user that there are no changes to restore.
  
    Use case ends.
<br><br><br>

*{More to be added}*

### Non-Functional Requirements

1. **Performance**
    - CraftConnect should initialize within 2 seconds on a mid-tier computer.
    - CraftConnect should be able to hold up to 1000 contacts while keeping all operations under 100ms.
    - CraftConnect should not exceed 200MB RAM during peak operations.

2. **Usability and Accessibility**
    - All errors should be clear and actionable (e.g. if the wrong email format is used, tell the user how a correct email should look like).
    - A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

3. **Portability**
    - CraftConnect should work on any _mainstream OS_ as long as it has Java `17` or above installed.
    - Any user should be able to run CraftConnect without needing to install any other applications/dependencies (except Java 17)

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Mid-tier computer**: A computer that is typically used for office operations and software development, but not strong enough to handle moderate gaming. For minimum-spec reference,
    * Processor (CPU): Intel Core i3 (2nd Gen)/AMD Athion 64 X2
    * Memory (RAM): 2GB RAM
    * Storage (HDD/SDD): 100MB of free disk space
    * Graphics: Integrated GPU (Intel HD Graphics 300 or equivalent)
    * Disk Speed: HDD (5400 RPM) or SSD if available.
* **Contacts**: Contacts are considered to be unique if and only if they have a unique email and phone number. Thus, two contacts can still have the same names.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Deleting a contact

1. Prerequisites: List all contacts using the `list` command. Multiple contacts in the list.
2. If there is not a contact with phone number 123, add a contact with such a phone number.
    `add n/dummy e/dummy@example.com p/123 a/dummydummy`
   If there is not a contact with phone number 456, add a contact with such a phone number.
    `add n/dummy2 e/dummy2@example.com p/456 a/dummydummy2`
   If there is not a contact with email address dummy3@example.com, add a contact with such an email address.
    `add n/dummy3 e/dummy3@example.com p/789 a/dummydummy3`
   If there is not a contact with email address dummy4@example.com, add a contact with such an email address.
    `add n/dummy4 e/dummy4@example.com p/012 a/dummydummy4`

3. Test case: `delete 1`<br>
   Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

4. Test case: `delete 0`<br>
   Expected: No contact is deleted. Error details shown in the status message. Status bar remains the same.

5. Test case: `delete p/123`<br>
   Expected: the contact with phone number 123 is deleted. 
   Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

6. Test case: `filter a/5`, `delete p/456`
   Expected: even though the contact with phone number 456 is filtered out, deletion by phone number still succeeds.
   Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

7. Test case: `delete e/dummy3@example.com`<br>
  Expected: the contact with the email address dummy3@example.com is deleted.
  Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

8. Test case: `filter a/5`, `delete e/dummy4@example.com`
  Expected: even though the contact with email address dummy4@example.com is filtered out, deletion by email address still succeeds.
  Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

9. Other incorrect delete commands to try: 
   - `delete` 
   - `delete x` (where x is larger than the list size)
   - `delete e\NON_EXISTENT_EMAIL`
   - `delete p\NON_EXISTENT_PHONE`
   - `...`
         Expected: No contact is deleted. Error details shown in the status message. Status bar remains the same.

