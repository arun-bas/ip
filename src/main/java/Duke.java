import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! I'm Bubba.");
        System.out.println("What can I do for you?");
        // loop and echo user input until bye.
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            System.out.println(input);
        }

        System.out.println("Goodbye. See you again.");
        scanner.close();
    }
}
