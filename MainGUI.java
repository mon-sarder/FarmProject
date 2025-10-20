import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// This is your new "View" and "Controller"
public class MainGUI {

    // These are your "Model" classes.
    // They must be refactored as described in step 1.
    private ArrayListMethods arrayListMethods;
    private CustomerQueue customerQueue;

    // --- GUI Components ---
    private JFrame mainFrame;
    private JTabbedPane tabbedPane;

    // --- Crop Tab Components ---
    private JPanel cropPanel;
    private JTextArea cropCatalogArea; // To display the catalog
    private JTextField cropNameField;
    private JTextField cropQuantityField;
    private JTextField cropRemoveField;
    private JTextField cropOldNameField;
    private JTextField cropNewNameField;

    // --- Customer Tab Components ---
    private JPanel customerPanel;
    private JTextArea customerLogArea;
    // (You would add text fields and buttons for customers here)

    // --- Queue Tab Components ---
    private JPanel queuePanel;
    private JTextArea queueArea;
    // (You would add text fields and buttons for the queue here)


    /**
     * Constructor: Sets up the entire GUI
     */
    public MainGUI() {
        // 1. Initialize your backend logic
        arrayListMethods = new ArrayListMethods();
        customerQueue = new CustomerQueue();

        // 2. Load data from files (just like your old main method)
        arrayListMethods.uploadArrayFiles();
        customerQueue.uploadQueueFiles();

        // 3. Set up the main window
        mainFrame = new JFrame("Lopez Urban Farm Management");
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // We handle closing

        // --- 4. Create the Menu Bar (for Save/Exit) ---
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        mainFrame.setJMenuBar(menuBar);

        // --- 5. Create the Tabbed Pane ---
        tabbedPane = new JTabbedPane();

        // --- Create the three tabs ---
        initCropTab();
        initCustomerTab();
        initQueueTab();

        tabbedPane.addTab("Crop Catalog", cropPanel);
        tabbedPane.addTab("Customer Log", customerPanel);
        tabbedPane.addTab("Customer Queue", queuePanel);

        mainFrame.add(tabbedPane, BorderLayout.CENTER);

        // --- 6. Add Listeners (The "Controller" part) ---

        // Listener for the "Save" menu item
        saveItem.addActionListener(e -> saveAllData());

        // Listener for the "Exit" menu item
        exitItem.addActionListener(e -> exitProgram());

        // Listener for the Window 'X' button
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProgram();
            }
        });

        // --- 7. Make the window visible ---
        mainFrame.setLocationRelativeTo(null); // Center on screen
        mainFrame.setVisible(true);

        // --- 8. Load initial data into text areas ---
        refreshCropCatalog();
        refreshCustomerLog();
        refreshQueue();
    }


    /**
     * Creates the "Crop Catalog" tab
     */
    private void initCropTab() {
        cropPanel = new JPanel(new BorderLayout(10, 10)); // 10px h/v gap
        cropPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 10px margin

        // --- Center: The Catalog Display ---
        cropCatalogArea = new JTextArea("Loading catalog...");
        cropCatalogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(cropCatalogArea); // Make it scrollable
        cropPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom: The Action Buttons ---
        JPanel actionPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 cols, 10px gaps

        // Add/Update
        actionPanel.add(new JLabel("Crop Name:"));
        cropNameField = new JTextField();
        actionPanel.add(cropNameField);

        actionPanel.add(new JLabel("Quantity to Add:"));
        cropQuantityField = new JTextField();
        actionPanel.add(cropQuantityField);

        JButton addButton = new JButton("Add / Update Crop");
        actionPanel.add(addButton);

        JButton refreshButton = new JButton("Refresh Catalog");
        actionPanel.add(refreshButton);

        // --- Add other actions (Remove, Edit, Search) ---
        // You would add more fields and buttons here for:
        // - Search (1 field, 1 button)
        // - Remove (1 field for name, 1 for quantity, 1 button)
        // - Edit Name (2 fields, 1 button)

        cropPanel.add(actionPanel, BorderLayout.SOUTH);


        // --- ACTION LISTENERS for this tab ---

        refreshButton.addActionListener(e -> refreshCropCatalog());

        addButton.addActionListener(e -> addCropItem());
    }

    /**
     * Creates the "Customer Log" tab (Placeholder)
     */
    private void initCustomerTab() {
        customerPanel = new JPanel(new BorderLayout());
        customerLogArea = new JTextArea("Customer log will appear here.");
        customerLogArea.setEditable(false);
        customerPanel.add(new JScrollPane(customerLogArea), BorderLayout.CENTER);

        // TODO: Create a JPanel with text fields and buttons for:
        // - Search Customer
        // - Add/Update Customer
        // - Remove Customer
        // - Edit Customer
        // Add that panel to BorderLayout.SOUTH
    }

    /**
     * Creates the "Customer Queue" tab (Placeholder)
     */
    private void initQueueTab() {
        queuePanel = new JPanel(new BorderLayout());
        queueArea = new JTextArea("Customer queue will appear here.");
        queueArea.setEditable(false);
        queuePanel.add(new JScrollPane(queueArea), BorderLayout.CENTER);

        // TODO: Create a JPanel with text fields and buttons for:
        // - Add to Queue
        // - Process Next Customer
        // Add that panel to BorderLayout.SOUTH
    }


    // --- ================================== ---
    // --- CONTROLLER METHODS (handle actions) ---
    // --- ================================== ---

    private void refreshCropCatalog() {
        // This calls your *refactored* backend method
        // You must create "getCatalogAsString()" in ArrayListMethods
        // String catalogData = arrayListMethods.getCatalogAsString();
        // cropCatalogArea.setText(catalogData);

        // --- Temporary message until refactored ---
        cropCatalogArea.setText("Please refactor ArrayListMethods to include:\npublic String getCatalogAsString() { ... }");
    }

    private void refreshCustomerLog() {
        // TODO: You need to create a "getCustomerLogAsString()"
        // method in ArrayListMethods, similar to getCatalogAsString()

        // String logData = arrayListMethods.getCustomerLogAsString();
        // customerLogArea.setText(logData);
    }

    private void refreshQueue() {
        // TODO: You need to create a "getQueueAsString()"
        // method in CustomerQueue

        // String queueData = customerQueue.getQueueAsString();
        // queueArea.setText(queueData);
    }

    /**
     * Example action for the "Add Crop" button
     */
    private void addCropItem() {
        String name = cropNameField.getText();
        String quantityStr = cropQuantityField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a crop name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a valid, positive quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the *refactored* backend method.
        // It should return a success message.
        // String result = arrayListMethods.addItem(name, quantity);

        // --- !!! ---
        // NOTE: Your old `addItem` method prints to console. You MUST refactor it
        // to return a String, like "Apple added." or "Apple quantity updated."

        // --- Temporary call until refactored ---
        arrayListMethods.addItem(name, quantity);
        String result = "Item " + name + " processed.\n(Refactor addItem to return a status message!)";
        // ---


        JOptionPane.showMessageDialog(mainFrame, result, "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields and refresh the display
        cropNameField.setText("");
        cropQuantityField.setText("");
        refreshCropCatalog();
    }


    private void saveAllData() {
        arrayListMethods.saveArrayFiles();
        customerQueue.saveQueueFiles();
        JOptionPane.showMessageDialog(mainFrame, "Data saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitProgram() {
        // Show a confirmation dialog
        int choice = JOptionPane.showConfirmDialog(
                mainFrame,
                "Do you want to save your changes before exiting?",
                "Exit Program",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveAllData();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        // If CANCEL_OPTION, do nothing and stay in the program.
    }


    /**
     * The new main method. Replaces CatalogProgram.main()
     */
    public static void main(String[] args) {
        // This makes Swing GUIs look a bit more modern (less "1990s")
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Runs the GUI constructor on the correct thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}