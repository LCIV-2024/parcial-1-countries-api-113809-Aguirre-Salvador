package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.country.AddCountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.country.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CountryControllerTests {

    @Mock
    private CountryService countryService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    @Spy
    private CountryController countryController = new CountryController();

    private List<Country> countries;

    @BeforeEach
    public void setUp() {
        countries = new ArrayList<>();
       Country argentina = new Country();
        argentina.setName("Argentina");
        argentina.setCode("ARG");
        Country china = new Country();
        china.setName("China");
        china.setCode("CHN");

        countries.add(argentina);
        countries.add(china);
    }

    @Test
    public void getAllCountries_shouldPass(){

        //Arrange
        when(countryService.getAllCountries()).thenReturn(countries);
        when(modelMapper.map(countries, CountryDTO.class)).thenReturn(new CountryDTO());

        //Act
        ResponseEntity<List<CountryDTO>> response =
                countryController.getCountries(null,null);

        //Assert
        assertNotNull(response);
        assertEquals(response.getBody().size(), countries.size());

    }

    @Test
    public void getAllCountriesByLanguage_shouldPass(){

        //Arrange
        String language = "English";

        when(countryService.getCountriesByLanguage(language)).thenReturn(countries);
        when(modelMapper.map(countries, CountryDTO.class)).thenReturn(new CountryDTO());

        //Act
        ResponseEntity<List<CountryDTO>> response =
                countryController.getCountriesByLanguage(language);

        //Assert
        assertNotNull(response);
        assertEquals(response.getBody().size(), countries.size());

    }

    @Test
    public void createCountries_shouldPass(){

        //Arrange
        AddCountryDTO addCountryDTO = new AddCountryDTO();
        addCountryDTO.setAmountOfCountryToSave(2);

        when(countryService.saveCountries(addCountryDTO.getAmountOfCountryToSave())).thenReturn(countries);
        when(modelMapper.map(countries, CountryDTO.class)).thenReturn(new CountryDTO());

        //Act
        ResponseEntity<List<CountryDTO>> response =
                countryController.createCountry(addCountryDTO);

        //Assert
        assertNotNull(response);
        assertEquals(response.getBody().size(), 2);
    }


}
