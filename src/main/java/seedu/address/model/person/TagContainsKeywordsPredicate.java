package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final String tagKeywords;

    public TagContainsKeywordsPredicate(String tagKeywords) {
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        Set<Tag> tags = person.getTags();
        // takes tagKeywords, which could be multiple different tags and check if any tags in person matches
        return tags.stream().anyMatch(tag -> StringUtil.containsWordIgnoreCase(tagKeywords, tag.tagName));
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
        return tagKeywords.equals(otherTagContainsKeywordsPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tagKeywords).toString();
    }

}
