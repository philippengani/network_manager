package FindBts.tableClasses;

public class Result {
    private int id;
    private String location;
    private double distance;
    private String switch_used;
    private String interface_needed;
    private int gb_ports;
    private int fe_ports;

    public Result(int id, String location, double distance, String switch_used, String interface_needed, int gb_ports, int fe_ports) {
        this.id = id;
        this.location = location;
        this.distance = distance;
        this.switch_used = switch_used;
        this.interface_needed = interface_needed;
        this.gb_ports = gb_ports;
        this.fe_ports = fe_ports;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getSwitch_used() {
        return switch_used;
    }

    public void setSwitch_used(String switch_used) {
        this.switch_used = switch_used;
    }

    public String getInterface_needed() {
        return interface_needed;
    }

    public void setInterface_needed(String interface_needed) {
        this.interface_needed = interface_needed;
    }
    public int getGb_ports() {
        return gb_ports;
    }

    public void setGb_ports(int gb_ports) {
        this.gb_ports = gb_ports;
    }

    public int getFe_ports() {
        return fe_ports;
    }

    public void setFe_ports(int fe_ports) {
        this.fe_ports = fe_ports;
    }
}
