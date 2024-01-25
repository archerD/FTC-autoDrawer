package animationtest;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class MainFrame extends JFrame {

    private static final Image icon = new ImageIcon(ClassLoader.getSystemResource("Icon.png")).getImage();
    static FTCauto.MainGraphicsPanel auto;
    static InfoBar.MainGraphicsPanel infoBar;
    static ProgramInfo info = null;
    private static boolean developing = false;
    private static File file = null;
    private static JMenuBar menubar;

    public static void main(String[] args) {
        for (String s : args) {
            if (s.equals("-d") || s.equalsIgnoreCase("--developing") || s.equalsIgnoreCase("-developing") || s.equals("developing"))
                developing = true;
        }

        for (int i = 0; i < args.length; i++) {
            if ((args[i].equalsIgnoreCase("-f") || args[i].equalsIgnoreCase("--file")) && i + 1 < args.length) {
                file = new File(args[i + 1]);
            }
        }

//        try {
//            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//            } catch (Exception e) {
//                // If Nimbus is not available, fall back to cross-platform
//             try {
//                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            } catch (Exception ex) {
//
//            }
//        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        MainFrame frame = new MainFrame();
        infoBar = new InfoBar.MainGraphicsPanel(developing);
        auto = new FTCauto.MainGraphicsPanel(developing);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(infoBar, BorderLayout.LINE_START);
        frame.getContentPane().add(auto, BorderLayout.CENTER);
        frame.pack();
        frame.setTitle("AutoDrawer");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        menubar = MenuBars.menuBars(developing);
        if (file != null) {
            if (file.getPath().substring(file.getPath().lastIndexOf('.') + 1).equals("bAD")) {
                auto.points = Export.readBinaryFile(file.getPath());
                auto.tool.history = new History(auto.points);
                auto.file = new File(file.getPath().substring(0, file.getPath().lastIndexOf(".")));
            } else if (file.getPath().substring(file.getPath().lastIndexOf('.') + 1).equals("tAD")) {
                auto.points = Export.readTextFile(file.getPath());
                auto.tool.history = new History(auto.points);
                auto.file = new File(file.getPath().substring(0, file.getPath().lastIndexOf(".")));
            } else {
                System.out.println("Specified file is not a valid autoDrawer file");
            }
        }
        frame.setJMenuBar(menubar);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(icon);
        frame.setVisible(true);

        auto.tool.toolType = 1;
    }
}
