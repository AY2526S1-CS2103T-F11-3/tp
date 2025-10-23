package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for TagCommandParser.
 */
public class TagCommandParserTest {

    private TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validIndexWithSingleTag_returnsTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("NewTag"));
        assertParseSuccess(parser, "1 t/NewTag", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_validIndexWithMultipleTags_returnsTagCommand() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Struggling"));
        tags.add(new Tag("Inactive"));
        assertParseSuccess(parser, "1 t/Struggling t/Inactive", new TagCommand(INDEX_FIRST_PERSON, tags));
    }

    @Test
    public void parse_validStudentIdWithSingleTag_returnsTagCommand() throws Exception {
        StudentId studentId = new StudentId("A0291772W");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Excelling"));
        assertParseSuccess(parser, " s/A0291772W t/Excelling", new TagCommand(studentId, tags));
    }

    @Test
    public void parse_validStudentIdWithMultipleTags_returnsTagCommand() throws Exception {
        StudentId studentId = new StudentId("A0291772W");
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Tag1"));
        tags.add(new Tag("Tag2"));
        tags.add(new Tag("Tag3"));
        assertParseSuccess(parser, " s/A0291772W t/Tag1 t/Tag2 t/Tag3", new TagCommand(studentId, tags));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "a t/NewTag", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStudentId_throwsParseException() {
        assertParseFailure(parser, " s/invalid t/NewTag", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noTags_throwsParseException() {
        assertParseFailure(parser, "1", TagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_noTagsWithStudentId_throwsParseException() {
        assertParseFailure(parser, " s/A0291772W", TagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "", TagCommand.MESSAGE_NO_TAGS_PROVIDED);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "invalid args", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertParseFailure(parser, "1 t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagCommand.MESSAGE_USAGE));
    }
}

