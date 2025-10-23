package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.grade.Grade;

/**
 * Jackson-friendly version of {@link Grade}.
 */
class JsonAdaptedGrade {
    private final String assignmentName;
    private final String score;

    /**
     * Constructs a {@code JsonAdaptedGrade} with the given grade details.
     */
    @JsonCreator
    public JsonAdaptedGrade(@JsonProperty("assignmentName") String assignmentName,
                            @JsonProperty("score") String score) {
        this.assignmentName = assignmentName;
        this.score = score;
    }

    /**
     * Converts a given {@code Grade} into this class for Jackson use.
     */
    public JsonAdaptedGrade(Grade source) {
        assignmentName = source.assignmentName;
        score = source.score;
    }

    /**
     * Converts this Jackson-friendly adapted grade object into the model's {@code Grade} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted grade.
     */
    public Grade toModelType() throws IllegalValueException {
        if (assignmentName == null || assignmentName.trim().isEmpty()) {
            throw new IllegalValueException("Assignment name should not be blank");
        }
        if (!Grade.isValidGrade(score)) {
            throw new IllegalValueException(Grade.MESSAGE_CONSTRAINTS);
        }
        return new Grade(assignmentName, score);
    }
}
