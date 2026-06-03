package co.edu.udistrital.model.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import co.edu.udistrital.model.dtos.DatabaseSnapshot;


public class BinaryDatabaseManager {

	private static final String FILE_PATH = "autorescate_db.dat";

	public static boolean saveSnapshot(DatabaseSnapshot snapshot) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
			oos.writeObject(snapshot);
			return true;
		} catch (IOException e) {
			System.err.println("Error guardando la BD binaria: " + e.getMessage());
			return false;
		}
	}

	public static DatabaseSnapshot loadSnapshot() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			return null; 
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (DatabaseSnapshot) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error cargando la BD binaria: " + e.getMessage());
			return null;
		}
	}
}