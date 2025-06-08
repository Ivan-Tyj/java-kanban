import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {

    @Test
    void addTask() throws IOException, ManagerSaveException {
        Task task = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        Epic epic = new Epic("e1", "e", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 1, 1),
                Duration.ofMinutes(10));
        Path path = Files.createTempFile(Path.of("test"), ".txt", "src");
        assertTrue(Files.exists(path));
        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(path);
        backedTaskManager.addTask(task);
        backedTaskManager.addEpic(epic);
        List<String> list = Files.readAllLines(path);
        assertEquals(3, list.size());
        Files.delete(path);
    }
}