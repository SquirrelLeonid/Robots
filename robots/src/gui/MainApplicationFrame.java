package gui;

import java.awt.*;
import log.Logger;
import javax.swing.*;
import log.ExceptionLogger;
import service.localization.Localizer;
import service.constants.Locales;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.ResourceBundle;

class MainApplicationFrame extends StorableJFrame implements PropertyChangeListener
{
    private final JDesktopPane m_desktopPane = new JDesktopPane();
    private final PropertiesKeeperSingleton m_keeper;

    MainApplicationFrame() {
        initMainApplicationFrame();
        m_keeper = PropertiesKeeperSingleton.getInstance();
        m_keeper.register(this.getTitle(), this);
        initInternalFrames();
        m_keeper.loadProperties();
        Localizer.addPropertyChangeListener(this);
    }

    private void initMainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge of the screen.
        setTitle("MainApplicationFrame");
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(m_desktopPane);

        setJMenuBar(generateMenuBar());
        setMinimumSize(new Dimension(640, 480));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosed((JFrame) e.getSource());
            }
        });
    }

    private void initInternalFrames() {
        //Robot is creating here to save encapsulation in future
        model.Robot robot = new model.Robot();
        GameWindow gameWindow = new GameWindow(m_keeper, robot);
        RobotWindow robotWindow = new RobotWindow(m_keeper, robot);
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), m_keeper);
        addWindow(logWindow);
        addWindow(gameWindow);
        addWindow(robotWindow);
    }

    private void addWindow(JInternalFrame frame) {
        m_desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu testMenu = generateTestMenu();
        JMenu lookAndFeelMenu = generateLookAndFeelMenu();
        JMenu languageMenu = generateLanguageMenu();
        JMenu closeWindowMenu = generateCloseWindowMenu();

        menuBar.add(testMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(languageMenu);
        menuBar.add(closeWindowMenu);
        return menuBar;
    }

    private JMenu generateCloseWindowMenu() {
        JMenu closeWindowMenu = new JMenu("Закрыть приложение");
        closeWindowMenu.setName("closeWindow_Menu");
        JMenuItem closeApplication = new JMenuItem("Закрыть");
        closeApplication.setName("closeWindow_close_MenuItem");
        closeApplication.addActionListener((event) -> {
            onWindowClosed(this);
            this.invalidate();
        });

        closeWindowMenu.add(closeApplication);
        return closeWindowMenu;
    }

    private JMenu generateLanguageMenu() {
        JMenu languageMenu = new JMenu("Сменить язык");
        languageMenu.setName("changeLanguage_Menu");

        JMenuItem itemRussian = new JMenuItem("Русский");
        JMenuItem itemEnglish = new JMenuItem("English");
        JMenuItem itemDeutsch = new JMenuItem("Deutsch");

        itemRussian.addActionListener((event) -> {
            Localizer.updateLocalize(new Locale(Locales.RUSSIAN));
        });

        itemEnglish.addActionListener((event) -> {
            Localizer.updateLocalize(Locale.ENGLISH);
        });

        itemDeutsch.addActionListener((event) -> {
            Localizer.updateLocalize(Locale.GERMAN);
        });

        languageMenu.add(itemRussian);
        languageMenu.add(itemEnglish);
        languageMenu.add(itemDeutsch);
        return languageMenu;
    }

    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.setName("displayMode_Menu");
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.setName("displayMode_systemScheme_MenuItem");
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.setName("displayMode_universalScheme_MenuItem");
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setName("tests_Menu");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.setName("tests_addMessage_MenuItem");
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            ExceptionLogger.writeException(e.getStackTrace(), "Something got wrong");
        }
    }

    private void onWindowClosed(JFrame frame) {
        ResourceBundle bundle = Localizer.getCurrentBundle();
        Object[] options = new Object[]{bundle.getString("yes_Option"), bundle.getString("no_Option")};
        int userAnswer = JOptionPane.showOptionDialog(
                frame,
                bundle.getString("closeWindow_Dialog_Message"),
                bundle.getString("closeWindow_Menu"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (userAnswer == 0) {
            m_keeper.saveProperties();
            m_keeper.unregister(this.getTitle());
            Localizer.removePropertyChangeListener(this);
            frame.dispose();
            System.exit(0);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        ResourceBundle bundle = (ResourceBundle)propertyChangeEvent.getNewValue();
        this.setLocale(bundle.getLocale());
        JMenuBar menuBar = this.getJMenuBar();
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            updateMenuLanguage(menu, bundle);
        }
    }

    private void updateMenuLanguage(JMenu menu, ResourceBundle bundle) {
        String menuName = menu.getName();
        menu.setText(bundle.getString(menuName));
        if (menuName.compareTo("changeLanguage_Menu") == 0)
            return;
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            String itemName = item.getName();
            String value = bundle.getString(itemName);
            item.setText(value);
        }
    }
}