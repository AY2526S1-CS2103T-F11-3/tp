package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UntagCommand;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for UntagCommandParser.
 */
public class UntagCommandParserTest {

    private UntagCommandParser parser = new UntagCommandParser();

    @Test
    public void parse_validIndexWithSingleTag_returnsUntagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("TestTag"));
        assertParseSuccess(parser, "1 t/TestTag", new UntagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_validIndexWithMultipleTags_returnsUntagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Struggling"));
        tags.add(new Tag("Inactive"));
        assertParseSuccess(parser, "1 t/Struggling t/Inactive", new UntagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_validStudentIdWithSingleTag_returnsUntagCommand() throws Exception {
        StudentId studentId = new StudentId("A0291772W");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Struggling"));
        assertParseSuccess(parser, " s/A0291772W t/Struggling", new UntagCommand(studentId, tags));
    }

    @Test
    public void parse_validStudentIdWithMultipleTags_returnsUntagCommand() throws Exception {
        StudentId studentId = new StudentId("A0291772W");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Tag1"));
        tags.add(new Tag("Tag2"));
        tags.add(new Tag("Tag3"));
        assertParseSuccess(parser, " s/A0291772W t/Tag1 t/Tag2 t/Tag3", new UntagCommand(studentId, tags));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a t/TestTag", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStudentId_throwsParseException() {
        assertParseFailure(parser, " s/invalid t/TestTag", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noTags_throwsParseException() {
        assertParseFailure(parser, "1", UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_noTagsWithStudentId_throwsParseException() {
        assertParseFailure(parser, " s/A0291772W", UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "", UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "invalid args", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertParseFailure(parser, "1 t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UntagCommand.MESSAGE_USAGE));
    }
}

