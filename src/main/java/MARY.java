import java.util.Scanner;

public class MARY {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String[] tasks = new String[100];
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
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
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
