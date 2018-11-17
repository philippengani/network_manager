package FindBts.tableClasses;

public class Adm {
    private int id;
    private String location;
    private Double latitude;
    private Double longitude;

    public Adm(int id, String location, Double latitude, Double longitude) {
        this.setId(id);
        this.setLocation(location);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
