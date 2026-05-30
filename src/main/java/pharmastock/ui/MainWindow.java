package pharmastock.ui;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import pharmastock.model.*;
import pharmastock.service.InventoryManager;
import pharmastock.service.ChatBot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;

public class MainWindow {

    private InventoryManager manager = new InventoryManager();
    private ChatBot chatBot = new ChatBot();

    private TextField txtId       = new TextField();
    private TextField txtName     = new TextField();
    private TextField txtPrice    = new TextField();
    private TextField txtStock    = new TextField();
    private TextField txtSchedule = new TextField();
    private CheckBox  cbLicense   = new CheckBox("Doctor License Required");
    private TextField txtSymptom  = new TextField();
    private TextField txtDiscount = new TextField();
    private ComboBox<String> comboType = new ComboBox<>();

    private Label lSchedule = new Label("Schedule Level:");
    private Label lSymptom  = new Label("Symptom:");
    private Label lDiscount = new Label("Discount (%):");

    private Label lblStatus = new Label("Ready.");

    private ComboBox<Medication> comboSelectMed = new ComboBox<>();
    private TextField txtCustName  = new TextField();
    private TextField txtCustPhone = new TextField();
    private TextField txtSaleQty   = new TextField();
    private TextArea  txtInvoiceArea = new TextArea();

    private ArrayList<Medication> cartItems      = new ArrayList<>();
    private ArrayList<Integer>    cartQuantities = new ArrayList<>();

    public MainWindow(String role) {

        manager.loadFile();
        chatBot.setInventoryContext(manager.getAllMedicinesAsText());

        Stage mainStage = new Stage();

        // ========== HEADER ==========
        Label appTitle    = new Label("PharmaStock");
        Label appSubtitle = new Label("Logged in as: " + role.toUpperCase());
        appTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        appTitle.setTextFill(Color.WHITE);
        appSubtitle.setFont(Font.font("Georgia", 13));
        appSubtitle.setTextFill(Color.web("#CBD5E1"));

        VBox titleBox = new VBox(2, appTitle, appSubtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(titleBox);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle("-fx-background-color: #1E3A5F;");

        // ========== TAB 1: INVENTORY ==========
        Tab tabInventory = new Tab("📦 Inventory Management");
        tabInventory.setClosable(false);

        String fieldStyle =
                "-fx-background-color: #F8FAFC;" +
                        "-fx-border-color: #CBD5E1;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 6 8 6 8;";

        String labelStyle =
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-font-weight: bold;";

        txtId.setStyle(fieldStyle);       txtId.setPromptText("e.g. M001");       txtId.setPrefWidth(210);
        txtName.setStyle(fieldStyle);     txtName.setPromptText("e.g. Panadol");  txtName.setPrefWidth(210);
        txtPrice.setStyle(fieldStyle);    txtPrice.setPromptText("e.g. 150.00");  txtPrice.setPrefWidth(210);
        txtStock.setStyle(fieldStyle);    txtStock.setPromptText("e.g. 50");      txtStock.setPrefWidth(210);
        txtSchedule.setStyle(fieldStyle); txtSchedule.setPromptText("1 to 5");    txtSchedule.setPrefWidth(210);
        txtSymptom.setStyle(fieldStyle);  txtSymptom.setPromptText("e.g. Fever"); txtSymptom.setPrefWidth(210);
        txtDiscount.setStyle(fieldStyle); txtDiscount.setPromptText("e.g. 10.0"); txtDiscount.setPrefWidth(210);

        comboType.getItems().addAll("Prescription", "OTC");
        comboType.setValue("Prescription");
        comboType.setPrefWidth(210);
        comboType.setStyle("-fx-font-size: 13px;");
        cbLicense.setStyle("-fx-font-size: 12px;");

        Label lID    = new Label("Medicine ID:");    lID.setStyle(labelStyle);
        Label lName  = new Label("Medicine Name:");  lName.setStyle(labelStyle);
        Label lPrice = new Label("Price (Rs.):");    lPrice.setStyle(labelStyle);
        Label lStock = new Label("Stock Qty:");      lStock.setStyle(labelStyle);
        Label lType  = new Label("Type:");           lType.setStyle(labelStyle);
        lSchedule.setStyle(labelStyle);
        lSymptom.setStyle(labelStyle);
        lDiscount.setStyle(labelStyle);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(11);
        form.add(lID,       0, 0); form.add(txtId,       1, 0);
        form.add(lName,     0, 1); form.add(txtName,     1, 1);
        form.add(lPrice,    0, 2); form.add(txtPrice,    1, 2);
        form.add(lStock,    0, 3); form.add(txtStock,    1, 3);
        form.add(lType,     0, 4); form.add(comboType,   1, 4);
        form.add(lSchedule, 0, 5); form.add(txtSchedule, 1, 5);
        form.add(cbLicense, 0, 6, 2, 1);
        form.add(lSymptom,  0, 7); form.add(txtSymptom,  1, 7);
        form.add(lDiscount, 0, 8); form.add(txtDiscount, 1, 8);

        lSymptom.setVisible(false);    lSymptom.setManaged(false);
        txtSymptom.setVisible(false);  txtSymptom.setManaged(false);
        lDiscount.setVisible(false);   lDiscount.setManaged(false);
        txtDiscount.setVisible(false); txtDiscount.setManaged(false);

        comboType.setOnAction(e -> {
            boolean isPrescription = comboType.getValue().equals("Prescription");
            lSchedule.setVisible(isPrescription);    lSchedule.setManaged(isPrescription);
            txtSchedule.setVisible(isPrescription);  txtSchedule.setManaged(isPrescription);
            cbLicense.setVisible(isPrescription);    cbLicense.setManaged(isPrescription);
            lSymptom.setVisible(!isPrescription);    lSymptom.setManaged(!isPrescription);
            txtSymptom.setVisible(!isPrescription);  txtSymptom.setManaged(!isPrescription);
            lDiscount.setVisible(!isPrescription);   lDiscount.setManaged(!isPrescription);
            txtDiscount.setVisible(!isPrescription); txtDiscount.setManaged(!isPrescription);
        });

        Button btnAdd    = makePrimaryButton("＋  Add Medicine");
        Button btnView   = makeOutlineButton("📋  View Inventory");
        Button btnSave   = makeOutlineButton("💾  Save Data");
        Button btnSort   = makeOutlineButton("🔤  Sort A–Z");
        Button btnClear  = makeOutlineButton("✖  Clear Fields");
        Button btnLogout = makeRedButton("🚪  Logout");

        btnAdd.setMaxWidth(Double.MAX_VALUE);

        if (role.equals("user")) {
            btnSave.setDisable(true);
            btnSave.setOpacity(0.4);
            btnSave.setTooltip(new Tooltip("Only Admin can save data"));
        }

        btnAdd.setOnAction(e -> {
            handleAddOrUpdateMedicine();
            updateBillingCombo();
            checkLowStockAlerts();
            chatBot.setInventoryContext(manager.getAllMedicinesAsText());
        });
        btnView.setOnAction(e  -> openInventoryWindow(role));
        btnSave.setOnAction(e  -> { manager.saveFile();      setStatus("Saved to pharmacy_db.txt", false); });
        btnSort.setOnAction(e  -> { manager.sortInventory(); setStatus("Inventory sorted A-Z.", false); });
        btnClear.setOnAction(e -> { clearFields();           setStatus("Fields cleared.", false); });

        btnLogout.setOnAction(e -> {
            mainStage.close();
            LoginWindow login = new LoginWindow();
            login.show(MainApp.parentStage);
        });

        HBox actionRow = new HBox(10, btnView, btnSave, btnSort, btnClear, btnLogout);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        Label formHeading = new Label("Add New Medicine");
        formHeading.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        formHeading.setTextFill(Color.web("#1E3A5F"));

        VBox formPanel = new VBox(12, formHeading, new Separator(), form, btnAdd, new Separator(), actionRow);
        formPanel.setPadding(new Insets(20));
        formPanel.setStyle("-fx-background-color: #FFFFFF;");
        tabInventory.setContent(formPanel);

        // ========== TAB 2: BILLING ==========
        Tab tabBilling = new Tab("🧾 Billing System");
        tabBilling.setClosable(false);

        VBox billingPanel = new VBox(12);
        billingPanel.setPadding(new Insets(20));
        billingPanel.setStyle("-fx-background-color: #FFFFFF;");

        Label billHeading = new Label("Customer Billing Counter");
        billHeading.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        billHeading.setTextFill(Color.web("#1E3A5F"));

        GridPane billForm = new GridPane();
        billForm.setHgap(12);
        billForm.setVgap(11);

        comboSelectMed.setStyle("-fx-font-size: 13px;");
        comboSelectMed.setPrefWidth(300);
        comboSelectMed.setPromptText("-- Select Medicine --");
        updateBillingCombo();

        txtCustName.setStyle(fieldStyle);  txtCustName.setPromptText("Enter Customer Name");  txtCustName.setPrefWidth(300);
        txtCustPhone.setStyle(fieldStyle); txtCustPhone.setPromptText("Enter Phone Number");   txtCustPhone.setPrefWidth(300);
        txtSaleQty.setStyle(fieldStyle);   txtSaleQty.setPromptText("e.g. 2");                 txtSaleQty.setPrefWidth(300);

        Label lSelMed = new Label("Select Medicine:"); lSelMed.setStyle(labelStyle);
        Label lCName  = new Label("Customer Name:");   lCName.setStyle(labelStyle);
        Label lCPhone = new Label("Phone Number:");    lCPhone.setStyle(labelStyle);
        Label lSQty   = new Label("Sale Quantity:");   lSQty.setStyle(labelStyle);

        billForm.add(lSelMed, 0, 0); billForm.add(comboSelectMed, 1, 0);
        billForm.add(lCName,  0, 1); billForm.add(txtCustName,    1, 1);
        billForm.add(lCPhone, 0, 2); billForm.add(txtCustPhone,   1, 2);
        billForm.add(lSQty,   0, 3); billForm.add(txtSaleQty,     1, 3);

        Button btnAddToCart   = makePrimaryButton("➕  Add to Cart");
        Button btnPrintBill   = makePrimaryButton("💰  Process Sale & Print Final Bill");
        Button btnRefreshBill = makeOutlineButton("🔄  New Bill / Reset Cart");

        btnAddToCart.setMaxWidth(Double.MAX_VALUE);
        btnPrintBill.setMaxWidth(Double.MAX_VALUE);
        btnRefreshBill.setMaxWidth(Double.MAX_VALUE);

        txtInvoiceArea.setEditable(false);
        txtInvoiceArea.setPrefHeight(280);
        txtInvoiceArea.setPromptText("Invoice receipt / Cart summary will appear here...");
        txtInvoiceArea.setFont(Font.font("Courier New", FontWeight.BOLD, 13));
        txtInvoiceArea.setStyle("-fx-control-inner-background: #F8FAFC; -fx-text-fill: #1E3A5F;");

        btnAddToCart.setOnAction(e  -> handleAddToCart());
        btnPrintBill.setOnAction(e  -> handleFinalSaleTransaction());
        btnRefreshBill.setOnAction(e -> {
            txtCustName.clear(); txtCustPhone.clear();
            txtSaleQty.clear();  txtInvoiceArea.clear();
            comboSelectMed.setValue(null);
            cartItems.clear();   cartQuantities.clear();
            updateBillingCombo();
            setStatus("Billing panel and cart cleared.", false);
        });

        HBox billActionRow = new HBox(10, btnPrintBill, btnRefreshBill);
        billActionRow.setAlignment(Pos.CENTER);

        billingPanel.getChildren().addAll(billHeading, new Separator(), billForm, btnAddToCart, billActionRow, new Separator(), txtInvoiceArea);
        tabBilling.setContent(billingPanel);

        // ========== TAB PANE — sirf 2 tabs ==========
        TabPane tabPane = new TabPane(tabInventory, tabBilling);
        tabPane.setStyle("-fx-tab-min-width: 160px; -fx-font-size: 13px;");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // ========== STATUS BAR ==========
        lblStatus.setFont(Font.font("Arial", 12));
        lblStatus.setTextFill(Color.web("#374151"));

        HBox statusBar = new HBox(lblStatus);
        statusBar.setPadding(new Insets(8, 20, 8, 20));
        statusBar.setStyle("-fx-background-color: #F1F5F9; -fx-border-color: #E2E8F0; -fx-border-width: 1 0 0 0;");

        VBox root = new VBox(0, header, tabPane, statusBar);

        Scene scene = new Scene(root, 700, 780);
        mainStage.setTitle("PharmaStock — " + role.toUpperCase());
        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMinWidth(600);
        mainStage.setMinHeight(650);
        mainStage.show();

        checkLowStockAlerts();

        // ========== FLOATING CHATBOT BUBBLE ==========
        new ChatBotButton(chatBot, mainStage);
    }

    // ========== LOW STOCK ALERT ==========
    private void checkLowStockAlerts() {
        StringBuilder lowStockMeds = new StringBuilder();
        for (Medication med : manager.getMedicineList()) {
            if (med.getStockCount() <= 5) {
                if (lowStockMeds.length() > 0) lowStockMeds.append(", ");
                lowStockMeds.append(med.getName()).append(" (").append(med.getStockCount()).append(")");
            }
        }
        if (lowStockMeds.length() > 0) {
            lblStatus.setText("⚠️ Low Stock Alert: " + lowStockMeds.toString());
            lblStatus.setTextFill(Color.web("#B45309"));
        } else {
            lblStatus.setText("✔ Ready. All stocks are healthy.");
            lblStatus.setTextFill(Color.web("#16A34A"));
        }
    }

    private void updateBillingCombo() {
        comboSelectMed.setItems(FXCollections.observableArrayList(manager.getMedicineList()));
    }

    private void handleAddToCart() {
        Medication selectedMed = comboSelectMed.getValue();
        String qtyText = txtSaleQty.getText().trim();

        if (selectedMed == null || qtyText.isEmpty()) {
            txtInvoiceArea.setText("⚠ Select a medicine and enter quantity!");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) { txtInvoiceArea.setText("⚠ Quantity must be greater than 0."); return; }
            if (qty > selectedMed.getStockCount()) {
                txtInvoiceArea.setText("⚠ Out of Stock!\nAvailable: " + selectedMed.getStockCount());
                return;
            }
            if (cartItems.contains(selectedMed)) {
                txtInvoiceArea.setText("⚠ Already in cart! Process bill or reset.");
                return;
            }

            cartItems.add(selectedMed);
            cartQuantities.add(qty);

            StringBuilder cartPreview = new StringBuilder("🛒 --- CURRENT BASKET ---\n\n");
            double currentTotal = 0;
            for (int i = 0; i < cartItems.size(); i++) {
                Medication med = cartItems.get(i);
                int q = cartQuantities.get(i);
                double cost = med.getPrice() * q;
                currentTotal += cost;
                cartPreview.append("- ").append(med.getName()).append(" x").append(q)
                        .append(" = Rs. ").append(cost).append("\n");
            }
            cartPreview.append("\n--------------------\n");
            cartPreview.append("Running Total: Rs. ").append(currentTotal);

            txtInvoiceArea.setText(cartPreview.toString());
            setStatus(selectedMed.getName() + " added to basket.", false);
            txtSaleQty.clear();

        } catch (NumberFormatException ex) {
            txtInvoiceArea.setText("⚠ Enter a valid number for quantity.");
        }
    }

    private void handleFinalSaleTransaction() {
        String custName  = txtCustName.getText().trim();
        String custPhone = txtCustPhone.getText().trim();

        if (cartItems.isEmpty()) {
            txtInvoiceArea.setText("⚠ Cart is empty! Add medicines first.");
            return;
        }
        if (custName.isEmpty()) {
            txtInvoiceArea.setText("⚠ Customer Name is required.");
            return;
        }

        StringBuilder bill = new StringBuilder();
        bill.append("====================================\n");
        bill.append("            PHARMASTOCK             \n");
        bill.append("====================================\n");
        bill.append("Customer : ").append(custName).append("\n");
        if (!custPhone.isEmpty()) bill.append("Phone    : ").append(custPhone).append("\n");
        bill.append("------------------------------------\n");
        bill.append(String.format("%-18s %-5s %-10s\n", "Item Name", "Qty", "Price"));
        bill.append("------------------------------------\n");

        double totalBill = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            Medication med = cartItems.get(i);
            int qty = cartQuantities.get(i);
            med.setStockCount(med.getStockCount() - qty);
            double itemCost = med.getPrice() * qty;
            totalBill += itemCost;
            bill.append(String.format("%-18s x%-4d Rs. %-8.2f\n", med.getName(), qty, itemCost));
        }

        bill.append("------------------------------------\n");
        bill.append("TOTAL : Rs. ").append(totalBill).append("\n");
        bill.append("====================================\n");
        bill.append("    Thank you for your business!    \n");

        txtInvoiceArea.setText(bill.toString());

        manager.saveFile();
        checkLowStockAlerts();
        chatBot.setInventoryContext(manager.getAllMedicinesAsText());

        cartItems.clear();
        cartQuantities.clear();
        updateBillingCombo();
    }

    // ========== INVENTORY WINDOW ==========
    private void openInventoryWindow(String role) {

        Stage invStage = new Stage();
        invStage.setTitle("Inventory — All Medicines");

        Label title = new Label("Current Inventory (Table View)");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        title.setTextFill(Color.WHITE);

        HBox invHeader = new HBox(title);
        invHeader.setPadding(new Insets(14, 20, 14, 20));
        invHeader.setStyle("-fx-background-color: #1E3A5F;");

        Label lSearch = new Label("🔍 Search:");
        lSearch.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Type name to filter...");
        txtSearch.setPrefWidth(300);
        txtSearch.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-radius: 4; -fx-padding: 5 8 5 8;");

        HBox searchRow = new HBox(12, lSearch, txtSearch);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.setPadding(new Insets(12, 16, 12, 16));
        searchRow.setStyle("-fx-background-color: #F1F5F9;");

        TableView<Medication> table = new TableView<>();
        table.setStyle("-fx-font-size: 13px;");

        TableColumn<Medication, String>  colId      = new TableColumn<>("Medicine ID");
        TableColumn<Medication, String>  colName    = new TableColumn<>("Medicine Name");
        TableColumn<Medication, Double>  colPrice   = new TableColumn<>("Price (Rs.)");
        TableColumn<Medication, Integer> colStock   = new TableColumn<>("Stock Qty");
        TableColumn<Medication, String>  colDetails = new TableColumn<>("Type / Details");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));           colId.setPrefWidth(100);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));        colName.setPrefWidth(160);
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));      colPrice.setPrefWidth(110);
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockCount")); colStock.setPrefWidth(90);
        colDetails.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().displayInfo()));
        colDetails.setPrefWidth(300);

        table.getColumns().addAll(colId, colName, colPrice, colStock, colDetails);

        FilteredList<Medication> filteredData = new FilteredList<>(
                FXCollections.observableArrayList(manager.getMedicineList()), p -> true
        );

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(med -> {
                if (newVal == null || newVal.isEmpty()) return true;
                return med.getName().toLowerCase().contains(newVal.toLowerCase());
            });
        });

        table.setItems(filteredData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        Label lQuickStock = new Label("Update Stock for Selected:");
        lQuickStock.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1E3A5F;");

        TextField txtAddStockQty = new TextField();
        txtAddStockQty.setPromptText("Qty to ADD");
        txtAddStockQty.setPrefWidth(120);
        txtAddStockQty.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-padding: 5;");

        Button btnUpdateStock = makePrimaryButton("➕ Update Stock");
        btnUpdateStock.setStyle("-fx-background-color: #16A34A; -fx-text-fill: white; -fx-padding: 6 12 6 12; -fx-cursor: hand; -fx-font-weight: bold;");

        Label lblActionStatus = new Label("");
        lblActionStatus.setFont(Font.font("Arial", 12));

        btnUpdateStock.setOnAction(e -> {
            Medication selected = table.getSelectionModel().getSelectedItem();
            String qtyStr = txtAddStockQty.getText().trim();

            if (selected == null) {
                lblActionStatus.setText("⚠ Select a medicine first.");
                lblActionStatus.setTextFill(Color.web("#DC2626"));
                return;
            }
            if (qtyStr.isEmpty()) {
                lblActionStatus.setText("⚠ Enter quantity.");
                lblActionStatus.setTextFill(Color.web("#DC2626"));
                return;
            }
            try {
                int addQty = Integer.parseInt(qtyStr);
                if (addQty <= 0) {
                    lblActionStatus.setText("⚠ Qty must be > 0.");
                    lblActionStatus.setTextFill(Color.web("#DC2626"));
                    return;
                }
                selected.setStockCount(selected.getStockCount() + addQty);
                manager.saveFile();
                table.refresh();
                txtAddStockQty.clear();
                lblActionStatus.setText("✔ Stock updated: " + selected.getName());
                lblActionStatus.setTextFill(Color.web("#16A34A"));
                checkLowStockAlerts();
                chatBot.setInventoryContext(manager.getAllMedicinesAsText());
            } catch (NumberFormatException ex) {
                lblActionStatus.setText("⚠ Invalid number.");
                lblActionStatus.setTextFill(Color.web("#DC2626"));
            }
        });

        HBox actionPanelRow = new HBox(10, lQuickStock, txtAddStockQty, btnUpdateStock);
        actionPanelRow.setAlignment(Pos.CENTER_LEFT);
        actionPanelRow.setPadding(new Insets(10, 16, 5, 16));
        actionPanelRow.setStyle("-fx-background-color: #FFFFFF;");

        VBox invRoot = new VBox(0, invHeader, searchRow, table, actionPanelRow);

        if (role.equals("admin")) {
            Label lDel = new Label("Delete by ID:");
            lDel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");

            TextField txtDeleteId = new TextField();
            txtDeleteId.setPromptText("ID to delete");
            txtDeleteId.setPrefWidth(120);
            txtDeleteId.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-padding: 5;");

            Button btnDelete = makeRedButton("🗑  Delete");

            btnDelete.setOnAction(e -> {
                String delId = txtDeleteId.getText().trim();
                if (delId.isEmpty()) {
                    lblActionStatus.setText("⚠ Enter ID first.");
                    lblActionStatus.setTextFill(Color.web("#DC2626"));
                    return;
                }
                boolean found = false;
                for (int i = 0; i < manager.getMedicineList().size(); i++) {
                    if (manager.getMedicineList().get(i).getId().equalsIgnoreCase(delId)) {
                        String deletedName = manager.getMedicineList().get(i).getName();
                        manager.getMedicineList().remove(i);
                        table.setItems(new FilteredList<>(
                                FXCollections.observableArrayList(manager.getMedicineList()), p -> true));
                        txtDeleteId.clear();
                        lblActionStatus.setText("✔ Deleted: " + deletedName);
                        lblActionStatus.setTextFill(Color.web("#16A34A"));
                        setStatus("Deleted: " + deletedName, false);
                        updateBillingCombo();
                        checkLowStockAlerts();
                        chatBot.setInventoryContext(manager.getAllMedicinesAsText());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    lblActionStatus.setText("⚠ No medicine found: " + delId);
                    lblActionStatus.setTextFill(Color.web("#DC2626"));
                }
            });

            HBox deleteRow = new HBox(10, lDel, txtDeleteId, btnDelete, lblActionStatus);
            deleteRow.setAlignment(Pos.CENTER_LEFT);
            deleteRow.setPadding(new Insets(5, 16, 12, 16));
            deleteRow.setStyle("-fx-background-color: #FFFFFF;");
            invRoot.getChildren().add(deleteRow);

        } else {
            Label noDelete = new Label("ℹ  View only mode. Login as Admin to manage.");
            noDelete.setFont(Font.font("Arial", 12));
            noDelete.setTextFill(Color.web("#6B7280"));
            HBox infoRow = new HBox(noDelete, lblActionStatus);
            infoRow.setPadding(new Insets(5, 16, 12, 16));
            infoRow.setStyle("-fx-background-color: #FFFBEB;");
            invRoot.getChildren().add(infoRow);
        }

        Scene invScene = new Scene(invRoot, 920, 640);
        invStage.setScene(invScene);
        invStage.setResizable(true);
        invStage.show();
    }

    // ========== ADD OR UPDATE MEDICINE ==========
    private void handleAddOrUpdateMedicine() {
        try {
            String id        = txtId.getText().trim();
            String name      = txtName.getText().trim();
            String priceText = txtPrice.getText().trim();
            String stockText = txtStock.getText().trim();

            if (id.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                setStatus("ID, Name, Price and Stock are all required.", true);
                return;
            }

            double price = Double.parseDouble(priceText);
            int    stock = Integer.parseInt(stockText);

            if (price < 0 || stock < 0) {
                setStatus("Price and Stock cannot be negative.", true);
                return;
            }

            Medication existingMed = null;
            for (Medication med : manager.getMedicineList()) {
                if (med.getId().equalsIgnoreCase(id)) {
                    existingMed = med;
                    break;
                }
            }

            if (existingMed != null) {
                existingMed.setStockCount(existingMed.getStockCount() + stock);
                existingMed.setPrice(price);
                manager.saveFile();
                setStatus("Stock updated for " + name + ". New total: " + existingMed.getStockCount(), false);
                clearFields();
                return;
            }

            if (comboType.getValue().equals("Prescription")) {
                String schedText = txtSchedule.getText().trim();
                if (schedText.isEmpty()) {
                    setStatus("Schedule Level is required for Prescription.", true);
                    return;
                }
                int     schedule = Integer.parseInt(schedText);
                boolean license  = cbLicense.isSelected();
                manager.addMedicine(id, name, price, stock, license, schedule);

            } else {
                String symptom      = txtSymptom.getText().trim();
                String discountText = txtDiscount.getText().trim();
                if (symptom.isEmpty() || discountText.isEmpty()) {
                    setStatus("Symptom and Discount are required for OTC.", true);
                    return;
                }
                double discount = Double.parseDouble(discountText);
                manager.addMedicine(id, name, price, stock, symptom, discount);
            }

            setStatus("Medicine added: " + name, false);
            clearFields();

        } catch (NumberFormatException ex) {
            setStatus("Invalid number in Price, Stock, Schedule or Discount.", true);
        } catch (Exception ex) {
            setStatus("Error: " + ex.getMessage(), true);
        }
    }

    // ========== HELPERS ==========
    private void clearFields() {
        txtId.clear(); txtName.clear(); txtPrice.clear(); txtStock.clear();
        txtSchedule.clear(); txtSymptom.clear(); txtDiscount.clear();
        cbLicense.setSelected(false);
        comboType.setValue("Prescription");
    }

    private void setStatus(String msg, boolean isError) {
        lblStatus.setText(isError ? "⚠  " + msg : "✔  " + msg);
        lblStatus.setTextFill(isError ? Color.web("#DC2626") : Color.web("#16A34A"));
    }

    private Button makePrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setTextFill(Color.WHITE);
        String base  = "-fx-background-color: #1E3A5F; -fx-background-radius: 5; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        String hover = "-fx-background-color: #2D5282; -fx-background-radius: 5; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Button makeOutlineButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 13));
        btn.setTextFill(Color.web("#1E3A5F"));
        String base  = "-fx-background-color: #FFFFFF; -fx-border-color: #1E3A5F; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12; -fx-cursor: hand;";
        String hover = "-fx-background-color: #EBF4FF; -fx-border-color: #1E3A5F; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Button makeRedButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 13));
        btn.setTextFill(Color.web("#DC2626"));
        String base  = "-fx-background-color: #FFFFFF; -fx-border-color: #DC2626; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12; -fx-cursor: hand;";
        String hover = "-fx-background-color: #FEF2F2; -fx-border-color: #DC2626; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 7 12 7 12; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }
}