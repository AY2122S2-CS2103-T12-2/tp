package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;


/**
 * Updates the notes of a person
 */
public class NoteCommand extends Command {
    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds the given note to the person identified "
            + "by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NOTE + "NOTES\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NOTE + "Likes to swim.";

    public static final String MESSAGE_UPDATE_NOTE_SUCCESS = "Added note to Person: %1$s";

    public static final String MESSAGE_NO_UPDATE_TO_NOTES = "No note added to Person: %1$s";

    private final Index index;
    private final String note;

    /**
     * @param index index of the person in the filtered list to edit
     * @param note new note to update with
     */
    public NoteCommand(Index index, String note) {
        requireAllNonNull(index, note);

        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        if (!Notes.isValidNote(note)) {
            return new CommandResult(generateNoChangeMessage(personToEdit));
        }
        Person editedPerson = updateNotes(personToEdit, note);
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Creates the message to be printed for the result of this NoteCommand
     * @param personToEdit the person edited by this NoteCommand
     * @return message to be printed
     */
    public String generateSuccessMessage(Person personToEdit) {
        return String.format(MESSAGE_UPDATE_NOTE_SUCCESS, personToEdit);
    }

    public String generateNoChangeMessage(Person person) {
        return String.format(MESSAGE_NO_UPDATE_TO_NOTES, person);
    }

    /**
     * Creates new Person object with the note added
     * @param personToEdit person to add note to
     * @param note note to add
     * @return new Person with note added
     */
    private static Person updateNotes(Person personToEdit, String note) {
        Notes oldNotes = personToEdit.getNotes();
        Notes newNotes = oldNotes.updateNotes(note);
        return new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getDeadlines(), newNotes,
                personToEdit.getTags(), personToEdit.getFavouriteStatus());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand e = (NoteCommand) other;
        return index.equals(e.index)
                && note.equals(e.note);
    }
}
