package mary.task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies list ordering, deletion boundaries, and collection ownership. */
class TaskListTest {
    @Test
    void add_emptyList_preservesInsertionOrderAndObjectIdentity() {
        TaskList list = new TaskList();
        assertTrue(list.isEmpty());
        Task first = new Todo("first");
        Task second = new Todo("second");
        list.add(first);
        list.add(second);
        assertEquals(2, list.size());
        assertSame(first, list.get(0));
        assertSame(second, list.get(1));
    }

    @Test
    void remove_middleFirstAndLast_updatesOrderAndSize() {
        Task a = new Todo("a");
        Task b = new Todo("b");
        Task c = new Todo("c");
        TaskList list = new TaskList(List.of(a, b, c));
        assertSame(b, list.remove(1));
        assertEquals(List.of(a, c), list.getTasks());
        assertSame(a, list.remove(0));
        assertSame(c, list.remove(0));
        assertTrue(list.isEmpty());
    }

    @Test
    void remove_invalidIndices_throwsWithoutChangingList() {
        Task task = new Todo("keep");
        TaskList list = new TaskList(List.of(task));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
        assertEquals(List.of(task), list.getTasks());
        assertThrows(IndexOutOfBoundsException.class, () -> new TaskList().remove(0));
    }

    @Test
    void constructor_sourceCollectionChanges_doNotChangeOwnedList() {
        ArrayList<Task> source = new ArrayList<>(List.of(new Todo("keep")));
        TaskList list = new TaskList(source);
        source.clear();
        assertEquals(1, list.size());
    }

    @Test
    void getTasks_modifyingSnapshot_doesNotChangeOwnedList() {
        Task task = new Todo("keep");
        TaskList list = new TaskList(List.of(task));
        List<Task> snapshot = list.getTasks();
        snapshot.clear();
        assertEquals(1, list.size());
        assertSame(task, list.get(0));
    }
}
