package Controller;

import Model.Event;
import Model.EventList;
import Model.EventListSaver;
import View.ServerStatusView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LectureSchedulerServer extends Application {
    public static final int PORT = 12345;
    private static final String SAVE_FILE = "events.csv";
    private static ServerStatusView statusView;

    public static void main(String[] args) {
        // Start JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the status view
        statusView = new ServerStatusView();
        statusView.start(primaryStage);

        // Start server in a new thread
        new Thread(this::startServer).start();
    }

    private void startServer() {
        EventList eventList = new EventList();
        EventListSaver saver = new EventListSaver();
        
        try {
            ArrayList<Event> savedEvents = saver.load(SAVE_FILE);
            eventList.setArrayList(savedEvents);
        } catch (IOException e) {
            System.err.println("Failed to load events: " + e.getMessage());
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                ClientHandler handler = new ClientHandler(clientSocket, eventList, saver, SAVE_FILE);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            try {
                saver.save(eventList.arrayList(), SAVE_FILE);
            } catch (IOException e) {
                System.err.println("Failed to save events: " + e.getMessage());
            }
        }
    }
}