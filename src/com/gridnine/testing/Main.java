package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();
        LocalDateTime nowDate = LocalDateTime.now();
        Filter filter = new Filter();

        System.out.println(" ------ Начальный набор перелетов: ------ ");
        flights.forEach(System.out::println);


        Predicate<Flight> departureDateAfterNow = flight -> flight.getSegments().stream()
                .anyMatch(segment -> segment.getDepartureDate().isAfter(nowDate));

        Predicate<Flight> departureDateBeforeArrivalDate = flight -> flight.getSegments().stream()
                .anyMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()));

        Predicate<Flight> downtimeMoreThan2Hours = flight -> {
            List<Segment> segments = flight.getSegments();
            if (segments.size() > 1) {
                long sumTime = 0;
                for (int k = 0; k < segments.size() - 1; k++) {
                    if (segments.get(k).getArrivalDate().isBefore(segments.get(k + 1).getDepartureDate())) {
                        sumTime += Duration.between(segments.get(k).getArrivalDate(), segments.get(k + 1).getDepartureDate()).toMinutes();
                        if (sumTime > 120) {
                            return false;
                        }
                    }
                }
            }
            return true;
        };


        System.out.println(" ------ Исключили перелеты с вылетом до текущего момента времени: ------ ");
        flights = filter.clean(flights, departureDateAfterNow);
        flights.forEach(System.out::println);

        System.out.println(" ------ Исключили перелеты, где имеются сегменты с датой прилёта раньше даты вылета: ------ ");
        flights = filter.clean(flights, departureDateBeforeArrivalDate);
        flights.forEach(System.out::println);

        System.out.println(" ------ Исключили перелеты, у которых общее время, проведённое на земле, превышает два часа: ------ ");
        flights = filter.clean(flights, downtimeMoreThan2Hours);
        flights.forEach(System.out::println);
    }
}
