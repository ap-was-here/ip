package mary.task;

import java.util.ArrayList;
import java.util.List;

/** Owns the collection of tasks and task-list operations. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() { tasks = new ArrayList<>(); }

    /** Creates a task list from loaded tasks. */
    public TaskList(List<Task> loadedTasks) { tasks = new ArrayList<>(loadedTasks); }

    public boolean isEmpty() { return tasks.isEmpty(); }
    public int size() { return tasks.size(); }
    public Task get(int index) { return tasks.get(index); }
    public void add(Task task) { tasks.add(task); }
    public Task remove(int index) { return tasks.remove(index); }
    public List<Task> getTasks() { return new ArrayList<>(tasks); }

    /**
     * Finds tasks whose descriptions contain the keyword, ignoring case.
     *
     * @param keyword non-null search text.
     * @return a new list of matching task objects in their original order.
     */
    public List<Task> find(String keyword) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.matchesDescription(keyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
