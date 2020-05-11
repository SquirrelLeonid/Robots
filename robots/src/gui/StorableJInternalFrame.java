package gui;

import log.ExceptionLogger;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

public class StorableJInternalFrame extends JInternalFrame implements Storable {

    StorableJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    @Override
    public HashMap<Property, String> getProperties() {
        HashMap<Property, String> properties = new HashMap<>();
        for (Property property : Property.values()) {
            switch (property) {
                case LOCATION:
                    properties.put(Property.LOCATION, "" + this.getX() + separator + this.getY());
                    break;
                case SIZE:
                    properties.put(Property.SIZE, "" + this.getWidth() + separator + this.getHeight());
                    break;
                case IS_ICON:
                    properties.put(Property.IS_ICON, "" + this.isIcon());
                    break;
                case IS_MAXIMUM:
                    properties.put(Property.IS_MAXIMUM, "" + this.isMaximum());
                    break;
                default:
                    break;
            }
        }
        return properties;
    }

    @Override
    public void setProperties(HashMap<Property, String> properties) {
        for (Map.Entry<Property, String> property : properties.entrySet()) {
            switch (property.getKey()) {
                case LOCATION:
                    String[] point = properties.get(Property.LOCATION).split(separator);
                    int x = Integer.parseInt(point[0]);
                    int y = Integer.parseInt(point[1]);
                    this.setLocation(x, y);
                    break;
                case SIZE:
                    String[] size = properties.get(Property.SIZE).split(separator);
                    int width = Integer.parseInt(size[0]);
                    int height = Integer.parseInt(size[1]);
                    this.setSize(width, height);
                    break;
                case IS_ICON:
                    String isIconProperty = properties.get(Property.IS_ICON);
                    if (isIconProperty.compareTo(Boolean.TRUE.toString()) == 0) {
                        try {
                            this.setIcon(true);
                        } catch (PropertyVetoException exception) {
                            ExceptionLogger.writeException(exception.getStackTrace(), "Exception during set properties");
                        }
                    }
                    break;
                case IS_MAXIMUM:
                    String isMaximumProperty = properties.get(Property.IS_MAXIMUM);
                    if (isMaximumProperty.compareTo(Boolean.TRUE.toString()) == 0) {
                        try {
                            this.setMaximum(true);
                        } catch (PropertyVetoException exception) {
                            ExceptionLogger.writeException(exception.getStackTrace(), "Exception during set properties");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}