import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



public class ArrayListMethods {
    //All initializations of lists, max array sizes, scanner, and file locations
    private static List<String> itemNames;
    private static List<Integer> quantityOfItem;
    private static List<String> customerNames;
    private static List<Integer> customerQuantity;
    private final int MAX_CROPS = 100;
    private final int MAX_CUSTOMERS = 100;
    private int resizable_crops = MAX_CROPS;
    private int resizable_customers = MAX_CUSTOMERS;
    Scanner scr = new Scanner(System.in);
    File folder = new File("Docs");
    File customerLogNames = new File(folder, "customerLogNames.txt");
    File cropCatalogNames = new File(folder, "cropCatalogNames.txt");
    File customerLogQuantity = new File(folder, "customerLogQuantities.txt");
    File cropCatalogQuantity = new File(folder, "cropCatalogQuantities.txt");

    //Public constructor
    public ArrayListMethods() {
        itemNames = new ArrayList<>(MAX_CROPS);
        quantityOfItem = new ArrayList<>(MAX_CROPS);
        customerNames = new ArrayList<>(MAX_CUSTOMERS);
        customerQuantity = new ArrayList<>(MAX_CUSTOMERS);
    }

    //When called searches for crop in parameters
    public void searchItem(String itemName) {
        int index = itemNames.indexOf(itemName);
        if (index != -1) {
            System.out.println(itemName + " stock: " + quantityOfItem.get(index));
        } else {
            System.out.println(itemName + " not found in the catalog.");
        }

    }

    //Adds crop to catalog
    public void addItem(String itemName, int quantity) {
        int index = itemNames.indexOf(itemName);
        if (index != -1) {
            quantityOfItem.set(index, quantityOfItem.get(index) + quantity);
            System.out.println(itemName + " quantity updated: " + quantityOfItem.get(index));
        } else {
            itemNames.add(itemName);
            quantityOfItem.add(quantity);
            System.out.println(itemName + " added to the catalog with quantity: " + quantity);
        }
    }

    //Removed a certain amount of quantity from crop or entire crop
    public void removeItem(String itemName, int quantity) {
        int index = itemNames.indexOf(itemName);
        if (index != -1) {
            int currentQuantity = quantityOfItem.get(index);
            if (currentQuantity >= quantity) {
                // subtract quantitiy if there is more avaialable than that being removed.
                quantityOfItem.set(index, currentQuantity - quantity);
                System.out.println(quantity + " " + itemName + " removed from the catalog. Quantity left: " + quantityOfItem.get(index));
                if (quantityOfItem.get(index) == 0) {
                    // check if the user wants to remove the item from the catalog.
                    System.out.println("Do you want to remove " + itemName + " from the Catalog (Yes or No)");
                    String removeChoice = scr.next();
                    switch (removeChoice) {
                        case "Yes":
                            itemNames.remove(index);
                            quantityOfItem.remove(index);
                            System.out.println(itemName + " removed from the catalog.");
                            break;
                        case "yes":
                            itemNames.remove(index);
                            quantityOfItem.remove(index);
                            System.out.println(itemName + " removed from the catalog.");
                            break;
                        case "y":
                            itemNames.remove(index);
                            quantityOfItem.remove(index);
                            System.out.println(itemName + " removed from the catalog.");
                            break;
                        case "Y":
                            itemNames.remove(index);
                            quantityOfItem.remove(index);
                            System.out.println(itemName + " removed from the catalog.");
                            break;
                        case "YES":
                            itemNames.remove(index);
                            quantityOfItem.remove(index);
                            System.out.println(itemName + " removed from the catalog.");
                            break;
                        default:
                            System.out.println("Will keep " + itemName + " with zero stock");

                    }
                }
            } else {
                System.out.println(itemName + " not found in the catalog.");
            }

        }
    }

    //Edit a crops name
    public void editItem(String itemName, String newItemName) {
        int index = itemNames.indexOf(itemName);
        // check if the item exists in the catalog. If it does, edit the name, otherwise inform user that item does not exist in catalog.
        if (index != -1) {
            itemNames.set(index, newItemName);
            System.out.println(itemName + " renamed to " + newItemName);
        } else {
            System.out.println("Item " + itemName + " not found in catalog.");
        }
    }

    //Shows crop catalog
    public void showCatalog() {
        System.out.println("Contents of Catalog:");
        //use a for loop to print the catalog and indicate empty spaces by mentioning they are empty.
        for (int i = 0; i < itemNames.size(); i++) {
            System.out.println(itemNames.get(i) + " stock: " + quantityOfItem.get(i));
        }
        if (itemNames.isEmpty()) {
            System.out.print("       Emtpy");
        }
    }


    public void searchCustomer(String customerName) {
        // finds customer if they do not exist
        int index = customerNames.indexOf(customerName);
        if (index != -1) {
            System.out.println(customerName + " stock: " + customerQuantity.get(index));
        }
        else {
            System.out.println(customerName + " not found in the customer logs.");
        }
    }

    public void addCustomer(String customerName, int quantity){
        // adds the customer if they do not exist, add to quantity if they do.
        int index = customerNames.indexOf(customerName);
        if (index != -1) {
            customerQuantity.set(index, customerQuantity.get(index) + quantity);
            System.out.println(customerName + " quantity updated: " + customerQuantity.get(index));
        }
        else {
            customerNames.add(customerName);
            customerQuantity.add(quantity);
            System.out.println(customerName + " added to the customer log with quantity: " + quantity);
        }
    }

    public void removeCustomer(String customerName) {
        //remove customer if they exist in the catalog. otherwise return that it is not found.
        int index = customerNames.indexOf(customerName);
        if (index != -1) {
            customerNames.remove(index);
            customerQuantity.remove(index);
            System.out.println(customerName + " removed from the catalog.");
        }
        else {
            System.out.println(customerName + " not found in the catalog.");
        }
    }

    public void editCustomerName(String customerName, String newCustomerName) {
        // similar to edit item, edit names of customers if they exist in the catalog.
        int index = customerNames.indexOf(customerName);
        if (index != -1) {
            customerNames.set(index, newCustomerName);
            System.out.println(customerName + " renamed to " + newCustomerName);
        }
        else {
            System.out.println("Item " + customerName + " not found in catalog.");
        }
    }

    //Edits customer quantity
    public void editCustomerQuantity(String customerName, int newCustomerAmount) {
        int index = customerNames.indexOf(customerName);
        int customerAmount = 0;
        if (index != -1) {
            customerAmount = customerQuantity.get(index);
            customerQuantity.set(index, newCustomerAmount);
            System.out.println(customerAmount + " renamed to " + newCustomerAmount);
        } else {
            System.out.println(customerName + " was not found in customer logs, no existing quantity to change");
        }
    }

    //Shows customer log displaying name and quantity taken
    public void showCustomerLog() {
        System.out.println("Contents of Customer Log:");
        for (int i = 0; i < customerNames.size(); i++) {
            System.out.println(customerNames.get(i) + " has taken " + customerQuantity.get(i) + " crops");
        }
        if (customerNames.isEmpty()) {
            System.out.println("  Empty");
        }
    }

    //checks if a resize is needed for the array
    public void checkForResizeCrops() {
        if (itemNames.size() >= resizable_crops);
        resizable_crops *= 2;
        ((ArrayList<String>)itemNames).ensureCapacity(resizable_crops);
        ((ArrayList<Integer>)quantityOfItem).ensureCapacity(resizable_crops);
    }

    //checks if a resize is needed for the array
    public void checkForResizeCustomers() {
        if (customerNames.size() >= resizable_customers)
            resizable_customers *= 2;
        ((ArrayList<String>)customerNames).ensureCapacity(resizable_customers);
        ((ArrayList<Integer>)customerQuantity).ensureCapacity(resizable_customers);
    }

    //Used for integer user inputs to make sure only integers in a certain range are accepted
    public int inputValidationInt(Scanner choice){
        int input;
        do {
            while (!choice.hasNextInt()) {
                System.out.println("Please enter a valid number (greater than -1)");
                choice.next();
            }
            input = choice.nextInt();
        } while (input < 0);
        return input;
    }

    //Saves ArrayLists in txt files for further use
    public void saveArrayFiles() {
        //Creates new files for each array
        createNewFiles(customerLogNames);
        createNewFiles(customerLogQuantity);
        createNewFiles(cropCatalogNames);
        createNewFiles(cropCatalogQuantity);

        //Attempts to write customerNames array onto the txt file if it exists
        try {
            PrintStream writer = new PrintStream(customerLogNames);
            for (int index = 0; index < customerNames.size(); index++) {
                if (customerNames.get(index) != null) {
                    writer.println(customerNames.get(index));
                }
            }
            writer.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("Customer Log names not saved, error has occurred");
            e.printStackTrace();
        }

        //Attempts to write cropNames array onto the txt file if it exists
        try {
            PrintStream writer = new PrintStream(cropCatalogNames);
            for (int index = 0; index < itemNames.size(); index++) {
                if (itemNames.get(index) != null) {
                    writer.println(itemNames.get(index));
                }
            }
            writer.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("Crop Catalog names not saved, error has occurred");
            e.printStackTrace();
        }

        //Attempts to write customerQuantity array onto the txt file if it exists
        try {
            PrintStream writer = new PrintStream(customerLogQuantity);
            for (int index = 0; index < customerQuantity.size(); index++) {
                if (customerQuantity.get(index) != null) {
                    writer.println(customerQuantity.get(index));
                }
            }
            writer.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("Customer Log quantities not saved, error has occurred");
            e.printStackTrace();
        }

        //Attempts to write cropQuantity array onto the txt file if it exists
        try {
            PrintStream writer = new PrintStream(cropCatalogQuantity);
            for (int index = 0; index < quantityOfItem.size(); index++) {
                if (quantityOfItem.get(index) != null) {
                    writer.println(quantityOfItem.get(index));
                }
            }
            writer.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("Crop Catalog quantities not saved, error has occurred");
            e.printStackTrace();
        }


    }

    //Uploads txt files storing Array information to use previously saved information
    public void uploadArrayFiles(){

        //checks to see if this file exists
        createNewFiles(customerLogNames);


        Scanner fileReader; //initializes a scanner to read files
        //Attempts to read customerLogNames and add each name to the appropriate array
        try {
            fileReader = new Scanner(customerLogNames);
            while(fileReader.hasNext()){
                String currentName = fileReader.next();
                customerNames.add(currentName);
            }
            fileReader.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("File customerQueue not found");
            e.printStackTrace();
        }

        //checks to see if this file exists
        createNewFiles(cropCatalogNames);

        //Attempts to read cropCatalogNames and add each name to the appropriate array
        try {
            fileReader = new Scanner(cropCatalogNames);
            while(fileReader.hasNext()){
                String currentName = fileReader.next();
                itemNames.add(currentName);
            }
            fileReader.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("File customerQueue not found");
            e.printStackTrace();
        }

        //checks to see if this file exists
        createNewFiles(customerLogQuantity);

        //Attempts to read customerLogQuantity and add each name to the appropriate array
        try {
            fileReader = new Scanner(customerLogQuantity);
            while(fileReader.hasNext()){
                String currentNumberString = fileReader.next();
                int currentNumber = Integer.parseInt(currentNumberString);
                customerQuantity.add(currentNumber);
            }
            fileReader.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("File customerQueue not found");
            e.printStackTrace();
        }

        //checks to see if this file exists
        createNewFiles(cropCatalogQuantity);

        //Attempts to read cropCatalogQuantity and add each name to the appropriate array
        try {
            fileReader = new Scanner(cropCatalogQuantity);
            while(fileReader.hasNext()){
                String currentNumberString = fileReader.next();
                int currentNumber = Integer.parseInt(currentNumberString);
                quantityOfItem.add(currentNumber);
            }
            fileReader.close();
            //If file is not found throws error
        } catch (FileNotFoundException e) {
            System.out.println("File customerQueue not found");
            e.printStackTrace();
        }
    }

    //Creates a new file if not already created
    public void createNewFiles(File fileName){
        try {
            if (fileName.createNewFile()) {
                //System.out.println("File customerLogNames created");
            } else {
                //System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }




}
