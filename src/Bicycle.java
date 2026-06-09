public class Bicycle extends Vehicle
        implements Parkable {

    private Garage garage;

    public Bicycle(String name) {

        super(name);
    }

    @Override
    public boolean park(Garage garage) {

        if (garage == null) {
            return false;
        }

        if (!garage.isEmpty()) {
            return false;
        }

        if (this.garage != null) {
            return false;
        }

        this.garage = garage;

        garage.setParkedVehicle(this);

        return true;
    }

    @Override
    public boolean unpark() {

        if (garage == null) {
            return false;
        }

        garage.setParkedVehicle(null);

        garage = null;

        return true;
    }

    @Override
    public boolean isParked() {
        return garage != null;
    }

    @Override
    public Garage getGarage() {
        return garage;
    }

    @Override
    public String toString() {

        String garageInfo =
                isParked()
                        ? "Garage " + garage.getNumber()
                        : "-";

        return "[" + getId() + "] Bicycle: "
                + getName()
                + " Parked=" + isParked()
                + " Garage=" + garageInfo;
    }
}