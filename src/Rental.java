import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Rental {

    private ArrayList<Vehicle> vehicles;

    private ArrayList<Garage> garages;

    private Scanner scanner;

    public Rental(int garageCount) {

        vehicles = new ArrayList<>();

        garages = new ArrayList<>();

        scanner = new Scanner(System.in);

        for (int i = 1; i <= garageCount; i++) {

            garages.add(new Garage(i));
        }
    }

    public void addVehicle(Vehicle vehicle) {

        vehicles.add(vehicle);
    }

    public void printVehicles() {

        if (vehicles.isEmpty()) {

            System.out.println("No vehicles found.");

            return;
        }

        for (Vehicle vehicle : vehicles) {

            System.out.println(vehicle);
        }
    }

    public Vehicle findVehicleById(int id) {

        for (Vehicle vehicle : vehicles) {

            if (vehicle.getId() == id) {

                return vehicle;
            }
        }

        return null;
    }

    public Garage findGarageByNumber(int number) {

        for (Garage garage : garages) {

            if (garage.getNumber() == number) {

                return garage;
            }
        }

        return null;
    }

    public void parkVehicle() {

        System.out.print("Enter vehicle ID: ");

        int vehicleId = scanner.nextInt();

        Vehicle vehicle = findVehicleById(vehicleId);

        if (vehicle == null) {

            System.out.println("Vehicle not found.");

            return;
        }

        if (!(vehicle instanceof Parkable)) {

            System.out.println("Vehicle is not parkable.");

            return;
        }

        System.out.print("Enter garage number: ");

        int garageNumber = scanner.nextInt();

        Garage garage = findGarageByNumber(garageNumber);

        if (garage == null) {

            System.out.println("Garage not found.");

            return;
        }

        Parkable parkableVehicle = (Parkable) vehicle;

        if (parkableVehicle.park(garage)) {

            System.out.println("Vehicle parked successfully.");

        } else {

            System.out.println(
                    "Could not park vehicle. " +
                            "Garage occupied or vehicle already parked.");
        }
    }

    public void removeVehicle() {

        System.out.print("Enter vehicle ID to remove: ");

        int id = scanner.nextInt();

        Vehicle vehicle = findVehicleById(id);

        if (vehicle == null) {

            System.out.println("Vehicle not found.");

            return;
        }

        if (vehicle instanceof Parkable) {

            Parkable parkable = (Parkable) vehicle;

            if (parkable.isParked()) {

                parkable.unpark();
            }
        }

        vehicles.remove(vehicle);

        System.out.println("Vehicle removed.");
    }

    public void addVehicleFromInput() {

        System.out.println("""
                Select vehicle type:
                1 - Car
                2 - Motorboat
                3 - Bicycle
                4 - Scooter
                """);

        int choice = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Enter name: ");

        String name = scanner.nextLine();

        switch (choice) {

            case 1 -> {

                System.out.println("""
                        Fuel types:
                        DIESEL = 1
                        PETROL = 2
                        LPG = 4
                        CNG = 8
                        """);

                System.out.print("Enter fuel mask: ");

                int fuelMask = scanner.nextInt();

                Car car = new Car(name, fuelMask);

                vehicles.add(car);

                System.out.println("Car added.");
            }

            case 2 -> {

                System.out.println("""
                        Fuel types:
                        DIESEL = 1
                        PETROL = 2
                        LPG = 4
                        CNG = 8
                        """);

                System.out.print("Enter fuel mask: ");

                int fuelMask = scanner.nextInt();

                Motorboat boat =
                        new Motorboat(name, fuelMask);

                vehicles.add(boat);

                System.out.println("Motorboat added.");
            }

            case 3 -> {

                Bicycle bicycle =
                        new Bicycle(name);

                vehicles.add(bicycle);

                System.out.println("Bicycle added.");
            }

            case 4 -> {

                Scooter scooter =
                        new Scooter(name);

                vehicles.add(scooter);

                System.out.println("Scooter added.");
            }

            default -> System.out.println("Invalid choice.");
        }
    }

    public void sortVehicles() {

        vehicles.sort((v1, v2) -> {

            boolean parked1 =
                    (v1 instanceof Parkable)
                            && ((Parkable) v1).isParked();

            boolean parked2 =
                    (v2 instanceof Parkable)
                            && ((Parkable) v2).isParked();

            if (parked1 != parked2) {

                return parked1 ? -1 : 1;
            }

            int type1 = getVehicleTypeOrder(v1);

            int type2 = getVehicleTypeOrder(v2);

            if (type1 != type2) {

                return Integer.compare(type1, type2);
            }

            int nameCompare =
                    v1.getName().compareToIgnoreCase(v2.getName());

            if (nameCompare != 0) {

                return nameCompare;
            }

            int fuelMask1 = 0;

            int fuelMask2 = 0;

            if (v1 instanceof CombustionVehicle cv1) {

                fuelMask1 = cv1.getSupportedFuelMask();
            }

            if (v2 instanceof CombustionVehicle cv2) {

                fuelMask2 = cv2.getSupportedFuelMask();
            }

            if (fuelMask1 != fuelMask2) {

                return Integer.compare(fuelMask1, fuelMask2);
            }

            double fuelAmount1 = 0;

            double fuelAmount2 = 0;

            if (v1 instanceof CombustionVehicle cv1) {

                fuelAmount1 = cv1.getFuelAmount();
            }

            if (v2 instanceof CombustionVehicle cv2) {

                fuelAmount2 = cv2.getFuelAmount();
            }

            return Double.compare(fuelAmount1, fuelAmount2);
        });

        System.out.println("Vehicles sorted successfully.");
    }

    private int getVehicleTypeOrder(Vehicle vehicle) {

        if (vehicle instanceof Car) {
            return 1;
        }

        if (vehicle instanceof Motorboat) {
            return 2;
        }

        if (vehicle instanceof Bicycle) {
            return 3;
        }

        return 4;
    }

    public void loadFromXML(String fileName) {

        try {

            File file = new File(fileName);

            if (!file.exists()) {

                System.out.println("XML file not found.");

                return;
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document document =
                    builder.parse(file);

            document.getDocumentElement().normalize();

            NodeList carList =
                    document.getElementsByTagName("car");

            for (int i = 0; i < carList.getLength(); i++) {

                Element element =
                        (Element) carList.item(i);

                String name =
                        element.getElementsByTagName("name")
                                .item(0)
                                .getTextContent();

                int fuelType =
                        Integer.parseInt(
                                element.getElementsByTagName("fuelType")
                                        .item(0)
                                        .getTextContent());

                vehicles.add(
                        new Car(name, fuelType));
            }

            NodeList boatList =
                    document.getElementsByTagName("motorboat");

            for (int i = 0; i < boatList.getLength(); i++) {

                Element element =
                        (Element) boatList.item(i);

                String name =
                        element.getElementsByTagName("name")
                                .item(0)
                                .getTextContent();

                int fuelType =
                        Integer.parseInt(
                                element.getElementsByTagName("fuelType")
                                        .item(0)
                                        .getTextContent());

                vehicles.add(
                        new Motorboat(name, fuelType));
            }

            NodeList bicycleList =
                    document.getElementsByTagName("bicycle");

            for (int i = 0; i < bicycleList.getLength(); i++) {

                Element element =
                        (Element) bicycleList.item(i);

                String name =
                        element.getElementsByTagName("name")
                                .item(0)
                                .getTextContent();

                vehicles.add(
                        new Bicycle(name));
            }

            NodeList scooterList =
                    document.getElementsByTagName("scooter");

            for (int i = 0; i < scooterList.getLength(); i++) {

                Element element =
                        (Element) scooterList.item(i);

                String name =
                        element.getElementsByTagName("name")
                                .item(0)
                                .getTextContent();

                vehicles.add(
                        new Scooter(name));
            }

            System.out.println("Vehicles loaded from XML.");

        } catch (Exception e) {

            System.out.println("Error loading XML.");
        }
    }

    public void saveToXML(String fileName) {

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            Document document =
                    builder.newDocument();

            Element root =
                    document.createElement("vehicles");

            document.appendChild(root);

            for (Vehicle vehicle : vehicles) {

                Element vehicleElement = null;

                if (vehicle instanceof Car car) {

                    vehicleElement =
                            document.createElement("car");

                    Element fuelType =
                            document.createElement("fuelType");

                    fuelType.setTextContent(
                            String.valueOf(
                                    car.getSupportedFuelMask()));

                    vehicleElement.appendChild(fuelType);

                } else if (vehicle instanceof Motorboat boat) {

                    vehicleElement =
                            document.createElement("motorboat");

                    Element fuelType =
                            document.createElement("fuelType");

                    fuelType.setTextContent(
                            String.valueOf(
                                    boat.getSupportedFuelMask()));

                    vehicleElement.appendChild(fuelType);

                } else if (vehicle instanceof Bicycle) {

                    vehicleElement =
                            document.createElement("bicycle");

                } else if (vehicle instanceof Scooter) {

                    vehicleElement =
                            document.createElement("scooter");
                }

                Element name =
                        document.createElement("name");

                name.setTextContent(vehicle.getName());

                vehicleElement.appendChild(name);

                root.appendChild(vehicleElement);
            }

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes");

            DOMSource source =
                    new DOMSource(document);

            StreamResult result =
                    new StreamResult(new File(fileName));

            transformer.transform(source, result);

            System.out.println("Vehicles saved to XML.");

        } catch (Exception e) {

            System.out.println("Error saving XML.");
        }
    }

    public void showMenu() {

        while (true) {

            System.out.println("""
                    
                    ===== VEHICLE RENTAL MENU =====
                    1 - Print all vehicles
                    2 - Add vehicle
                    3 - Remove vehicle
                    4 - Park vehicle
                    5 - Sort vehicles
                    6 - Exit
                    """);

            int choice = scanner.nextInt();

            switch (choice) {

                case 1 -> printVehicles();

                case 2 -> addVehicleFromInput();

                case 3 -> removeVehicle();

                case 4 -> parkVehicle();

                case 5 -> sortVehicles();

                case 6 -> {

                    saveToXML("vehicles.xml");

                    System.out.println("Exiting program.");

                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }
}