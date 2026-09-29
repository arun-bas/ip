package bubba;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads and saves task records; leaves displaying errors to the user interface.
 */
public class Storage {
    private final ArrayList<String> loadWarnings = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns a copy of warnings from the most recent load for Ui to display.
     */
    public ArrayList<String> getLoadWarnings() {
        return new ArrayList<>(loadWarnings);
    }

    public ArrayList<Task> load() {
        loadWarnings.clear();
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(" \\| ");

                try {
                    String type = parts[0];
                    boolean isDone = parts[1].equals("1");
                    String description = parts[2];
                    Task task;
                    if (type.equals("T")) {
                        task = new Todo(description);
                    } else if (type.equals("D")) {
                        LocalDateTime by = LocalDateTime.parse(parts[3], formatter);
                        task = new Deadline(description, by);
                    } else if (type.equals("E")) {
                        LocalDateTime from = parseEventDate(parts[3]);
                        LocalDateTime to = parseEventDate(parts[4]);
                        task = new Event(description, from, to);
                    } else {
                        continue;
                    }

                    if (isDone) {
                        task.done();
                    }

                    tasks.add(task);
                } catch (DateTimeParseException e) {
                    loadWarnings.add("Enter the correct format!");
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            loadWarnings.add("File wasn't found!");
        }

        return tasks;
    }

    /**
     * Also accepts ISO dates written by earlier versions of the event serializer.
     */
    private LocalDateTime parseEventDate(String text) {
        try {
            return LocalDateTime.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(text);
        }
    }

    public void save(ArrayList<Task> tasks) throws BubbaException {
        try {
            File file = new File(filePath);

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            PrintWriter writer = new PrintWriter(file);

            for (Task task : tasks) {
                writer.println(task.toStorageString());
            }

            writer.close();
        } catch (FileNotFoundException e) {
            throw new BubbaException("Unable to save tasks to file!");
        }
    }
}
