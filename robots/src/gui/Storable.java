package gui;

import java.util.HashMap;

interface Storable {
    enum Property {
        LOCATION,
        SIZE,
        IS_ICON,
        IS_MAXIMUM,
        EXTENDED_STATE
    }

    String separator = ";";

    HashMap<Property, String> getProperties();

    void setProperties(HashMap<Property, String> properties);
}
