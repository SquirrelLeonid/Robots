package gui;

import java.awt.*;
import log.Logger;
import javax.swing.*;
import log.ExceptionLogger;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

class MainApplicationFrame extends StorableJFrame {
    private final JDesktopPane m_desktopPane = new JDesktopPane();
    private final PropertiesKeeperSingleton m_keeper;

    MainApplicationFrame() {
        initMainApplicationFrame();
        m_keeper = PropertiesKeeperSingleton.getInstance();
        m_keeper.register(this.getTitle(), this);
        initInternalFrames();
        m_keeper.loadProperties();
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
        JMenu closeWindowMenu = generateCloseWindowMenu();

        menuBar.add(testMenu);
        menuBar.add(lookAndFeelMenu);
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
            ExceptionLogger.writeException(e.getStackTrace(), "Something got wrong");
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
            m_keeper.saveProperties();
            m_keeper.unregister(this.getTitle());
            frame.dispose();
            System.exit(0);
        }
    }
}