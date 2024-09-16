package com.e22e.moya.common.entity.species;

import jakarta.persistence.*;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;


// 공원에 있는 동식물의 위치
@Entity
@Table(name = "species_pos")
@ToString
public class SpeciesPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> pos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_species_id")
    @Exclude
    private ParkSpecies parkSpecies;

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Point<G2D> getPos() {
        return pos;
    }

    public void setPos(Point<G2D> pos) {
        this.pos = pos;
    }

    public ParkSpecies getParkSpecies() {
        return parkSpecies;
    }

    public void setParkSpecies(ParkSpecies parkSpecies) {
        this.parkSpecies = parkSpecies;
    }

}
