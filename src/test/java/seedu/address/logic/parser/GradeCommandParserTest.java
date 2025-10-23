package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.GradeCommand;
import seedu.address.model.grade.Grade;

public class GradeCommandParserTest {

    private GradeCommandParser parser = new GradeCommandParser();

    @Test
    public void parse_validArgs_returnsGradeCommand() {
        Set<Grade> expectedGrades = new HashSet<>();
        expectedGrades.add(new Grade("Midterm", "85"));

        assertParseSuccess(parser, "1 g/Midterm:85",
                new GradeCommand(INDEX_FIRST_PERSON, expectedGrades));
    }

    @Test
    public void parse_multipleGrades_returnsGradeCommand() {
        Set<Grade> expectedGrades = new HashSet<>();
        expectedGrades.add(new Grade("Midterm", "85"));
        expectedGrades.add(new Grade("Quiz1", "90"));

        assertParseSuccess(parser, "1 g/Midterm:85 g/Quiz1:90",
                new GradeCommand(INDEX_FIRST_PERSON, expectedGrades));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a g/Midterm:85",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertParseFailure(parser, "g/Midterm:85",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingGrade_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, GradeCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidGradeFormat_throwsParseException() {
        // Missing colon
        assertParseFailure(parser, "1 g/Midterm85",
                "Grade format should be ASSIGNMENT_NAME:SCORE");
    }

    @Test
    public void parse_invalidScore_throwsParseException() {
        // Score above 100
        assertParseFailure(parser, "1 g/Midterm:150",
                Grade.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_negativeScore_throwsParseException() {
        assertParseFailure(parser, "1 g/Midterm:-10",
                Grade.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonNumericScore_throwsParseException() {
        assertParseFailure(parser, "1 g/Midterm:abc",
                Grade.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyAssignmentName_throwsParseException() {
        assertParseFailure(parser, "1 g/:85",
                "Assignment name should not be blank");
    }

    @Test
    public void parse_blankAssignmentName_throwsParseException() {
        assertParseFailure(parser, "1 g/   :85",
                "Assignment name should not be blank");
    }
}
