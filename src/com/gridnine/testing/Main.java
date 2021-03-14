package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Main {


    public static void main(String[] args) {

        List<Flight> flights = FlightBuilder.createFlights();

        LocalDateTime nowDate = LocalDateTime.now();

        System.out.println(" ------ Начальный набор перелетов: ------- ");
        flights.forEach(System.out::println);

        System.out.println(" ------ Исключили перелеты с вылетом до текущего момента времени: ------- ");

        flights = flights.stream().filter(
                flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getDepartureDate().isAfter(nowDate))).collect(Collectors.toList());

        flights.forEach(System.out::println);

        System.out.println(" ------ Исключили перелеты, где имеются сегменты с датой прилёта раньше даты вылета: ------- ");

        flights = flights.stream().filter(
                flight -> flight.getSegments().stream()
                        .anyMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()))).collect(Collectors.toList());

        flights.forEach(System.out::println);

        System.out.println(" ------ Исключили перелеты, у которых общее время, проведённое на земле, превышает два часа: ------- ");

        Iterator<Flight> itr = flights.iterator();

        while (itr.hasNext()) {
            Flight flight = itr.next();
            List<Segment> segments = flight.getSegments();

                if (segments.size() > 1) {
                    long sumTime = 0;
                    for (int k = 0; k < segments.size() - 1; k++) {
                        if (segments.get(k).getArrivalDate().isBefore(segments.get(k + 1).getDepartureDate())) {
                            sumTime += Duration.between(segments.get(k).getArrivalDate(), segments.get(k + 1).getDepartureDate()).toMinutes();
                            if (sumTime > 120) {
                                itr.remove();
                            }
                        }
                    }
                }
        }
        flights.forEach(System.out::println);
    }
}
