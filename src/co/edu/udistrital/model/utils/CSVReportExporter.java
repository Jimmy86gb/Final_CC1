package co.edu.udistrital.model.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import co.edu.udistrital.model.structures.SimpleList;
import co.edu.udistrital.model.structures.SimpleList.Iterator;


public class CSVReportExporter {

	private static final String SEPARATOR = ";";

	
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