package be.ap.edu.mobiledevelopmentexamen;

/**
 * Created by CaruCath on 06-10-17.
 */

public class Coords {

    private int _id;
    private double lat;
    private double lon;
    private String omschrijving;

    public Coords() {
    }

    public Coords(double lat, double lon, String omschrijving) {
        this.lat = lat;
        this.lon = lon;
        this.omschrijving = omschrijving;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
}
