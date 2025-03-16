package Tests;

import Manager.InMemoryTaskManager;
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

        Assertions.assertEquals(firstTask.getTaskId(), inMemoryTaskManager.getTaskId(), "Таски равны по Id");
    }
}