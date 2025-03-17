package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final String tagKeyword;

    public TagContainsKeywordsPredicate(String tagKeyword) {
        this.tagKeyword = tagKeyword;
    }

    @Override
    public boolean test(Person person) {
        Set<Tag> tags = person.getTags();
        return tags.contains(new Tag(tagKeyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagContainsKeywordsPredicate)) {
            return false;
        }

        TagContainsKeywordsPredicate otherTagContainsKeywordsPredicate = (TagContainsKeywordsPredicate) other;
        return tagKeyword.equals(otherTagContainsKeywordsPredicate.tagKeyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tagKeyword).toString();
    }

}
