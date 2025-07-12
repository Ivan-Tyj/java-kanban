package http;

import com.sun.net.httpserver.HttpServer;
import http.handlers.subs.*;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final String tasksPath = "/tasks";
    private static final String subTasksPath = "/subtasks";
    private static final String epicsPath = "/epics";
    private static final String historyPath = "/history";
    private static final String prioritizedPath = "/prioritized";
    HttpServer httpServer;

    public void start(InMemoryTaskManager manager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext(tasksPath, new TasksHttpHandler(manager));
            httpServer.createContext(subTasksPath, new SubHttpHandler(manager));
            httpServer.createContext(epicsPath, new EpicHttpHandler(manager));
            httpServer.createContext(historyPath, new HistoryHttpHandler(manager));
            httpServer.createContext(prioritizedPath, new PrioritizedHttpHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}
