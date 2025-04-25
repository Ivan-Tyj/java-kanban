import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifEpicIdEqualsOtherEpicId() throws IOException, ManagerSaveException {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        int epicId = inMemoryTaskManager.getEpicTaskList().indexOf(firstEpic) + 1;

        Assertions.assertEquals(firstEpic.getTaskId(), epicId, "Epic равны по Id");
    }
}