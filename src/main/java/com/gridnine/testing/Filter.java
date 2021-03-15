package com.gridnine.testing;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filter implements Filterable {

    @Override
    public List<Flight> clean(List<Flight> flights, Predicate<Flight> predicate) {
        return flights.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
