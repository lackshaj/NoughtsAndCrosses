module comp1322.part1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens comp1322.part1 to javafx.fxml;
    exports comp1322.part1;
}