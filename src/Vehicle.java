public abstract class Vehicle {

    private final int id;

    private String name;

    private static int nextId = 1;

    // Fuel bitmask constants
    public static final int DIESEL = 1 << 0;
    public static final int PETROL = 1 << 1;
    public static final int LPG = 1 << 2;
    public static final int CNG = 1 << 3;

    public Vehicle(String name) {

        this.id = nextId++;

        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public abstract String toString();
}