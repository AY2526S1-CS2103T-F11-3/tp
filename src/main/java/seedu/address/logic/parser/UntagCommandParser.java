package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new UntagCommand object
 */
public class UntagCommandParser implements Parser<UntagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UntagCommand
     * and returns an UntagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UntagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_ID, PREFIX_TAG);

        // Check if student ID prefix is present
        if (argMultimap.getValue(PREFIX_STUDENT_ID).isPresent()) {
            // Parse as student ID
            StudentId studentId;
            try {
                studentId = ParserUtil.parseStudentId(argMultimap.getValue(PREFIX_STUDENT_ID).get());
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
            }

            // Check if tags are provided
            if (argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                throw new ParseException(UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
            }

            Set<Tag> tags;
            try {
                tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
            }

            return new UntagCommand(studentId, tags);
        } else {
            // Parse as index - validate it first
            Index index;
            try {
                index = ParserUtil.parseIndex(argMultimap.getPreamble());
            } catch (ParseException pe) {
                // If no tags provided and index is invalid, it's likely an empty/malformed command
                if (argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                    // Check if the input is completely empty
                    if (argMultimap.getPreamble().trim().isEmpty()) {
                        throw new ParseException(UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
                    }
                    // Otherwise it's a malformed command
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
                }
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
            }

            // Check if tags are provided
            if (argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                throw new ParseException(UntagCommand.MESSAGE_NO_TAGS_PROVIDED);
            }

            Set<Tag> tags;
            try {
                tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE), pe);
            }

            return new UntagCommand(index, tags);
        }
    }
}

