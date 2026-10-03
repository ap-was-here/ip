package mary.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Owns the collection of tasks and task-list operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from loaded tasks.
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Returns whether the list contains no tasks.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the zero-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Appends a task to the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the zero-based index.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a defensive copy of the task collection.
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds tasks whose descriptions contain the keyword, ignoring case.
     *
     * @param keyword non-null search text.
     * @return a new list of matching task objects in their original order.
     */
    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.matchesDescription(keyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
