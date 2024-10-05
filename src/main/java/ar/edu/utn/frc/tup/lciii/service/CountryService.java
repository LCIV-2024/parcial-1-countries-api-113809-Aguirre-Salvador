package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.country.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entity.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private CountryRepository countryRepository;

        private final RestTemplate restTemplate;

    @Qualifier("modelMapper")
    @Autowired
    private ModelMapper modelMapper;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");


                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .code((String) countryData.get("cca3"))
                        .borders((ArrayList<String>) countryData.get("borders"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }

        public List<Country> getAllCountriesWithFilters(String name, String code) {
                List<Country> countries = getAllCountries();

                if (code != null && !code.isEmpty()) {
                        countries = countries.stream().filter(c -> c.getCode().equals(code)).collect(Collectors.toList());
                }

                if (name != null && !name.isEmpty()) {
                        countries = countries.stream().filter(c -> c.getName().contains(name)).collect(Collectors.toList());
                }
                return countries;
        }

        public List<Country> getCountriesByContinent(String continent) {
                List<Country> countries = getAllCountries();
                return countries.stream().filter(c -> c.getRegion().contains(continent)).collect(Collectors.toList());
        }

        public List<Country> getCountriesByLanguage(String language) {
                List<Country> countries = getAllCountries();
                ArrayList<Country> arrayList = new ArrayList<>();
                for (Country country : countries) {
                        if (country.getLanguages() != null && country.getLanguages().containsValue(language)) {
                                arrayList.add(country);
                        }
                }
                return arrayList;
        }

        public Country getCountryWithMostBorders(){
                List<Country> countries = getAllCountries();
                int maxBorders = countries.get(2).getBorders().size();
                Country mostBordersCountry = countries.get(2);
                for (Country country : countries) {
                        if (country.getBorders() != null && country.getBorders().size() > maxBorders) {
                                maxBorders = country.getBorders().size();
                                mostBordersCountry = country;
                        }
                }
                return mostBordersCountry;
        }

        public List<Country> saveCountries(Integer amountToSave){
                List<Country> countries = getAllCountries();

                Random random = new Random();
                List<Country> savedCountries = new ArrayList<>();

                for (int i = 0; i < amountToSave; i++) {
                        Integer randomIndex = random.nextInt(250); //Cantidad de paises en GET
                        Country country = countries.get(randomIndex);
                        savedCountries.add(saveInDB(country));
                }
                return savedCountries;
        }

        public Country saveInDB(Country country ){
                CountryEntity entity =
                        modelMapper.map(country, CountryEntity.class);
                CountryEntity savedEntity = countryRepository.save(entity);
                return modelMapper.map(savedEntity, Country.class);
        }
}