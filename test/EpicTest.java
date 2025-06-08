import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifEpicIdEqualsOtherEpicId() throws IOException, ManagerSaveException {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        inMemoryTaskManager.addEpic(firstEpic);
        int epicId = inMemoryTaskManager.getEpicTaskList().indexOf(firstEpic) + 1;

        Assertions.assertEquals(firstEpic.getTaskId(), epicId, "Epic равны по Id");
    }
}