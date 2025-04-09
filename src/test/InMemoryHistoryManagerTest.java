package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import allTask.Status;
import allTask.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


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
    
    @Test
    void remove() {
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