package mary.command;

import mary.exception.MaryException;
import mary.parser.Parser;
import mary.storage.Storage;
import mary.task.Task;
import mary.task.TaskList;
import mary.ui.Ui;

/**
 * Adds a todo, deadline, or event task.
 */
public class AddCommand extends Command {
    private final String fullCommand;

    /**
     * Creates a command retaining the supplied input for execution-time validation.
     *
     * @param fullCommand complete, non-null user input.
     */
    public AddCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Parses and adds a task, then saves the list and displays confirmation.
     * Validation and storage errors are displayed through the UI; a save failure does not undo the addition.
     *
     * @param tasks current in-memory task list.
     * @param ui user-facing message handler.
     * @param storage persistence service (unused by read-only commands).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            Task task = Parser.parseTask(fullCommand);
            tasks.add(task);
            storage.save(tasks.getTasks());
            ui.showMessages(" Got it. I've added this task:",
                    "   " + task,
                    " Now you have " + tasks.size() + " tasks in the list.");
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
