package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} has all of the tags given.
 */
public class PersonHasAllTagsPredicate implements Predicate<Person> {
    private final Set<Tag> tags;

    public PersonHasAllTagsPredicate(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().containsAll(tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonHasAllTagsPredicate)) {
            return false;
        }

        PersonHasAllTagsPredicate otherPredicate = (PersonHasAllTagsPredicate) other;
        return tags.equals(otherPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tags).toString();
    }
}

