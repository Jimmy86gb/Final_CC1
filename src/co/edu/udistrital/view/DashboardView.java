package co.edu.udistrital.view;

import co.edu.udistrital.model.enums.ProfileType;
import javafx.scene.layout.VBox;

public class DashboardView {
    private VBox rootContainer;
    private ProfileType role;
    public DashboardView(ProfileType role) {
        this.role = role;
        rootContainer = new VBox(30);
    }

    public VBox getView() {
        return rootContainer;
    }
}