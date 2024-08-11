package com.example.adopet.Models;

import android.util.Log;

import com.example.adopet.Utilities.DbManager;

import java.util.HashMap;

public class User {
    private static User instance;
    private String id;
    private String name;
    private String email;
    private String phone;
    private String image;
    private HashMap<String, Integer> favorites = new HashMap<>();
    private HashMap<String, Integer> pets = new HashMap<>();


    public static void init() {
        if (instance == null)
            instance = new User();
    }

    private User() {
    }

    public static User getInstance() {
        return instance;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<String, Integer> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, Integer> favorites) {
        this.favorites = favorites;
    }

    public void addFavorite(String petId) {
        favorites.put(petId, 1);
    }

    public void removeFavorite(String petId) {
        favorites.remove(petId);
    }
    public boolean isFavorite(String petId) {
        return favorites.containsKey(petId);
    }

    public void toggleFavorite(String petId) {
        if (favorites.containsKey(petId)) {
            favorites.remove(petId);
        } else {
            favorites.put(petId, 1);
        }
        Log.d("User", "Favorites: " + favorites);
    }

    public HashMap<String, Integer> getPets() {
        return pets;
    }

    public User setPets(HashMap<String, Integer> pets) {
        this.pets = pets;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", favorites=" + favorites +
                '}';
    }

    public void cleanUserData(){
        this.id = null;
        this.name = null;
        this.email = null;
        this.phone = null;
        this.image = null;
        this.favorites.clear();
        this.pets.clear();
    }
}
