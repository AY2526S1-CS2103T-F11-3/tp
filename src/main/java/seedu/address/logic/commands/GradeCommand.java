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
 * Adds grades to an existing student in the address book.
 */
public class GradeCommand extends Command {

    public static final String COMMAND_WORD = "grade";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds grades to the student identified "
            + "by the index number used in the displayed person list. "
            + "Grades will be added to the student's existing grades.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_GRADE + "ASSIGNMENT_NAME:SCORE...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_GRADE + "Midterm:85 "
            + PREFIX_GRADE + "Assignment1:92";

    public static final String MESSAGE_ADD_GRADE_SUCCESS = "Added grades to Student: %1$s";
    public static final String MESSAGE_NOT_STUDENT = "The person at the specified index is not a student. "
            + "Grades can only be added to students.";
    public static final String MESSAGE_DUPLICATE_GRADE = "This student already has a grade for assignment: %1$s";

    private final Index index;
    private final Set<Grade> gradesToAdd;

    /**
     * @param index of the person in the filtered person list to add grades to
     * @param gradesToAdd grades to be added to the person
     */
    public GradeCommand(Index index, Set<Grade> gradesToAdd) {
        requireNonNull(index);
        requireNonNull(gradesToAdd);

        this.index = index;
        this.gradesToAdd = gradesToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if the person is a student (has a student ID)
        if (personToEdit.getStudentId() == null) {
            throw new CommandException(MESSAGE_NOT_STUDENT);
        }

        // Check for duplicate grades (same assignment name)
        Set<String> existingAssignments = new HashSet<>();
        for (Grade existingGrade : personToEdit.getGrades()) {
            existingAssignments.add(existingGrade.assignmentName);
        }

        for (Grade newGrade : gradesToAdd) {
            if (existingAssignments.contains(newGrade.assignmentName)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_GRADE, newGrade.assignmentName));
            }
        }

        Person gradedPerson = createGradedPerson(personToEdit, gradesToAdd);

        model.setPerson(personToEdit, gradedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_GRADE_SUCCESS, Messages.format(gradedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the grades added.
     */
    private static Person createGradedPerson(Person personToEdit, Set<Grade> gradesToAdd) {
        assert personToEdit != null;

        Set<Grade> updatedGrades = new HashSet<>(personToEdit.getGrades());
        updatedGrades.addAll(gradesToAdd);

        // Create a new person with the updated grades
        // Use student constructor if phone/address are null
        if (personToEdit.getPhone() == null || personToEdit.getAddress() == null) {
            return new Person(personToEdit.getName(), personToEdit.getStudentId(), personToEdit.getEmail(),
                    personToEdit.getModuleCodes(), personToEdit.getTags(), updatedGrades);
        } else {
            return new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), personToEdit.getTags(), personToEdit.getStudentId(),
                    personToEdit.getModuleCodes(), updatedGrades);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GradeCommand)) {
            return false;
        }

        GradeCommand otherGradeCommand = (GradeCommand) other;
        return index.equals(otherGradeCommand.index)
                && gradesToAdd.equals(otherGradeCommand.gradesToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("gradesToAdd", gradesToAdd)
                .toString();
    }
}
