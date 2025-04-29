import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import tasks.Status;
import tasks.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void add() throws IOException, ManagerSaveException {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryHistoryManager.add(task1);
        assertEquals("task1", inMemoryHistoryManager.getHistory().getFirst().getName());
        inMemoryTaskManager.removeTask(task1.getTaskId());
        assertEquals("task1", inMemoryHistoryManager.getHistory().getFirst().getName());
    }

    @Test
    void remove() throws IOException, ManagerSaveException {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryHistoryManager.add(task1);
        ArrayList<Task> list = inMemoryHistoryManager.getTasks();
        assertEquals("task1", list.getFirst().getName());
        inMemoryHistoryManager.remove(task1.getTaskId());
        list = inMemoryHistoryManager.getTasks();
        assertTrue(list.isEmpty());
    }
}