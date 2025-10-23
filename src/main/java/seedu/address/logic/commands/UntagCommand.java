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
 * Removes tags from an existing person in the address book.
 */
public class UntagCommand extends Command {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes tags from the person identified "
            + "by the index number used in the displayed person list or by their student ID. "
            + "The specified tags will be removed from the existing tags.\n"
            + "Parameters: INDEX (must be a positive integer) or " + PREFIX_STUDENT_ID + "STUDENT_ID "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_TAG + "Struggling " + PREFIX_TAG + "Inactive\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_STUDENT_ID + "A0291772W "
            + PREFIX_TAG + "Struggling";

    public static final String MESSAGE_UNTAG_PERSON_SUCCESS = "Removed tags from Person: %1$s";
    public static final String MESSAGE_NO_TAGS_PROVIDED = "At least one tag must be provided.";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with student ID: %1$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "Some tags do not exist on this person: %1$s";

    private final Index index;
    private final StudentId studentId;
    private final Set<Tag> tagsToRemove;

    /**
     * Creates an UntagCommand to remove tags from the person at the specified {@code Index}.
     *
     * @param index of the person in the filtered person list
     * @param tagsToRemove tags to remove from the person
     */
    public UntagCommand(Index index, Set<Tag> tagsToRemove) {
        requireNonNull(index);
        requireNonNull(tagsToRemove);

        this.index = index;
        this.studentId = null;
        this.tagsToRemove = tagsToRemove;
    }

    /**
     * Creates an UntagCommand to remove tags from the person with the specified {@code StudentId}.
     *
     * @param studentId of the person
     * @param tagsToRemove tags to remove from the person
     */
    public UntagCommand(StudentId studentId, Set<Tag> tagsToRemove) {
        requireNonNull(studentId);
        requireNonNull(tagsToRemove);

        this.index = null;
        this.studentId = studentId;
        this.tagsToRemove = tagsToRemove;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToUntag;

        if (index != null) {
            // Untag by index
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            personToUntag = lastShownList.get(index.getZeroBased());
        } else {
            // Untag by student ID
            personToUntag = model.findPersonByStudentId(studentId);

            if (personToUntag == null) {
                throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, studentId));
            }
        }

        // Check if all tags to remove exist on the person
        Set<Tag> currentTags = personToUntag.getTags();
        Set<Tag> missingTags = new HashSet<>();
        for (Tag tag : tagsToRemove) {
            if (!currentTags.contains(tag)) {
                missingTags.add(tag);
            }
        }

        if (!missingTags.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, missingTags));
        }

        Person untaggedPerson = createUntaggedPerson(personToUntag, tagsToRemove);

        model.setPerson(personToUntag, untaggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UNTAG_PERSON_SUCCESS, Messages.format(untaggedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the tags from {@code tagsToRemove} removed from
     * the existing tags of {@code personToUntag}.
     */
    private static Person createUntaggedPerson(Person personToUntag, Set<Tag> tagsToRemove) {
        assert personToUntag != null;

        // Remove specified tags from existing tags
        Set<Tag> updatedTags = new HashSet<>(personToUntag.getTags());
        updatedTags.removeAll(tagsToRemove);

        // Check if this is a student (has studentId but no phone/address)
        if (personToUntag.getStudentId() != null && personToUntag.getPhone() == null
                && personToUntag.getAddress() == null) {
            // Use student constructor
            return new Person(personToUntag.getName(), personToUntag.getStudentId(),
                    personToUntag.getEmail(), personToUntag.getModuleCodes(), updatedTags, personToUntag.getGrades());
        } else {
            // Use regular person constructor
            return new Person(personToUntag.getName(), personToUntag.getPhone(), personToUntag.getEmail(),
                    personToUntag.getAddress(), updatedTags, personToUntag.getStudentId(),
                    personToUntag.getModuleCodes(), personToUntag.getGrades());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UntagCommand)) {
            return false;
        }

        UntagCommand otherUntagCommand = (UntagCommand) other;

        // Both use index
        if (index != null && otherUntagCommand.index != null) {
            return index.equals(otherUntagCommand.index)
                    && tagsToRemove.equals(otherUntagCommand.tagsToRemove);
        }

        // Both use student ID
        if (studentId != null && otherUntagCommand.studentId != null) {
            return studentId.equals(otherUntagCommand.studentId)
                    && tagsToRemove.equals(otherUntagCommand.tagsToRemove);
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
        builder.add("tagsToRemove", tagsToRemove);
        return builder.toString();
    }
}

