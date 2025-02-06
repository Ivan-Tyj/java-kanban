package Tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (this.getClass() != obj.getClass())) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status);
    }
    @Override
    public int hashCode() {
        int hash = 11;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 13;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash += status.hashCode();
        }
        return hash;
    }
}