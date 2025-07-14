package http;

import com.sun.net.httpserver.HttpServer;
import http.handlers.subs.*;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final String TASKS_PATH = "/tasks";
    private static final String SUB_TASKS_PATH = "/subtasks";
    private static final String EPICS_PATH = "/epics";
    private static final String HISTORY_PATH = "/history";
    private static final String PRIORITIZED_PATH = "/prioritized";
    private HttpServer httpServer;

    public void start(InMemoryTaskManager manager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext(TASKS_PATH, new TasksHttpHandler(manager));
            httpServer.createContext(SUB_TASKS_PATH, new SubHttpHandler(manager));
            httpServer.createContext(EPICS_PATH, new EpicHttpHandler(manager));
            httpServer.createContext(HISTORY_PATH, new HistoryHttpHandler(manager));
            httpServer.createContext(PRIORITIZED_PATH, new PrioritizedHttpHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}
