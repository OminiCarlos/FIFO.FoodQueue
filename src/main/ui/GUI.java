package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// GUI hosts the visual components such as frames, panels, and buttons.
public class GUI {

    ShelfLifeTrackerGUI tracker;

    // Effects: instantiate a GUI.
    public GUI() {
        initializeTracker();
        createAndShowGUI();
    }

    //Requires: ShelfLifeTrackerGUI exists.
    //Effects: instantiate the tracker.
    public void initializeTracker() {
        tracker = new ShelfLifeTrackerGUI();
    }


    // Effects: Create the main program frame.
    public void createAndShowGUI() {
        TrackerFrame frame = new TrackerFrame("Food Queue");
    }

    public class TrackerFrame extends JFrame implements ActionListener {
        JScrollPane inventoryPanel;
        JPanel itemFormPanel;
        JPanel buttonsPanel;
        JButton transferButton;
        JButton deleteButton;
        JButton saveButton;
        JButton loadButton;
        JButton addButton;
        JTable inventory;
        JComboBox typeBox;
        JTextField unitField;
        JComboBox conditionDropdown;
        JTextField quantField;
        JTextField purDateField;
        JTextField nameField;
        int tableCount = 0;

        // Effects: initialize the components in the tracker frame.
        public TrackerFrame(String title) {
            super(title);
            this.setVisible(true);
            this.inventoryPanel = new JScrollPane();
            this.setSize(815, 525);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ImageIcon icon = new ImageIcon("src/logo.jpg");// don't know why can't set the logo?
            this.setIconImage(icon.getImage());
            this.setLayout(null);
            this.layPanels();
            this.updateInventory();
        }


        // Modifies: this.
        // Effects: discard the old inventory panel, and replace with a new table with updated data.
        public void updateInventory() {
            this.remove(inventoryPanel);
            String[] col = new String[]{"Name", "Purchase Date ", "Expiration Date", "Quantity", "Unit", "Location"};
            Object[][] data;
            data = tracker.getData();

            // Initialize a JTable
            // Create a table model that does not allow editing
            DefaultTableModel inventoryModel = new DefaultTableModel(data, col) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // All cells are not editable
                    return false;
                }
            };
            inventory = new JTable(inventoryModel);
            // Set the selection mode to single selection to select a single row at a time
            inventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            inventory.setRowSelectionAllowed(true);
            inventory.setColumnSelectionAllowed(false);
            tableCount += 1;
//            System.out.println("jTable regenerated. TableCount" + tableCount);
            // inventory.setRowSelectionInterval(0, 0);

            //Put the table in a scroll pane.

            inventoryPanel = new JScrollPane(inventory);
            inventoryPanel.setBounds(0, 0, 800, 300);
            this.add(inventoryPanel);

            // Refresh the JFrame
            this.revalidate();
            this.repaint();
        }


        // Effects: create the general panels that hold information.
        private void layPanels() {

            double ratio = (double) 1 / (double) 3;
            int breakPoint = (int) (800 * ratio);

            setButtonsPanel(breakPoint);
            setForm(breakPoint);

            this.add(buttonsPanel);
            this.add(itemFormPanel);

            // Refresh the JFrame
            this.revalidate();
            this.repaint();
        }

        // Effects: create the buttons panel.
        public void setButtonsPanel(int breakPoint) {
            buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new GridLayout(2, 2));

            buttonsPanel.setBackground(Color.BLUE);
            buttonsPanel.setBounds(0, 300, breakPoint, 200);

            makeButtons();
        }

        // Effects: add buttons to the panel.
        public void makeButtons() {
            transferButton = new JButton("Transfer Storage");
            deleteButton = new JButton("Delete");
            saveButton = new JButton("Save");
            loadButton = new JButton("Load");

            buttonsPanel.add(transferButton);
            buttonsPanel.add(deleteButton);
            buttonsPanel.add(saveButton);
            buttonsPanel.add(loadButton);
            addButtonListeners();
        }

        // Effects: specify button actions.
        public void addButtonListeners() {
            saveButton.addActionListener(this);
            loadButton.addActionListener(this);
            transferButton.addActionListener(this);
            deleteButton.addActionListener(this);
        }

        // Effects: specify button actions.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == saveButton) {
                tracker.doSaveInventory();
                this.updateInventory();
//                System.out.println("Inventory saved.");
            } else if (e.getSource() == loadButton) {
                tracker.doLoadInventory();
                this.updateInventory();
//                System.out.println("Inventory reloaded.");
            } else if (e.getSource() == transferButton) {
                transferWhenClick();
            } else if (e.getSource() == addButton) {
                enterItem();
            } else if (e.getSource() == deleteButton) {
                int foodIndex = inventory.getSelectedRow();
//                System.out.println("Selected index " + foodIndex);
                tracker.doDeleteFood(foodIndex);
                this.updateInventory();
            }
        }

        // Requires: transfer button works properly.
        // Effects: helper function - create a prompt for destination, then transfer food.
        public void transferWhenClick() {
            int foodIndex = inventory.getSelectedRow();
//            System.out.println("Selected index " + foodIndex);

            String receiver = askForDestination();
            if (foodIndex != -1) {
                tracker.doTransfer(foodIndex, receiver);
                this.updateInventory();
            } else {
                JOptionPane.showMessageDialog(this, "Food not in inventory."
                        + "\n selected index: " + foodIndex);
            }
        }

        // Effects: collect info on the new item panel, and add the item to the inventory.
        public void enterItem() {
            String name = nameField.getText();
            String purchaseDate = purDateField.getText();
            String quantity = quantField.getText();
            String unit = unitField.getText();
            int type = typeBox.getSelectedIndex();
            int receiver = conditionDropdown.getSelectedIndex();
            tracker.doEnterFood(name, purchaseDate, quantity, unit, type, receiver);
            this.updateInventory();
        }

        // Requires: Transfer when click works properly.
        // Effects: pops a dropdown window to get the destination.
        public String askForDestination() {

            Object[] storages = {"Pantry", "Fridge", "Freezer"};
            Object destination = JOptionPane.showInputDialog(this, "Available Storages",
                    "Choose Storage", JOptionPane.INFORMATION_MESSAGE, null, storages, storages[0]);

            return (String) destination;
        }

        // Effects: populates the itemFormPanel.
        public void setForm(int breakPoint) {
            itemFormPanel = new JPanel();
            itemFormPanel.setBackground(Color.lightGray);
            itemFormPanel.setBounds(breakPoint, 300, (800 - breakPoint), 200);

            itemFormPanel.setLayout(new FlowLayout());

            setNameFields();
            setPurDateFields();
            setQuantFields();
            setUnitFields();
            setTypeCombo();
            setConditionCombo();
            setAddButton();
        }

        // Effects: set add button.
        public void setAddButton() {
            addButton = new JButton("Add Item");
            addButton.setSize(80, 20);
            addButton.addActionListener(this);
            itemFormPanel.add(addButton);
        }

        // Effects: set ConditionCombo.
        public void setConditionCombo() {
            JLabel conditionLabel = createTextLabel("Storage Condition:");
            String[] conditionOptions = {"Pantry", "Fridge", "Freezer"};
            conditionDropdown = new JComboBox(conditionOptions);
            itemFormPanel.add(conditionLabel);
            itemFormPanel.add(conditionDropdown);
        }

        // Effects: set Type Combo.
        public void setTypeCombo() {
            JLabel typeLabel = createTextLabel("Type: ");
            String[] options = {"Perishable", "Non-Perishable"};
            typeBox = new JComboBox(options);
            typeBox.setPreferredSize(new Dimension(100, 20));
            itemFormPanel.add(typeLabel);
            itemFormPanel.add(typeBox);
        }

        // Effects: set unit field.
        public void setUnitFields() {
            JLabel unitLabel = createTextLabel("Unit");
            unitField = new JTextField();
            unitField.setText("each");
            unitField.setPreferredSize(new Dimension(80, 20));
            itemFormPanel.add(unitLabel);
            itemFormPanel.add(unitField);
        }

        // Effects: set quantity field.
        public void setQuantFields() {
            JLabel quantLabel = createTextLabel("Quantity: ");
            quantField = new JTextField();
            quantField.setText("14.2");
            quantField.setPreferredSize(new Dimension(80, 20));
            itemFormPanel.add(quantLabel);
            itemFormPanel.add(quantField);
        }

        // Effects: set purchase date field.
        public void setPurDateFields() {
            JLabel purDateLabel = createTextLabel("Purchase Date: ");
            purDateField = new JTextField();
            purDateField.setText("YYYY-MM-DD");
            purDateField.setPreferredSize(new Dimension(80, 20));
            itemFormPanel.add(purDateLabel);
            itemFormPanel.add(purDateField);
        }

        // Effects: set name field.
        public void setNameFields() {
            JLabel nameLabel = createTextLabel("Item Name: ");
            nameField = new JTextField();
            nameField.setText("Name");
            nameField.setPreferredSize(new Dimension(80, 20));
            itemFormPanel.add(nameLabel);
            itemFormPanel.add(nameField);
        }

    }

    // Effects: helper function, create JLabel with text.
    public JLabel createTextLabel(String text) {
        JLabel label = new JLabel();
        label.setText(text);
        return label;
    }

}
