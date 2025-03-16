package Tests;

import Manager.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import Tasks.Status;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void add() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryHistoryManager.add(task1);
        assertEquals("task1", inMemoryHistoryManager.getHistory().getFirst().getName());
        inMemoryTaskManager.removeTask(task1.getTaskId());
        assertEquals("task1", inMemoryHistoryManager.getHistory().getFirst().getName());
    }

}