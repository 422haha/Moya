package com.e22e.moya.common.entity.species;

import jakarta.persistence.*;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.locationtech.jts.geom.Point;

// 공원에 있는 동식물의 위치
@Entity
@Table(name = "species_pos")
@ToString
public class SpeciesPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point pos;

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

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public ParkSpecies getParkSpecies() {
        return parkSpecies;
    }

    public void setParkSpecies(ParkSpecies parkSpecies) {
        this.parkSpecies = parkSpecies;
    }

}
