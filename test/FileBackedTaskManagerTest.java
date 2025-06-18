import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {



    @Override
    @Test
    public void addTask() throws ManagerSaveException, IOException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        Path path = Files.createTempFile(Path.of("test"), ".txt", "src");
        assertTrue(Files.exists(path));
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(path);
        backedTaskManager.addTask(task);
        List<String> list = Files.readAllLines(path);
        assertEquals(2, list.size());
        Files.delete(path);
    }

    @Override
    @Test
    public void addEpic() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic", "epic description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        Path path = Files.createTempFile(Path.of("test"), ".txt", "src");
        assertTrue(Files.exists(path));
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(path);
        backedTaskManager.addEpic(epic);
        List<String> list = Files.readAllLines(path);
        assertEquals(2, list.size());
        Files.delete(path);
    }

    @Override
    @Test
    public void addSub() throws ManagerSaveException, IOException {
        Epic epic = new Epic("epic", "epic description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        SubTask subTask = new SubTask("sub", "sub description", Status.NEW, epic.getTaskId(),
                LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        Path path = Files.createTempFile(Path.of("test"), ".txt", "src");
        assertTrue(Files.exists(path));
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(path);
        backedTaskManager.addEpic(epic);
        backedTaskManager.addSub(subTask);
        List<String> list = Files.readAllLines(path);
        assertEquals(3, list.size());
        Files.delete(path);
    }
}