import manager.ManagerSaveException;
import manager.TaskManager;
import java.io.IOException;

abstract class TaskManagerTest {


    public abstract void addTask() throws ManagerSaveException, IOException;

    public abstract void addEpic() throws ManagerSaveException, IOException;

    public abstract void addSub() throws ManagerSaveException, IOException;
}
