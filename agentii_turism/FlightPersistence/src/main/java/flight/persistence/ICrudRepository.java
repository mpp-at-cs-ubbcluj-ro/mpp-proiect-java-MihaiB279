package flight.persistence;


import flight.model.Entity;

public interface ICrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
}
