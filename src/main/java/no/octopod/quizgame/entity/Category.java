package no.octopod.quizgame.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private List<SubCategory> subCategories;

    public Category() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
