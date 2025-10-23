package seedu.address.model.grade;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Grade for an assignment in TeachMate.
 * Guarantees: immutable; is valid as declared in {@link #isValidGrade(String)}
 */
public class Grade {
    public static final String MESSAGE_CONSTRAINTS =
            "Grades should be a number between 0 and 100";

    public static final String VALIDATION_REGEX = "^(100|[0-9]{1,2})$";

    public final String assignmentName;
    public final String score;

    /**
     * Constructs a {@code Grade}.
     *
     * @param assignmentName A valid assignment name.
     * @param score A valid score between 0 and 100.
     */
    public Grade(String assignmentName, String score) {
        requireNonNull(assignmentName);
        requireNonNull(score);
        String trimmedAssignmentName = assignmentName.trim();
        String trimmedScore = score.trim();
        checkArgument(isValidGrade(trimmedScore), MESSAGE_CONSTRAINTS);
        checkArgument(isValidAssignmentName(trimmedAssignmentName), "Assignment name should not be blank");
        this.assignmentName = trimmedAssignmentName;
        this.score = trimmedScore;
    }

    /**
     * Returns true if a given string is a valid grade.
     */
    public static boolean isValidGrade(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid assignment name.
     */
    public static boolean isValidAssignmentName(String test) {
        return test != null && !test.trim().isEmpty();
    }

    @Override
    public String toString() {
        return assignmentName + ": " + score;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Grade)) {
            return false;
        }

        Grade otherGrade = (Grade) other;
        return assignmentName.equals(otherGrade.assignmentName)
                && score.equals(otherGrade.score);
    }

    @Override
    public int hashCode() {
        return assignmentName.hashCode() + score.hashCode();
    }
}
