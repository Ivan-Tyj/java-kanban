package Manager;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicTaskList = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    HistoryManager getDefaultHistory = Managers.getDefaultHistory();


    @Override
    public int getTaskId() {
        return taskId;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> listOfTasks = new ArrayList<>(taskList.size());
        listOfTasks.addAll(taskList.values());
        return listOfTasks;
    }

    @Override
    public ArrayList<Epic> getEpicTaskList() {
        ArrayList<Epic> listOfTasks = new ArrayList<>(epicTaskList.size());
        listOfTasks.addAll(epicTaskList.values());
        return listOfTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> listOfTasks = new ArrayList<>(subTaskList.size());
        listOfTasks.addAll(subTaskList.values());
        return listOfTasks;
    }

    @Override
    public void clearTask() {
        if (!taskList.isEmpty()) {
            taskList.clear();
        }
    }

    @Override
    public void clearEpicTask() {
        if (!epicTaskList.isEmpty()) {
            epicTaskList.clear();
            if (!subTaskList.isEmpty()) {
                subTaskList.clear();
            }
        }
    }

    @Override
    public void clearSubTask() {
        if (!subTaskList.isEmpty()) {
            subTaskList.clear();
            for (Integer key : epicTaskList.keySet()) {
                epicTaskList.get(key).setStatus(Status.NEW);
            }
        }
    }

    @Override
    public Task findTask(int findTaskId) {
        Task task = taskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(task);
        return task;
    }

    @Override
    public Epic findEpic(int findTaskId) {
        Epic epic = epicTaskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(epic);
        return epic;
    }

    @Override
    public SubTask findSub(int findTaskId) {
        SubTask subTask = subTaskList.getOrDefault(findTaskId, null);
        getDefaultHistory.add(subTask);
        return subTask;
    }

    @Override
    public boolean findEpicForSub(int epicId) {
        if (epicTaskList.containsKey(epicId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addTask(Task task) {
        taskId++;
        task.setTaskId(taskId);
        taskList.put(taskId, task);
        getDefaultHistory.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        taskId++;
        epic.setTaskId(taskId);
        epicTaskList.put(taskId, epic);
        getDefaultHistory.add(epic);
    }

    @Override
    public void addSub(SubTask sub) {
        taskId++;
        sub.setTaskId(taskId);
        subTaskList.put(taskId, sub);
        epicStatus(sub);
        getDefaultHistory.add(sub);
    }

    @Override
    public void epicStatus(SubTask subTask) {
        int statusNew = 0;
        int statusDone = 0;
        int statusInProgress = 0;
        int epicId = epicTaskList.get(subTask.getEpicId()).getTaskId();
        for (SubTask sub : subTaskList.values()) {
            if (sub.getEpicId() == epicId) {
                if (sub.getStatus().equals(Status.NEW)) {
                    statusNew++;
                } else if (sub.getStatus().equals(Status.DONE)) {
                    statusDone++;
                } else {
                    statusInProgress++;
                    break;
                }
            }
        }
        if (statusDone == 0 && statusInProgress == 0) {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.NEW);
        } else if (statusDone > 0 && statusNew == 0 && statusInProgress == 0) {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.DONE);
        } else {
            epicTaskList.get(subTask.getEpicId()).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateTask(Task newTask) {
        int key = newTask.getTaskId();
        taskList.put(key, newTask);
        getDefaultHistory.add(newTask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        int key = newEpic.getTaskId();
        epicTaskList.put(key, newEpic);
        getDefaultHistory.add(newEpic);
    }

    @Override
    public void updateSub(SubTask newSub) {
        int key = newSub.getTaskId();
        subTaskList.put(key, newSub);
        epicStatus(newSub);
        getDefaultHistory.add(newSub);
    }

    @Override
    public void removeTask(Integer key) {
        getDefaultHistory.add(taskList.get(key));
        taskList.remove(key);
    }

    @Override
    public void removeEpic(Integer key) {
        getDefaultHistory.add(epicTaskList.get(key));
        epicTaskList.remove(key);
        for (Integer keySub : subTaskList.keySet()) {
            if (subTaskList.get(keySub).getEpicId() == key) {
                subTaskList.remove(keySub);
            }
        }
    }

    @Override
    public void removeSub(Integer key) {
        getDefaultHistory.add(subTaskList.get(key));
        SubTask subTask = subTaskList.get(key);
        subTaskList.remove(key);
        epicStatus(subTask);
    }

    @Override
    public ArrayList subForEpic(int epicKey) {
        ArrayList<SubTask> subForEpic = new ArrayList<>();
        for (Integer key : subTaskList.keySet()) {
            SubTask subTask = subTaskList.get(key);
            if (subTask.getEpicId() == epicKey) {
                subForEpic.add(subTask);
            }
        }
        return subForEpic;
    }
}
