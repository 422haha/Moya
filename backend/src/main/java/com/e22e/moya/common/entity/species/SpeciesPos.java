package com.e22e.moya.common.entity.species;

import jakarta.persistence.*;

// 공원에 있는 동식물의 위치
@Entity
@Table(name = "species_pos")
public class SpeciesPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double latitude;

    private double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "park_species_id")
    private ParkSpecies parkSpecies;
=======
    @JoinColumn(name = "species_id")
    private Species species;
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3

    // getter, setter

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

<<<<<<< HEAD
    public ParkSpecies getParkSpecies() {
        return parkSpecies;
    }

    public void setParkSpecies(ParkSpecies parkSpecies) {
        this.parkSpecies = parkSpecies;
    }

=======
    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
>>>>>>> 3c2a0e63c7b9f199925d1933d62250ec919940f3
}
