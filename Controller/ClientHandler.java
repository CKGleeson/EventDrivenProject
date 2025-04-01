package Controller;

import Model.IncorrectActionException;
import Model.EventListSaver;
import Model.Event;
import Model.EventList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Handles communication between the server and a connected client.
 * Processes client commands related to event management.
 */
public class ClientHandler implements Runnable {
    private EventListSaver saver; // Handles event list saving
    private String saveFilePath; // File path for saving events
    private Socket clientSocket; // Client connection socket
    private EventList eventList; // List of scheduled events

    /**
     * Constructs a new ClientHandler.
     *
     * @param clientSocket the socket for client communication
     * @param eventList the event list to manage
     * @param saver the event list saver instance
     * @param saveFilePath the file path for saving events
     */
    public ClientHandler(Socket clientSocket, EventList eventList, EventListSaver saver, String saveFilePath) {
        this.clientSocket = clientSocket;
        this.eventList = eventList;
        this.saver = saver;
        this.saveFilePath = saveFilePath;
    }

    /**
     * Runs the client handler, processing client requests until termination.
     */
    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("STOP")) {
                    out.println("TERMINATE");
                    break;
                }
                String response = processCommand(inputLine, out);
                out.println(response);
            }
        } catch (Exception e) {
            System.err.println("ClientHandler exception: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception ex) {
                // Optionally log the error
            }
        }
    }

    /**
     * Processes a command sent by the client.
     *
     * @param command the command string received from the client
     * @param out the PrintWriter to send responses to the client
     * @return the response message based on the command execution
     */
    private String processCommand(String command, PrintWriter out) {
        String[] parts = command.split("\\|");
        String action = parts[0].toUpperCase();
        try {
            switch (action) {
                case "ADD":
                    if (parts.length != 8) {
                        throw new IncorrectActionException("Event Details Cannot Contain the character \"|\" ");
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String name = parts[1].trim();
                    LocalDateTime startTime = LocalDateTime.parse(parts[2].trim(), formatter);
                    LocalDateTime endTime = LocalDateTime.parse(parts[3].trim(), formatter);
                    String location = parts[4].trim();
                    String module = parts[5].trim();
                    String type = parts[6].trim();
                    String lecturer = parts[7].trim();
                    Event newEvent = new Event(name, startTime, endTime, location, module, type, lecturer);
                    eventList.add(newEvent);
                    saver.save(eventList.arrayList(), saveFilePath);
                    return "Event added successfully: " + newEvent;
                
                case "REMOVE":
                    if (parts.length != 3) {
                        throw new IncorrectActionException("REMOVE command requires 2 parameters.");
                    }
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String rName = parts[1].trim();
                    LocalDateTime rStartTime = LocalDateTime.parse(parts[2].trim(), dtf);
                    ArrayList<Event> removeEvents = eventList.alleventswithstarttime(rStartTime);
                    for (Event e : removeEvents) {
                        if (e.getName().equalsIgnoreCase(rName)) {
                            eventList.remove(e);
                            saver.save(eventList.arrayList(), saveFilePath);
                            return "Event removed successfully: " + e;
                        }
                    }
                    return "Error: Event not found.";
                
                case "LOAD_CSV":
                    ArrayList<Event> loadEvents = eventList.arrayList();
                    for (Event event : loadEvents) {
                        String line = String.join(",",
                            event.getName(),
                            event.getStartTime().toString(),
                            event.getEndTime().toString(),
                            event.getLocation(),
                            event.getModule(),
                            event.getType(),
                            event.getLecturer()
                        );
                        out.println(line);
                    }
                    return "END_CSV";
                
                case "DISPLAY":
                    ArrayList<Event> allEvents = eventList.arrayList();
                    if (allEvents.isEmpty()) return "No events scheduled.";
                    return EventList.sortList(allEvents).toString();
                
                default:
                    throw new IncorrectActionException("Unsupported action: " + action);
            }
        } catch (IncorrectActionException | IllegalArgumentException ex) {
            return "ERROR: " + ex.getMessage();
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }
}
