package com.e22e.moya.common.entity;

import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.common.entity.species.SpeciesPos;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.ToString;
import lombok.ToString.Exclude;

// 사용자가 수집한 동식물
@Entity
@Table(name = "discovery")
@ToString
public class Discovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime discoveryTime;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    @Exclude
    private Species species;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_pos_id")
    @Exclude
    private SpeciesPos speciesPos;

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(LocalDateTime discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public SpeciesPos getSpeciesPos() {
        return speciesPos;
    }

    public void setSpeciesPos(SpeciesPos speciesPos) {
        this.speciesPos = speciesPos;
    }

}
