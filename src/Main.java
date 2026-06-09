public class Main {
    /* Student: Maksatbek Adylov
       Student ID: 56422
       Set: Set-4 */
    public static void main(String[] args) {

        Rental rental = new Rental(5);

        rental.loadFromXML("vehicles.xml");

        rental.showMenu();
    }
}