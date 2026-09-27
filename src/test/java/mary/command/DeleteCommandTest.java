package mary.command;

import mary.task.Todo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests one-based deletion, renumbering, persistence, and invalid input. */
class DeleteCommandTest extends CommandTestSupport {
    @Test
    void execute_middleTask_removesOnlySelectedTaskAndSaves() throws Exception {
        tasks.add(new Todo("a"));
        tasks.add(new Todo("b"));
        tasks.add(new Todo("c"));
        assertEquals(" Noted. I've removed this task:\n   [T][ ] b\n Now you have 2 tasks in the list.\n",
                execute(new DeleteCommand("delete 2")));
        assertEquals("[T][ ] a", tasks.get(0).toString());
        assertEquals("[T][ ] c", tasks.get(1).toString());
        assertEquals(2, storage.load().size());
    }

    @Test
    void execute_lastRemainingTask_savesEmptyList() throws Exception {
        tasks.add(new Todo("a"));
        execute(new DeleteCommand("delete 1"));
        assertTrue(tasks.isEmpty());
        assertTrue(storage.load().isEmpty());
    }

    @Test
    void execute_missingOrInvalidNumber_doesNotRemoveTask() {
        tasks.add(new Todo("keep"));
        for (String input : new String[]{"delete", "delete ", "delete abc", "delete 1.5",
                "delete 0", "delete -1", "delete 2", "delete 2147483648"}) {
            assertTrue(execute(new DeleteCommand(input)).startsWith(" Error:"), input);
            assertEquals(1, tasks.size(), input);
        }
    }

    @Test
    void execute_emptyList_explainsValidNumberLookup() {
        assertEquals(" Error: task 1 does not exist; use 'list' to see valid task numbers.\n",
                execute(new DeleteCommand("delete 1")));
    }
}
