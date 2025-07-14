package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    int getTaskId();

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicTaskList();

    ArrayList<SubTask> getSubTaskList();

    void clearTask();

    void clearEpicTask() throws IOException;

    void clearSubTask() throws IOException;

    Task findTask(int findTaskId);

    Epic findEpic(int findTaskId);

    SubTask findSub(int findTaskId);

    boolean findEpicForSub(int epicId);

    void addTask(Task task) throws IOException;

    void addEpic(Epic epic) throws IOException;

    void addSub(SubTask sub) throws IOException;

    void updateTask(Task newTask) throws IOException;

    void updateEpic(Epic newEpic) throws IOException;

    void updateSub(SubTask newSub) throws IOException;

    void removeTask(Integer key) throws IOException;

    void removeEpic(Integer key) throws IOException;

    void removeSub(Integer key) throws IOException;

    ArrayList<SubTask> subForEpic(int epicKey);

    List<Task> getHistory();

    ArrayList<Task> getPrioritizedTasks();

    ArrayList<SubTask> getPrioritizedSubTasks();

    <T extends Task> boolean isIntersectionTask(ArrayList<T> list);
}
