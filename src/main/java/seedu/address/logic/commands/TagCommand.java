package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;

/**
 * Adds tags to an existing person in the address book.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to the person identified "
            + "by the index number used in the displayed person list or by their student ID. "
            + "The tags will be added to the existing tags.\n"
            + "Parameters: INDEX (must be a positive integer) or " + PREFIX_STUDENT_ID + "STUDENT_ID "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "Struggling " + PREFIX_TAG + "Inactive\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STUDENT_ID + "A0291772W "
            + PREFIX_TAG + "Excelling";

    public static final String MESSAGE_TAG_PERSON_SUCCESS = "Added tags to Person: %1$s";
    public static final String MESSAGE_NO_TAGS_PROVIDED = "At least one tag must be provided.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with student ID: %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "Some tags already exist on this person and were not added.";

    private final Index index;
    private final StudentId studentId;
    private final Set<Tag> tagsToAdd;

    /**
     * Creates a TagCommand to add tags to the person at the specified {@code Index}.
     *
     * @param index of the person in the filtered person list
     * @param tagsToAdd tags to add to the person
     */
    public TagCommand(Index index, Set<Tag> tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.studentId = null;
        this.tagsToAdd = tagsToAdd;
    }

    /**
     * Creates a TagCommand to add tags to the person with the specified {@code StudentId}.
     *
     * @param studentId of the person
     * @param tagsToAdd tags to add to the person
     */
    public TagCommand(StudentId studentId, Set<Tag> tagsToAdd) {
        requireNonNull(studentId);
        requireNonNull(tagsToAdd);

        this.index = null;
        this.studentId = studentId;
        this.tagsToAdd = tagsToAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToTag;

        if (index != null) {
            // Tag by index
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            personToTag = lastShownList.get(index.getZeroBased());
        } else {
            // Tag by student ID
            personToTag = model.findPersonByStudentId(studentId);

            if (personToTag == null) {
                throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, studentId));
            }
        }

        Person taggedPerson = createTaggedPerson(personToTag, tagsToAdd);

        model.setPerson(personToTag, taggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_TAG_PERSON_SUCCESS, Messages.format(taggedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the tags from {@code tagsToAdd} added to
     * the existing tags of {@code personToTag}.
     */
    private static Person createTaggedPerson(Person personToTag, Set<Tag> tagsToAdd) {
        assert personToTag != null;

        // Combine existing tags with new tags
        Set<Tag> updatedTags = new HashSet<>(personToTag.getTags());
        updatedTags.addAll(tagsToAdd);

        // Check if this is a student (has studentId but no phone/address)
        if (personToTag.getStudentId() != null && personToTag.getPhone() == null
                && personToTag.getAddress() == null) {
            // Use student constructor
            return new Person(personToTag.getName(), personToTag.getStudentId(),
                    personToTag.getEmail(), personToTag.getModuleCodes(), updatedTags, personToTag.getGrades());
        } else {
            // Use regular person constructor
            return new Person(personToTag.getName(), personToTag.getPhone(), personToTag.getEmail(),
                    personToTag.getAddress(), updatedTags, personToTag.getStudentId(),
                    personToTag.getModuleCodes(), personToTag.getGrades());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherTagCommand = (TagCommand) other;

        // Both use index
        if (index != null && otherTagCommand.index != null) {
            return index.equals(otherTagCommand.index)
                    && tagsToAdd.equals(otherTagCommand.tagsToAdd);
        }

        // Both use student ID
        if (studentId != null && otherTagCommand.studentId != null) {
            return studentId.equals(otherTagCommand.studentId)
                    && tagsToAdd.equals(otherTagCommand.tagsToAdd);
        }

        return false;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        if (index != null) {
            builder.add("index", index);
        }
        if (studentId != null) {
            builder.add("studentId", studentId);
        }
        builder.add("tagsToAdd", tagsToAdd);
        return builder.toString();
    }
}

