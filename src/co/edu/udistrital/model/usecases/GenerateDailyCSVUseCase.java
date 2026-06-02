package co.edu.udistrital.model.usecases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import co.edu.udistrital.model.dtos.ResponseDTO;
import co.edu.udistrital.model.entities.Report;
import co.edu.udistrital.model.enums.CriticLevel;
import co.edu.udistrital.model.enums.ReportStatus;
import co.edu.udistrital.model.repositories.ReportRepository;
import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;
import co.edu.udistrital.model.utils.CSVReportExporter;

/**
 * Caso de uso encargado de filtrar los casos exitosos del día actual,
 * estructurar la información (incluyendo tipos de cliente y unidad) y orquestar
 * la generación del reporte.
 *
 * @author Juan David Diaz Perez
 */
public class GenerateDailyCSVUseCase {

	/**
	 * Instancia privada del repositorio de reportes en memoria
	 */
	private final ReportRepository reportRepository;

	/**
	 * Constructor que inyecta el repositorio al caso de uso
	 * 
	 * @param reportRepository Repositorio de reportes
	 */
	public GenerateDailyCSVUseCase(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Caso de uso que da el texto en crudo a guardar en el archivo
	 * 
	 * @param directoryPath Direccion de guardado
	 * @return Si la operacion es exitosa
	 */
	public ResponseDTO execute(String directoryPath) {
		try {
			SimpleList<String[]> csvRows = new SimpleList<>();
			LocalDate today = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			CriticLevel[] levels = { CriticLevel.HIGH, CriticLevel.MEDIUM, CriticLevel.LOW };

			for (int i = 0; i < levels.length; i++) {
				SimpleList<Report> subList = reportRepository.getReportsByCriticLevelAndStatus(levels[i],
						ReportStatus.DONE);
				Iterator<Report> iterator = subList.iterador();

				while (iterator.hasNext()) {
					Report report = iterator.Next();

					// Filtro: Solo casos de HOY
					if (report.getReportTime().toLocalDate().equals(today)) {

						// 1. Extracción Segura de Cliente (Ahora con Tipo)
						String clientName = report.getClient() != null ? report.getClient().getName() : "N/A";
						String clientType = (report.getClient() != null && report.getClient().getType() != null)
								? report.getClient().getType().toString()
								: "N/A";
						String clientContact = report.getClient() != null ? report.getClient().getContactInfo() : "N/A";

						// 2. Extracción Segura de Recursos (Ahora con Tipo de Unidad)
						String techName = report.getAssignedTechnician() != null
								? report.getAssignedTechnician().getName()
								: "N/A";
						String unitId = report.getAssignedUnit() != null ? report.getAssignedUnit().getId().toString()
								: "N/A";
						String unitType = (report.getAssignedUnit() != null
								&& report.getAssignedUnit().getType() != null)
										? report.getAssignedUnit().getType().toString()
										: "N/A";

						// 3. Empaquetar la fila en un arreglo de Strings
						String[] row = { report.getTicketID().toString(), report.getReportTime().format(formatter),
								clientName, clientType, // Novedad agregada
								clientContact, report.getProblemDescription(), report.getProblemType().getDisplayName(),
								report.getPriority().getDisplayName(), report.getReportZone().getDisplayName(),
								techName, unitId, unitType, // Novedad agregada
								report.getStatus().getDisplayName() };

						csvRows.add(row);
					}
				}
			}

			if (csvRows.isEmpty()) {
				return new ResponseDTO(false, "No hay casos finalizados el día de hoy para exportar.");
			}

			String[] headers = { "ID Tiquete", "Fecha y Hora", "Cliente", "Tipo de Cliente", "Contacto", "Descripcion",
					"Especialidad", "Prioridad", "Zona", "Tecnico Asignado", "ID Unidad", "Tipo de Unidad", "Estado" };

			String dateString = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String fullFilePath = directoryPath + "/Casos_Cubiertos_" + dateString + ".csv";

			boolean success = CSVReportExporter.exportData(headers, csvRows, fullFilePath);

			if (success) {
				return new ResponseDTO(true, "Archivo CSV generado exitosamente en: " + fullFilePath);
			} else {
				return new ResponseDTO(false, "Ocurrió un error en el disco al intentar guardar el archivo.");
			}

		} catch (Exception e) {
			return new ResponseDTO(false, "Error inesperado al generar el reporte diario: " + e.getMessage());
		}
	}
}