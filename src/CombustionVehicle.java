public interface CombustionVehicle {

    boolean refuel(int fuelMask, double liters);

    int getSupportedFuelMask();

    double getFuelAmount();
}