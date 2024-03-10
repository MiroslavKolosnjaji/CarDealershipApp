package cardealershipapp.server.repository;

import cardealershipapp.common.domain.PurchaseOrder;
import cardealershipapp.server.exception.RepositoryException;

public interface ExtendedRepository<T, ID> extends Repository<T, ID> {
    T saveAndReturn(T item) throws RepositoryException;

}
