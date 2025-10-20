import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.*;
/*
 *   Queue to track incoming customers waiting for a request or any line
 *    scenarios where customers would wait
 */
public class CustomerQueue {
    private Queue<String> customerQueue;
    File folder = new File("Docs");
    File customerQueueFile = new File(folder, "customerQueue.txt");

    //Public constructor
    public CustomerQueue() {
        customerQueue = new LinkedList<>();
    }

    //Adds customer to front of queue
    public void addCustomer(String customerName) {
        customerQueue.add(customerName);
    }

    //Removes customer from front of queue and returns customer
    public String getNextCustomer() {
        return customerQueue.poll();
    }

    //Shows full queue
    public void showQueue() {
        if (customerQueue.isEmpty()) {
            System.out.println("The queue is empty.");
        } else {
            System.out.println("Customer queue:");
            for (String customer : customerQueue) {
                System.out.println(customer);
            }
        }
    }

    //Saves queue into txt files for reuse when running program again
    public void saveQueueFiles() {
        //creates txt file if not made already
        try {
            if (customerQueueFile.createNewFile()) {
                //System.out.println("customerQueueFile created");
            } else {
                //System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        //Prints the current customer queue onto the txt file
        try {
            PrintStream writer = new PrintStream(customerQueueFile);
            for (int index = 0; index < customerQueue.size(); index++) {
                if (((LinkedList<String>) customerQueue).get(index) != null) {
                    writer.println(((LinkedList<String>) customerQueue).get(index));
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Customer Log names not saved, error has occurred");
            e.printStackTrace();
        }
    }

    //Uploads txt file on run to use saved queue
    public void uploadQueueFiles(){
        //creates new txt file if not created
        try {
            if (customerQueueFile.createNewFile()) {
                //System.out.println("customerQueueFile created");
            } else {
                //System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        //Outputs txt file into an array
        Scanner fileReader;
        try {
            fileReader = new Scanner(customerQueueFile);
            while(fileReader.hasNext()){
                String currentName = fileReader.next();
                customerQueue.add(currentName);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File customerQueue not found");
            e.printStackTrace();
        }

    }

}
