public class Scooter extends Vehicle {

    public Scooter(String name) {

        super(name);
    }

    @Override
    public String toString() {

        return "[" + getId() + "] Scooter: "
                + getName();
    }
}