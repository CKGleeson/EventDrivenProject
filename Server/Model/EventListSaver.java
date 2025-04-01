package Server.Model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Handles saving and loading of event data to and from a CSV file.
 */
public class EventListSaver {

    /**
     * Loads events from a CSV file.
     *
     * @param filePath The path to the CSV file.
     * @return A list of events loaded from the file.
     * @throws IOException If an error occurs while reading the file.
     */
    public ArrayList<Event> load(String filePath) throws IOException {
        ArrayList<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                // CSV columns: name, startTime, endTime, location, module, type, lecturer
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // Ensure all fields are present
                String name = parts[0].trim();
                LocalDateTime startTime = LocalDateTime.parse(parts[1].trim());
                LocalDateTime endTime = LocalDateTime.parse(parts[2].trim());
                String location = parts[3].trim();
                String module = parts[4].trim();
                String type = parts[5].trim();
                String lecturer = parts[6].trim();
                
                events.add(new Event(name, startTime, endTime, location, module, type, lecturer));
            }
        }
        return events;
    }
    
    /**
     * Saves a list of events to a CSV file.
     *
     * @param events The list of events to save.
     * @param filePath The path to the CSV file.
     * @throws IOException If an error occurs while writing the file.
     */
    public void save(ArrayList<Event> events, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Event event : events) {
                String line = String.join(",",
                        event.getName(),
                        event.getStartTime().toString(),
                        event.getEndTime().toString(),
                        event.getLocation(),
                        event.getModule(),
                        event.getType(),
                        event.getLecturer());
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Main method for testing event saving functionality.
     */
//    public static void main(String[] args) {
//        ArrayList<Event> events = new ArrayList<>();
//        // Example 1: A lecture event
//        events.add(new Event("Maths tut",
//                LocalDateTime.of(2025, 3, 10, 9, 0, 0),
//                LocalDateTime.of(2025, 3, 10, 10, 0, 0),
//                "CS3005b", "MS101", "Tutorial", "Dr. Smith"));
//        // Example 2: A lab event
//        events.add(new Event("Comp Sci Lec",
//                LocalDateTime.of(2025, 3, 10, 11, 0, 0),
//                LocalDateTime.of(2025, 3, 10, 12, 0, 0),
//                "CSG001", "CS4115", "Lecture", "Dr. Mann"));
//
//        EventListSaver saver = new EventListSaver();
//        String filePath = "example_events.csv";
//        
//        try {
//            saver.save(events, filePath);
//            System.out.println("Events saved successfully to " + filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
