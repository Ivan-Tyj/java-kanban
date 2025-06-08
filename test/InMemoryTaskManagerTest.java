import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
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
    void clearTask() throws ManagerSaveException {
        Task firstTask = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(firstTask);
        inMemoryTaskManager.clearTask();
        ArrayList<Task> arrayList = inMemoryTaskManager.getTaskList();
        Assertions.assertEquals(0, arrayList.size());
    }

    @Test
    void clearEpicTask() throws ManagerSaveException {
        Epic firstEpic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask subTask = new SubTask("sub1", "1", Status.NEW, 1,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        inMemoryTaskManager.clearEpicTask();
        ArrayList<Epic> epicList = inMemoryTaskManager.getEpicTaskList();
        Assertions.assertEquals(0, epicList.size());
        ArrayList<SubTask> subList = inMemoryTaskManager.getSubTaskList();
        Assertions.assertEquals(0, subList.size());
    }

    @Test
    void clearSubTask() throws ManagerSaveException {
        Epic firstEpic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, firstEpic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        inMemoryTaskManager.clearSubTask();
        ArrayList<SubTask> arrayList = inMemoryTaskManager.getSubTaskList();
        Assertions.assertEquals(0, arrayList.size());
        Assertions.assertEquals(Status.NEW, firstEpic.getStatus(), "Статус эпика обновлен");
    }

    @Test
    void findTask() throws ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        Task findTask = inMemoryTaskManager.findTask(task.getTaskId());
        Assertions.assertEquals(task.getTaskId(), findTask.getTaskId(), "Задача найдена");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void findEpic() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        Epic findEpic = inMemoryTaskManager.findEpic(epic.getTaskId());
        Assertions.assertEquals(epic.getTaskId(), findEpic.getTaskId(), "Эпик найден");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void findSub() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        SubTask findSubTask = inMemoryTaskManager.findSub(subTask.getTaskId());
        Assertions.assertEquals(subTask.getTaskId(), findSubTask.getTaskId(), "Подзадача найдена");
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addTask() throws ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        int taskId = inMemoryTaskManager.getTaskList().indexOf(task) + 1;
        assertTrue(taskId > 0);
        assertFalse(inMemoryTaskManager.getTaskList().isEmpty());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addEpic() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        int epicId = inMemoryTaskManager.getEpicTaskList().indexOf(epic) + 1;
        assertTrue(epicId > 0);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void addSub() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        assertEquals(2, subTask.getTaskId());
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        assertFalse(getDefaultHistory.getHistory().isEmpty());
    }

    @Test
    void epicStatus() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        assertEquals(Status.NEW, epic.getStatus());

        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask2 = new SubTask("sub2", "2", Status.NEW, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask2);
        assertEquals(Status.NEW, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask3 = new SubTask("sub3", "3", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask3);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void updateTask() throws ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        Task newTask = new Task("task2", "task2 description", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.updateTask(newTask);
        assertEquals("task2", newTask.getName());
    }

    @Test
    void updateEpic() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        Epic newEpic = new Epic("epic2", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.updateEpic(newEpic);
        assertEquals("epic2", newEpic.getName());
    }

    @Test
    void updateSub() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        assertFalse(getDefaultHistory.getHistory().isEmpty());
        SubTask subTask2 = new SubTask("sub2", "2", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.updateSub(subTask2);
        assertEquals("sub2", subTask2.getName());
    }

    @Test
    void removeTask() throws ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        inMemoryTaskManager.removeTask(task.getTaskId());
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
    }

    @Test
    void removeEpic() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        inMemoryTaskManager.removeEpic(epic.getTaskId());
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
    }

    @Test
    void removeSub() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        inMemoryTaskManager.removeSub(subTask1.getTaskId());
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        assertEquals(Status.NEW, epic.getStatus());
    }
    @Test void isEpicStatus() throws ManagerSaveException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.NEW, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        SubTask subTask2 = new SubTask("sub1", "1", Status.NEW, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 12, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask2);
        assertSame(Status.NEW, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask3 = new SubTask("sub1", "1", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask3);
        SubTask subTask4 = new SubTask("sub1", "1", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 12, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask4);
        assertSame(Status.DONE, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask5 = new SubTask("sub1", "1", Status.NEW, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask5);
        SubTask subTask6 = new SubTask("sub1", "1", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 12, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask6);
        assertSame(Status.IN_PROGRESS, epic.getStatus());
        inMemoryTaskManager.clearSubTask();

        SubTask subTask9 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask9);
        SubTask subTask10 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 12, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask10);
        assertSame(Status.IN_PROGRESS, epic.getStatus());
        inMemoryTaskManager.clearSubTask();
    }

    @Test
    public void isIntersectionTask() throws ManagerSaveException {
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(3));
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 2),
                Duration.ofMinutes(1));
        inMemoryTaskManager.addTask(task2);
        ArrayList<Task> list = inMemoryTaskManager.getPrioritizedTasks();
        assertEquals(2, list.size());
        assertTrue(inMemoryTaskManager.isIntersectionTask());
    }

}