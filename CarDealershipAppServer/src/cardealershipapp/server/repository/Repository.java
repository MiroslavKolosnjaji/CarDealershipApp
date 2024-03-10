package cardealershipapp.server.repository;

import cardealershipapp.server.exception.EntityNotFoundException;
import cardealershipapp.server.exception.RepositoryException;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface Repository<T, V> {

    void save(T t) throws RepositoryException;

    void update(T t) throws RepositoryException;

    void delete(T t) throws RepositoryException;
    
    void deleteMultiple(List<T> t) throws RepositoryException;

    List<T> getAll() throws RepositoryException;

    T findById(V id) throws RepositoryException, EntityNotFoundException;

    List<T> findByQuery(String query) throws RepositoryException;

}
