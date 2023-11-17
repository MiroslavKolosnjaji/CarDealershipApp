package cardealershipapp.server.repository;

import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface Repository<T, V> {

    void add(T t) throws Exception;

    void update(T t) throws Exception;

    void delete(T t) throws Exception;
    
    void deleteMultiple(List<T> t) throws Exception;

    List<T> getAll() throws Exception;

    T findById(V id) throws Exception;

    List<T> findByQuery(String query) throws Exception;

}
