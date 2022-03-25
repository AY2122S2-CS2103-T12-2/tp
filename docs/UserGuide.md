---
layout: page title: User Guide
---

d'Intérieur is a **desktop app for managing contacts, optimized for use via a Command Line Interface** (CLI) while still
having the benefits of a Graphical User Interface (GUI). If you can type fast, d'Intérieur can get your contact
management tasks done faster than traditional GUI apps.

* Table of Contents {:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

2. Download the latest `dinterieur.jar` from [here](https://github.com/AY2122S2-CS2103T-T12-2/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your d'Intérieur.

4. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app
   contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will
   open the help window.<br>
   Some example commands you can try:

    * **`list`** : Lists all contacts.

    * **`add n/Mary Jane p/12345678 e/maryJ@example.com`** : Adds a contact named Mary Jane to the Address Book.

    * **`delete`** `3` : Deletes the 3rd contact shown in the current list.

    * **`fav`** `2` : Adds the 2nd contact shown in the current list to your list of favourite contacts

    * **`favourites`** : Lists all your favourite contacts.

    * **`clear`** : Deletes all contacts.

    * **`exit`** : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/Mary Jane`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/Mary Jane t/friend` or as `n/Mary Jane`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of
  the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be
  ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

</div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

### Adding a contact : `add`

You can add a contact to the address book with the address as an optional field.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL [a/ADDRESS] [t/TAG]…​`

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A contact can have 0 or 1 address
</div>
<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A contact can have any number of tags (including 0)
</div>

Examples:

* `add n/John Doe p/98765432 e/johnd@example.com`
* `add n/Mary Jane p/12345678 e/maryJ@example.com a/Bukit Timah t/completed`

### Listing all contacts : `list`

Shows a list of all contacts in the address book.

Format: `list`

### Editing a contact : `edit`

Edits an existing contact in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the contact at the specified `INDEX`. The index refers to the index number shown in the displayed contact list.
  The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the contact will be removed i.e adding of tags is not cumulative.
* You can remove all the contact’s tags by typing `t/` without specifying any tags after it.

Examples:

* `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st contact to be `91234567`
  and `johndoe@example.com` respectively.
* `edit 2 n/Betsy Crower t/` Edits the name of the 2nd contact to be `Betsy Crower` and clears all existing tags.

### Adding favourites : `fav`

Adds the contact to your list of favourite contacts and view them.

Format:

- `fav INDEX`
- `favourites` — Shows a list of your favourite contacts

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
You can run `fav INDEX` where `INDEX` is the index of a contact that currently belongs in your favourites list to remove them.
</div>

Examples:

- `fav 1` — Adds contact at index 1 to your list of favourites

### Locating contacts by name : `find`

Find contacts whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Contacts matching at least one keyword will be returned (i.e. `OR` search). e.g. `Hans Bo` will return `Hans Gruber`
  , `Bo Yang`

Examples:

* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Locating contacts by tag : `findtag`

Find contact with the given tag.

Format: `findtag KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `tag` will match `Tag`
* Only the tag is searched
* Only full words will be matched e.g. `Ta` will not match `Tag`
* List of contacts matching at least the searched tag\(s\) will be returned. e.g. `Tag1` will return `Contact` A with
  tags `Tag1` and `Tag2` will be returned.

Examples:

* `findtag Friends` returns contacts with tag `Friends`
* `findtag InProgress AlmostFinished` returns contacts tagged by at least both `InProgress` and `AlmostFinished`

### Deleting a contact : `delete`

Deletes the specified contact from the address book.

Format: `delete INDEX`

* Deletes the contact at the specified `INDEX`.
* The index refers to the index number shown in the displayed contact list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:

* `list` followed by `delete 2` deletes the 2nd contact in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st contact in the results of the `find` command.

### Adding deadlines to meet in relation to a contact : `deadline`

Creates a deadline that is placed under the profile of a contact.

Format: `deadline INDEX d/DESCRIPTION DATE [d/DESCRIPTION DATE]...`

- deadline must have description.
- The given date is added to the contact as deadline.
- Date should be dd/mm/yyyy

Example:

- `deadline 1 d/windows 01/01/2022` adds the description `windows` and date `01/01/2022` to the contact in index `1`.

### Add additional notes to a contact : `note`

Adds the given note under the contact.

Format: `note INDEX r/NOTES`

- Notes are displayed in a list.
- The given note is appended to the existing list of notes at the end.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
Notes store good-to-know information about the user. To classify contacts so that you can search for them, use tags instead.
</div>

Example:

`note 2 r/loves green` will create a note under the contact at index 2 that reads `loves green`

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

d'Intérieur data are saved in the hard disk automatically after any command that changes the data. There is no need to
save manually.

### Editing the data file

d'Intérieur data are saved as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to
update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, d'Intérieur will discard all data and start with an empty data file at the next run.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains
the data of your previous d'Intérieur home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action         | Format, Examples                                                                                                                                                                                                                                                      |
|----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**        | `add n/NAME p/PHONE_NUMBER e/EMAIL [a/ADDRESS] [t/TAG]…​` <br> e.g., `add n/Mary Jane p/12345678 e/maryJ@example.com a/Bukit Timah t/completed`                                                                                                                       |
| **Clear**      | `clear`                                                                                                                                                                                                                                                               |
| **Deadline**   | `deadline INDEX DATE`<br> e.g., `deadline 1 01/01/2022`                                                                                                                                                                                                               |
| **Delete**     | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                                                                                                                   |
| **Edit**       | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                                                                                                           |
| **Fav**        | `fav INDEX` <br> e.g., `fav 1`                                                                                                                                                                                                                                        |
| **Favourites** | `favourites`                                                                                                                                                                                                                                                          |
| **Find**       | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                                                                                                            |
| **Find Tag**   | `findtag KEYWORD [MORE_KEYWORDS}` <br> e.g., `findtag Friends`                                                                                                                                                                                                        |
| **Help**       | `help`                                                                                                                                                                                                                                                                |
| **List**       | `list`                                                                                                                                                                                                                                                                |
| **Note**       | `note INDEX r/NOTES`<br> e.g. `note 2 r/loves green`                                                                                                                                                                                                   |
