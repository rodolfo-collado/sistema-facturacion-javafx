module ni.edu.uam.sistemafacturacionjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires static lombok;


    exports ni.edu.uam.sistemafacturacionjavafx.application;
    exports ni.edu.uam.sistemafacturacionjavafx.model;


    opens ni.edu.uam.sistemafacturacionjavafx.controller to javafx.fxml;
    opens ni.edu.uam.sistemafacturacionjavafx.model to javafx.base;
}
