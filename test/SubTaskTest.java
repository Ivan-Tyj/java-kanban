import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SubTaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifTaskIdEqualsOtherTaskId() throws IOException, ManagerSaveException {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask firstSub = new SubTask("sub", "sub description", Status.NEW, firstEpic.getTaskId());
        inMemoryTaskManager.addSub(firstSub);
        int subId = inMemoryTaskManager.getSubTaskList().indexOf(firstSub) + 2;

        Assertions.assertEquals(firstSub.getTaskId(), subId, "Subs равны по Id");
    }
}