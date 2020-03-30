package gui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class StorableJFrame extends JFrame implements Storable {

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
                case EXTENDED_STATE:
                    properties.put(Property.EXTENDED_STATE, "" + this.getExtendedState());
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
                case EXTENDED_STATE:
                    int extendedState = Integer.parseInt(properties.get(Property.EXTENDED_STATE));
                    this.setExtendedState(extendedState);
                    break;
                default:
                    break;
            }
        }
    }
}
