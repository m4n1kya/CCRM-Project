package edu.ccrm.service;

import java.util.List;
import java.util.function.Predicate;

public interface Searchable<T> {
    List<T> search(Predicate<T> predicate);
    List<T> findAll();
    
    // Default method demonstrating diamond problem resolution
    default String getSearchDescription() {
        return "Generic searchable interface";
    }
}