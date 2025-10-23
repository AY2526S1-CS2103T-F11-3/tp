package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.grade.Grade;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for GradeCommand.
 */
public class GradeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addGradeUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Grade> gradesToAdd = new HashSet<>();
        gradesToAdd.add(new Grade("Midterm", "85"));

        GradeCommand gradeCommand = new GradeCommand(INDEX_FIRST_PERSON, gradesToAdd);

        Person gradedPerson = new PersonBuilder(firstPerson).withGrades("Midterm:85").build();

        String expectedMessage = String.format(GradeCommand.MESSAGE_ADD_GRADE_SUCCESS,
                Messages.format(gradedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, gradedPerson);

        assertCommandSuccess(gradeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleGrades_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Grade> gradesToAdd = new HashSet<>();
        gradesToAdd.add(new Grade("Midterm", "85"));
        gradesToAdd.add(new Grade("Quiz1", "90"));

        GradeCommand gradeCommand = new GradeCommand(INDEX_FIRST_PERSON, gradesToAdd);

        Person gradedPerson = new PersonBuilder(firstPerson).withGrades("Midterm:85", "Quiz1:90").build();

        String expectedMessage = String.format(GradeCommand.MESSAGE_ADD_GRADE_SUCCESS,
                Messages.format(gradedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, gradedPerson);

        assertCommandSuccess(gradeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Grade> gradesToAdd = new HashSet<>();
        gradesToAdd.add(new Grade("Midterm", "85"));

        GradeCommand gradeCommand = new GradeCommand(outOfBoundIndex, gradesToAdd);

        assertCommandFailure(gradeCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateGrade_throwsCommandException() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // First, add a grade
        Set<Grade> initialGrades = new HashSet<>();
        initialGrades.add(new Grade("Midterm", "85"));
        GradeCommand firstGradeCommand = new GradeCommand(INDEX_FIRST_PERSON, initialGrades);

        try {
            firstGradeCommand.execute(model);
        } catch (Exception e) {
            // Should not throw
        }

        // Try to add another grade with the same assignment name
        Set<Grade> duplicateGrades = new HashSet<>();
        duplicateGrades.add(new Grade("Midterm", "90"));
        GradeCommand secondGradeCommand = new GradeCommand(INDEX_FIRST_PERSON, duplicateGrades);

        assertCommandFailure(secondGradeCommand, model,
                String.format(GradeCommand.MESSAGE_DUPLICATE_GRADE, "Midterm"));
    }

    @Test
    public void equals() {
        Set<Grade> grades1 = new HashSet<>();
        grades1.add(new Grade("Midterm", "85"));

        Set<Grade> grades2 = new HashSet<>();
        grades2.add(new Grade("Final", "90"));

        GradeCommand gradeFirstCommand = new GradeCommand(INDEX_FIRST_PERSON, grades1);
        GradeCommand gradeSecondCommand = new GradeCommand(INDEX_SECOND_PERSON, grades1);
        GradeCommand gradeDifferentGradesCommand = new GradeCommand(INDEX_FIRST_PERSON, grades2);

        // same object -> returns true
        assertTrue(gradeFirstCommand.equals(gradeFirstCommand));

        // same values -> returns true
        GradeCommand gradeFirstCommandCopy = new GradeCommand(INDEX_FIRST_PERSON, grades1);
        assertTrue(gradeFirstCommand.equals(gradeFirstCommandCopy));

        // different types -> returns false
        assertFalse(gradeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(gradeFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(gradeFirstCommand.equals(gradeSecondCommand));

        // different grades -> returns false
        assertFalse(gradeFirstCommand.equals(gradeDifferentGradesCommand));
    }

    @Test
    public void toStringMethod() {
        Set<Grade> grades = new HashSet<>();
        grades.add(new Grade("Midterm", "85"));
        GradeCommand gradeCommand = new GradeCommand(INDEX_FIRST_PERSON, grades);
        String expected = GradeCommand.class.getCanonicalName() + "{index=" + INDEX_FIRST_PERSON
                + ", gradesToAdd=" + grades + "}";
        assertEquals(expected, gradeCommand.toString());
    }
}
