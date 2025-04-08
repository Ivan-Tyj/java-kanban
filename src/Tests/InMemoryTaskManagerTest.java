package Tests;

import Manager.HistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    HistoryManager getDefaultHistory = Managers.getDefaultHistory();

    @Test
    void getTaskList() {
        ArrayList<Task> arrayList = inMemoryTaskManager.getTaskList();
        Assertions.assertNotNull(arrayList);
    }

    @Test
    void getEpicTaskList() {
        ArrayList<Epic> arrayList = inMemoryTaskManager.getEpicTaskList();
        Assertions.assertNotNull(arrayList);
    }

    @Test
    void getSubTaskList() {
        ArrayList<SubTask> arrayList = inMemoryTaskManager.getSubTaskList();
        Assertions.assertNotNull(arrayList);
    }

    @Test
    void clearTask() {
        Task firstTask = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(firstTask);
        inMemoryTaskManager.clearTask();
        ArrayList<Task> arrayList = inMemoryTaskManager.getTaskList();
        Assertions.assertEquals(0, arrayList.size());
    }

    @Test
    void clearEpicTask() {
        Epic firstEpic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask subTask = new SubTask("sub1", "1", Status.NEW, 1);
        inMemoryTaskManager.addSub(subTask);
        inMemoryTaskManager.clearEpicTask();
        ArrayList<Epic> epicList = inMemoryTaskManager.getEpicTaskList();
        Assertions.assertEquals(0, epicList.size());
        ArrayList<SubTask> subList = inMemoryTaskManager.getSubTaskList();
        Assertions.assertEquals(0, subList.size());
    }

    @Test
    void clearSubTask() {
        Epic firstEpic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, firstEpic.getTaskId());
        inMemoryTaskManager.addSub(subTask);
        inMemoryTaskManager.clearSubTask();
        ArrayList<SubTask> arrayList = inMemoryTaskManager.getSubTaskList();
        Assertions.assertEquals(0, arrayList.size());
        Assertions.assertEquals(Status.NEW, firstEpic.getStatus(), "Статус эпика обновлен");
    }

    @Test
    void findTask() {
        Task task = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task);
        Task findTask = inMemoryTaskManager.findTask(task.getTaskId());
        Assertions.assertEquals(task.getTaskId(), findTask.getTaskId(), "Задача найдена");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void findEpic() {
        Epic epic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(epic);
        Epic findEpic = inMemoryTaskManager.findEpic(epic.getTaskId());
        Assertions.assertEquals(epic.getTaskId(), findEpic.getTaskId(), "Эпик найден");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void findSub() {
        Epic epic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask);
        SubTask findSubTask = inMemoryTaskManager.findSub(subTask.getTaskId());
        Assertions.assertEquals(subTask.getTaskId(), findSubTask.getTaskId(), "Подзадача найдена");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addTask() {
        Task task = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task);
        int taskId = inMemoryTaskManager.getTaskList().indexOf(task) + 1;
        assertTrue(taskId > 0);
        assertFalse(inMemoryTaskManager.getTaskList().isEmpty());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(epic);
        int epicId = inMemoryTaskManager.getEpicTaskList().indexOf(epic) + 1;
        assertTrue(epicId > 0);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addSub() {
        Epic epic = new Epic("epic1", "1", Status.NEW);
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask);
        assertEquals(2, subTask.getTaskId());
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void epicStatus() {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());

        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask2 = new SubTask("sub2", "2", Status.NEW, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask2);
        assertEquals(Status.NEW, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask3 = new SubTask("sub3", "3", Status.DONE, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask3);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void updateTask() {
        Task task = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        Task newTask = new Task("task2", "task2 description", Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(newTask);
        assertEquals("task2", newTask.getName());
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        Epic newEpic = new Epic("epic2", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.updateEpic(newEpic);
        assertEquals("epic2", newEpic.getName());
    }

    @Test
    void updateSub() {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask1);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        SubTask subTask2 = new SubTask("sub2", "2", Status.DONE, epic.getTaskId());
        inMemoryTaskManager.updateSub(subTask2);
        assertEquals("sub2", subTask2.getName());
    }

    @Test
    void removeTask() {
        Task task = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.removeTask(task.getTaskId());
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask1);
        inMemoryTaskManager.removeEpic(epic.getTaskId());
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
    }

    @Test
    void removeSub() {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId());
        inMemoryTaskManager.addSub(subTask1);
        inMemoryTaskManager.removeSub(subTask1.getTaskId());
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        assertEquals(Status.NEW, epic.getStatus());
    }

}