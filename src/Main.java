public class Main {

    public static void main(String[] args) {

        Rental rental = new Rental(5);

        rental.loadFromXML("vehicles.xml");

        rental.showMenu();
    }
}