package org.RickAndMorty.Data;

import java.util.List;

public class Character {
    private int id, idOriginAux, idLocationAux;
    private String name, status, species, type, gender, image, url;
    private List<String> episodes;
    OriginAux origin;
    LocationAux location;


    public Character(int id, String name, String status, String species, String type, String gender,
                     OriginAux origin, LocationAux location, String image, List<String> episodes, String url) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.origin = origin;
        this.location = location;
        this.image = image;
        this.episodes = episodes;
        this.url = url;
    }

    public Character() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<String> episodes) {
        this.episodes = episodes;
    }

    public OriginAux getOrigin() {
        return origin;
    }

    public void setOrigin(OriginAux origin) {
        this.origin = origin;
    }

    public LocationAux getLocation() {
        return location;
    }

    public void setLocation(LocationAux location) {
        this.location = location;
    }

    public void setIdOriginAux(int idOriginAux) {
        this.idOriginAux = idOriginAux;
    }

    public void setIdLocationAux(int idLocationAux) {
        this.idLocationAux = idLocationAux;
    }

    public int getIdOriginAux() {
        return idOriginAux;
    }

    public int getIdLocationAux() {
        return idLocationAux;
    }
}
