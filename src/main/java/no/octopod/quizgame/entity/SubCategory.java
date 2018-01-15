package no.octopod.quizgame.entity;

import javax.persistence.*;

@Entity
public class SubCategory {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Category parent;

    public SubCategory() {
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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
