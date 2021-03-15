package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static Predicate<Flight> DEPARTURE_DATE_AFTER_NOW = flight -> flight.getSegments().stream()
            .anyMatch(segment -> segment.getDepartureDate().isAfter(LocalDateTime.now()));

    public static Predicate<Flight> DEPARTURE_DATE_BEFORE_ARRIVAL_DATE = flight -> flight.getSegments().stream()
            .anyMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()));

    public static Predicate<Flight> DOWNTIME_MORE_THAN_2_HOURS = flight -> {
        List<Segment> segments = flight.getSegments();
        if (segments.size() > 1) {
            long sumTime = 0;
            for (int k = 0; k < segments.size() - 1; k++) {
                if (segments.get(k).getArrivalDate().isBefore(segments.get(k + 1).getDepartureDate())) {
                    sumTime += Duration.between(segments.get(k).getArrivalDate(), segments.get(k + 1)
                            .getDepartureDate()).toMinutes();
                    if (sumTime > 120) {
                        return false;
                    }
                }
            }
        }
        return true;
    };

    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        Filter filter = new Filter();

        System.out.println(" ------ initial flights ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DEPARTURE_DATE_AFTER_NOW);
        System.out.println(" ------ after deleting flights with departures before the current time ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DEPARTURE_DATE_BEFORE_ARRIVAL_DATE);
        System.out.println(" ------ after deleting flights where segments with arrival date earlier than the departure date ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DOWNTIME_MORE_THAN_2_HOURS);
        System.out.println(" ------ after deleting flights where the total time on earth more then 2 hours ------ ");
        flights.forEach(System.out::println);
    }
}
