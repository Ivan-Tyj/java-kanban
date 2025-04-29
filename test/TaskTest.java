import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import tasks.Status;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifTaskIdEqualsOtherTaskId() throws IOException, ManagerSaveException {
        Task firstTask = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.addTask(firstTask);
        int epicId = inMemoryTaskManager.getTaskList().indexOf(firstTask) + 1;

        Assertions.assertEquals(firstTask.getTaskId(), epicId, "Таски равны по Id");
    }
}