package cardealershipapp.server.service.impl;

import cardealershipapp.common.domain.City;
import cardealershipapp.common.validation.InputValidationException;
import cardealershipapp.server.repository.Repository;
import cardealershipapp.server.service.CityService;
import java.util.List;


/**
 *
 * @author Miroslav Kološnjaji
 */
public class CityServiceImpl implements CityService{

    private final Repository<City, Long> cityRepository;

    public CityServiceImpl(Repository cityRepository) {
            this.cityRepository = cityRepository;
    }

    @Override
    public void add(City city) throws Exception {

        String query = "SELECT ZipCode, CityName FROM city WHERE ZipCode = '" + city.getZipCode() + "' AND CityName = '" + city.getName() + "'";
        List<City> cities = cityRepository.findByQuery(query);
        
        if(cities.isEmpty()){
           cityRepository.add(city);
        }else {
            throw new InputValidationException("Grad vec postoji u sistemu!");
        }
   
    }

    @Override
    public void update(City city) throws Exception {
        cityRepository.update(city);
    }

    @Override
    public void delete(City city) throws Exception {
        cityRepository.delete(city);
    }

    @Override
    public List<City> getAll() throws Exception {
        return cityRepository.getAll();
    }

    @Override
    public City findById(Long id) throws Exception {
        return cityRepository.findById(id);
    }

}
