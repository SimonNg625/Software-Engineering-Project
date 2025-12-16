package sportapp;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

import sportapp.util.DataInit;

/**
 * Application entry point.
 * <p>
 * Reads application properties, initializes default data and starts the console UI loop.
 */
public class Main {
    /**
     * Constructs a Main instance.
     * <p>
     * This constructor initializes the Main object with default values.
     */
    public Main() {
        // Default constructor
    }

    /**
     * Main program entry.
     * @param args program arguments (ignored)
     */
    public static void main(String[] args) {
        String root_path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String version, course_info, proj_name, proj_title, proj_group;
        String loadRecord = "false";

        try {
            String prop_path = "app.properties";
            Properties prop = new Properties();
            prop.load(new FileInputStream(prop_path));
            
            version = prop.getProperty("app.version", "v3.0");
            course_info = prop.getProperty("course.info", "2025/26 Sem A");
            proj_name = prop.getProperty("project.name", "Sport Centre Management System");
            proj_title = prop.getProperty("project.title", "CS3343 Group Project");
            proj_group = prop.getProperty("project.group", "5");
            loadRecord = prop.getProperty("load.record", "false");

            System.out.printf("\n%s\n", course_info);
            System.out.printf("%s Group %s\n", proj_title, proj_group);
            System.out.printf("%s\n", proj_name);
            System.out.printf("Version: %s\n", version);
            System.out.println("");
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        DataInit.initDefaultData(loadRecord);

        Scanner scanner = new Scanner(System.in);
        SportApp app = new SportApp(scanner);
        app.run();

        scanner.close();
    }
}