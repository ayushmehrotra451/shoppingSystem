import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

public class Project1 {
    // Customer class to hold customer information
    public static class Customer {
        private String name;
        private int age;
        private String address;
        private String id;
        private String phoneNumber; // New attribute for phone number

        public Customer(String name, int age, String address, String phoneNumber, String id) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.id = id;
        }

        // Getters
        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getAddress() {
            return address;
        }

        public String getId() {
            return id;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }

    public class Product {
        private String id;
        private String name;
        private int quantity;
        private double price;

        public Product(String id, String name, int quantity, double price) {
            this.id = id;
            this.name = name; 
            this.quantity = quantity;
            this.price = price;
        }

        // Getters and setters
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }


    public class ShoppingCart {
        private Product[] items;
        private int itemCount;

        public ShoppingCart() {
            items = new Product[10];
            itemCount = 0;
        }

        public void addItem(Product item) {
            items[itemCount] = item;
            itemCount++;
        }

        public void removeItem(Product item) {
            int index = -1;
            for (int i = 0; i < itemCount; i++) {
                if (items[i].equals(item)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                for (int i = index; i < itemCount - 1; i++) {
                    items[i] = items[i + 1];
                }
                items[itemCount - 1] = null;
                itemCount--;
            }
        }

        public double getTotalPrice() {
            double totalPrice = 0;
            for (int i = 0; i < itemCount; i++) {
                totalPrice += items[i].getPrice() * items[i].getQuantity();
            }
            return totalPrice;
        }

        public void printBill() {
            System.out.println("\nBill:");
            for (int i = 0; i < itemCount; i++) {
                Product item = items[i];
                System.out.println(
                        item.getName() + " x " + item.getQuantity() + " - Rs" + item.getPrice() * item.getQuantity());
            }
            System.out.println("Total Price: Rs" + getTotalPrice());
        }
    }


    private static Map<String, String> customerCredentials = new HashMap<>();

    private static List<Customer> customers = new ArrayList<>();

    private static List<Product> availableItems = new ArrayList<>();


    private static final String CUSTOMER_DATA_FILE = "customer_data.txt";
    private static final String RETAILER_DATA_FILE = "retailer_data.txt";

    public static void main() {

        loadDataFromFile(CUSTOMER_DATA_FILE);
        String retailerUsername = "ret";
        String retailerPassword = "ret123";

        System.out.println("*");
        System.out.println("*     Welcome to the Online Shop!       *");
        System.out.println("*");
        while (true) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("---------------------------------------");
            System.out.println("|      Are you a customer or retailer?|");
            System.out.println("---------------------------------------");
            System.out.println("|   1. Customer                       |");
            System.out.println("|   2. Retailer                       |");
            System.out.println("---------------------------------------");
            System.out.print("Enter your choice: ");
            String userType = scanner.nextLine().toLowerCase();
            String cusNeworOld;
            clearScreen();
            String username = "";
            String password = "";

            if (userType.equals("retailer")) {
                System.out.println("---------------------------------------------");
                System.out.println("|             Retailer Login                |");
                System.out.println("---------------------------------------------");
                System.out.print("| Username: ");
                username = scanner.nextLine();
                System.out.print("| Password: ");
                password = scanner.nextLine();

                if ((username.equals(retailerUsername) && password.equals(retailerPassword))) {
                    System.out.println("|        Login successful!                  |");
                    System.out.println("---------------------------------------------");
                    runRetailerMenu();
                }

            } else if (userType.equals("customer")) {

                System.out.println("---------------------------------------------");
                System.out.println("|  Do you want to SignUp or Login           |");
                System.out.println("|   (1 - signup / 2 - login):               |");
                System.out.println("---------------------------------------------");
                cusNeworOld = scanner.nextLine().toLowerCase();
                if (cusNeworOld.equals("1") || cusNeworOld.equals("signup")) {
                    populateCustomers();
                } else if (cusNeworOld.equals("2") || cusNeworOld.equals("login")) {
                    System.out.println("|           Customer Login                  |");
                    System.out.print("| Username: ");
                    username = scanner.nextLine();
                    System.out.print("| Password: ");
                    password = scanner.nextLine();


                    if (customerCredentials.containsKey(username)
                            && customerCredentials.get(username).equals(password)) {
                        System.out.println("|        Login successful!                  |");
                        System.out.println("---------------------------------------------");

                        Customer customer = getCustomerByUsername(username);

                        if (customer != null) {
                            Project1 obj = new Project1();
                            obj.runCustomerMenu(customer);
                        } else {
                            System.out.println("|          Customer not found.              |");
                            System.out.println("---------------------------------------------");
                        }
                    } else {
                        System.out.println("|     Invalid username or password.         |");
                        System.out.println("---------------------------------------------");
                    }
                } else {
                    System.out.println("|              Invalid choice.               |");
                    System.out.println("---------------------------------------------");
                }
            } else {

                System.out.println("|  Invalid input. Please enter 'customer'   |");
                System.out.println("|  or 'retailer'.                           |");
                System.out.println("---------------------------------------------");
            }

        }

    }


    private static void loadDataFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); 
                if (line.isEmpty()) { 
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 5) { 
                    String name = data[0];
                    int age = Integer.parseInt(data[1].trim());
                    String address = data[2];
                    String phoneNumber = data[3]; 
                    String id = data[4];
                    String password = data[data.length - 1];

                    
                    customers.add(new Customer(name, age, address, phoneNumber, id));
                    
                    customerCredentials.put(name, password);
                } else {
                    System.out.println("Invalid data format in the file: " + filename);
                }
            }
            scanner.close();
            System.out.println("Data loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing age data: " + e.getMessage());
        }
    }


    private static void populateCustomers() {
        Scanner scanner = new Scanner(System.in);

        try {
            File file = new File(CUSTOMER_DATA_FILE);
            if (file.createNewFile()) {
                System.out.println("Customer data file created: " + file.getName());
            } else {
                System.out.println("Customer data file exists.");
                
                loadDataFromFile(CUSTOMER_DATA_FILE);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the customer data file.");
            e.printStackTrace();
        }

        System.out.println("Enter details for the new customer:");
        System.out.print("Enter Your Name: ");
        String name = scanner.nextLine();
        boolean nameExists = customers.stream().anyMatch(customer -> customer.getName().equalsIgnoreCase(name));
        if (!name.matches("[a-zA-Z\\s]+")) {
            System.out.println("Invalid name format. Name should contain only letters and spaces.");
            return;
        } else if (nameExists) {
            System.out.println("Customer with the same name already exists.");
            return;
        }

        System.out.print("Enter Your Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Your Address: ");
        String address = scanner.nextLine();

        System.out.print("Enter Your Phone Number: ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.matches("\\d{10}")) {
            System.out.println("Invalid phone number format. Phone number should contain exactly 10 digits.");
            return;
        }

        System.out.print("Create Password: ");
        String password = scanner.nextLine();

        String id = String.valueOf(customers.size() + 1);

        Customer newCustomer = new Customer(name, age, address, phoneNumber, id);
        customers.add(newCustomer);

        customerCredentials.put(name, password);

        saveDataToFile(CUSTOMER_DATA_FILE, name, String.valueOf(age), address, phoneNumber, id, password);

        clearScreen();
        System.out.println("Registration Successful");
        clearScreen();
    }


    public static void clearScreen() {
        System.out.print("");
        System.out.flush();
    }

    private static void saveDataToFile(String filename, String... data) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            for (String datum : data) {
                writer.write(datum + ",");
            }
            writer.write("\n");
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file: " + filename);
            e.printStackTrace();
        }
    }

    private static Customer getCustomerByUsername(String username) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(username)) {
                return customer;
            }
        }
        return null;
    }

   public static void runRetailerMenu() {
    Scanner scanner = new Scanner(System.in);
    int choice;

    while (true) {
        System.out.println("+-=-=-=-=-=-=-=-=- Retailer Menu -=-=-=-=-=-=-=-=-+");
        System.out.println("| 1. Add New Item                                 |");
        System.out.println("| 2. Remove Item                                  |");
        System.out.println("| 3. Edit Item                                    |");
        System.out.println("| 4. Show Item                                    |");
        System.out.println("| 5. View All Customers                           |");
        System.out.println("| 6. View All Orders                              |");
        System.out.println("| 7. Exit Retailer Menu                           |");
        System.out.println("+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
        System.out.print("Enter your choice: ");

        choice = scanner.nextInt();
        Project1 project = new Project1();

        switch (choice) {
            case 1:
                project.addItem();
                break;
            case 2:
                project.removeItem();
                break;
            case 3:
                project.editItem();
                break;
            case 4:
                project.showItem();
                break;
            case 5:
                project.viewAllCustomers();
                break;
            case 6:
                project.viewAllOrders();
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}


    
     public void addItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID: ");
        String id = scanner.next();
        System.out.print("Enter product name: ");
        String name = scanner.next();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        availableItems.add(new Product(id, name, quantity, price));

        saveProductToFile(id, name, quantity, price);

        System.out.println("Item added successfully.");
    }
    
    public void removeItem() {
        showItem();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to remove: ");
        String productIdToRemove = scanner.next();

        boolean found = false;
        List<Product> updatedProducts = new ArrayList<>();

        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2].trim());
                double price = Double.parseDouble(data[3]);

                if (id.equals(productIdToRemove)) {
                    found = true;
                    System.out.println("Item removed successfully.");
                } else {
                    updatedProducts.add(new Product(id, name, quantity, price));
                }
            }
            fileScanner.close();

            if (!found) {
                System.out.println("Product with ID " + productIdToRemove + " not found.");
                return;
            }

            FileWriter writer = new FileWriter(RETAILER_DATA_FILE);
            for (Product product : updatedProducts) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getQuantity() + ","
                        + product.getPrice() + "\n");
            }
            writer.close();
            System.out.println("Product data file updated.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating product data file.");
            e.printStackTrace();
        }
    }
    
    public void editItem() {
        showItem();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to edit: ");
        String productIdToEdit = scanner.next();

        boolean found = false;
        List<Product> updatedProducts = new ArrayList<>();

        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);

                if (id.equals(productIdToEdit)) {
                    found = true;
                    System.out.println("Editing Item: " + name);
                    System.out.print("Enter new name: ");
                    String newName = scanner.next();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();

                    updatedProducts.add(new Product(id, newName, newQuantity, newPrice));
                    System.out.println("Item edited successfully.");
                } else {
                    updatedProducts.add(new Product(id, name, quantity, price));
                }
            }
            fileScanner.close();

            if (!found) {
                System.out.println("Product with ID " + productIdToEdit + " not found.");
                return;
            }

            FileWriter writer = new FileWriter(RETAILER_DATA_FILE);
            for (Product product : updatedProducts) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getQuantity() + ","
                        + product.getPrice() + "\n");
            }
            writer.close();
            System.out.println("Product data file updated.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating product data file.");
            e.printStackTrace();
        }
    }

    public static void showItem() {
        System.out.println("\nAvailable Items:");
        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner scanner = new Scanner(file);
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2].trim());
                double price = Double.parseDouble(data[3]);
                System.out.println(lineNumber + ". ID: " + id + ", Name: " + name + ", Quantity: " + quantity
                        + ", Price: Rs" + price);
                lineNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + RETAILER_DATA_FILE);
        }
    }
    
    public void viewAllCustomers() {
    System.out.println("All Customers:");
    try {
        File file = new File(CUSTOMER_DATA_FILE);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String name = data[0];
                    int age = Integer.parseInt(data[1].trim());
                    String address = data[2];
                    String phoneNumber = data[3];
                    String id = data[4];
                    
                    System.out.println("Name: " + name);
                    System.out.println("Age: " + age);
                    System.out.println("Address: " + address);
                    System.out.println("Phone Number: " + phoneNumber);
                    System.out.println("ID: " + id);
                    System.out.println("-----------------------");
                } else {
                    System.out.println("Invalid data format in the file: " + CUSTOMER_DATA_FILE);
                }
            }
        }
        scanner.close();
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + CUSTOMER_DATA_FILE);
    } catch (NumberFormatException e) {
        System.out.println("Error parsing age data: " + e.getMessage());
    }
}

public void viewAllOrders() {
    System.out.println("All Orders:");
    try {
        
        BufferedReader customerReader = new BufferedReader(new FileReader("customer_data.txt"));
        String line;
        while ((line = customerReader.readLine()) != null) {
            String[] customerData = line.split(",");
            if (customerData.length >= 5) {
                String customerId = customerData[4].trim(); 
                String customerName = customerData[0].trim();

                
                String orderFileName = "order_details_" + customerId + ".txt";
                File orderFile = new File(orderFileName);
                if (orderFile.exists()) {
                    System.out.println("Customer ID: " + customerId);
                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Orders:");
                    System.out.println();

                    BufferedReader orderReader = new BufferedReader(new FileReader(orderFile));
                    String orderLine;
                    while ((orderLine = orderReader.readLine()) != null) {
                        System.out.println(orderLine);
                    }
                    System.out.println("------------------------------------------------------------------------");
                    orderReader.close();
                } else {
                    System.out.println("No orders found for customer ID: " + customerId);
                }
            } else {
                System.out.println("Invalid customer data format.");
            }
        }
        customerReader.close();
    } catch (IOException e) {
        System.out.println("An error occurred while reading files: " + e.getMessage());
    }
}



    private void runCustomerMenu(Customer customer) {
        ShoppingCart cart = new ShoppingCart();
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("+-----------------------------------------------+");
            System.out.println("|               Customer Menu                   |");
            System.out.println("+-----------------------------------------------+");
            System.out.println("| 1. View Available Items                       |");
            System.out.println("| 2. Add Item to Cart                           |");
            System.out.println("| 3. Remove Item from Cart                      |");
            System.out.println("| 4. View Cart                                  |");
            System.out.println("| 5. View Previously Bought Products            |");
            System.out.println("| 6. Checkout                                   |");
            System.out.println("| 7. Exit Customer Menu                         |");
            System.out.println("+-----------------------------------------------+");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    displayAvailableItems();
                    break;
                case 2:
                    displayItems();
                    addItemToCart(cart);
                    break;
                case 3:
                    removeItemFromCart(cart);
                    break;
                case 4:
                    displayCart(cart);
                    break;
                case 5:
                    viewPreviousOrders(customer);
                    break;
                case 6:
                    checkout(cart, customer);
                    return;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void viewPreviousOrders(Customer customer) {
        String orderFileName = "order_details_" + customer.getId() + ".txt";
        try {
            File file = new File(orderFileName);
            Scanner scanner = new Scanner(file);
            boolean found = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
                found = true;
            }

            if (!found) {
                System.out.println("No previous orders found for Customer ID: " + customer.getId());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Order details file not found.");
        }
    }

    private static void updateProductDataFile() {
        try {
            FileWriter writer = new FileWriter(RETAILER_DATA_FILE);
            for (Product product : availableItems) {
                writer.write(product.getId() + "," + product.getName() + "," + product.getQuantity() + ","
                        + product.getPrice() + "\n");
            }
            writer.close();
            System.out.println("Product data file updated.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating product data file.");
            e.printStackTrace();
        }
    }

    private static void saveProductToFile(String id, String name, int quantity, double price) {
        try {
            FileWriter writer = new FileWriter(RETAILER_DATA_FILE, true);
            writer.write(id + "," + name + "," + quantity + "," + price + "\n");
            writer.close();
            System.out.println("Product details saved to " + RETAILER_DATA_FILE);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file: " + RETAILER_DATA_FILE);
            e.printStackTrace();
        }
    }

    public static void displayItems() {
        System.out.println("\nAvailable Items:");
        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner scanner = new Scanner(file);
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                String quantity = data[2];
                double price = Double.parseDouble(data[3]);
                System.out.println(lineNumber + ". ID: " + id + ", Name: " + name + ", Quantity: " + quantity
                        + ", Price: Rs" + price);
                lineNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + RETAILER_DATA_FILE);
        }
    }

    public static void displayAvailableItems() {
        System.out.println("\nAvailable Items:");
        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner scanner = new Scanner(file);
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2].trim());
                double price = Double.parseDouble(data[3]);
                System.out.println(lineNumber + ". ID: " + id + ", Name: " + name + ", Quantity: " + quantity
                        + ", Price: Rs" + price);
                lineNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + RETAILER_DATA_FILE);
        }
    }

    public void addItemToCart(ShoppingCart cart) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to add to cart: ");
        String productId = scanner.next();

        Product selectedProduct = null;
        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                String id = data[0];
                String name = data[1];
                int quantity = Integer.parseInt(data[2].trim());
                double price = Double.parseDouble(data[3]);

                if (id.equals(productId)) {
                    selectedProduct = new Product(id, name, quantity, price);
                    break;
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + RETAILER_DATA_FILE);
        }

        if (selectedProduct != null) {
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();

            if (quantity > 0 && quantity <= selectedProduct.getQuantity()) {
                selectedProduct.setQuantity(quantity);
                cart.addItem(selectedProduct);
                System.out.println("Item added to cart.");
            } else {
                System.out.println("Invalid quantity or not enough stock.");
            }
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    public static void removeItemFromCart(ShoppingCart cart) {
        Scanner scanner = new Scanner(System.in);
        if (cart.itemCount == 0) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour Cart:");
            for (int i = 0; i < cart.itemCount; i++) {
                System.out.println((i + 1) + ". " + cart.items[i].getName() + " x " + cart.items[i].getQuantity()
                        + " - Rs" + cart.items[i].getPrice());
            }
            System.out.print("Enter product ID from cart to remove: ");
            String productIdToRemove = scanner.next();

            boolean found = false;

            for (int i = 0; i < cart.itemCount; i++) {
                Product selectedProduct = cart.items[i];
                if (selectedProduct.getId().equals(productIdToRemove)) {
                    found = true;
                    System.out.print("Enter quantity to remove: ");
                    int quantityToRemove = scanner.nextInt();

                    if (quantityToRemove <= selectedProduct.getQuantity() && quantityToRemove > 0) {
                        if (quantityToRemove == selectedProduct.getQuantity()) {
                            cart.removeItem(selectedProduct);
                            System.out.println("Item completely removed from cart.");
                        } else {
                            selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityToRemove);
                            System.out.println("Quantity of item updated in cart.");
                        }
                    } else {
                        System.out.println("Invalid quantity to remove.");
                    }
                    break;
                }
            }

            if (!found) {
                System.out.println("Product with ID " + productIdToRemove + " not found in cart.");
            }
        }
    }

    public static void displayCart(ShoppingCart cart) {
        if (cart.itemCount == 0) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nYour Cart:");
            for (int i = 0; i < cart.itemCount; i++) {
                Product product = cart.items[i];
                System.out.println((i + 1) + ". " + product.getName() + " (ID: " + product.getId() + ") x "
                        + product.getQuantity() + " - Rs" + product.getPrice());
            }
            System.out.println("Total Price: Rs" + cart.getTotalPrice());
        }
    }

    public void checkout(ShoppingCart cart, Customer customer) {
        if (cart.itemCount == 0) {
            System.out.println("Your cart is empty. No items to checkout.");
        } else {
            cart.printBill();

            System.out.println("Select payment option:");
            System.out.println("1. Card Payment");
            System.out.println("2. UPI Payment");
            System.out.println("3. Cash on Delivery (COD)");
            System.out.println("4. Cancel");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your choice: ");
            int paymentOption = scanner.nextInt();
            int c = 0;

            switch (paymentOption) {
                case 1:
                    System.out.println("Enter card details:");
                    System.out.print("Card Number: ");
                    long cardNumber = scanner.nextLong();
                    System.out.print("Expiry Date (MM/YYYY): ");
                    String expiryDate = scanner.next();
                    System.out.print("CVV (3 or 4 digits): ");
                    int cvv = scanner.nextInt();
                    
                    if (isValidCardNumber(cardNumber) && isValidExpiryDate(expiryDate) && isValidCVV(cvv)) {
                        
                        processCardPayment(cart.getTotalPrice(), cardNumber, expiryDate, cvv);
                        c = 1;
                        System.out.println("Thank You  For Shopping 🛍 ");
                    } else {
                        System.out.println("Invalid card details. Please check and try again.");
                    }
                    break;
                case 2:
                    System.out.println("Enter UPI ID (ayushm1008@okaxis):");
                    String upiId = scanner.next();
                    
                    if (isValidUpiId(upiId)) {
                        
                        processUpiPayment(cart.getTotalPrice(), upiId);
                        c = 1;
                        System.out.println("Thank You  For Shopping 🛍 ");
                    } else {
                        System.out.println("Invalid UPI ID format. Please enter a valid UPI ID.");
                    }
                    break;
                case 3:
                    
                    System.out.println(
                            "Your order will be delivered soon. Payment will be collected at the time of delivery.");
                    c = 1;
                    break;
                case 4:
                    System.out.println("Checkout Cancelled!!");
                    break;
                default:
                    System.out.println("Invalid payment option.");
            }
            if (c == 1) {
                updateProductQuantities(cart);

                String orderDetails="";
                orderDetails += "Items:\n";
                for (int i = 0; i < cart.itemCount; i++) {
                    Product product = cart.items[i];
                    orderDetails += "Product ID: " + product.getId() +
                            ", Name: " + product.getName() +
                            ", Quantity: " + product.getQuantity() +
                            ", Price: " + product.getPrice() + "\n";
                }
                orderDetails += "Total Price: Rs" + cart.getTotalPrice()+"\n";

                saveOrderDetailsToFile(customer.getId(), orderDetails);
            } else {
                System.out.println();
                System.out.println("Redirecting you to the login page!!");
            }
            System.out.println("Thank you for shopping with us!");
        }
    }

    private boolean isValidCardNumber(long cardNumber) {
        
        String cardNumberStr = String.valueOf(cardNumber);
        return cardNumberStr.length() >= 13 && cardNumberStr.length() <= 19;
    }

    private boolean isValidExpiryDate(String expiryDate) {
        
        String regex = "^(0[1-9]|1[0-2])/(20)\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expiryDate);
        return matcher.matches();
    }

    private boolean isValidCVV(int cvv) {

        return cvv >= 100 && cvv <= 9999;
    }

    private boolean isValidUpiId(String upiId) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(upiId);
        return matcher.matches();
    }

    private static void saveOrderDetailsToFile(String customerId, String orderDetails) {
        String orderFileName = "order_details_" + customerId + ".txt";
        try {
            FileWriter writer = new FileWriter(orderFileName, true);
            writer.write(orderDetails + "\n");
            writer.close();
            System.out.println("Order details saved to " + orderFileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving order details to file: " + orderFileName);
            e.printStackTrace();
        }
    }

    public static void processCardPayment(double amount, long cardNumber, String expiryDate, int cvv) {

        System.out.println("Card payment processed successfully for Rs" + amount);
    }

    public static void processUpiPayment(double amount, String upiId) {

        System.out.println("UPI payment processed successfully for Rs" + amount + " to UPI ID: " + upiId);
    }

    public void updateProductQuantities(ShoppingCart cart) {
        try {
            File file = new File(RETAILER_DATA_FILE);
            Scanner scanner = new Scanner(file);
            ArrayList<Product> updatedProducts = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String id = parts[0].trim();
                String name = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                double price = Double.parseDouble(parts[3].trim());

                for (int i = 0; i < cart.itemCount; i++) {
                    Product product = cart.items[i];
                    if (product.getId().equals(id)) {
                        quantity -= product.getQuantity();
                        break;
                    }
                }

                
                Product updatedProduct = new Product(id, name, quantity, price);
                updatedProducts.add(updatedProduct);
            }
            scanner.close();

            FileWriter writer = new FileWriter(RETAILER_DATA_FILE);
            for (Product product : updatedProducts) {
                writer.write(product.getId() + ", " + product.getName() + ", " + product.getQuantity() + ", "
                        + product.getPrice() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while updating product quantities.");
            e.printStackTrace();
        }
    }

}