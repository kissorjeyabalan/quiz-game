package no.octopod.quizgame.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class SubCategory {

    @Id @GeneratedValue
    private Long id;

    private String name;

    public SubCategory() {}

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
}
