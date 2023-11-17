package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Model;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.ModelService;
import java.util.List;


/**
 *
 * @author Miroslav Kološnjaji
 */
public class ModelServiceImpl implements ModelService {

    private final Repository modelRepository;

    public ModelServiceImpl(Repository modelRepository) {
            this.modelRepository = modelRepository;
    }

    @Override
    public void add(Model model) throws Exception {

        String query = "SELECT Id, ModelName, BrandId FROM model where ModelName = '" + model.getName() + "' AND BrandId = " + model.getBrand().getId();
        List<Model> models = modelRepository.findByQuery(query);
        
        if (models.isEmpty()) {
            modelRepository.add(model);
        }else {
            throw new InputValidationException("Model vec postoji u sistemu!");
        }
        
    }

    @Override
    public void update(Model model) throws Exception {
        modelRepository.update(model);
    }

    @Override
    public void delete(Model model) throws Exception {
        modelRepository.delete(model);
    }

    @Override
    public void deleteMultiple(List<Model> models) throws Exception {
        modelRepository.deleteMultiple(models);
    }
    

    @Override
    public List<Model> getAll() throws Exception {
        return modelRepository.getAll();
    }

    @Override
    public Model findById(Long id) throws Exception {
        return (Model) modelRepository.findById(id);
    }

}
