package gui;

import log.ExceptionLogger;

import java.io.*;
import java.util.HashMap;


class PropertiesKeeperSingleton implements Serializable {

    private static final PropertiesKeeperSingleton m_keeper = new PropertiesKeeperSingleton();
    private static final String m_fileName = "RobotsWindowProperty.bin";
    private static final String m_directory = System.getProperty("user.home");
    private final HashMap<String, Storable> m_storableWindows;

    private PropertiesKeeperSingleton() {
        m_storableWindows = new HashMap<>();
    }

    static PropertiesKeeperSingleton getInstance() {
        return m_keeper;
    }

    void register(String title, Storable object) {
        m_storableWindows.put(title, object);
    }

    void unregister(String title) {
        m_storableWindows.remove(title);
    }

    void saveProperties() {
        HashMap<String, HashMap<Storable.Property, String>> windowsProperties = new HashMap<>();
        m_storableWindows.forEach((String title, Storable object) -> {
            windowsProperties.put(title, object.getProperties());
        });
        File file = new File(m_directory + File.separator + m_fileName);
        try (OutputStream os = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
                try {
                    oos.writeObject(windowsProperties);
                    oos.flush();
                } catch (Exception exception) {
                    ExceptionLogger.writeException(exception.getStackTrace(), "Exception during saving properties");
                }
            }
        } catch (IOException exception) {
            ExceptionLogger.writeException(exception.getStackTrace(), "Exception during write file");
        }
    }

    void loadProperties() {
        File file = new File(m_directory + File.separator + m_fileName);
        HashMap<String, HashMap<Storable.Property, String>> windowsProperties = new HashMap<>();
        try (InputStream is = new FileInputStream(file)) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
                windowsProperties = (HashMap<String, HashMap<Storable.Property, String>>) ois.readObject();
            } catch (ClassNotFoundException exception) {
                ExceptionLogger.writeException(exception.getStackTrace(), "Exception during load properties");
            }
        } catch (IOException exception) {
            ExceptionLogger.writeException(exception.getStackTrace(), "Exception during read file");
        }

        if (windowsProperties.size() == 0)
            return;
        HashMap<String, HashMap<Storable.Property, String>> finalWindowsProperties = windowsProperties;
        m_storableWindows.forEach((String title, Storable object) -> {
            HashMap<Storable.Property, String> properties = finalWindowsProperties.get(title);
            if (properties != null && properties.size() != 0) {
                object.setProperties(finalWindowsProperties.get(title));
            }
        });
    }
}