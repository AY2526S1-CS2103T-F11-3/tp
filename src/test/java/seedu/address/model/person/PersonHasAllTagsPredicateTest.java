package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonHasAllTagsPredicateTest {

    @Test
    public void equals() {
        Set<Tag> firstPredicateTagSet = new HashSet<>();
        firstPredicateTagSet.add(new Tag("first"));

        Set<Tag> secondPredicateTagSet = new HashSet<>();
        secondPredicateTagSet.add(new Tag("first"));
        secondPredicateTagSet.add(new Tag("second"));

        PersonHasAllTagsPredicate firstPredicate = new PersonHasAllTagsPredicate(firstPredicateTagSet);
        PersonHasAllTagsPredicate secondPredicate = new PersonHasAllTagsPredicate(secondPredicateTagSet);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonHasAllTagsPredicate firstPredicateCopy = new PersonHasAllTagsPredicate(firstPredicateTagSet);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different tags -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasAllTags_returnsTrue() {
        // One tag
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        PersonHasAllTagsPredicate predicate = new PersonHasAllTagsPredicate(tagSet);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Multiple tags - person has all
        tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        tagSet.add(new Tag("owesMoney"));
        predicate = new PersonHasAllTagsPredicate(tagSet);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "owesMoney").build()));

        // Multiple tags - person has more than required
        tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        predicate = new PersonHasAllTagsPredicate(tagSet);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends", "owesMoney", "colleague").build()));
    }

    @Test
    public void test_personDoesNotHaveAllTags_returnsFalse() {
        // Zero tags in filter
        Set<Tag> tagSet = new HashSet<>();
        PersonHasAllTagsPredicate predicate = new PersonHasAllTagsPredicate(tagSet);
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Non-matching tag
        tagSet = new HashSet<>();
        tagSet.add(new Tag("colleague"));
        predicate = new PersonHasAllTagsPredicate(tagSet);
        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Person has only some of the required tags
        tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        tagSet.add(new Tag("owesMoney"));
        predicate = new PersonHasAllTagsPredicate(tagSet);
        assertFalse(predicate.test(new PersonBuilder().withTags("friends").build()));

        // Person has no tags
        tagSet = new HashSet<>();
        tagSet.add(new Tag("friends"));
        predicate = new PersonHasAllTagsPredicate(tagSet);
        assertFalse(predicate.test(new PersonBuilder().build()));
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        tags.add(new Tag("owesMoney"));
        PersonHasAllTagsPredicate predicate = new PersonHasAllTagsPredicate(tags);

        String expected = PersonHasAllTagsPredicate.class.getCanonicalName() + "{tags=" + tags + "}";
        assertEquals(expected, predicate.toString());
    }
}

