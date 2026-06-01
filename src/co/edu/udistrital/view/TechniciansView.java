package co.edu.udistrital.view;

import co.edu.udistrital.controller.AppController;
import co.edu.udistrital.model.enums.ProfileType;
import javafx.scene.layout.*;

public class TechniciansView {
    private VBox rootContainer;
    private AppController appController;
    private ProfileType role;
    
    public TechniciansView(ProfileType role) {
    }
    
    public void setController(AppController controller) { 
    	this.appController = controller; 
    }

   
    public VBox getView() { 
    	return rootContainer; 
    	}
}