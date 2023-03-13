package org.example.repository;

import org.example.domain.Entity;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
}
