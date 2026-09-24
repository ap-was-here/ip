/** Adds a todo, deadline, or event task. */
public class AddCommand extends Command {
    private final String fullCommand;

    public AddCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            Task task = Parser.parseTask(fullCommand);
            tasks.add(task);
            storage.save(tasks.getTasks());
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        } catch (MaryException exception) {
            ui.showError(exception.getMessage());
        }
    }
}
