package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    // November 1st, 2020, 22:30, 120 minutes
    Event simple;

    // Un événement qui se répète toutes les semaines et se termine à une date
    // donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre
    // donné d'occurrences
    Event fixedRepetitions;

    // A daily repetitive event, never ending
    // Un événement répétitif quotidien, sans fin
    // November 1st, 2020, 22:30, 120 minutes
    Event neverEnding;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", nov_1_2020_22_30, min_120);

        fixedTermination = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);

        neverEnding = new Event("Never Ending", nov_1_2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }

    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(),
                "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    // Tests pour findByTitle()
    @Test
    public void testFindByTitle() {
        List<Event> found = agenda.findByTitle("Simple event");
        assertEquals(1, found.size(), 
                "Devrait trouver 1 événement avec ce titre");
        assertTrue(found.contains(simple));
    }

    @Test
    public void testFindByTitleMultipleResults() {
        List<Event> found = agenda.findByTitle("Fixed termination weekly");
        assertEquals(2, found.size(), 
                "Devrait trouver 2 événements avec le même titre");
    }

    @Test
    public void testFindByTitleNoMatch() {
        List<Event> found = agenda.findByTitle("Inexistant");
        assertTrue(found.isEmpty(), 
                "Ne devrait trouver aucun événement");
    }

    // Tests pour isFreeFor()
    @Test
    public void testIsFreeForNonOverlapping() {
        LocalDateTime morningTime = LocalDateTime.of(2020, 11, 1, 10, 0);
        Event morningEvent = new Event("Morning", morningTime, Duration.ofMinutes(60));
        
        assertTrue(agenda.isFreeFor(morningEvent), 
                "Devrait être libre le matin");
    }

    @Test
    public void testIsNotFreeForOverlapping() {
        LocalDateTime overlappingTime = LocalDateTime.of(2020, 11, 1, 23, 0);
        Event overlapping = new Event("Overlap", overlappingTime, Duration.ofMinutes(60));
        
        assertFalse(agenda.isFreeFor(overlapping), 
                "Ne devrait pas être libre (chevauchement)");
    }

    @Test
    public void testIsFreeForConsecutive() {
        LocalDateTime consecutiveTime = nov_1_2020_22_30.plus(min_120);
        Event consecutive = new Event("Consecutive", consecutiveTime, Duration.ofMinutes(60));
        
        assertTrue(agenda.isFreeFor(consecutive), 
                "Devrait être libre pour un événement consécutif");
    }
}
