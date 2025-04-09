package test;

import Manager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void ifTaskIdEqualsOtherTaskId() {
        Epic firstEpic = new Epic("epic1", "epic1 description", Status.NEW);
        inMemoryTaskManager.addEpic(firstEpic);
        SubTask firstSub = new SubTask("sub", "sub description", Status.NEW, firstEpic.getTaskId());
        inMemoryTaskManager.addSub(firstSub);
        int subId = inMemoryTaskManager.getSubTaskList().indexOf(firstSub) + 2;

        Assertions.assertEquals(firstSub.getTaskId(), subId, "Subs равны по Id");
    }
}