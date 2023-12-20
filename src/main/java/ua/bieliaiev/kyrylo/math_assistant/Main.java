package ua.bieliaiev.kyrylo.math_assistant;

import ua.bieliaiev.kyrylo.math_assistant.controller.EquationsController;
import ua.bieliaiev.kyrylo.math_assistant.view.EquationView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Path.of("src/main/resources/database.properties"),
					StandardOpenOption.READ));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		new EquationView(new EquationsController(properties));
	}
}
