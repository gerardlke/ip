import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Finn {
    public static void main(String[] args) {
        // Greeting text
        String banner = " ____ ___ _   _ _   _ \n"
                + "|  __|_ _| \\ | | \\ | |\n"
                + "| |_  | ||  \\| |  \\| |\n"
                + "|  _| | || |\\  | |\\  |\n"
                + "|_|  |___|_| \\_|_| \\_|\n";
        String breakline = "____________________________________________________________\n";

        String opening = banner + breakline;
        System.out.println(opening);

        String greeting = "Hello! I'm Finn.\nYour personal AI assistant!\n" + breakline;
        System.out.println(greeting);

        // Tools
        Scanner scanner = new Scanner(System.in);
        List<String> commands = new ArrayList<>();

        while (true) {
            System.out.print("What can I do for you? ");
            String command = scanner.nextLine();

            System.out.println(breakline);

            // End program if command is "bye"
            if (command.equals("bye")) {
                break;
            }

            // Execute list 
            if (command.equals("list")) {
                for (int cur = 1; cur <= commands.size(); cur++) {
                    System.out.println(cur + ". " + commands.get(cur - 1));
                }
                System.out.println(breakline);
                continue;
            }

            System.out.println("Executing: " + command);
            commands.add(command);

            System.out.println(breakline);
        }

        // Closing text
        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }
}
