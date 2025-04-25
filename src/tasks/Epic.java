package tasks;

public class Epic extends Task {
    private final Type type = Type.EPIC;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        setStatus(Status.NEW);
    }

    @Override
    public Type getType() {
        return type;
    }
}
