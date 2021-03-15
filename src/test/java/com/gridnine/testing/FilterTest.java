package com.gridnine.testing;

import org.junit.Test;

import java.util.List;

import static com.gridnine.testing.Main.*;
import static org.junit.Assert.assertEquals;

public class FilterTest {

    private final Filter filter = new Filter();
    private final List<Flight> flights = FlightBuilder.createFlights();

    @Test
    public void clean1() {
        List<Flight> flightsActual = filter.clean(flights, DEPARTURE_DATE_AFTER_NOW);

        assertEquals(5, flightsActual.size());
    }

    @Test
    public void clean2() {
        List<Flight> flightsActual = filter.clean(flights, DEPARTURE_DATE_BEFORE_ARRIVAL_DATE);

        assertEquals(5, flightsActual.size());
    }

    @Test
    public void clean3() {
        List<Flight> flightsActual = filter.clean(flights, DOWNTIME_MORE_THAN_2_HOURS);

        assertEquals(4, flightsActual.size());
    }

}