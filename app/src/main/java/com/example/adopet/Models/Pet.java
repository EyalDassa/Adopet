package com.example.adopet.Models;

public class Pet {
    private String id;
    private String name;
    private String breed;
    private String about;
    private String img;
    private int age;
    private int favoriteCount;
    private String location;
    private String phone;
    private String owner;
    private String type;
    private String sex;

    private String ownerId;

    public Pet() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Pet setId(String id) {
        this.id = id;
        return this;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public String getBreed() {
        return breed;
    }

    public Pet setBreed(String breed) {
        this.breed = breed;
        return this;
    }

    public String getAbout() {
        return about;
    }

    public Pet setAbout(String about) {
        this.about = about;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Pet setImg(String img) {
        this.img = img;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Pet setAge(int age) {
        this.age = age;
        return this;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public Pet setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Pet setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Pet setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public Pet setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getType() {
        return type;
    }

    public Pet setType(String type) {
        this.type = type;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public Pet setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Pet setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", breed='" + breed + '\'' + ", about='" + about + '\'' + ", img='" + img + '\'' + ", age=" + age + ", favoriteCount=" + favoriteCount +
                ", location='" + location + '\'' + ", phone='" + phone + '\'' + ", owner='" + owner + '\'' + ", type='" + type + '\'' + ", sex='" + sex + '\'' + '}';


    }
}
