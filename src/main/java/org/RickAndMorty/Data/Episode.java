package org.RickAndMorty.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Episode {
    private int id;
    private String name, air_date, episode, url;
    private List<String> characters;


    public Episode(int id, String name, String air_date, String episode, List<String> characters, String url) {
        this.id = id;
        this.name = name;
        this.air_date = air_date;
        this.episode = episode;
        this.characters = characters;
        this.url = url;
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

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public Date getSqlDate() throws ParseException {
        String patron = "MMMM d, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(patron, Locale.ENGLISH);

        java.util.Date usefulDate = sdf.parse(air_date);
        return new Date(usefulDate.getTime());
    }
}
