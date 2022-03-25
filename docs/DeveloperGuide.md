---
layout: page
title: Developer Guide
---
* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

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

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Find tag feature

#### Implementation

The find tag feature is used when a user is interested in finding contacts who have a certain `Tag`. Each `Person` has a set of `Tags` which contains unique `Tags` since each `Person` should not have more than 1 of the same tag.

This feature is facilitated by `FindTagCommand`, which makes use of a `TagContainsKeywordPredicate` that checks if the `Tag` set of a `Person` contains all the tag names in the `List` of keywords (case-insensitive).

The `FindTagCommand#execute()` method looks through the `Tag` set of each `Person` and updates the `Model#filteredPersons` using `Model#updateFilteredPersonsList()` which uses the `TagContainsKeywordPredicate`. The `Model` then displays the currently most updated filtered person list which reflects contacts with the specified tags in the list.

As such, `FindTagCommand` extends `Command` and executes the command by calling `FindTagCommand#execute()`.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If there are multiple keywords, e.g.`findtag friends colleagues`, then the contact should have both tags `friends` and `colleagues` in order for the contact to be reflected on the updated filtered person list.

</div>

Given below is an example usage scenario and how the find tag feature behaves at each step.

Step 1. The user launches the address book for the first time, initialized with the initial address book state.

Step 2. The user executes the command `findtag friends` to find contacts with the tag `friends` in their set of tags.

Step 3. The AddressBookParser parses the command `findtag friends` and creates a `FindTagCommandParser` to parse the keyword `friends`.

Step 4. The `TagContainsKeywordPredicate` is created and passed onto the constructor of `FindTagCommand` to create a new `FindTagCommand` object.

Step 5. The `LogicManager` then calls `FindTagCommand#execute()`, calling `Model#updateFilteredPersonList()` which updates the list of persons to be displayed.

Step 6. The `CommandResult` created from `FindTagCommand#execute()` is returned to the `LogicManager` which will be reflected in the `ResultDisplay` of the `GUI`.

![FindTagSequenceDiagram](images/FindTagSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FindTagCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

#### Design considerations:

**Aspect: Should contacts have all the keywords searched for:**

* **Alternative 1 (current choice):** Contacts containing strictly the same tags as all the keywords searched for.
    * Pros: More intuitive, as only contacts with strictly the same tags as all the keywords searched will be listed.
    * Cons: Have to ensure the contacts are tagged correctly, or they will not be found with this feature.

* **Alternative 2:** Contacts containing any tags of the keywords searched for.
    * Pros: Can find contacts who have any tags the keywords searched.
    * Cons: Unintuitive, as we usually narrow down the scope for filtering.

### Notes feature

#### Implementation

The Notes class encapsulates a List<String> that contains the notes added to a Person. The main feature of the class is that updating a Notes object, be it adding or deleting a note, returns a new Notes object that contains a new list with the result.

In the execution of a command that modify a contact's notes, for instance, the NoteCommand which adds a given note to a contact, a new Person object is created with the new Notes object. Thus, this implementation of Notes avoids the problem of aliasing between different Person objects, and reduces coupling.

The sequence diagram below illustrates this behaviour.

![NoteCommandExecution](images/NoteSequenceExecution.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifelines for `personToEdit` and `oldNotes` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifelines reaches the end of diagram.

</div>

This implementation is in line with other forms of editing a contact, and will facilitate new features you may plan to implement, such as the planned undo/redo feature.

Another reason for this implementation is the ease of testing. You will find that the TypicalPersons contains Person objects as the baseline to create a list of contacts. Writing tests for Notes is easier without having to intently reverse any changes to the Notes of these Persons.

### Favourite feature

#### Implementation

The favourites feature is implemented by identifying the user's favourite contacts through their `favouriteStatus` state,  which is stored as a `Persons` field. The favourite-status of a person is represented by a `Favourite`, which can be either `IS_FAVOURITE`, or `NOT_FAVOURITE` singletons.

Although it is a `Persons` field, it cannot be instantiated in an add command, nor modified through an edit command, unlike other fields such as "name" or "address". It should be thought of as metadata for the contact, and is set as `NOT_FAVOURITE` by default.

As such, a contact's favourite-status is handled through the `FavouritesCommand`, which extends `Command`. Toggles the contact's favourite status based on their current favourite-status. Its execution behaves rather similarly to `edit`, and has its own `FavouritesCommand#createFavouritedPerson(target, newPerson)` operation.

As part of the favourites feature, a user can also list all their favourite contacts through the `ListFavouritesCommand`, which extends `Command` as well. It leverages on the `PersonIsFavouriteContactPredicate`, which can be passed as an argument to `Model#updateFilteredPersonList(predicate)`

Given below is an example scenario of the favourites feature in operation.

Step 1. When a user is created, they are automatically assigned `NOT_FAVOURITE` as their favourite-status.

![FavouriteState0](images/favourite/FavouriteState0.png)

Step 2. The user executes `fav 3` command to favourite the 3rd person in the address book. The `fav` command executes and determines the current `favouriteStatus` of the contact (which in this case is `NOT_FAVOURITE`). It then calls the private helper method `createFavouritedPerson` to create a copy of the target contact, except with their `favouriteStatus` now pointing to the opposite `Favourite` value (which will be the `IS_FAVOURITE` singleton).

![FavouriteState1](images/favourite/FavouriteState1.png)

Step 3. The command execution continues on to replace the existing person object with the newly favourited contact through `Model#setPerson`. After the execution successfully terminates, the user can visually identify their favourite contacts through the yellow star right beside their contact names.

![FavouriteState2](images/favourite/FavouriteState2.png)

Step 4. The user decides that they want to see only their favourite contacts. The `favourites` command will then execute, passing the `PersonIsFavouriteContactPredicate` into the `Model#updateFilteredPersonList`.

![FavouriteState3](images/favourite/FavouriteState3.png)

The following sequence diagram shows how the favourite operation works:

![FavouriteSequenceDiagram](images/favourite/FavouriteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FavouriteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

If the user runs `fav 3` again, or commands `fav` on an existing favourited contact, the only difference in the execution would be that the `NOT_FAVOURITE` reference will be passed into the `FavouriteCommand#createFavouritePerson` method instead.

From the user experience point of view, not having an un-favourite command is intuitive. In order to favourite a contact, they will need to look-up their current index number, in which case, they will already know if they are a favourite contact or not.
This makes toggling instead of hard-setting the favourite status of a contact feasible, and reduces the number of commands the user has to remember.

#### Design considerations:

**Aspect: How favourite and list favourite commands execute:**

* **Alternative 1 (current choice):** Each person responsible for their own favourite state
    * Pros:
        * Less coupling between favourite and other commands such as edit and delete, as well as the model.
        * Easy to reference to check favourite status of contact.
        * Easy to implement.
    * Cons:
        * Adds more fields to the person contact, increasing the complexity of the contact in the long run.
        * Need to have a `Favourite` class to represent a contact's status.
* **Alternative 2:** Store favourited contacts into a `FavouritePersonList`.
    * Pros:
        * No need to add field into `Persons` class.
        * No need to have `Favourite` class, which basically just acts as a boolean.
        * `List` command will be easy to implement.
    * Cons:
        * Have to update the `FavouritePersonList` if a favourited contact has been modified.
        * Needs more work, specially within the storage.

### Deadline feature

#### Implementation
The `deadline` mechanism borrows from the current `edit` mechanism. The main idea of the mechanism is creating a new `Person` object with added `deadline`, then replace existing `Person` in list with the new `Person`.

Step 1. The user is currently on the screen displaying the list of `Person`.

Diagram below represents the list of `Person`
<img src="images/DeadlineState1.png" width="574" />

Step 2. The user executes `deadline 1 /d return book 1/1/2023` command to add the deadline `1/1/2023` with description `return book` to the first person in the address book.

<div markdown="span" class="alert alert-info">:information_source: **Note:** Adding multiple `deadline` is supported, for example: `deadline 1 /d return book 1/1/2023 /d write report 5/1/2023`.
</div>

Step 3. The `DeadlineCommand#execute()` method will find the `Person` at the index specified.
<img src="images/DeadlineState2.png" width="574" />

Step 4. After getting `personToAddDeadline`, `DeadlineCommand#execute()` method creates a new `Person` called `editedPerson` with added `Deadline` and all the other attributes of `personToAddDeadline`.
<img src="images/DeadlineState3.png" width="574" />

Step 5. The `editedPerson` replaces the `personToAddDeadline` in the list.
<img src="images/DeadlineState4.png" width="574" />

Step 6. The `UI` displays the updated list.

Diagram below shows the execution of `deadline 1 /d return book 1/1/2023` command
<img src="images/DeadlineSequenceDiagram.png" width="574" />

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeadlineCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram
</div>

<div markdown="span" class="alert alert-info">:information_source: **Note:** Calling `deadline 1` without the date and description clears the deadlines for contact specified in index given.
</div>

<div markdown="span" class="alert alert-info">:information_source: **Note:** Duplicate `deadline` for the same `Person` is not allowed.
</div>

#### Limitations and proposed solutions
Currently, calling `deadline` command resets all `deadlines` previously stored for `Person` specified, so it does not scale well.

**Solution 1**: Allow `deadline` command to differentiate between adding and deleting `deadline`.

**Solution 2**: Modify the `add` command and `delete` command to include adding and deleting deadlines.

The main problem with the solutions is that new delimiters have to be created. These delimiters might not be intuitive for users to remember or might overlap with other delimiters.

(more limitations and solutions to be discovered...)

### Add contact with address as optional field feature

#### Implementation

The implemented enhanced add mechanism is facilitated by the `add` command.

Classes changed for this feature:
- `AddCommand`
- `AddCommandParser`

Given below is a code snippet implemented for `AddCommandParser`:

![AddCommandParserEnhancement](images/AddCommandParserEnhancement.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** A string value of `*No Address Specified*` is used for `Address.EMPTY_ADDRESS` instead of an empty string.
</div>

#### Design considerations:

**Aspect: Values to accept in address field:**

* **Alternative 1 (current choice):** Having a string `*No Address Specified*` in the address field
    * Pros: Easy to implement.
    * Cons: UI may look less pleasing with repetitive words for each contact.

* **Alternative 2:** Accepting empty string in address field
    * Pros: UI may look neater and cleaner.
    * Cons: Will need to change many components for address field to be an empty string.

### Add a high importance flag feature

#### Implementation

The implemented high importance flag feature is facilitated by using the `execute()` method in `HighImportanceCommand` with the keyword `impt` to execute the command. This feature is stored as an additional attribute `highImportanceStatus` for each person within the application and is implemented as a separate class within the `Person` package.

The `highImportanceStatus` contains an attribute `value` which takes on the values of either `"true"` or `"false"`.
- `"true"` denotes that the `Person` is a person of high importance.
- `"false"` denotes that the `Person` is a person not of high importance.

Classes added for this feature:
- `HighImportance`
- `HighImportanceCommand`
- `HighImportanceParser`

The following sequence diagram shows how the feature works:

![HighImportanceSequenceDiagram](images/HighImportanceSequenceDiagram.png)

_{more aspects and alternatives to be added}_

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.


### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

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

d'Intérieur is designed for interior designers who:

* Have a preference for CLI apps
* have many clients and projects to keep track of
* can type fast
* prefer typing to mouse interactions

**Value proposition**: organise information on client and projects, giving a one-stop overview of current work progress and schedule.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                   | I want to …​                                                                         | So that I can…​                                                                                   |
|----------|-------------------------------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `* * *`  | new user                                  | look at a basic tutorial for adding contacts                                         | understand what details I can add to a contact                                                    |
| `* * *`  | busy interior designer                    | add a new contact quickly                                                            | reduce time on adding new contacts                                                                |
| `* * *`  | user                                      | delete a contact                                                                     | remove entries that I no longer need                                                              |
| `* * *`  | user                                      | find a contact by name                                                               | locate details of contacts without having to go through the entire list                           |
| `* * *`  | interior designer                         | label my contacts with some fixed labels                                             | categorise my clients based on the stage of design I am at                                        |
| `* * *`  | interior designer with many ongoing leads | filter my contacts list based on label                                               | easily keep track of who I am working with at a specific point of time without having to remember |
| `* * *`  | interior designer                         | add notes under a contact                                                            | keep track of the demands and requirements of each individual project                             |
| `* * *`  | busy interior designer                    | add a deadline under a contact                                                       | keep track of my deadlines easily                                                                 |
| `* * *`  | user                                      | favourite certain contacts                                                           | look up favourites and contact them easily                                                        |
| `* *`    | interior designer                         | check upcoming deadlines in chronological order                                      | keep track of deadlines automatically                                                             |
| `* *`    | long-time user                            | separate past and current clients                                                    | avoid contacting clients with similar names or old clients that I am not presently working with   |
| `* *`    | new user                                  | view a list of commands                                                              | know what commands are available and the right commands to use                                    |
| `* *`    | interior designer                         | add a high importance flag to a client                                               | take note of pressing issues regarding a client, such as mobility issues                          |
| `*`      | interior designer                         | add images under a client                                                            | keep track of images such as floor plans and inspirational designs relevant to the client         |
| `*`      | interior designer                         | track and calculate costs accumulated for a client                                   | at a glance, know how much money has been spent on them for a project                             |
| `*`      | interior designer                         | generate invoices                                                                    | easily generate, store and print invoices for my clients                                          |
| `*`      | interior designer                         | send out automated messages/emails to clients to wish them well on festive occasions | maintain good rapport with clients                                                                |
| `*`      | user                                      | create contacts by importing information from other apps                             | create contacts more easily                                                                       |

### Use cases

(For all use cases below, the **System** is `d'Intérieur` and the **Actor** is the `user`, unless specified otherwise)

**UC01: Add a contact**

**MSS**

1. User adds a person with the required fields.
2. d'Intérieur shows the new contact added with the details entered.

   Use case ends.

**Extensions**

* 1a. User includes address field.

    * Use case resumes at step 2.

**UC02: Delete a contact**

**MSS**

1. User requests to list contacts.
2. d'Intérieur shows a list of contacts.
3. User requests to delete a specific contact in the list.
4. d'Intérieur shows contact deleted and updates the list.

Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**UC03: Add a label to a contact**

**MSS**

1. User requests to list contacts.
2. d'Intérieur shows a list of contacts.
3. User requests to add a label to a specific contact in the list.
4. d'Intérieur adds the label to the contact and shows the contact.

Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 3b. The label requested does not currently exist in d'Intérieur.

    * To be added

**UC04: Filter contacts using labels**

**MSS**

1. User requests list of contacts filtered to those containing the given label.
2. d'Intérieur shows a list of contacts who have the given label.

Use case ends.

**Extensions**

* 1a. The given label does not exist.

    * To be added

**UC05: Adding a note to a contact**

**MSS**

1. User requests to add a note to a specific contact in the list.
2. d'Intérieur adds the note to the contact and shows the contact.

Use case ends.

**Extensions**

* 1a. User enters only whitespaces or nothing as a note.

    * 1a1. d'Intérieur alerts the user that no changes have been made to notes

      Use case ends.

* 1b. The given index is invalid.

    * 1b1. AddressBook shows an error message.

      Use case resumes at step 1.

**UC06: Adding a contact to favourites**

**MSS**

1. User requests to add a specific contact in the list to favourites.
2. d'Intérieur adds the contact to favourites and shows the contact.

Use case ends.

**Extensions**

* 1a. The given index is invalid.

    * 1a1. AddressBook shows an error message.

      Use case resumes at step 1.

**UC07: Add a deadline**

1. User requests to add a deadline under a contact.
2. d'Intérieur adds the deadline under the contact and shows the contact.

Use case ends.

**Extensions**

* 1a. The deadline given does not contain a valid date or time.

    * To be added

* 1b. The given index is invalid.

    * 1b1. AddressBook shows an error message.

      Use case resumes at step 1.

*{More to be added}*

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 contacts without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should work on both 32-bit and 64-bit environments.
5. The system should boot up within five seconds.
6. The system is not required to handle a non-text input.
7. The system should be able to have up to 5000 contacts.
8. The response to any action should become visible within five seconds.

*{More to be added}*

### Glossary

* **User** and **Interior Designer**: Both are used interchangeably as d'Intérieur is enhanced for interior designers
* **Contact**: An entry in the d'Intérieur app
* **Client**: A specific type of contact that interior designers will most likely keep track of
* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
