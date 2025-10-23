package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UntagCommand.
 */
public class UntagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_removeTagByIndexUnfilteredList_success() throws Exception {
        // First add a tag to remove
        Person personToUntag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("TestTag"));

        Set<Tag> updatedTagsWithNew = new HashSet<>(personToUntag.getTags());
        updatedTagsWithNew.addAll(tagsToAdd);

        Person personWithTag;
        if (personToUntag.getStudentId() != null && personToUntag.getPhone() == null
                && personToUntag.getAddress() == null) {
            personWithTag = new Person(personToUntag.getName(), personToUntag.getStudentId(),
                    personToUntag.getEmail(), personToUntag.getModuleCodes(), updatedTagsWithNew,
                    personToUntag.getGrades());
        } else {
            personWithTag = new Person(personToUntag.getName(), personToUntag.getPhone(), personToUntag.getEmail(),
                    personToUntag.getAddress(), updatedTagsWithNew, personToUntag.getStudentId(),
                    personToUntag.getModuleCodes(), personToUntag.getGrades());
        }

        model.setPerson(personToUntag, personWithTag);

        // Now remove the tag
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("TestTag"));

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        Set<Tag> expectedTags = new HashSet<>(personWithTag.getTags());
        expectedTags.removeAll(tagsToRemove);

        Person expectedPerson;
        if (personWithTag.getStudentId() != null && personWithTag.getPhone() == null
                && personWithTag.getAddress() == null) {
            expectedPerson = new Person(personWithTag.getName(), personWithTag.getStudentId(),
                    personWithTag.getEmail(), personWithTag.getModuleCodes(), expectedTags,
                    personWithTag.getGrades());
        } else {
            expectedPerson = new Person(personWithTag.getName(), personWithTag.getPhone(), personWithTag.getEmail(),
                    personWithTag.getAddress(), expectedTags, personWithTag.getStudentId(),
                    personWithTag.getModuleCodes(), personWithTag.getGrades());
        }

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_PERSON_SUCCESS,
                Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithTag, expectedPerson);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeNonExistentTag_throwsCommandException() throws Exception {
        Person personToUntag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("NonExistentTag"));

        UntagCommand untagCommand = new UntagCommand(INDEX_FIRST_PERSON, tagsToRemove);

        assertCommandFailure(untagCommand, model,
                String.format(UntagCommand.MESSAGE_TAG_NOT_FOUND, tagsToRemove));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("TestTag"));

        UntagCommand untagCommand = new UntagCommand(outOfBoundIndex, tagsToRemove);

        assertCommandFailure(untagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_removeTagByValidStudentId_success() throws Exception {
        Person studentToUntag = new PersonBuilder().withName("Test Student")
                .withEmail("test@example.com")
                .withStudentId("A9876543Z")
                .withModuleCode("CS2103T")
                .withTags("ExistingTag")
                .build();
        model.addPerson(studentToUntag);

        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("ExistingTag"));

        StudentId studentId = new StudentId("A9876543Z");
        UntagCommand untagCommand = new UntagCommand(studentId, tagsToRemove);

        Set<Tag> expectedTags = new HashSet<>(studentToUntag.getTags());
        expectedTags.removeAll(tagsToRemove);

        Person expectedPerson;
        if (studentToUntag.getStudentId() != null && studentToUntag.getPhone() == null
                && studentToUntag.getAddress() == null) {
            expectedPerson = new Person(studentToUntag.getName(), studentToUntag.getStudentId(),
                    studentToUntag.getEmail(), studentToUntag.getModuleCodes(), expectedTags,
                    studentToUntag.getGrades());
        } else {
            expectedPerson = new Person(studentToUntag.getName(), studentToUntag.getPhone(),
                    studentToUntag.getEmail(), studentToUntag.getAddress(), expectedTags,
                    studentToUntag.getStudentId(), studentToUntag.getModuleCodes(), studentToUntag.getGrades());
        }

        String expectedMessage = String.format(UntagCommand.MESSAGE_UNTAG_PERSON_SUCCESS,
                Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentToUntag, expectedPerson);

        assertCommandSuccess(untagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeTagByInvalidStudentId_throwsCommandException() throws Exception {
        StudentId nonExistentStudentId = new StudentId("A9999999Z");
        Set<Tag> tagsToRemove = new HashSet<>();
        tagsToRemove.add(new Tag("TestTag"));

        UntagCommand untagCommand = new UntagCommand(nonExistentStudentId, tagsToRemove);

        assertCommandFailure(untagCommand, model,
                String.format(UntagCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentStudentId));
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("Tag1"));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("Tag2"));

        UntagCommand untagFirstCommand = new UntagCommand(INDEX_FIRST_PERSON, tags1);
        UntagCommand untagSecondCommand = new UntagCommand(INDEX_SECOND_PERSON, tags1);

        StudentId studentId = new StudentId("A9876543Z");
        UntagCommand untagStudentIdCommand = new UntagCommand(studentId, tags1);

        // same object -> returns true
        assertTrue(untagFirstCommand.equals(untagFirstCommand));

        // same values (index and tags) -> returns true
        UntagCommand untagFirstCommandCopy = new UntagCommand(INDEX_FIRST_PERSON, tags1);
        assertTrue(untagFirstCommand.equals(untagFirstCommandCopy));

        // same values (student ID and tags) -> returns true
        UntagCommand untagStudentIdCommandCopy = new UntagCommand(new StudentId("A9876543Z"), tags1);
        assertTrue(untagStudentIdCommand.equals(untagStudentIdCommandCopy));

        // different types -> returns false
        assertFalse(untagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(untagFirstCommand.equals(null));

        // different person index -> returns false
        assertFalse(untagFirstCommand.equals(untagSecondCommand));

        // different tags -> returns false
        UntagCommand untagFirstCommandDifferentTags = new UntagCommand(INDEX_FIRST_PERSON, tags2);
        assertFalse(untagFirstCommand.equals(untagFirstCommandDifferentTags));

        // different search method (index vs student ID) -> returns false
        assertFalse(untagFirstCommand.equals(untagStudentIdCommand));
    }

    @Test
    public void toStringMethod() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("TestTag"));

        Index targetIndex = Index.fromOneBased(1);
        UntagCommand untagCommand = new UntagCommand(targetIndex, tags);
        String expected = UntagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tagsToRemove=" + tags + "}";
        assertEquals(expected, untagCommand.toString());

        StudentId studentId = new StudentId("A9876543Z");
        UntagCommand untagStudentIdCommand = new UntagCommand(studentId, tags);
        String expectedStudentId = UntagCommand.class.getCanonicalName() + "{studentId=" + studentId
                + ", tagsToRemove=" + tags + "}";
        assertEquals(expectedStudentId, untagStudentIdCommand.toString());
    }
}

