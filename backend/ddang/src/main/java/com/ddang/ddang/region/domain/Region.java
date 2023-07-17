package com.ddang.ddang.region.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"secondRegions", "thirdRegions"})
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @OneToMany(mappedBy = "firstRegion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Region> secondRegions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_region_id", foreignKey = @ForeignKey(name = "fk_region_first_second"))
    private Region firstRegion;

    @OneToMany(mappedBy = "secondRegion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Region> thirdRegions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_region_id", foreignKey = @ForeignKey(name = "fk_region_second_third"))
    private Region secondRegion;

    public Region(final String name) {
        this.name = name;
    }

    public void addSecondRegion(final Region secondRegion) {
        this.secondRegions.add(secondRegion);
        secondRegion.firstRegion = this;
    }

    public void addThirdRegion(final Region thirdRegion) {
        this.thirdRegions.add(thirdRegion);
        thirdRegion.secondRegion = this;
        thirdRegion.firstRegion = this.firstRegion;
    }
}
