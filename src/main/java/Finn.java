import java.util.Scanner;

public class Finn {
    public static void main(String[] args) {
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

        // TODO
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("What can I do for you? ");
            String command = scanner.nextLine();

            System.out.println(breakline);

            if (command.equals("bye")) {
                break;
            }

            System.out.println("Executing: " + command);

            System.out.println(breakline);
        }

        String exit = "Bye. Hope to see you again soon!";
        System.out.println(exit);
    }
}
