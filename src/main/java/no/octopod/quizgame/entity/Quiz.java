package no.octopod.quizgame.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Quiz {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private SubCategory subCategory;

    @NotBlank
    @Size(min=2,max=128)
    @Column(unique = true)
    private String question;

    @NotBlank
    @Size(max=128)
    private String firstAnswer;

    @NotBlank
    @Size(max=128)
    private String secondAnswer;

    @NotBlank
    @Size(max=128)
    private String thirdAnswer;

    @NotBlank
    @Size(max=128)
    private String fourthAnswer;

    @NotNull
    @Range(max=3)
    private int correctAnswerIndex;

    public Quiz() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(String firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    public String getThirdAnswer() {
        return thirdAnswer;
    }

    public void setThirdAnswer(String thirdAnswer) {
        this.thirdAnswer = thirdAnswer;
    }

    public String getFourthAnswer() {
        return fourthAnswer;
    }

    public void setFourthAnswer(String fourthAnswer) {
        this.fourthAnswer = fourthAnswer;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
