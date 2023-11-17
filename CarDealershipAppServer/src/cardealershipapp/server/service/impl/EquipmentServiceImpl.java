package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Equipment;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.EquipmentService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class EquipmentServiceImpl implements EquipmentService{
    
    private final Repository<Equipment, Long> equipmentRepository;

    public EquipmentServiceImpl(Repository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }
    
    

    @Override
    public void add(Equipment equipment) throws Exception {
       equipmentRepository.add(equipment);
    }

    @Override
    public void update(Equipment equipment) throws Exception {
        equipmentRepository.update(equipment);
    }

    @Override
    public void delete(Equipment equipment) throws Exception {
        equipmentRepository.delete(equipment);
    }

    @Override
    public List<Equipment> getAll() throws Exception {
       return equipmentRepository.getAll();
    }

    @Override
    public Equipment findById(Long id) throws Exception {
       return equipmentRepository.findById(id);
    }
    
}
