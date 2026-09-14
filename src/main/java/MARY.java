import java.util.Scanner;

public class MARY {
    public static void main(String[] args) {
        String separator = "_._._._._._._._._._._._._._._._._._._._._._._._._._._._._._._";
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
                System.out.println(" Bye. Complete your tasks on time!");
                System.out.println(separator);
                break;
            }

            // MARY echoes every command so the user can confirm what was entered.
            System.out.println(" " + command);
            System.out.println(separator);
        }
    }
}
