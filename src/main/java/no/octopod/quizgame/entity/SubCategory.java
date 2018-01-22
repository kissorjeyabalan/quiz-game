package no.octopod.quizgame.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class SubCategory {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min=2,max=128)
    private String name;

    @NotNull
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
