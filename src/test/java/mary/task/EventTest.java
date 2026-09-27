package mary.task;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/** Verifies multi-day event display and both stored endpoints. */
class EventTest {
    @Test
    void toString_andStorageRecord_preserveBothEndpointsAndStatus() {
        Event task = new Event("camp", LocalDateTime.of(2019, 12, 2, 14, 0),
                LocalDateTime.of(2019, 12, 4, 16, 0));
        assertEquals("[E][ ] camp (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)", task.toString());
        task.markAsDone();
        assertEquals("[E][X] camp (from: 2 Dec 2019 14:00 to: 4 Dec 2019 16:00)", task.toString());
        assertEquals("E | 1 | camp | 2019-12-02T14:00 | 2019-12-04T16:00", task.toStorageRecord());
    }
}
