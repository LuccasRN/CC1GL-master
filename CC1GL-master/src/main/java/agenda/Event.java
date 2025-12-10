package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Event {
    private String myTitle;
    private LocalDateTime myStart;
    private Duration myDuration;
    private Repetition repetition;
    private Set<LocalDate> exceptions = new HashSet<>();

    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
        exceptions.add(date);
    }

    public void setTermination(LocalDate terminationInclusive) {
        if (repetition != null) {
            LocalDate startDate = myStart.toLocalDate();
            Termination termination = new Termination(startDate, repetition.getFrequency(), terminationInclusive);
            repetition.setTermination(termination);
        }
    }

    public void setTermination(long numberOfOccurrences) {
        if (repetition != null) {
            LocalDate startDate = myStart.toLocalDate();
            Termination termination = new Termination(startDate, repetition.getFrequency(), numberOfOccurrences);
            repetition.setTermination(termination);
        }
    }

    public int getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            return (int) repetition.getTermination().numberOfOccurrences();
        }
        return 1;
    }

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            return repetition.getTermination().terminationDateInclusive();
        }
        return myStart.toLocalDate();
    }

    public boolean isInDay(LocalDate aDay) {
    LocalDate startDate = myStart.toLocalDate();
    
    if (repetition == null) {
        LocalDateTime eventEnd = myStart.plus(myDuration);
        LocalDate endDate = eventEnd.toLocalDate();
        return !aDay.isBefore(startDate) && !aDay.isAfter(endDate);
    }
    
    if (aDay.isBefore(startDate)) {
        return false;
    }
    
    if (exceptions.contains(aDay)) {
        return false;
    }
    
    ChronoUnit frequency = repetition.getFrequency();
    
    long unitsBetween = frequency.between(startDate, aDay);
    
    for (long offset = Math.max(0, unitsBetween - 1); offset <= unitsBetween; offset++) {
        LocalDate occurrenceDate = startDate.plus(offset, frequency);
        
        if (repetition.getTermination() != null) {
            long maxOccurrences = repetition.getTermination().numberOfOccurrences();
            if (offset >= maxOccurrences) {
                continue; 
            }
            
            LocalDate terminationDate = repetition.getTermination().terminationDateInclusive();
            if (occurrenceDate.isAfter(terminationDate)) {
                continue; 
            }
        }
        
        if (exceptions.contains(occurrenceDate)) {
            continue;
        }
        
        LocalDateTime occurrenceStart = LocalDateTime.of(occurrenceDate, myStart.toLocalTime());
        LocalDateTime occurrenceEnd = occurrenceStart.plus(myDuration);
        LocalDate occurrenceEndDate = occurrenceEnd.toLocalDate();
        
        if (!aDay.isBefore(occurrenceDate) && !aDay.isAfter(occurrenceEndDate)) {
            return true;
        }
    }
    
        return false;
    }

    public String getTitle() {
        return myTitle;
    }

    public LocalDateTime getStart() {
        return myStart;
    }

    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}

