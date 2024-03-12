package cardealershipapp.server.service;

import cardealershipapp.common.exception.ServiceException;

import java.util.List;

public interface ServiceCRUD<T, ID> {

    void save(T item) throws ServiceException;
    void update(T item) throws ServiceException;
    void delete(T item) throws ServiceException;
    void deleteMultiple(List<T> listItems) throws ServiceException;
    List<T> getAll() throws ServiceException;
    T findById(ID id) throws ServiceException;
}
