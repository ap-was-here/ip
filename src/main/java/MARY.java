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

            if (command.equals("list")) {
                if (taskCount == 0) {
                    System.out.println(" MARY has no saved tasks yet.");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                }
            } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                boolean markDone = command.startsWith("mark ");
                String numberText = command.substring(markDone ? 5 : 7).trim();

                try {
                    int taskNumber = Integer.parseInt(numberText);
                    int taskIndex = taskNumber - 1;

                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println(" MARY could not find task " + taskNumber + ".");
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
                    System.out.println(" Please provide a valid task number.");
                }
            } else if (taskCount < tasks.length) {
                Task newTask = createTask(command);
                tasks[taskCount] = newTask;
                taskCount++;
                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + taskCount + " tasks in the list.");
            } else {
                System.out.println(" MARY's task list is full.");
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
    private static Task createTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring(5).trim());
        }

        if (command.startsWith("deadline ")) {
            String content = command.substring(9).trim();
            int marker = content.indexOf(" /by ");
            if (marker >= 0) {
                return new Deadline(content.substring(0, marker).trim(), content.substring(marker + 5).trim());
            }
            return new Deadline(content, "");
        }

        if (command.startsWith("event ")) {
            String content = command.substring(6).trim();
            int fromMarker = content.indexOf(" /from ");
            int toMarker = content.indexOf(" /to ");
            if (fromMarker >= 0 && toMarker > fromMarker) {
                String description = content.substring(0, fromMarker).trim();
                String from = content.substring(fromMarker + 7, toMarker).trim();
                String to = content.substring(toMarker + 5).trim();
                return new Event(description, from, to);
            }
            return new Event(content, "", "");
        }

        return new Todo(command);
    }
}
