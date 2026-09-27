package mary.task;

import java.util.ArrayList;
import java.util.List;

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
     * Copies the supplied collection while retaining the same task objects.
     *
     * @param loadedTasks initial tasks in list order.
     */
    public TaskList(List<Task> loadedTasks) {
        tasks = new ArrayList<>(loadedTasks);
    }

    /**
     * Checks whether there are any tasks.
     *
     * @return true if the list is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Counts the tasks currently stored.
     *
     * @return the task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves a task by zero-based index, not its displayed one-based number.
     *
     * @param index zero-based task position.
     * @return the original task object.
     * @throws IndexOutOfBoundsException if the index is outside the list.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Appends a task without saving the list to disk.
     *
     * @param task task to append.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task and shifts later tasks down by one position.
     *
     * @param index zero-based task position.
     * @return the removed task.
     * @throws IndexOutOfBoundsException if the index is outside the list.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns a shallow copy: changing the collection does not affect this list,
     * but changes to its task objects are shared.
     *
     * @return a new mutable list in task order.
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }
}
