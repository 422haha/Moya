package com.e22e.moya.exploration.dto.info;

import java.util.List;

public class ExplorationStartDto {

    private Long explorationId;
    private List<SpeciesDto> myDiscoveredSpecies; // 내가 찾은 species들
    private List<SpeciesDto> species; // 내가 찾지 못한 species들
    private List<NpcDto> npcs; // npc들

    public Long getExplorationId() {
        return explorationId;
    }

    public void setExplorationId(Long explorationId) {
        this.explorationId = explorationId;
    }

    public List<SpeciesDto> getMyDiscoveredSpecies() {
        return myDiscoveredSpecies;
    }

    public void setMyDiscoveredSpecies(
        List<SpeciesDto> myDiscoveredSpecies) {
        this.myDiscoveredSpecies = myDiscoveredSpecies;
    }

    public List<SpeciesDto> getSpecies() {
        return species;
    }

    public void setSpecies(List<SpeciesDto> species) {
        this.species = species;
    }

    public List<NpcDto> getNpcs() {
        return npcs;
    }

    public void setNpcs(List<NpcDto> npcs) {
        this.npcs = npcs;
    }
}
