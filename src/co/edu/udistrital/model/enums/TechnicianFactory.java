package co.edu.udistrital.model.enums;


public class TechnicianFactory {

    
    public TechnicianSpecialty generaTechnicianSpecialty(String input) {
        
        String cleanInput = input.replaceAll("\\s+", "").toLowerCase();
        
        
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