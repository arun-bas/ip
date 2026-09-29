package bubba;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Loads and saves task records, leaving error presentation to the user interface.
 */
public class Storage {
    private final ArrayList<String> loadWarnings = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private final String filePath;

    /**
     * Creates storage for the given path without reading or writing the file.
     *
     * @param filePath Save-file path, resolved against the working directory if relative.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns warnings from the most recent load for the user interface to display.
     *
     * @return Copy of the warning list.
     */
    public ArrayList<String> getLoadWarnings() {
        return new ArrayList<>(loadWarnings);
    }

    /**
     * Loads tasks in file order, returning an empty list if the file does not exist.
     * Blank lines and unknown task types are skipped. Invalid dates are skipped with a warning.
     * Loading warnings are cleared at the start of each call.
     *
     * @return Loaded tasks with their saved completion states.
     * @throws ArrayIndexOutOfBoundsException If a nonblank record lacks required fields.
     */
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
                String line =  scanner.nextLine();
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
                } catch (DateTimeParseException e){
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
     * Parses a stored event date, also accepting ISO dates written by earlier versions.
     *
     * @param text Stored date and time.
     * @return Parsed date and time.
     * @throws DateTimeParseException If neither supported format can parse the value.
     */
    private LocalDateTime parseEventDate(String text) {
        try {
            return LocalDateTime.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(text);
        }
    }

    /**
     * Replaces the save file with the supplied tasks in list order.
     * Creates missing parent directories before opening the file.
     *
     * @param tasks Tasks to serialize, including their completion states.
     * @throws BubbaException If the file cannot be opened for writing.
     */
    public void save(ArrayList<Task> tasks) throws BubbaException {
        try {
            File file = new File(filePath);

            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            PrintWriter writer = new PrintWriter(file);

            for (Task task: tasks) {
                writer.println(task.toStorageString());
            }

            writer.close();
        } catch (FileNotFoundException e) {
            throw new BubbaException("Unable to save tasks to file!");
        }
    }
}
