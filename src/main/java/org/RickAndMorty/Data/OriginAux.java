package org.RickAndMorty.Data;

public class OriginAux {
    String name, url;

    public OriginAux(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public OriginAux() {
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
