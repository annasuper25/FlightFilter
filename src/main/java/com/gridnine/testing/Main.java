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

        System.out.println(" ------ Начальный набор перелетов: ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DEPARTURE_DATE_AFTER_NOW);
        System.out.println(" ------ Исключили перелеты с вылетом до текущего момента времени: ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DEPARTURE_DATE_BEFORE_ARRIVAL_DATE);
        System.out.println(" ------ Исключили перелеты, где имеются сегменты с датой прилёта раньше даты вылета: ------ ");
        flights.forEach(System.out::println);

        flights = filter.clean(flights, DOWNTIME_MORE_THAN_2_HOURS);
        System.out.println(" ------ Исключили перелеты, у которых общее время, проведённое на земле, превышает два часа: ------ ");
        flights.forEach(System.out::println);
    }
}
