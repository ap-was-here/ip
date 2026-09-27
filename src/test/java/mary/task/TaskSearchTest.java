package mary.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * Tests description matching and the ownership of search result collections.
 */
class TaskSearchTest {
    /**
     * Checks literal substrings, punctuation, and non-ASCII descriptions.
     */
    @Test
    void matchesDescription_substrings_ignoresCaseWithoutUsingRegex() {
        Task task = new Todo("Read Notebook [draft] CAFÉ 书");
        assertTrue(task.matchesDescription("book"));
        assertTrue(task.matchesDescription("READ"));
        assertTrue(task.matchesDescription("[draft]"));
        assertTrue(task.matchesDescription("café"));
        assertTrue(task.matchesDescription("书"));
        assertFalse(task.matchesDescription(".*"));
        assertFalse(task.matchesDescription("missing"));
    }

    /**
     * Checks that date formatting and completion/type markers are not searchable.
     */
    @Test
    void matchesDescription_metadataOnly_doesNotMatch() {
        Task task = new Deadline("return book", LocalDateTime.of(2019, 12, 2, 18, 0));
        task.markAsDone();
        for (String keyword : new String[] {"Dec", "2019", "[D]", "[X]", "by:"}) {
            assertFalse(task.matchesDescription(keyword), keyword);
        }
    }

    /**
     * Checks case folding is independent of the machine's default locale.
     */
    @Test
    void matchesDescription_turkishLocale_matchesAsciiCase() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertTrue(new Todo("VISIT library").matchesDescription("visit"));
        } finally {
            Locale.setDefault(original);
        }
    }

    /**
     * Checks duplicate matches, stable order, and an independently mutable result list.
     */
    @Test
    void find_duplicateTasks_preservesOrderWithoutExposingOwnedList() {
        Task first = new Todo("book");
        Task second = new Todo("book");
        TaskList tasks = new TaskList(List.of(first, new Todo("bread"), second));
        List<Task> matches = tasks.find("book");
        assertEquals(List.of(first, second), matches);
        matches.clear();
        assertEquals(3, tasks.size());
        assertTrue(tasks.find("absent").isEmpty());
        assertTrue(new TaskList().find("book").isEmpty());
    }
}
