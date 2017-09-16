package br.com.mespinasso.gamelib.models;

import java.io.Serializable;

/**
 * Created by MatheusEspinasso on 10/09/17.
 */

public class CatalogGame implements Serializable {

    private int id;
    private String title;
    private String description;
    private String developer;
    private String publisher;
    private String platform;
    private String genre;
    private String coverURL;
    private String bannerURL;

    public CatalogGame(int id, String title, String description, String developer, String publisher, String platform, String genre, String coverURL, String bannerURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        this.platform = platform;
        this.genre = genre;
        this.coverURL = coverURL;
        this.bannerURL = bannerURL;
    }

    public CatalogGame(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!CatalogGame.class.isAssignableFrom(obj.getClass()))
            return false;

        final CatalogGame other = (CatalogGame) obj;
        if(this.id != other.id)
            return false;

        return true;
    }
}
