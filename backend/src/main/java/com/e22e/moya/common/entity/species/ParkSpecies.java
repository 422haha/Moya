package com.e22e.moya.common.entity.species;

import com.e22e.moya.common.entity.park.Park;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Table(name = "park_species", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"park_id", "species_id"})
})
@ToString
public class ParkSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    @Exclude
    private Park park;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    @Exclude
    private Species species;

    @OneToMany(mappedBy = "parkSpecies", cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<SpeciesPos> positions = new ArrayList<>();

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public List<SpeciesPos> getPositions() {
        return positions;
    }

    public void setPositions(List<SpeciesPos> positions) {
        this.positions = positions;
    }

}