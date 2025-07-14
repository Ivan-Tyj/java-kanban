import manager.*;
import org.junit.Before;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Before
    public void clearManager() throws IOException, ManagerSaveException {
        inMemoryTaskManager.clearTask();
        inMemoryTaskManager.clearEpicTask();
        inMemoryTaskManager.clearSubTask();
    }

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
    void clearTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        assertFalse(inMemoryTaskManager.getTaskList().isEmpty());
    }

    @Test
    void clearEpicTask() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic", "epic description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
    }

    @Test
    void clearSubTask() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic", "epic description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub", "sub description", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.clearSubTask();
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
        assertEquals(Status.NEW, inMemoryTaskManager.getEpicTaskList().getFirst().getStatus());
    }

    @Test
    void findTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        Task findTask = inMemoryTaskManager.findTask(task.getTaskId());
        Assertions.assertEquals(task.getTaskId(), findTask.getTaskId());
    }

    @Test
    void findEpic() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        Epic findEpic = inMemoryTaskManager.findEpic(epic.getTaskId());
        Assertions.assertEquals(epic.getTaskId(), findEpic.getTaskId());
    }

    @Test
    void findSub() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask);
        SubTask findSubTask = inMemoryTaskManager.findSub(subTask.getTaskId());
        Assertions.assertEquals(subTask.getTaskId(), findSubTask.getTaskId());
    }

    @Override
    @Test
    public void addTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
        inMemoryTaskManager.addTask(task);
        assertFalse(inMemoryTaskManager.getTaskList().isEmpty());
    }

    @Override
    @Test
    public void addEpic() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        inMemoryTaskManager.addEpic(epic);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
    }

    @Override
    @Test
    public void addSub() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.addSub(subTask);
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
    }

    @Test
    void epicStatus() throws ManagerSaveException, IOException {
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
    void updateTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addTask(task);
        Task newTask = new Task("task2", "task2 description", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.updateTask(newTask);
        assertEquals("task2", newTask.getName());
    }

    @Test
    void updateSub() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addSub(subTask1);
        SubTask subTask2 = new SubTask("sub2", "2", Status.DONE, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.updateSub(subTask2);
        assertEquals("sub2", subTask2.getName());
    }

    @Test
    void removeTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
        inMemoryTaskManager.addTask(task);
        assertFalse(inMemoryTaskManager.getTaskList().isEmpty());
        inMemoryTaskManager.removeTask(task.getTaskId());
        assertTrue(inMemoryTaskManager.getTaskList().isEmpty());
    }

    @Test
    void removeEpic() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        inMemoryTaskManager.addEpic(epic);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.addSub(subTask1);
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.removeEpic(epic.getTaskId());
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
    }

    @Test
    void removeSub() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic1", "1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getEpicTaskList().isEmpty());
        inMemoryTaskManager.addEpic(epic);
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        SubTask subTask1 = new SubTask("sub1", "1", Status.IN_PROGRESS, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.addSub(subTask1);
        assertFalse(inMemoryTaskManager.getSubTaskList().isEmpty());
        inMemoryTaskManager.removeSub(subTask1.getTaskId());
        assertFalse(inMemoryTaskManager.getEpicTaskList().isEmpty());
        assertTrue(inMemoryTaskManager.getSubTaskList().isEmpty());
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void isIntersectionTask() throws ManagerSaveException, IOException {
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(3));
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("task2", "task2 description", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 1, 1, 1, 2),
                Duration.ofMinutes(1));
        inMemoryTaskManager.addTask(task2);
        ArrayList<Task> list = inMemoryTaskManager.getPrioritizedTasks();
        assertEquals(2, list.size());
        assertTrue(inMemoryTaskManager.isIntersectionTask(inMemoryTaskManager.getPrioritizedTasks()));
    }
}