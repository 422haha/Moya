package com.e22e.moya.common.entity.park;

import jakarta.persistence.*;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;


@Entity
@Table(name = "park_pos")
@ToString
public class ParkPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> pos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    @Exclude
    private Park park;

    private String name;

    // Getter,  setter

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

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}