import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {


    @Test
    void addTask() throws IOException, ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW);
        Path path = Files.createTempFile(Path.of("test"), ".txt", "src");
        assertTrue(Files.exists(path));
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(path);
        backedTaskManager.addTask(task);
        List<String> list = Files.readAllLines(path);
        assertEquals(2, list.size());
        Files.delete(path);
    }
}