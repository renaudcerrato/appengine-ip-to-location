package ip2location.appengine;

/**
 * The object model for the data we are sending through endpoints
 */
public class IPLocation {

    private Double latitude, longitude;
    private String country;
    private String city;
    private String region;
    private String ip;

    public IPLocation(String ip) {
        this.ip = ip;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getIP() {
        return ip;
    }
}