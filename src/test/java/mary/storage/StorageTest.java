package mary.storage;

import mary.exception.MaryException;
import mary.task.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Tests persistence using isolated files, never the user's saved tasks. */
class StorageTest {
    @TempDir
    Path directory;

    @Test
    void load_missingFile_returnsEmptyWithoutCreatingFile() throws Exception {
        Path file = directory.resolve("missing.txt");
        assertTrue(new Storage(file.toString()).load().isEmpty());
        assertFalse(Files.exists(file));
    }

    @Test
    void load_emptyAndBlankLines_returnsEmptyList() throws Exception {
        Path file = directory.resolve("tasks.txt");
        Files.writeString(file, "\n   \n\t\n");
        assertTrue(new Storage(file.toString()).load().isEmpty());
    }

    @Test
    void saveAndLoad_allSubtypesAndStates_preservesOrderAndValues() throws Exception {
        Path file = directory.resolve("tasks.txt");
        Task todo = new Todo("read café 书");
        todo.markAsDone();
        Deadline deadline = new Deadline("return", LocalDateTime.of(2019, 12, 2, 18, 0));
        Event event = new Event("camp", LocalDateTime.of(2019, 12, 2, 14, 0),
                LocalDateTime.of(2019, 12, 4, 16, 0));
        event.markAsDone();
        List<Task> expected = List.of(todo, deadline, event);
        new Storage(file.toString()).save(expected);
        List<Task> loaded = new Storage(file.toString()).load();
        assertEquals(expected.stream().map(Task::toStorageRecord).toList(),
                loaded.stream().map(Task::toStorageRecord).toList());
        assertInstanceOf(Todo.class, loaded.get(0));
        assertEquals(deadline.getBy(), assertInstanceOf(Deadline.class, loaded.get(1)).getBy());
        assertEquals(event.getTo(), assertInstanceOf(Event.class, loaded.get(2)).getTo());
    }

    @Test
    void save_replacementAndEmptyList_removesOldRecords() throws Exception {
        Path file = directory.resolve("tasks.txt");
        Storage storage = new Storage(file.toString());
        storage.save(List.of(new Todo("a"), new Todo("b")));
        storage.save(List.of(new Todo("c")));
        assertEquals(List.of("T | 0 | c"), Files.readAllLines(file));
        storage.save(List.of());
        assertEquals("", Files.readString(file));
        assertTrue(storage.load().isEmpty());
    }

    @Test
    void load_badTypesAndFieldCounts_reportsRecordLine() throws Exception {
        for (String record : List.of("nonsense", "Q | 0 | read", "T | 0",
                "D | 0 | read", "E | 0 | read | 2019-12-02T14:00",
                "D | 0 | read | 2019-12-02T18:00 | extra")) {
            Path file = directory.resolve("tasks.txt");
            Files.writeString(file, "T | 0 | valid\n" + record + "\n");
            MaryException error = assertThrows(MaryException.class,
                    () -> new Storage(file.toString()).load(), record);
            assertEquals("invalid record on line 2.", error.getMessage());
        }
    }

    @Test
    void load_extraTodoFields_rejectsCorruptRecord() throws Exception {
        Path file = directory.resolve("tasks.txt");
        for (String record : List.of("T | 0 | read | unexpected", "T | 0 | read | ")) {
            Files.writeString(file, record + "\n");
            assertEquals("invalid record on line 1.", assertThrows(MaryException.class,
                    () -> new Storage(file.toString()).load(), record).getMessage());
        }
    }

    @Test
    void load_badStatusAndEmptyDescription_reportsSpecificError() throws Exception {
        Path file = directory.resolve("tasks.txt");
        Storage storage = new Storage(file.toString());
        Files.writeString(file, "T | 2 | read\n");
        assertEquals("invalid completion status on line 1.",
                assertThrows(MaryException.class, storage::load).getMessage());
        Files.writeString(file, "T | 0 | \n");
        assertEquals("empty task description on line 1.",
                assertThrows(MaryException.class, storage::load).getMessage());
    }

    @Test
    void load_badDates_reportsDateError() throws Exception {
        Path file = directory.resolve("tasks.txt");
        for (String record : List.of("D | 0 | read | Sunday",
                "E | 0 | camp | bad | 2019-12-02T16:00",
                "E | 0 | camp | 2019-12-02T14:00 | bad")) {
            Files.writeString(file, record);
            assertEquals("invalid date/time on line 1.", assertThrows(MaryException.class,
                    () -> new Storage(file.toString()).load()).getMessage());
        }
    }

    @Test
    void load_directoryInsteadOfFile_wrapsIoFailure() {
        assertEquals("could not read " + directory + ".", assertThrows(MaryException.class,
                () -> new Storage(directory.toString()).load()).getMessage());
    }

    @Test
    void save_directoryInsteadOfFile_wrapsIoFailure() {
        assertEquals("could not save tasks to " + directory + ".", assertThrows(MaryException.class,
                () -> new Storage(directory.toString()).save(List.of(new Todo("read")))).getMessage());
    }
}
