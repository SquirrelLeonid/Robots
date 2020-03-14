package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        initMainApplicationFrame();

        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = new GameWindow();

        addWindow(logWindow);
        addWindow(gameWindow);

        tryRestoreProperties();
    }

    private void initMainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosed((JFrame) e.getSource());
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu lookAndFeelMenu = generateLookAndFeelMenu();
        JMenu testMenu = generateTestMenu();
        JMenu closeWindowMenu = generateCloseWindowMenu();

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(closeWindowMenu);
        return menuBar;
    }

    private JMenu generateCloseWindowMenu() {
        JMenu closeWindowMenu = new JMenu("Закрыть приложение");

        JMenuItem closeApplication = new JMenuItem("Закрыть");
        closeApplication.addActionListener((event) -> {
            onWindowClosed(this);
            this.invalidate();
        });

        closeWindowMenu.add(closeApplication);
        return closeWindowMenu;
    }

    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
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
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
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
            // just ignore
        }
    }

    private void onWindowClosed(JFrame frame) {
        Object[] options = new Object[]{"Да", "Нет"};
        int userAnswer = JOptionPane.showOptionDialog(
                frame,
                "Вы точно хотите закрыть приложение?",
                "Закрыть приложение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (userAnswer == 0) {
            saveWindowsState();
            frame.dispose();
            System.exit(0);
        }

    }

    private void saveWindowsState() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        PropertiesKeeper keeper = new PropertiesKeeper();
        for (JInternalFrame frame : frames) {
            keeper.saveProperties(frame);
        }
        keeper.serializeKeeper();
    }

    private void tryRestoreProperties() {
        PropertiesKeeper keeper = PropertiesKeeper.deserializeKeeper();
        if (keeper != null) {
            JInternalFrame[] frames = desktopPane.getAllFrames();

            for (JInternalFrame frame : frames) {
                keeper.loadProperties(frame);
            }
        }
    }

}