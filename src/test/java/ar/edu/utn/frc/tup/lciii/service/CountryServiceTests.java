package ar.edu.utn.frc.tup.lciii.service;


import ar.edu.utn.frc.tup.lciii.dtos.common.country.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entity.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CountryServiceTests {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    @InjectMocks
    private CountryService countryService = new CountryService(new RestTemplate());

    private final Integer COUNTRY_COUNT = 250;


    @Test
    public void getAllCountries_shouldPass(){

        //Arrange

        //Act
        List<Country> result = countryService.getAllCountries();

        //Assert
        assertNotNull(result);
        assertEquals(COUNTRY_COUNT, result.size());
    }

    @Test
    public void getCountriesWithMostBorders_shouldBeChina(){

        //Arrange
        Country country = new Country();
        country.setName("China");
        country.setCode("CHN");

        //Act
        Country result = countryService.getCountryWithMostBorders();

        //Assert
        assertNotNull(result);
        assertEquals(result.getName(), country.getName());
        assertEquals(result.getCode(), country.getCode());
    }

    @Test
    public void getCountriesWithFilters_CompleteFilters(){
        //Arrange
        Country country = new Country();
        country.setName("Argentina");
        country.setCode("ARG");

        //Act
        List<Country> result = countryService.getAllCountriesWithFilters("Argentina","ARG");

        //Assert
        assertNotNull(result);
        assertEquals(result.get(0).getName(), country.getName());
        assertEquals(result.get(0).getCode(), country.getCode());
    }

    @Test
    public void getCountriesByLanguage_ShouldPass(){
        //Arrange
        final Integer ENGLISH_SPEAKING_LANGUAGES_QUANTITY = 91;
        String language = "English";

        //Act
        List<Country> result = countryService.getCountriesByLanguage(language);

        //Assert
        assertNotNull(result);
        assertEquals(result.size(), ENGLISH_SPEAKING_LANGUAGES_QUANTITY);
    }


    @Test
    public void createCountries_shouldPass(){
        //Arrange
        Integer amountToSave = 3;
        CountryEntity countryEntity = new CountryEntity();
        when(countryRepository.save(any())).thenReturn(countryEntity);
        when(modelMapper.map(countryEntity, CountryDTO.class)).thenReturn(new CountryDTO());

        //Act
        List<Country> result = countryService.saveCountries(amountToSave);

        //Assert
        assertNotNull(result);
        assertEquals(result.size(), amountToSave);
    }
}
