package co.edu.udistrital.model.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;

/**
 * Clase utilitaria genérica encargada de la persistencia de datos en archivos
 * planos CSV. Desacoplada de las entidades del negocio (Clean Architecture).
 *
 * @author Juan David Diaz Perez
 */
public class CSVReportExporter {

	private static final String SEPARATOR = ";";

	/**
	 * Escribe cualquier conjunto de datos en un archivo CSV genérico.
	 * 
	 * @param headers  Arreglo con los títulos de las columnas.
	 * @param dataRows Lista donde cada elemento es un arreglo de Strings (una
	 *                 fila).
	 * @param filePath Ruta completa donde se guardará el archivo.
	 * @return true si el archivo se guardó correctamente, false si hubo un error.
	 */
	public static boolean exportData(String[] headers, SimpleList<String[]> dataRows, String filePath) {

		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

			writer.println(String.join(SEPARATOR, headers));

			Iterator<String[]> iterator = dataRows.iterador();
			while (iterator.hasNext()) {
				String[] row = iterator.Next();
				writer.println(String.join(SEPARATOR, row));
			}

			return true;

		} catch (IOException e) {
			System.err.println("Error de escritura al generar el CSV: " + e.getMessage());
			return false;
		}
	}
}