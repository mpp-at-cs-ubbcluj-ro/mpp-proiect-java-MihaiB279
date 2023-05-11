package flight.rest.agentii_turism.repository;


import flight.rest.agentii_turism.domain.Entity;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
}
