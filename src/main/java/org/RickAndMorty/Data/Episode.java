package org.RickAndMorty.Data;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Episode {
    private final int id;
    private final String name, air_date, episode;
    private final List<String> characters;

    public Episode(int id, String name, String air_date, String episode, List<String> characters) {
        this.id = id;
        this.name = name;
        this.air_date = air_date;
        this.episode = episode;
        this.characters = characters;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEpisode() {
        return episode;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public Date getSqlDate() throws ParseException {
        String patron = "MMMM d, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(patron, Locale.ENGLISH);

        java.util.Date usefulDate = sdf.parse(air_date);
        return new Date(usefulDate.getTime());
    }
}
