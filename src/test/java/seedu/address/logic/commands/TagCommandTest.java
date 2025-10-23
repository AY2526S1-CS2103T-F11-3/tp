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
 * Contains integration tests (interaction with the Model) and unit tests for TagCommand.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addTagByIndexUnfilteredList_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("NewTag"));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToTag.getTags());
        expectedTags.addAll(tagsToAdd);

        Person expectedPerson;
        if (personToTag.getStudentId() != null && personToTag.getPhone() == null
                && personToTag.getAddress() == null) {
            expectedPerson = new Person(personToTag.getName(), personToTag.getStudentId(),
                    personToTag.getEmail(), personToTag.getModuleCodes(), expectedTags, personToTag.getGrades());
        } else {
            expectedPerson = new Person(personToTag.getName(), personToTag.getPhone(), personToTag.getEmail(),
                    personToTag.getAddress(), expectedTags, personToTag.getStudentId(),
                    personToTag.getModuleCodes(), personToTag.getGrades());
        }

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS,
                Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToTag, expectedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMultipleTagsByIndex_success() throws Exception {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("Tag1"));
        tagsToAdd.add(new Tag("Tag2"));
        tagsToAdd.add(new Tag("Tag3"));

        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToTag.getTags());
        expectedTags.addAll(tagsToAdd);

        Person expectedPerson;
        if (personToTag.getStudentId() != null && personToTag.getPhone() == null
                && personToTag.getAddress() == null) {
            expectedPerson = new Person(personToTag.getName(), personToTag.getStudentId(),
                    personToTag.getEmail(), personToTag.getModuleCodes(), expectedTags, personToTag.getGrades());
        } else {
            expectedPerson = new Person(personToTag.getName(), personToTag.getPhone(), personToTag.getEmail(),
                    personToTag.getAddress(), expectedTags, personToTag.getStudentId(),
                    personToTag.getModuleCodes(), personToTag.getGrades());
        }

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS,
                Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToTag, expectedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("NewTag"));

        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_addTagByValidStudentId_success() throws Exception {
        Person studentToTag = new PersonBuilder().withName("Test Student")
                .withEmail("test@example.com")
                .withStudentId("A9876543Z")
                .withModuleCode("CS2103T")
                .build();
        model.addPerson(studentToTag);

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("Excelling"));

        StudentId studentId = new StudentId("A9876543Z");
        TagCommand tagCommand = new TagCommand(studentId, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(studentToTag.getTags());
        expectedTags.addAll(tagsToAdd);

        Person expectedPerson;
        if (studentToTag.getStudentId() != null && studentToTag.getPhone() == null
                && studentToTag.getAddress() == null) {
            expectedPerson = new Person(studentToTag.getName(), studentToTag.getStudentId(),
                    studentToTag.getEmail(), studentToTag.getModuleCodes(), expectedTags, studentToTag.getGrades());
        } else {
            expectedPerson = new Person(studentToTag.getName(), studentToTag.getPhone(),
                    studentToTag.getEmail(), studentToTag.getAddress(), expectedTags,
                    studentToTag.getStudentId(), studentToTag.getModuleCodes(), studentToTag.getGrades());
        }

        String expectedMessage = String.format(TagCommand.MESSAGE_TAG_PERSON_SUCCESS,
                Messages.format(expectedPerson));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(studentToTag, expectedPerson);

        assertCommandSuccess(tagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addTagByInvalidStudentId_throwsCommandException() throws Exception {
        StudentId nonExistentStudentId = new StudentId("A9999999Z");
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("NewTag"));

        TagCommand tagCommand = new TagCommand(nonExistentStudentId, tagsToAdd);

        assertCommandFailure(tagCommand, model,
                String.format(TagCommand.MESSAGE_PERSON_NOT_FOUND, nonExistentStudentId));
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(new Tag("Tag1"));

        Set<Tag> tags2 = new HashSet<>();
        tags2.add(new Tag("Tag2"));

        TagCommand tagFirstCommand = new TagCommand(INDEX_FIRST_PERSON, tags1);
        TagCommand tagSecondCommand = new TagCommand(INDEX_SECOND_PERSON, tags1);

        StudentId studentId = new StudentId("A9876543Z");
        TagCommand tagStudentIdCommand = new TagCommand(studentId, tags1);

        // same object -> returns true
        assertTrue(tagFirstCommand.equals(tagFirstCommand));

        // same values (index and tags) -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, tags1);
        assertTrue(tagFirstCommand.equals(tagFirstCommandCopy));

        // same values (student ID and tags) -> returns true
        TagCommand tagStudentIdCommandCopy = new TagCommand(new StudentId("A9876543Z"), tags1);
        assertTrue(tagStudentIdCommand.equals(tagStudentIdCommandCopy));

        // different types -> returns false
        assertFalse(tagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(tagFirstCommand.equals(null));

        // different person index -> returns false
        assertFalse(tagFirstCommand.equals(tagSecondCommand));

        // different tags -> returns false
        TagCommand tagFirstCommandDifferentTags = new TagCommand(INDEX_FIRST_PERSON, tags2);
        assertFalse(tagFirstCommand.equals(tagFirstCommandDifferentTags));

        // different search method (index vs student ID) -> returns false
        assertFalse(tagFirstCommand.equals(tagStudentIdCommand));
    }

    @Test
    public void toStringMethod() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("TestTag"));

        Index targetIndex = Index.fromOneBased(1);
        TagCommand tagCommand = new TagCommand(targetIndex, tags);
        String expected = TagCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", tagsToAdd=" + tags + "}";
        assertEquals(expected, tagCommand.toString());

        StudentId studentId = new StudentId("A9876543Z");
        TagCommand tagStudentIdCommand = new TagCommand(studentId, tags);
        String expectedStudentId = TagCommand.class.getCanonicalName() + "{studentId=" + studentId
                + ", tagsToAdd=" + tags + "}";
        assertEquals(expectedStudentId, tagStudentIdCommand.toString());
    }
}

