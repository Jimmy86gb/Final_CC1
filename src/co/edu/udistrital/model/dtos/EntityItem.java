package co.edu.udistrital.model.dtos;

public class EntityItem {
    private final String id;
    private final String displayName;

    public EntityItem(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() { return id; }
    
    @Override
    public String toString() { return displayName; } // Esto es lo que verá el usuario
}