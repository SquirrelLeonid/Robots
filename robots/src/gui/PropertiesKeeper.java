package gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.HashMap;

enum Property {
    LOCATION,
    SIZE,
    IS_ICON,
    IS_MAXIMUM
}

class PropertiesKeeper implements Serializable {
    private HashMap<String, HashMap<Property, String>> windowsProperties;
    private static final String fileName = "RobotsWindowProperty.bin";
    private static final String separator = ";";

    PropertiesKeeper() {
        windowsProperties = new HashMap<>();
    }

    void saveProperties(JInternalFrame frame) {
        String windowName = frame.getTitle();
        HashMap<Property, String> properties = getProperties(frame);
        windowsProperties.put(windowName, properties);
    }

    void loadProperties(JInternalFrame frame) {
        String windowName = frame.getTitle();
        if (windowsProperties.containsKey(windowName)) {
            setProperties(frame, windowsProperties.get(windowName));
        }
    }

    static PropertiesKeeper deserializeKeeper() {
        String userDirectory = System.getProperty("user.home");
        File file = new File(userDirectory + File.separator + fileName);
        PropertiesKeeper restoredKeeper = null;
        try (InputStream is = new FileInputStream(file)) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
                restoredKeeper = (PropertiesKeeper) ois.readObject();
            } catch (ClassNotFoundException exception) {
                //That's ok
            }
        } catch (IOException exception) {
            //That's ok
        }
        return restoredKeeper;
    }

    private HashMap<Property, String> getProperties(JInternalFrame frame) {
        HashMap<Property, String> properties = new HashMap<>();
        for (Property property : Property.values()) {
            switch (property) {
                case LOCATION:
                    Point point = frame.getLocation();
                    properties.put(Property.LOCATION, "" + point.x + separator + point.y);
                    break;
                case SIZE:
                    Dimension size = frame.getSize();
                    properties.put(Property.SIZE, "" + size.width + separator + size.height);
                    break;
                case IS_ICON:
                    properties.put(Property.IS_ICON, "" + frame.isIcon());
                    break;
                case IS_MAXIMUM:
                    properties.put(Property.IS_MAXIMUM, "" + frame.isMaximum());
                    break;
                default:
                    break;
            }
        }
        return properties;
    }

    private void setProperties(JInternalFrame frame, HashMap<Property, String> properties) {
        for (Property property : Property.values()) {
            switch (property) {
                case LOCATION:
                    String[] point = properties.get(Property.LOCATION).split(separator);
                    int x = Integer.parseInt(point[0]);
                    int y = Integer.parseInt(point[1]);
                    frame.setLocation(x, y);
                    break;
                case SIZE:
                    String[] size = properties.get(Property.SIZE).split(separator);
                    int width = Integer.parseInt(size[0]);
                    int height = Integer.parseInt(size[1]);
                    frame.setSize(width, height);
                    break;
                case IS_ICON:
                    String isIconProperty = properties.get(Property.IS_ICON);
                    if (isIconProperty.compareTo("true") == 0) {
                        try {
                            frame.setIcon(true);
                        } catch (PropertyVetoException exception) {
                            //That's ok
                        }
                    }
                    break;
                case IS_MAXIMUM:
                    String isMaximumProperty = properties.get(Property.IS_MAXIMUM);
                    if (isMaximumProperty.compareTo("true") == 0) {
                        try {
                            frame.setMaximum(true);
                        } catch (PropertyVetoException exception) {
                            //That's ok
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    void serializeKeeper() {
        String userDirectory = System.getProperty("user.home");
        File file = new File(userDirectory + File.separator + fileName);
        try (OutputStream os = new FileOutputStream(file)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
                try {
                    oos.writeObject(this);
                    oos.flush();
                } finally {
                    oos.close();
                }
            } finally {
                os.close();
            }
        } catch (IOException exception) {
            //That's Ok
        }
    }
}
