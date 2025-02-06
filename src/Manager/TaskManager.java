package Manager;

import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Task> epicTaskList = new HashMap<>();
    private final HashMap<Integer, Task> subTaskList = new HashMap<>();

    public int getTaskId() {
        return taskId;
    }
    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }
    public HashMap<Integer, Task> getEpicTaskList() {
        return epicTaskList;
    }
    public HashMap<Integer, Task> getSubTaskList() {
        return subTaskList;
    }

    public ArrayList getListOfTask(HashMap map) {
        ArrayList<Object> listOfTasks = new ArrayList<>(map.size());
        listOfTasks.addAll(map.values());
        return listOfTasks;
    }

    public void clearAllTask(HashMap map) {
        if (!map.isEmpty()) {
            map.clear();
        }
    }

    public Task findTask(int findTaskId) {
        if (epicTaskList.containsKey(findTaskId)) {
            return epicTaskList.get(findTaskId);
        } else if (subTaskList.containsKey(findTaskId)) {
            return subTaskList.get(findTaskId);
        } else if (taskList.containsKey(findTaskId)) {
            return taskList.get(findTaskId);
        } else {
            return null;
        }
    }

    public boolean findEpicForSub(int epicId) {
        if (epicTaskList.containsKey(epicId)) {
            return true;
        } else {
            return false;
        }
    }

    public void addTaskList(Task task) {
        taskId++;
        taskList.put(taskId, task);
    }
    public void addEpicList(Epic task) {
        taskId++;
        epicTaskList.put(taskId, task);
    }
    public void addSubList(SubTask task) {
        taskId++;
        subTaskList.put(taskId, task);
        newEpicStatus(task);
    }

    private void newEpicStatus(SubTask task) {
        if (epicTaskList.containsKey(task.getEpicId())) {
            Epic epic = (Epic) epicTaskList.get(task.getEpicId());
            if (epic.getStatus() == Status.NEW && epic.getStatus() == task.getStatus()) {
                epicTaskList.get(task.getEpicId()).setStatus(Status.NEW);
            } else if ((epic.getStatus() == Status.NEW || epic.getStatus() == Status.DONE)
                    && task.getStatus() == Status.DONE) {
                epicTaskList.get(task.getEpicId()).setStatus(Status.DONE);
            } else {
                epicTaskList.get(task.getEpicId()).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateTask(Integer key, Task newTask) {
        taskList.put(key, newTask);
    }
    public void updateEpic(Integer key, Epic newEpic) {
        epicTaskList.put(key, newEpic);
    }
    public void updateSub(Integer key, SubTask newSub) {
        subTaskList.put(key, newSub);
    }

    public void removeTask(Integer key) {
        taskList.remove(key);
    }
    public void removeEpic(Integer key) {
        epicTaskList.remove(key);
    }
    public void removeSub(Integer key) {
        subTaskList.remove(key);
    }

    public ArrayList subForEpic(int epicKey) {
        ArrayList<SubTask> subForEpic = new ArrayList<>();
        for (Integer key : subTaskList.keySet()) {
            SubTask subTask = (SubTask) subTaskList.get(key);
            if (subTask.getEpicId() == epicKey) {
                subForEpic.add(subTask);
            }
        }
        System.out.println(subForEpic);
        return subForEpic;
    }
}
