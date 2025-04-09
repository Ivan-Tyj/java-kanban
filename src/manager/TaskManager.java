package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicTaskList();

    ArrayList<SubTask> getSubTaskList();

    void clearTask();

    void clearEpicTask();

    void clearSubTask();

    Task findTask(int findTaskId);

    Epic findEpic(int findTaskId);

    SubTask findSub(int findTaskId);

    boolean findEpicForSub(int epicId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSub(SubTask sub);

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSub(SubTask newSub);

    void removeTask(Integer key);

    void removeEpic(Integer key);

    void removeSub(Integer key);

    ArrayList<Task> subForEpic(int epicKey);

    ArrayList<Task> getHistory();

}
