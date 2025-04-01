package Server.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Manages a collection of events with conflict detection and filtering capabilities.
 * Provides methods to add, remove, and query events based on various criteria.
 */
public class EventList {
    private static ArrayList<Event> eventList = new ArrayList<>();

    /**
     * Adds an event to the list after checking for time conflicts on the same date.
     * @param event The event to be added
     * @throws IllegalArgumentException if there's a time overlap with existing events
     */
    public void add(Event event) {
        ArrayList<Event> eventsOnSameDate = alleventsondate(event.getStartTime().toLocalDate());
        for (Event existingEvent : eventsOnSameDate) {
            if (event.getStartTime().isBefore(existingEvent.getEndTime()) &&
                event.getEndTime().isAfter(existingEvent.getStartTime())) {
                throw new IllegalArgumentException("Event time conflict with: " + existingEvent);
            }
        }
        eventList.add(event);
    }

    /**
     * Removes an event from the list.
     * @param event The event to be removed
     * @throws IllegalArgumentException if the event is not found in the list
     */
    public void remove(Event event) {
        if (!eventList.remove(event)) {
            throw new IllegalArgumentException("Event not found: " + event);
        }
    }

    /**
     * Returns the underlying ArrayList of events.
     * @return A list containing all events
     */
    public ArrayList<Event> arrayList() {
        return eventList;
    }

    /**
     * Replaces the current list of events with a new collection.
     * @param events The new list of events to use
     */
    public void setArrayList(ArrayList<Event> events) {
        eventList = events;
    }

    /**
     * Retrieves all events occurring on a specific date.
     * @param date The target date to filter events
     * @return A list of events occurring on the specified date
     */
    public ArrayList<Event> alleventsondate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getStartTime().toLocalDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by name (case-insensitive).
     * @param name The name to search for
     * @return A list of events with matching names
     */
    public ArrayList<Event> alleventswithname(String name) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getName().equalsIgnoreCase(name)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by exact start time.
     * @param startTime The start time to match
     * @return A list of events starting at the specified time
     */
    public ArrayList<Event> alleventswithstarttime(LocalDateTime startTime) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getStartTime().equals(startTime)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by exact end time.
     * @param endTime The end time to match
     * @return A list of events ending at the specified time
     */
    public ArrayList<Event> alleventswithendtime(LocalDateTime endTime) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEndTime().equals(endTime)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by location (case-insensitive).
     * @param location The location to search for
     * @return A list of events at the specified location
     */
    public ArrayList<Event> alleventswithlocation(String location) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getLocation().equalsIgnoreCase(location)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by module (case-insensitive).
     * @param module The module to search for
     * @return A list of events associated with the specified module
     */
    public ArrayList<Event> alleventswithmodule(String module) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getModule().equalsIgnoreCase(module)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by type (case-insensitive).
     * @param type The event type to search for ("Lecture", "Lab", or "Tutorial")
     * @return A list of events of the specified type
     */
    public ArrayList<Event> alleventswithtype(String type) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getType().equalsIgnoreCase(type)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Filters events by lecturer (case-insensitive).
     * @param lecturer The lecturer's name to search for
     * @return A list of events taught by the specified lecturer
     */
    public ArrayList<Event> alleventswithlecturer(String lecturer) {
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getLecturer().equalsIgnoreCase(lecturer)) {
                events.add(event);
            }
        }
        return events;
    }

    /**
     * Sorts a list of events chronologically by start time, using end time as a tiebreaker.
     * @param events The list of events to sort
     * @return A new sorted list in chronological order
     */
    public static ArrayList<Event> sortList(ArrayList<Event> events) {
        ArrayList<Event> unsorted = new ArrayList<>(events);
        ArrayList<Event> sorted = new ArrayList<>();
        
        while (!unsorted.isEmpty()) {
            int earliestIndex = 0;
            for (int i = 1; i < unsorted.size(); i++) {
                Event current = unsorted.get(i);
                Event earliest = unsorted.get(earliestIndex);
                int cmp = current.getStartTime().compareTo(earliest.getStartTime());
                if (cmp < 0 || (cmp == 0 && current.getEndTime().compareTo(earliest.getEndTime()) < 0)) {
                    earliestIndex = i;
                }
            }
            sorted.add(unsorted.remove(earliestIndex));
        }
        return sorted;
    }
}