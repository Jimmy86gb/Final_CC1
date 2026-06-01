package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.scene.layout.*;

public class KitsView {
    private VBox rootContainer;
    private GridPane dataGrid;
    private int currentRow = 1;
    private AppController appController;
    private ProfileType role;
    
    public KitsView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(25);
    }

    public void setController(AppController controller) { 
    	this.appController = controller; 
    	}

    public VBox getView() { 
    	return rootContainer; 
    	}
}