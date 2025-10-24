package com.jb.jb.entities;

import jakarta.persistence.*;

@Entity
@Table(name="testimonial")
public class TestimonialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String occupation;
    private String imgPath;

    @Column(length = 1000)
    private String feedback;
    public TestimonialEntity() {
    }

    public TestimonialEntity(long id, String name, String occupation, String imgPath, String feedback) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
        this.imgPath = imgPath;
        this.feedback = feedback;
    }

    public TestimonialEntity(long id, String name, String imgPath, String feedback) {
        this.id = id;
        this.name = name;
        this.imgPath = imgPath;
        this.feedback = feedback;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
