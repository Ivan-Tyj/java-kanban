package test;

import manager.InMemoryTaskManager;
import Tasks.Status;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifTaskIdEqualsOtherTaskId() {
        Task firstTask = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(firstTask);
        int epicId = inMemoryTaskManager.getTaskList().indexOf(firstTask) + 1;

        Assertions.assertEquals(firstTask.getTaskId(), epicId, "Таски равны по Id");
    }
}