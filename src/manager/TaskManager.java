package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicTaskList();

    ArrayList<SubTask> getSubTaskList();

    void clearTask() throws IOException, ManagerSaveException;

    void clearEpicTask() throws IOException, ManagerSaveException;

    void clearSubTask() throws IOException, ManagerSaveException;

    Task findTask(int findTaskId);

    Epic findEpic(int findTaskId);

    SubTask findSub(int findTaskId);

    boolean findEpicForSub(int epicId);

    void addTask(Task task) throws IOException, ManagerSaveException;

    void addEpic(Epic epic) throws IOException, ManagerSaveException;

    void addSub(SubTask sub) throws IOException, ManagerSaveException;

    void updateTask(Task newTask) throws IOException, ManagerSaveException;

    void updateEpic(Epic newEpic) throws IOException, ManagerSaveException;

    void updateSub(SubTask newSub) throws IOException, ManagerSaveException;

    void removeTask(Integer key) throws IOException, ManagerSaveException;

    void removeEpic(Integer key) throws IOException, ManagerSaveException;

    void removeSub(Integer key) throws IOException, ManagerSaveException;

    ArrayList<Task> subForEpic(int epicKey);

    ArrayList<Task> getHistory();

}
