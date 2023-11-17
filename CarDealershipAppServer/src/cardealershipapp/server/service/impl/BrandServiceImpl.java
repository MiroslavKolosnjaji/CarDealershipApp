package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.Brand;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.BrandService;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class BrandServiceImpl implements BrandService {

    private final Repository<Brand, Long> brandRepository;

    public BrandServiceImpl(Repository brandRepository) {
            this.brandRepository = brandRepository;
    }

    @Override
    public void add(Brand brand) throws Exception {

        String query = "SELECT * FROM brand WHERE BrandName = '" + brand.getBrandName() + "'";
        List<Brand> b = brandRepository.findByQuery(query);
        if (b.isEmpty()) {
            brandRepository.add(brand);
        } else {
            throw new InputValidationException("Marka vec postoji u sistemu!");
        }
    }

    @Override
    public void update(Brand brand) throws Exception {
        brandRepository.update(brand);
    }

    @Override
    public void delete(Brand brand) throws Exception {
        brandRepository.delete(brand);
    }

    @Override
    public List<Brand> getAll() throws Exception {
        return brandRepository.getAll();
    }

    @Override
    public Brand findById(Long id) throws Exception {
        return brandRepository.findById(id);
    }

}
