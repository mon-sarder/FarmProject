package app;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * A tidy Swing GUI that now includes a Login/Register panel
 * and uses a CardLayout to show the main app only after successful login.
 */
public class MainGUI extends JFrame {

    // --- CardLayout Constants ---
    private static final String LOGIN_PANEL = "LoginPanel";
    private static final String APP_PANEL = "AppPanel";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // --- Auth ---
    private final AuthService authService = new AuthService();

    // --- App UI Components ---
    private final ArrayListMethods inventory = new ArrayListMethods();
    private final CustomerQueue queue = new CustomerQueue();
    private final DefaultTableModel inventoryModel = new DefaultTableModel(new Object[]{"Item", "Qty"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable inventoryTable = new JTable(inventoryModel);
    private final JTextField itemNameField = new JTextField();
    private final JSpinner itemQtyField = new JSpinner(new SpinnerNumberModel(1, 0, 100000, 1));
    private final JTextField searchItemField = new JTextField();
    private final DefaultTableModel queueModel = new DefaultTableModel(new Object[]{"Name", "Note"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable queueTable = new JTable(queueModel);
    private final JTextField customerNameField = new JTextField();
    private final JTextField customerNoteField = new JTextField();
    private final JTextField searchCustomerField = new JTextField();

    // --- Login UI Components ---
    private final JTextField loginUserField = new JTextField(20);
    private final JPasswordField loginPassField = new JPasswordField(20);
    private final JTextField regUserField = new JTextField(20);
    private final JPasswordField regPassField = new JPasswordField(20);
    private final JLabel loginErrorLabel = new JLabel(" ");

    public MainGUI() {
        super("Customer Queue & Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Build the two main panels
        JPanel loginPanel = buildLoginPanel();
        JPanel appPanel = buildAppPanel(); // This contains your original tabbed UI

        mainPanel.add(loginPanel, LOGIN_PANEL);
        mainPanel.add(appPanel, APP_PANEL);

        // Start by showing the login panel
        cardLayout.show(mainPanel, LOGIN_PANEL);

        setContentPane(mainPanel);
        setVisible(true);
    }

    /**
     * Builds the Login/Register UI panel.
     */
    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Login Section ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(new JLabel("<html><h2>Login</h2></html>"), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(loginUserField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(loginPassField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // --- Error Label ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        loginErrorLabel.setForeground(Color.RED);
        panel.add(loginErrorLabel, gbc);

        // --- Registration Section ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("<html><h3>Register New User</h3></html>"), gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(regUserField, gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(regPassField, gbc);

        gbc.gridy = 9;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        // --- Actions ---
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        return panel;
    }

    private void handleLogin() {
        AuthDtos.LoginDTO dto = new AuthDtos.LoginDTO(
                loginUserField.getText(),
                new String(loginPassField.getPassword())
        );

        if (authService.login(dto)) {
            // SUCCESS: Switch to the main app panel
            loginErrorLabel.setText(" ");
            cardLayout.show(mainPanel, APP_PANEL);
            // Load initial data *after* logging in
            refreshInventoryTable();
            refreshQueueTable();
        } else {
            // FAILED: Show error
            loginErrorLabel.setText("Invalid username or password.");
        }
    }

    private void handleRegister() {
        AuthDtos.RegisterDTO dto = new AuthDtos.RegisterDTO(
                regUserField.getText(),
                new String(regPassField.getPassword())
        );

        try {
            authService.register(dto);
            loginErrorLabel.setText(" ");
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            regUserField.setText("");
            regPassField.setText("");
        } catch (IllegalArgumentException e) {
            // FAILED: Show error from validation
            loginErrorLabel.setText(" ");
            JOptionPane.showMessageDialog(this, e.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Builds the main application panel (your original UI).
     */
    private JPanel buildAppPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Inventory", buildInventoryPanel());
        tabs.addTab("Customer Queue", buildQueuePanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildInventoryPanel() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Top: Add/Set
        JPanel addPanel = new JPanel(new GridLayout(2, 1, 6,6));
        JPanel row1 = new JPanel(new BorderLayout(8,8));
        row1.add(new JLabel("Item name:"), BorderLayout.WEST);
        row1.add(itemNameField, BorderLayout.CENTER);
        JPanel row2 = new JPanel(new BorderLayout(8,8));
        row2.add(new JLabel("Quantity:"), BorderLayout.WEST);
        row2.add(itemQtyField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,0));
        buttons.add(new JButton(new AbstractAction("Add / Increment") {
            @Override public void actionPerformed(ActionEvent e) {
                String n = itemNameField.getText().trim();
                int q = (Integer) itemQtyField.getValue();
                if (n.isEmpty() || q <= 0) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Enter a name and positive quantity.");
                    return;
                }
                try {
                    inventory.addOrIncrement(n, q);
                    itemNameField.setText("");
                    itemQtyField.setValue(1);
                    refreshInventoryTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
        buttons.add(new JButton(new AbstractAction("Set Quantity") {
            @Override public void actionPerformed(ActionEvent e) {
                String n = itemNameField.getText().trim();
                int q = (Integer) itemQtyField.getValue();
                if (n.isEmpty() || q < 0) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Enter a name and non-negative quantity.");
                    return;
                }
                try {
                    inventory.setQuantity(n, q);
                    refreshInventoryTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }));
        addPanel.add(row1);
        addPanel.add(row2);

        JPanel north = new JPanel(new BorderLayout(8,8));
        north.add(addPanel, BorderLayout.CENTER);
        north.add(buttons, BorderLayout.EAST);

        // Center: Table
        inventoryTable.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(inventoryTable);

        // South: search + delete
        JPanel south = new JPanel(new BorderLayout(8,8));
        south.add(new JLabel("Search:"), BorderLayout.WEST);
        south.add(searchItemField, BorderLayout.CENTER);
        JButton deleteBtn = new JButton(new AbstractAction("Delete Selected") {
            @Override public void actionPerformed(ActionEvent e) {
                int row = inventoryTable.getSelectedRow();
                if (row == -1) return;
                String name = (String) inventoryModel.getValueAt(row, 0);
                try {
                    boolean ok = inventory.remove(name);
                    if (!ok) JOptionPane.showMessageDialog(MainGUI.this, "Item not found.");
                    refreshInventoryTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        south.add(deleteBtn, BorderLayout.EAST);

        searchItemField.getDocument().addDocumentListener(simple(() -> filterInventory(searchItemField.getText())));

        root.add(north, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildQueuePanel() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Top form
        JPanel form = new JPanel(new GridLayout(2,2,6,6));
        form.add(new JLabel("Customer name:"));
        form.add(customerNameField);
        form.add(new JLabel("Note (optional):"));
        form.add(customerNoteField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,0));
        buttons.add(new JButton(new AbstractAction("Enqueue") {
            @Override public void actionPerformed(ActionEvent e) {
                String name = customerNameField.getText().trim();
                String note = customerNoteField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(MainGUI.this, "Enter a customer name.");
                    return;
                }
                queue.enqueue(new CustomerQueue.Customer(name, note));
                customerNameField.setText("");
                customerNoteField.setText("");
                refreshQueueTable();
            }
        }));
        buttons.add(new JButton(new AbstractAction("Serve Next (Dequeue)") {
            @Override public void actionPerformed(ActionEvent e) {
                queue.dequeue();
                refreshQueueTable();
            }
        }));
        buttons.add(new JButton(new AbstractAction("Clear") {
            @Override public void actionPerformed(ActionEvent e) {
                queue.clear();
                refreshQueueTable();
            }
        }));

        JPanel north = new JPanel(new BorderLayout(8,8));
        north.add(form, BorderLayout.CENTER);
        north.add(buttons, BorderLayout.EAST);

        // Table
        queueTable.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(queueTable);

        // South search
        JPanel south = new JPanel(new BorderLayout(8,8));
        south.add(new JLabel("Search by name:"), BorderLayout.WEST);
        south.add(searchCustomerField, BorderLayout.CENTER);

        searchCustomerField.getDocument().addDocumentListener(simple(() -> filterQueue(searchCustomerField.getText())));

        root.add(north, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        return root;
    }

    private void refreshInventoryTable() {
        List<ArrayListMethods.Item> data = inventory.list();
        inventoryModel.setRowCount(0);
        for (ArrayListMethods.Item it : data) {
            inventoryModel.addRow(new Object[]{ it.getName(), it.getQuantity() });
        }
    }

    private void filterInventory(String q) {
        if (q == null || q.isBlank()) {
            refreshInventoryTable();
            return;
        }
        List<ArrayListMethods.Item> data = inventory.search(q);
        inventoryModel.setRowCount(0);
        for (ArrayListMethods.Item it : data) {
            inventoryModel.addRow(new Object[]{ it.getName(), it.getQuantity() });
        }
    }

    private void refreshQueueTable() {
        List<CustomerQueue.Customer> data = queue.toList();
        queueModel.setRowCount(0);
        for (CustomerQueue.Customer c : data) {
            queueModel.addRow(new Object[]{ c.getName(), c.getNote() });
        }
    }

    private void filterQueue(String q) {
        if (q == null || q.isBlank()) {
            refreshQueueTable();
            return;
        }
        List<CustomerQueue.Customer> data = queue.searchByName(q);
        queueModel.setRowCount(0);
        for (CustomerQueue.Customer c : data) {
            queueModel.addRow(new Object[]{ c.getName(), c.getNote() });
        }
    }

    private static DocumentListener simple(Runnable onChange) {
        return new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void removeUpdate(DocumentEvent e) { onChange.run(); }
            @Override public void changedUpdate(DocumentEvent e) { onChange.run(); }
        };
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // This now launches the GUI which will show the login panel first.
        SwingUtilities.invokeLater(MainGUI::new);
    }
}

