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

        String greeting = "Hello! I'm Finn.\nWhat can I do for you?\n" + breakline;
        System.out.println(greeting);

        // TODO

        String exit = "Bye. Hope to see you again soon!\n" + breakline;
        System.out.println(exit);
    }
}
