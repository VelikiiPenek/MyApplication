package com.example.myapplication;

// конструктор для записи данных в базу FireStore
public class Request {

    public String name, telephone, image, nomination, description;

    public Request() {

    }

    public Request(String name, String telephone, String image, String nomination, String description) {
        this.name = name;
        this.telephone = telephone;
        this.image = image;
        this.nomination = nomination;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNomination() {
        return nomination;
    }

    public void setNomination(String nomination) {
        this.nomination = nomination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
