package test;

import manager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifEpicIdEqualsOtherEpicId() {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        int epicId = inMemoryTaskManager.getEpicTaskList().indexOf(firstEpic) + 1;

        Assertions.assertEquals(firstEpic.getTaskId(), epicId, "Epic равны по Id");
    }
}