package Tests;

import Manager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifEpicIdEqualsOtherEpicId() {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);

        Assertions.assertEquals(firstEpic.getTaskId(), inMemoryTaskManager.getTaskId(), "Epic равны по Id");
    }
}