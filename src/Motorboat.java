public class Motorboat extends Vehicle
        implements CombustionVehicle {

    private int supportedFuelMask;

    private double fuelAmount;

    public Motorboat(String name, int supportedFuelMask) {

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
    public String toString() {

        return "[" + getId() + "] Motorboat: "
                + getName()
                + " FuelMask=" + supportedFuelMask
                + " Fuel=" + fuelAmount;
    }
}