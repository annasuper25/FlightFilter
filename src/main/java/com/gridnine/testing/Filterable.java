package com.gridnine.testing;

import java.util.List;
import java.util.function.Predicate;

public interface Filterable {

    List<Flight> clean(List<Flight> flights, Predicate<Flight>predicate);
}
