package org.RickAndMorty.Data;

public class Character {
    private int id, idOriginAux, idLocationAux;
    private String name, status, species, type, gender;
    OriginAux origin;
    LocationAux location;

    public Character() {}

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

    public OriginAux getOrigin() {
        return origin;
    }

    public LocationAux getLocation() {
        return location;
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

    @Override
    public String toString() {
        return name + " - " + status + ", " + species + ", " + type + ", " + gender + ".";
    }
}
