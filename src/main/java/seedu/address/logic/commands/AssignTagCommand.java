package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.DeadlineList;
import seedu.address.model.person.Email;
import seedu.address.model.person.Favourite;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Assigns a tag to a contact in the address book.
 */
public class AssignTagCommand extends Command {

    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a tag with a given tag name "
            + "(case-insensitive) and a given index of the contact in the address book.\n"
            + "Parameters: INDEX (must be a positive integer), TAGNAME"
            + "Example: " + COMMAND_WORD + " 1 friends";

    public static final String MESSAGE_SUCCESS = "Tag successfully assigned: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This tag was already assigned to this person previously";
    public static final String MESSAGE_UNKNOWN_TAG = "Tag '%1$s' has not been created yet.";

    private final String tagName;
    private final Index targetIndex;

    public AssignTagCommand(Index targetIndex, String tagName) {
        this.tagName = tagName;
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Tag checkingTag = new Tag(tagName);
        boolean tagHasBeenCreated = model.hasTag(checkingTag);

        if (!tagHasBeenCreated) {
            throw new CommandException(String.format(MESSAGE_UNKNOWN_TAG, tagName));
        }

        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        Set<Tag> personTags = personToEdit.getTags();
        Set<Tag> copyOfPersonTags = new HashSet<>(personTags);
        boolean personNotTagged = copyOfPersonTags.add(checkingTag);

        if (!personNotTagged) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }

        Person editedPerson = addTagToNewPerson(personToEdit, copyOfPersonTags);
        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedPerson));
    }

    private static Person addTagToNewPerson(Person personToEdit, Set<Tag> newTags) {
        requireNonNull(personToEdit);
        requireAllNonNull(newTags);
        assert personToEdit != null;
        assert newTags != null;

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        DeadlineList deadlines = personToEdit.getDeadlines();
        Notes notes = personToEdit.getNotes();
        Favourite favourite = personToEdit.getFavouriteStatus();

        return new Person(name, phone, email, address, deadlines, notes, newTags, favourite);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AssignTagCommand // instanceof handles nulls
                && tagName.equals(((AssignTagCommand) other).tagName)
                && targetIndex.equals(((AssignTagCommand) other).targetIndex));
    }
}
