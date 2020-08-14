package service.localization;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class Localizer
{
    //private static final String LANGUAGE_FOLDER = setLanguageFolder();
    private static final Localizer INSTANCE;
    private static final String BUNDLE_BASENAME = "Localization\\Language";

    private static Locale m_currentLocale;
    private static ResourceBundle m_previousBundle;
    private static ResourceBundle m_currentBundle;
    private static PropertyChangeSupport m_support;

    static {
        INSTANCE = new Localizer();
        m_currentLocale = Locale.getDefault();
        m_support = new PropertyChangeSupport(INSTANCE);
        m_currentBundle = ResourceBundle.getBundle(BUNDLE_BASENAME, m_currentLocale);
    }

    public static ResourceBundle getCurrentBundle() {
        return m_currentBundle;
    }

    public static void updateLocalize(Locale locale)
    {
        m_previousBundle = m_currentBundle;
        m_currentBundle = ResourceBundle.getBundle(BUNDLE_BASENAME, locale);
        m_support.firePropertyChange("Language was changed", m_previousBundle, m_currentBundle);
    }

    public static void addPropertyChangeListener(PropertyChangeListener pcl) {
        m_support.addPropertyChangeListener(pcl);
    }

    public static void removePropertyChangeListener(PropertyChangeListener pcl) {
        m_support.removePropertyChangeListener(pcl);
    }

    private static String setLanguageFolder()
    {
        return System.getProperty("user.dir") + File.separator + "Resources" + File.separator + "Localization";
    }
}
