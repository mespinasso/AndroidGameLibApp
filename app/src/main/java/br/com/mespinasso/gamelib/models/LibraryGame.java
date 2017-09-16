package br.com.mespinasso.gamelib.models;

import java.io.Serializable;

/**
 * Created by MatheusEspinasso on 11/09/17.
 */

public class LibraryGame implements Serializable {

    private CatalogGame game;
    private Double rating;
    private String notes;

    public LibraryGame(CatalogGame game, Double rating, String notes) {
        this.game = game;
        this.rating = rating;
        this.notes = notes;
    }

    public CatalogGame getGame() {
        return game;
    }

    public void setGame(CatalogGame game) {
        this.game = game;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
