import java.util.Scanner;

public class MARY {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;
        String banner = "███╗   ███╗ █████╗ ██████╗ ██╗   ██╗\n"
                + "████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝\n"
                + "██╔████╔██║███████║██████╔╝ ╚████╔╝\n"
                + "██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝\n"
                + "██║ ╚═╝ ██║██║  ██║██║  ██║   ██║\n"
                + "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hi! I'm MARY.");
        System.out.println("What have you got for me today?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            System.out.println(separator);
            if (command.equals("bye")) {
                System.out.println("See you later. Complete your tasks on time!");
                System.out.println(separator);
                break;
            }

            try {
                if (command.isBlank()) {
                    throw new MaryException("please enter a command or task.");
                }

                if (command.equals("list")) {
                if (taskCount == 0) {
                    System.out.println(" MARY has no saved tasks yet.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                }
                } else if (command.startsWith("mark") || command.startsWith("unmark")) {
                boolean markDone = command.startsWith("mark ");
                int prefixLength = markDone ? 5 : 7;
                if (!command.startsWith(markDone ? "mark " : "unmark ")
                        || command.substring(prefixLength).trim().isEmpty()) {
                    throw new MaryException("use 'mark N' or 'unmark N', where N is a task number.");
                }
                String numberText = command.substring(prefixLength).trim();

                try {
                    int taskNumber = Integer.parseInt(numberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        throw new MaryException("task " + taskNumber
                                + " does not exist; use 'list' to see valid task numbers.");
                    } else {
                        if (markDone) {
                            tasks[taskIndex].markAsDone();
                        } else {
                            tasks[taskIndex].markAsNotDone();
                        }
                        if (markDone) {
                            System.out.println(" Nice! I've marked this task as done:");
                        } else {
                            System.out.println(" OK, I've marked this task as not done yet:");
                        }
                        System.out.println("   " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    throw new MaryException("'" + numberText
                            + "' is not a valid task number; use a positive whole number.");
                }
                } else if (command.startsWith("todo") || command.startsWith("deadline")
                        || command.startsWith("event")) {
                    if (taskCount >= tasks.length) {
                        throw new MaryException("the task list is full; remove a task before adding another.");
                    }
                    Task newTask = createTask(command);
                    tasks[taskCount] = newTask;
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    throw new MaryException("I don't recognize that command; use todo, deadline, event, list, mark, unmark, or bye.");
                }
            } catch (MaryException exception) {
                System.out.println(" Error: " + exception.getMessage());
            }

            System.out.println(separator);
        }
    }

    /**
     * Converts a user's task command into a Task object without using inheritance.
     *
     * @param command the complete command entered by the user
     * @return the task represented by the command
     */
    private static Task createTask(String command) throws MaryException {
        if (command.startsWith("todo ")) {
            String description = command.substring(5).trim();
            if (description.isEmpty()) {
                throw new MaryException("please add a task description after 'todo'.");
            }
            return new Todo(description);
        }

        if (command.startsWith("deadline ")) {
            String content = command.substring(9).trim();
            int marker = content.indexOf(" /by ");
            if (marker < 0 || content.substring(0, marker).trim().isEmpty()
                    || content.substring(marker + 5).trim().isEmpty()) {
                throw new MaryException("use 'deadline description /by date or time'.");
            }
            return new Deadline(content.substring(0, marker).trim(), content.substring(marker + 5).trim());
        }

        if (command.startsWith("event ")) {
            String content = command.substring(6).trim();
            int fromMarker = content.indexOf(" /from ");
            int toMarker = content.indexOf(" /to ");
            if (fromMarker < 0 || toMarker <= fromMarker) {
                throw new MaryException("use 'event description /from start /to end'.");
            }
            String description = content.substring(0, fromMarker).trim();
            String from = content.substring(fromMarker + 7, toMarker).trim();
            String to = content.substring(toMarker + 5).trim();
            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new MaryException("event description, start time, and end time cannot be empty.");
            }
            return new Event(description, from, to);
        }

        throw new MaryException("use 'todo description' to add a task without a date.");
    }
}
