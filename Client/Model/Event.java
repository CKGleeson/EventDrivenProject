package Client.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a scheduled event such as a lecture, lab, or tutorial.
 * Contains details about the event's name, time, location, associated module, type, and lecturer.
 */
public class Event {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String module;
    private String type; // "Lecture", "Lab", or "Tutorial"
    private String lecturer;

    /**
     * Constructs an Event with specified details.
     *
     * @param name       the name/title of the event
     * @param startTime  the start date and time of the event
     * @param endTime    the end date and time of the event
     * @param location   the physical/virtual location of the event
     * @param module     the academic module associated with the event
     * @param type       the type of event ("Lecture", "Lab", or "Tutorial")
     * @param lecturer   the name of the lecturer/instructor
     */
    public Event(String name, LocalDateTime startTime, LocalDateTime endTime, String location, String module, String type, String lecturer) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.module = module;
        this.type = type;
        this.lecturer = lecturer;
    }

    // Getters and Setters

    /**
     * Gets the name of the event.
     * @return the event name
     */
    public String getName() { return name; }

    /**
     * Sets the name of the event.
     * @param name the new name for the event
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the start time of the event.
     * @return the start date and time
     */
    public LocalDateTime getStartTime() { return startTime; }

    /**
     * Sets the start time of the event.
     * @param startTime the new start date and time
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    /**
     * Gets the end time of the event.
     * @return the end date and time
     */
    public LocalDateTime getEndTime() { return endTime; }

    /**
     * Sets the end time of the event.
     * @param endTime the new end date and time
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    /**
     * Gets the location of the event.
     * @return the event location
     */
    public String getLocation() { return location; }

    /**
     * Sets the location of the event.
     * @param location the new location
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Gets the academic module associated with the event.
     * @return the module name/code
     */
    public String getModule() { return module; }

    /**
     * Sets the academic module associated with the event.
     * @param module the new module name/code
     */
    public void setModule(String module) { this.module = module; }

    /**
     * Gets the type of event ("Lecture", "Lab", or "Tutorial").
     * @return the event type
     */
    public String getType() { return type; }

    /**
     * Sets the type of event.
     * @param type the new type ("Lecture", "Lab", or "Tutorial")
     */
    public void setType(String type) { this.type = type; }

    /**
     * Gets the lecturer/instructor for the event.
     * @return the lecturer's name
     */
    public String getLecturer() { return lecturer; }

    /**
     * Sets the lecturer/instructor for the event.
     * @param lecturer the new lecturer's name
     */
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }

    /**
     * Extracts the date portion from the event's start time.
     * @return the date as a string in ISO-8601 format (YYYY-MM-DD)
     */
    public String getDate() {
        return startTime.toLocalDate().toString();
    }

    /**
     * Returns a formatted string representation of the event.
     * @return a string in the format: "Event: [name], Date: [date], Time: [start]-[end], Module: [module], Location: [location], Type: [type], Lecturer: [lecturer]"
     */
    @Override
    public String toString() {
        return String.format("Event: %s, Date: %s, Time: %s - %s, Module: %s, Location: %s, Type: %s, Lecturer: %s",
                name, getDate(), startTime.toLocalTime(), endTime.toLocalTime(), module, location, type, lecturer);
    }
    
    public String getFormattedTime() {
    return String.format("%s - %s",
        startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
        endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
}

public String getFormattedDetails() {
    return String.format("Location: %s | Module: %s | Lecturer: %s",
        location, module, lecturer);
}

}