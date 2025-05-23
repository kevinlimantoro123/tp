package seedu.address.model.predicate;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Tests that at least one of the {@code Person}'s {@code Tag} <b>approximately</b> matches any of the tags given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final String tagKeywords;

    public TagContainsKeywordsPredicate(String tagKeywords) {
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Person person) {
        Set<Tag> tags = person.getTags();
        return tags.stream().anyMatch(tag -> {
            String tagName = tag.tagName;
            String[] tagKeywordParts = tagKeywords.split("\\s+");
            for (String tagKeywordPart : tagKeywordParts) {
                if (tagKeywordPart.length() < 3
                        && !StringUtil.containsWordIgnoreCase(tagName, tagKeywordPart, true,
                        tagKeywordPart.length())) {
                    return false;
                }
            }
            return StringUtil.computeCloseness(tagName, tagKeywords) < 3;
        });
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
