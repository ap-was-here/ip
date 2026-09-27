package mary.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies todo display and persisted completion markers. */
class TodoTest {
    @Test
    void toString_andStorageRecord_completionChangesPreserveDescription() {
        Todo todo = new Todo("read book");
        assertEquals("[T][ ] read book", todo.toString());
        assertEquals("T | 0 | read book", todo.toStorageRecord());
        todo.markAsDone();
        assertEquals("[T][X] read book", todo.toString());
        assertEquals("T | 1 | read book", todo.toStorageRecord());
    }
}
