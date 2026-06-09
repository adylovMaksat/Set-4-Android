public class Car extends Vehicle
        implements CombustionVehicle, Parkable {

    private int supportedFuelMask;

    private double fuelAmount;

    private Garage garage;

    public Car(String name, int supportedFuelMask) {

        super(name);

        this.supportedFuelMask = supportedFuelMask;
    }

    @Override
    public boolean refuel(int fuelMask, double liters) {

        if (liters <= 0) {
            return false;
        }

        if ((supportedFuelMask & fuelMask) != 0) {

            fuelAmount += liters;

            return true;
        }

        return false;
    }

    @Override
    public int getSupportedFuelMask() {
        return supportedFuelMask;
    }

    @Override
    public double getFuelAmount() {
        return fuelAmount;
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

        return "[" + getId() + "] Car: "
                + getName()
                + " FuelMask=" + supportedFuelMask
                + " Fuel=" + fuelAmount
                + " Parked=" + isParked()
                + " Garage=" + garageInfo;
    }
}