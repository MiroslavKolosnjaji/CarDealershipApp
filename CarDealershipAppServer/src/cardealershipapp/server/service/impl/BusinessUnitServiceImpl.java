package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.BusinessUnit;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.BusinessUnitService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private final Repository<BusinessUnit, Long> businessUnitRepository;

    public BusinessUnitServiceImpl(Repository businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Override
    public void add(BusinessUnit businessUnit) throws Exception {

        String query = "SELECT `Name`, CompanyRegNum, TaxId, Address, Phone, Email, CityId FROM business_unit";
        List<BusinessUnit> businessUnits = businessUnitRepository.findByQuery(query);
        if (businessUnits.isEmpty()) {
            businessUnitRepository.add(businessUnit);
        }else {
            throw new InputValidationException("Poslovna jedinica vec postoji u sistemu!");
        }
    }

    @Override
    public void update(BusinessUnit businessUnit) throws Exception {
        businessUnitRepository.update(businessUnit);
    }

    @Override
    public void delete(BusinessUnit businessUnit) throws Exception {
        businessUnitRepository.delete(businessUnit);
    }

    @Override
    public List<BusinessUnit> getAll() throws Exception {
        return businessUnitRepository.getAll();
    }

    @Override
    public BusinessUnit findById(Long id) throws Exception {
        return businessUnitRepository.findById(id);
    }

}
