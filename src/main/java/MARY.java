import java.util.Scanner;

public class MARY {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String[] tasks = new String[100];
        boolean[] completed = new boolean[100];
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
                        String status = completed[i] ? "[X]" : "[ ]";
                        System.out.println(" " + (i + 1) + "." + status + " " + tasks[i]);
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
                        completed[taskIndex] = markDone;
                        String status = markDone ? "[X]" : "[ ]";
                        if (markDone) {
                            System.out.println(" Nice! I've marked this task as done:");
                        } else {
                            System.out.println(" OK, I've marked this task as not done yet:");
                        }
                        System.out.println("   " + status + " " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException exception) {
                    System.out.println(" Please provide a valid task number.");
                }
            } else if (taskCount < tasks.length) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            } else {
                System.out.println(" MARY's task list is full.");
            }

            System.out.println(separator);
        }
    }
}
