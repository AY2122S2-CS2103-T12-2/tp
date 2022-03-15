package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Deadline;
import seedu.address.model.person.Email;
import seedu.address.model.person.Favourite;
import seedu.address.model.person.Name;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class FavouriteCommand extends Command {
    public static final String COMMAND_WORD = "fav";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Favourites the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_FAVOURITE_PERSON_SUCCESS = "Changed Person's Favourite Status: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to favourite.
     */
    public FavouriteCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
    }

    /**
     * Executes the Favourite command based on the {@code targetIndex} given.
     *
     * @param model {@code Model} which the command should operate on.
     * @return the result of the command.
     * @throws CommandException if the index given is out of bounds.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToFavourite = lastShownList.get(targetIndex.getZeroBased());
        Favourite newFavouriteStatus = personToFavourite.getFavouriteStatus().equals(Favourite.IS_FAVOURITE)
                ? Favourite.NOT_FAVOURITE
                : Favourite.IS_FAVOURITE;
        Person editedPerson = createFavouritedPerson(personToFavourite, newFavouriteStatus);

        if (!personToFavourite.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToFavourite, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_FAVOURITE_PERSON_SUCCESS, editedPerson));
    }

    private static Person createFavouritedPerson(Person personToEdit, Favourite newFavouriteStatus) {
        assert personToEdit != null;
        assert newFavouriteStatus != null;

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        Deadline deadline = personToEdit.getDeadline();
        Notes notes = personToEdit.getNotes();
        Set<Tag> tags = personToEdit.getTags();

        return new Person(name, phone, email, address, deadline, notes, tags, newFavouriteStatus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FavouriteCommand)) {
            return false;
        }

        FavouriteCommand e = (FavouriteCommand) other;
        return targetIndex.equals(e.targetIndex);
    }
}
