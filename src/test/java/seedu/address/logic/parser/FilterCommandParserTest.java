package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonHasAllTagsPredicate;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noTags_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // one tag
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        FilterCommand expectedFilterCommand = new FilterCommand(new PersonHasAllTagsPredicate(tagSet));
        assertParseSuccess(parser, " t/friends", expectedFilterCommand);

        // multiple tags
        tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        tagSet.add(new Tag("owesMoney"));
        expectedFilterCommand = new FilterCommand(new PersonHasAllTagsPredicate(tagSet));
        assertParseSuccess(parser, " t/friends t/owesMoney", expectedFilterCommand);

        // multiple tags with extra whitespaces
        assertParseSuccess(parser, " \n t/friends \n \t t/owesMoney  \t", expectedFilterCommand);
    }

    @Test
    public void parse_invalidTagFormat_throwsParseException() {
        // invalid tag name (contains non-alphanumeric characters)
        assertParseFailure(parser, " t/friend@", Tag.MESSAGE_CONSTRAINTS);

        // invalid tag name (contains spaces)
        assertParseFailure(parser, " t/close friend", Tag.MESSAGE_CONSTRAINTS);
    }
}

