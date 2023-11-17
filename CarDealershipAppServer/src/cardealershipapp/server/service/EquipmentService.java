package cardealershipapp.server.service;

import cardealershipapp.common.domain.Equipment;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public interface EquipmentService {

    void add(Equipment equipment) throws Exception;

    void update(Equipment equipment) throws Exception;

    void delete(Equipment equipment) throws Exception;

    List<Equipment> getAll() throws Exception;

    Equipment findById(Long id) throws Exception;
}
