package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.scene.layout.*;
public class RequestsView {
    private VBox rootContainer;
    private ProfileType role;
    private AppController appController;
    
    public RequestsView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
 
    }

    public void setController(AppController controller) {
        this.appController = controller;
    }

    public VBox getView() { return rootContainer; }
}