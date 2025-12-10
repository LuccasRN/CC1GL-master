package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Agenda {
    private List<Event> events = new ArrayList<>();

    public void addEvent(Event e) {
        events.add(e);
    }

    public List<Event> eventsInDay(LocalDate day) {
        return events.stream()
                .filter(event -> event.isInDay(day))
                .collect(Collectors.toList());
    }
    
    public List<Event> findByTitle(String title) {
        return events.stream()
                .filter(event -> event.getTitle().equals(title))
                .collect(Collectors.toList());
    }
    
    public boolean isFreeFor(Event e) {
        LocalDate eventStart = e.getStart().toLocalDate();
        LocalDate eventEnd = e.getStart().plus(e.getDuration()).toLocalDate();
        
        for (LocalDate day = eventStart; !day.isAfter(eventEnd); day = day.plusDays(1)) {
            for (Event existingEvent : events) {
                if (existingEvent.isInDay(day) && eventsOverlap(e, existingEvent)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean eventsOverlap(Event e1, Event e2) {
        LocalDateTime start1 = e1.getStart();
        LocalDateTime end1 = start1.plus(e1.getDuration());
        LocalDateTime start2 = e2.getStart();
        LocalDateTime end2 = start2.plus(e2.getDuration());
        
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}

