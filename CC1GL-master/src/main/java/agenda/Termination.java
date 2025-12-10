package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Termination {
    private LocalDate start;
    private ChronoUnit frequency;
    private LocalDate terminationDateInclusive;
    private Long numberOfOccurrences;

    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.start = start;
        this.frequency = frequency;
        this.terminationDateInclusive = terminationInclusive;
        this.numberOfOccurrences = frequency.between(start, terminationInclusive) + 1;
    }

    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.start = start;
        this.frequency = frequency;
        this.numberOfOccurrences = numberOfOccurrences;
        this.terminationDateInclusive = start.plus(numberOfOccurrences - 1, frequency);
    }

    public LocalDate terminationDateInclusive() {
        return terminationDateInclusive;
    }

    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }
}

