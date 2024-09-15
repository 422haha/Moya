package com.e22e.moya.common.entity;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.quest.QuestCompleted;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.locationtech.jts.geom.LineString;

@Entity
@Table(name = "exploration")
@ToString
public class Exploration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exploration_id", unique = true)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    @Exclude
    private Park park;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private double distance;

    private Integer steps;

    @Column(name = "startdate")
    private LocalDate startDate;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(columnDefinition = "geometry(LineString,4326)")
    private LineString route;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<Discovery> discoveries = new ArrayList<>();

    @OneToMany(mappedBy = "exploration", cascade = CascadeType.ALL, orphanRemoval = true)
    @Exclude
    private List<QuestCompleted> questCompleted = new ArrayList<>();

    // boolean completed = false;

    //getter, setter

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LineString getRoute() {
        return route;
    }

    public void setRoute(LineString route) {
        this.route = route;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public List<Discovery> getDiscoveries() {
        return discoveries;
    }

    public void setDiscoveries(List<Discovery> discoveries) {
        this.discoveries = discoveries;
    }

    public List<QuestCompleted> getQuestCompleted() {
        return questCompleted;
    }

    public void setQuestCompleted(
        List<QuestCompleted> questCompleted) {
        this.questCompleted = questCompleted;
    }

}
