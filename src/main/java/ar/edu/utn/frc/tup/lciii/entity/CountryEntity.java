package ar.edu.utn.frc.tup.lciii.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "countries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String code;
    @Column
    private Double area;
    @Column
    private Long population;
}
