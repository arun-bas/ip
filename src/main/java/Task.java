public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void done() {
        isDone = true;
    }

    public void undoDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        if (isDone) {
            return "[X] " + description;

        } else {
            return "[ ] " + description;
        }
    }
}
