package co.edu.udistrital.model.enums;

/**
 * Factory robusto para convertir strings de la vista a Enums de especialidad.
 * @author Jimmy86gb
 */
public class TechnicianFactory {

    /**
     * Convierte el texto recibido (aunque tenga espacios) a un Enum de especialidad válido.
     */
    public TechnicianSpecialty generaTechnicianSpecialty(String input) {
        // 1. Limpieza: Quitamos todos los espacios y lo pasamos a minúsculas
        String cleanInput = input.replaceAll("\\s+", "").toLowerCase();
        
        // 2. Mapeo flexible
        return switch (cleanInput) {
            case "mecanicogeneral" -> TechnicianSpecialty.GENERAL_MECHANICS;
            case "electricoautomotriz" -> TechnicianSpecialty.AUTOMOTIVE_ELECTRICITY;
            case "cerrajerodevehiculos" -> TechnicianSpecialty.VEHICULAR_LOCKSMITH;
            case "operadordegrua" -> TechnicianSpecialty.CRANE_OPERATION;
            case "operariomontallantas" -> TechnicianSpecialty.PLUMBING;
            default -> throw new IllegalArgumentException("Tipo de especialidad no soportado: " + input);
        };
    }
}