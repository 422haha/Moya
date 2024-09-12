package com.e22e.moya.exploration.dto;

import java.util.List;

public class ExplorationStartDto {

    private List<SpeciesDto> species;
    private List<NpcDto> npcs;
    private List<QuestDto> quests;

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

    public List<QuestDto> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestDto> quests) {
        this.quests = quests;
    }
}
