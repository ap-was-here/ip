package mary.command;

import mary.storage.Storage;
import mary.task.TaskList;
import mary.ui.Ui;

/**
 * Displays all tasks currently stored by MARY.
 */
public class ListCommand extends Command {
    /**
     * Displays tasks with one-based numbers, or an empty-list message.
     *
     * @param tasks current in-memory task list.
     * @param ui user-facing message handler.
     * @param storage persistence service (unused by read-only commands).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            System.out.println(" MARY has no saved tasks yet.");
            return;
        }
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }
}
