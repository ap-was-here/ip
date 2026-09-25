package mary.command;

import mary.storage.Storage;
import mary.task.TaskList;
import mary.ui.Ui;

/** Reports an input that does not match a supported command. */
public class UnknownCommand extends Command {
    private final String input;

    public UnknownCommand(String input) { this.input = input; }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (input.isBlank()) ui.showError("please enter a command or task.");
        else ui.showError("I don't recognize that command; use todo, deadline, event, on, list, mark, unmark, delete, or bye.");
    }
}
