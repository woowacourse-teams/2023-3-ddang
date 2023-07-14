package com.ddang.ddang.category.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"subCategories", "mainCategory"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @OneToMany(mappedBy = "mainCategory", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Category> subCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id", foreignKey = @ForeignKey(name = "fk_main_sub"))
    private Category mainCategory;

    public Category(final String name) {
        this.name = name;
    }

    public void initCategory(final Category subCategory) {
        subCategories.add(subCategory);
        subCategory.mainCategory = this;
    }
}
