import http.HttpTaskServer;
import manager.InMemoryTaskManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        httpTaskServer.start(manager);
        System.out.println("Сервер запущен, слушает порт 8080");
    }
}
