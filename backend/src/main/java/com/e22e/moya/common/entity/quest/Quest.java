package com.e22e.moya.common.entity.quest;

import jakarta.persistence.*;

@Entity
@Table(name = "quest")
public class Quest {

    @Id
    private long id;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}