module pharmacy.pillcapsuleanalyser {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;


    opens Application to javafx.fxml;
    exports Application;
    exports Manager;
    opens Manager to javafx.fxml;
}