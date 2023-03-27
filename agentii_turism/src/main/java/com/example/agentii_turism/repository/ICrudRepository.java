package com.example.agentii_turism.repository;


import com.example.agentii_turism.domain.Entity;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
}
