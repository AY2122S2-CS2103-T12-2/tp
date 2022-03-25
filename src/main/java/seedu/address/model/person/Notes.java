package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;

public class Notes {

    public static final String MESSAGE_CONSTRAINTS = "Notes can take any values, and it should not be blank";

    private static final char WHITESPACE = ' ';

    public final List<String> value;

    /**
     * Constructs a new {@code Notes}.
     */
    private Notes() {
        value = new ArrayList<>();
    }

    /**
     * Checks if the given note is a valid note that will be added.
     * A note is valid as long as it contains at least 1 character that
     * is not a whitespace.
     * @param noteToAdd note to check validity
     * @return true if string is valid, false otherwise
     */
    public static boolean isValidNote(String noteToAdd) {
        boolean isAllWhiteSpace = true;
        int i = 0;
        while (isAllWhiteSpace && i < noteToAdd.length()) {
            isAllWhiteSpace = noteToAdd.charAt(i) == WHITESPACE;
            i++;
        }
        return !isAllWhiteSpace;
    }

    /**
     * Create a fresh Notes for when a new contact is
     * created, or when notes for a contact are cleared.
     * @return Notes object with empty list
     */
    public static Notes getNewNotes() {
        return new Notes();
    }

    /**
     * Initialises a Notes object containing notes that
     * are in the List passed to it.
     * @param notes list of notes to initialise the Notes object with
     * @return Notes containing notes passed to it
     */
    public static Notes loadNotesFromList(List<String> notes) {
        Notes newNotes = getNewNotes();
        newNotes.value.addAll(notes);
        return newNotes;
    }

    /**
     * Creates a new Notes that has this Notes' contents and the given
     * new note appended.
     * @param newNote new note to be added
     * @return new Notes
     */
    public Notes updateNotes(String newNote) {
        Notes newNotes = new Notes();
        newNotes.value.addAll(this.value);
        newNotes.value.add(newNote);
        return newNotes;
    }

    /**
     * Produces a string representation of this Notes
     * object for printing to GUI.
     * @return list view of notes in this Notes object
     */
    public String listFormat() {
        if (value.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= value.size(); i++) {
            result.append(i).append(". ").append(value.get(i - 1)).append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Notes // instanceof handles nulls
                && value.equals(((Notes) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
