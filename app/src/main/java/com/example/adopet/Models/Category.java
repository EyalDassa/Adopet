package com.example.adopet.Models;

import java.util.HashMap;

public class Category {
    private HashMap<String,Integer> pets = new HashMap<>();
    private String name;
    private String img;
    private int petsCount;
    boolean isPopular;

    public Category(String name, String img,int petsCount, boolean isPopular) {
        this.name = name;
        this.img = img;
        this.petsCount = petsCount;
        this.isPopular = isPopular;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Category setImg(String img) {
        this.img = img;
        return this;
    }

    public HashMap<String, Integer> getPets() {
        return pets;
    }

    public Category setPets(HashMap<String, Integer> pets) {
        this.pets = pets;
        return this;
    }

    public Category addPet(String petName) {
        pets.put(petName,1);
        return this;
    }

    public int categorySize(){
        return pets.size();
    }

    public int getPetsCount() {
        return petsCount;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public Category setPetsCount(int petsCount) {
        this.petsCount = petsCount;
        return this;
    }

}
