package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final Type type = Type.EPIC;
    private final List<SubTask> subForEpicList = new ArrayList<>();

    public Epic(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        setStatus(Status.NEW);
    }

    public List<SubTask> getSubForEpicList() {
        return new ArrayList<>(subForEpicList);
    }

    public void addSubForEpicList(SubTask subTask) {
        subForEpicList.add(subTask);
    }

    public void removeSubForEpicList(SubTask subTask) {
        subForEpicList.remove(subTask);
    }

    public void clearSubForEpicList() {
        subForEpicList.clear();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public LocalDateTime getStartTime() {
            List<SubTask> list = getSubForEpicList();
            return list.stream()
                    .min((Comparator.comparing(SubTask::getStartTime)))
                    .map(SubTask::getStartTime)
                    .orElseThrow(NullPointerException::new);
    }

    @Override
    public LocalDateTime getEndTime() {
            List<SubTask> list = getSubForEpicList();
            return list.stream()
                    .min((Comparator.comparing(SubTask::getEndTime)))
                    .map(SubTask::getEndTime)
                    .orElseThrow(NullPointerException::new);
    }

    @Override
    public Duration getDuration() {
        try {
            LocalDateTime first = getStartTime();
            LocalDateTime last = getEndTime();
            return Duration.between(first, last);
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public String toString() {
        return "\n" + getTaskId() + "," + getType() + "," + getName() + "," + getStatus()
                + "," + getDescription() + "," + getStartTime() + "," + getEndTime()
                + "," + getDuration().toMinutes();
    }
}
