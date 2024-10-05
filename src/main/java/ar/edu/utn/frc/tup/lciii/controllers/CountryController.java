package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.country.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<CountryDTO>> getCountries(@RequestParam(required = false) String code,
                                                         @RequestParam(required = false) String name) {
        List<Country> response = new ArrayList<>();
        if (code == null && name == null) {
            response = countryService.getAllCountries();
        }
        else {
            response = countryService.getAllCountriesWithFilters(name,code);
        }
        List<CountryDTO> dtoResponse = response.stream().map(country ->
                modelMapper.map(country, CountryDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(dtoResponse);
    }

    @GetMapping("{continent}/continent")
    public ResponseEntity<List<CountryDTO>> getCountriesByContinent(@PathVariable String continent){
        List<Country> response = countryService.getCountriesByContinent(continent);
        List<CountryDTO> dtoResponse = response.stream().map(country ->
                modelMapper.map(country, CountryDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(dtoResponse);
    }

    @GetMapping("{language}/language")
    public ResponseEntity<List<CountryDTO>> getCountriesByLanguage(@PathVariable String language){
        List<Country> response = countryService.getCountriesByLanguage(language);
        List<CountryDTO> dtoResponse = response.stream().map(country ->
                modelMapper.map(country, CountryDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok(dtoResponse);
    }
}