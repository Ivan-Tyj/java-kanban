package Tests;

import Manager.InMemoryTaskManager;
import Tasks.Status;
import Tasks.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifTaskIdEqualsOtherTaskId() {
        SubTask firstSub = new SubTask("sub", "sub description", Status.NEW, 1);
        inMemoryTaskManager.addTask(firstSub);

        Assertions.assertEquals(firstSub.getTaskId(), inMemoryTaskManager.getTaskId(), "Subs равны по Id");
    }
}