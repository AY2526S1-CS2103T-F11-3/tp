package seedu.address.model.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GradeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Grade(null, "85"));
        assertThrows(NullPointerException.class, () -> new Grade("Midterm", null));
    }

    @Test
    public void constructor_invalidScore_throwsIllegalArgumentException() {
        String validAssignment = "Midterm";
        String invalidScore = "150"; // Above 100
        assertThrows(IllegalArgumentException.class, () -> new Grade(validAssignment, invalidScore));
    }

    @Test
    public void constructor_invalidScoreNegative_throwsIllegalArgumentException() {
        String validAssignment = "Midterm";
        String invalidScore = "-10";
        assertThrows(IllegalArgumentException.class, () -> new Grade(validAssignment, invalidScore));
    }

    @Test
    public void constructor_invalidScoreFormat_throwsIllegalArgumentException() {
        String validAssignment = "Midterm";
        String invalidScore = "abc";
        assertThrows(IllegalArgumentException.class, () -> new Grade(validAssignment, invalidScore));
    }

    @Test
    public void constructor_emptyAssignmentName_throwsIllegalArgumentException() {
        String emptyAssignment = "";
        String validScore = "85";
        assertThrows(IllegalArgumentException.class, () -> new Grade(emptyAssignment, validScore));
    }

    @Test
    public void constructor_blankAssignmentName_throwsIllegalArgumentException() {
        String blankAssignment = "   ";
        String validScore = "85";
        assertThrows(IllegalArgumentException.class, () -> new Grade(blankAssignment, validScore));
    }

    @Test
    public void isValidGrade() {
        // null score
        assertThrows(NullPointerException.class, () -> Grade.isValidGrade(null));

        // invalid scores
        assertFalse(Grade.isValidGrade("")); // empty string
        assertFalse(Grade.isValidGrade(" ")); // spaces only
        assertFalse(Grade.isValidGrade("101")); // above 100
        assertFalse(Grade.isValidGrade("-1")); // negative
        assertFalse(Grade.isValidGrade("abc")); // non-numeric
        assertFalse(Grade.isValidGrade("85.5")); // decimal

        // valid scores
        assertTrue(Grade.isValidGrade("0")); // minimum
        assertTrue(Grade.isValidGrade("100")); // maximum
        assertTrue(Grade.isValidGrade("50")); // typical
        assertTrue(Grade.isValidGrade("5")); // single digit
    }

    @Test
    public void isValidAssignmentName() {
        // null assignment name
        assertFalse(Grade.isValidAssignmentName(null));

        // invalid assignment names
        assertFalse(Grade.isValidAssignmentName("")); // empty string
        assertFalse(Grade.isValidAssignmentName("   ")); // spaces only

        // valid assignment names
        assertTrue(Grade.isValidAssignmentName("Midterm"));
        assertTrue(Grade.isValidAssignmentName("Assignment 1"));
        assertTrue(Grade.isValidAssignmentName("Quiz-2"));
        assertTrue(Grade.isValidAssignmentName("Final Exam"));
    }

    @Test
    public void equals() {
        Grade grade = new Grade("Midterm", "85");

        // same values -> returns true
        assertTrue(grade.equals(new Grade("Midterm", "85")));

        // same object -> returns true
        assertTrue(grade.equals(grade));

        // null -> returns false
        assertFalse(grade.equals(null));

        // different type -> returns false
        assertFalse(grade.equals(5.0f));

        // different assignment name -> returns false
        assertFalse(grade.equals(new Grade("Final", "85")));

        // different score -> returns false
        assertFalse(grade.equals(new Grade("Midterm", "90")));
    }

    @Test
    public void toStringMethod() {
        Grade grade = new Grade("Midterm", "85");
        String expected = "Midterm: 85";
        assertEquals(expected, grade.toString());
    }

    @Test
    public void hashCodeMethod() {
        Grade grade1 = new Grade("Midterm", "85");
        Grade grade2 = new Grade("Midterm", "85");
        Grade grade3 = new Grade("Final", "90");

        // same values -> same hash code
        assertEquals(grade1.hashCode(), grade2.hashCode());

        // different values -> likely different hash code
        assertFalse(grade1.hashCode() == grade3.hashCode());
    }

    @Test
    public void constructor_trimsWhitespace() {
        Grade grade = new Grade("  Midterm  ", "  85  ");
        assertEquals("Midterm", grade.assignmentName);
        assertEquals("85", grade.score);
    }
}
