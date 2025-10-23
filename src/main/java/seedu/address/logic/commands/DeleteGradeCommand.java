package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRADE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.grade.Grade;
import seedu.address.model.person.Person;

/**
 * Deletes specific grades from a student.
 */
public class DeleteGradeCommand extends Command {

    public static final String COMMAND_WORD = "deletegrade";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes specific grades from the student "
            + "identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_GRADE + "ASSIGNMENT_NAME...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_GRADE + "Midterm "
            + PREFIX_GRADE + "Quiz1";

    public static final String MESSAGE_DELETE_GRADE_SUCCESS = "Deleted grades from Student: %1$s";
    public static final String MESSAGE_NOT_STUDENT = "The person at the specified index is not a student. "
            + "Grades can only be deleted from students.";
    public static final String MESSAGE_GRADE_NOT_FOUND = "Grade not found for assignment: %1$s";

    private final Index index;
    private final Set<String> assignmentNamesToDelete;

    /**
     * @param index of the person in the filtered person list to delete grades from
     * @param assignmentNamesToDelete names of assignments whose grades should be deleted
     */
    public DeleteGradeCommand(Index index, Set<String> assignmentNamesToDelete) {
        requireNonNull(index);
        requireNonNull(assignmentNamesToDelete);

        this.index = index;
        this.assignmentNamesToDelete = assignmentNamesToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if the person is a student
        if (personToEdit.getStudentId() == null) {
            throw new CommandException(MESSAGE_NOT_STUDENT);
        }

        // Check if all grades exist
        Set<String> existingAssignments = new HashSet<>();
        for (Grade grade : personToEdit.getGrades()) {
            existingAssignments.add(grade.assignmentName);
        }

        for (String assignmentName : assignmentNamesToDelete) {
            if (!existingAssignments.contains(assignmentName)) {
                throw new CommandException(String.format(MESSAGE_GRADE_NOT_FOUND, assignmentName));
            }
        }

        Person updatedPerson = createPersonWithDeletedGrades(personToEdit, assignmentNamesToDelete);

        model.setPerson(personToEdit, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_GRADE_SUCCESS, Messages.format(updatedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the specified grades removed.
     */
    private static Person createPersonWithDeletedGrades(Person person, Set<String> assignmentNamesToDelete) {
        assert person != null;

        Set<Grade> updatedGrades = new HashSet<>();
        for (Grade grade : person.getGrades()) {
            if (!assignmentNamesToDelete.contains(grade.assignmentName)) {
                updatedGrades.add(grade);
            }
        }

        // Use student constructor if phone/address are null
        if (person.getPhone() == null || person.getAddress() == null) {
            return new Person(person.getName(), person.getStudentId(), person.getEmail(),
                    person.getModuleCodes(), person.getTags(), updatedGrades);
        } else {
            return new Person(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), person.getTags(), person.getStudentId(),
                    person.getModuleCodes(), updatedGrades);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteGradeCommand)) {
            return false;
        }

        DeleteGradeCommand otherCommand = (DeleteGradeCommand) other;
        return index.equals(otherCommand.index)
                && assignmentNamesToDelete.equals(otherCommand.assignmentNamesToDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("assignmentNamesToDelete", assignmentNamesToDelete)
                .toString();
    }
}
