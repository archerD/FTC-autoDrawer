package animationtest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static animationtest.MainFrame.auto;

class MenuBars {


    public static JMenuBar menuBar = new JMenuBar();
    public static JMenu tool = new JMenu("Tool Type");
    public static JMenuItem toolAdd = new JMenuItem("Add", new ImageIcon(ClassLoader.getSystemResource("Waypoint.png")));
    public static JMenuItem toolDelete = new JMenuItem("Delete", new ImageIcon(ClassLoader.getSystemResource("Delete.png")));
    public static JMenuItem toolEdit = new JMenuItem("Edit", new ImageIcon(ClassLoader.getSystemResource("Edit.png")));

    public static JMenu file = new JMenu("File");
    public static JMenuItem newFile = new JMenuItem("New File", KeyEvent.VK_S);
    public static JMenuItem saveText = new JMenuItem("Save Text", KeyEvent.VK_S);
    public static JMenuItem saveBinary = new JMenuItem("Save Binary", KeyEvent.VK_S);
    public static JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_S);
    public static JMenuItem export = new JMenuItem("Export");
    public static JMenuItem openText = new JMenuItem("Open Text", KeyEvent.VK_S);
    public static JMenuItem openBinary = new JMenuItem("Open Binary", KeyEvent.VK_S);
    public static JMenuItem close = new JMenuItem("Close");
    public static JMenuItem undo = new JMenuItem("Undo", KeyEvent.VK_S);
    public static JMenuItem redo = new JMenuItem("Redo", KeyEvent.VK_S);

    public static JMenuItem changeExtraCode = new JMenuItem("Change Extra code", KeyEvent.VK_S);

    public static JMenu view = new JMenu("View");
    public static JMenuItem showRobot = new JMenuItem("Show Robot Outline", KeyEvent.VK_S);

    public static JMenu robot = new JMenu("Robot");
    public static JMenuItem openRobotEditor = new JMenuItem("Open Robot Editor", KeyEvent.VK_S);


    //testing methods, will be added to menu if
    //developing in MainFrame is true
    public static JMenu testing = new JMenu("Testing Methods");
    public static JMenuItem testingGetDistance = new JMenuItem("Get Distance", new ImageIcon(ClassLoader.getSystemResource("Ruler.jpg")));

    public static JMenuBar menuBars(boolean developing) {

        file.setMnemonic(KeyEvent.VK_A);
        file.getAccessibleContext().setAccessibleDescription("Test Main menu");
        menuBar.add(file);

        //newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        newFile.getAccessibleContext().setAccessibleDescription("Make a new blank file");
        file.add(newFile);

        saveText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveText.getAccessibleContext().setAccessibleDescription("Save the file in text format");
        file.add(saveText);

        saveText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveText.getAccessibleContext().setAccessibleDescription("Save the file in binary format");
        file.add(saveBinary);

        //saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
        saveAs.getAccessibleContext().setAccessibleDescription("Save the file");
        file.add(saveAs);

        export.getAccessibleContext().setAccessibleDescription("Export the file");
        file.add(export);

        file.addSeparator();

        openText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openText.getAccessibleContext().setAccessibleDescription("Open a text file");
        file.add(openText);

        openText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openText.getAccessibleContext().setAccessibleDescription("Open a binary file");
        file.add(openBinary);

        file.addSeparator();

        close.getAccessibleContext().setAccessibleDescription("Close the program");
        file.add(close);

        file.addSeparator();

        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        file.add(undo);

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.points = auto.tool.history.getPreviousVersion();
            }
        });

        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        file.add(redo);

        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.points = auto.tool.history.getNextVersion();
            }
        });

        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to overwrite the existing data?", "New File", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                System.out.println(n);

                if (n == JOptionPane.YES_OPTION) {
                    //Reset All data
                    auto.points = new PointArray();
                    auto.tool.history = new History(auto.points);
                    auto.file = null;
                }
            }
        });

        saveText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (auto.file == null) {
                        auto.file = FileChooser.fileChooser("Save", "Save", "Save a file", true);
                        Export.writeTextFile(Export.pointArrayToString(auto.points), auto.file.getAbsolutePath() + ".tAD"); // tAd = text AutoDrawer
                        auto.tool.history = new History(auto.points);
                    } else {
                        Export.writeTextFile(Export.pointArrayToString(auto.points), auto.file.getAbsolutePath() + ".tAD"); // tAd = text AutoDrawer
                        auto.tool.history = new History(auto.points);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MenuBars.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        saveBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (auto.file == null) {
                        auto.file = FileChooser.fileChooser("Save", "Save", "Save a file", true);
                        Export.writeBinaryFile(auto.file.getAbsolutePath() + ".bAD", auto.points); // bAD = binary AutoDrawer
                        auto.tool.history = new History(auto.points);
                    } else {
                        Export.writeBinaryFile(auto.file.getAbsolutePath() + ".bAD", auto.points); // bAD = binary AutoDrawer
                        auto.tool.history = new History(auto.points);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MenuBars.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.file = FileChooser.fileChooser("Save", "Save", "Save a file", true);
                //TODO: Ask User What format, then call the appropriate saving methods.
            }
        });

        openText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = FileChooser.fileChooser("Open", "Open", "Open a file", true).getAbsolutePath();
                    if (path.substring(path.lastIndexOf('.')).equals(".tAD")) {
                        auto.points = Export.readTextFile(path);
                        auto.tool.history = new History(auto.points);
                        auto.file = new File(path.substring(0, path.lastIndexOf(".")));
                    } else {
                        System.out.println("Invalid file type!");
                    }
                } catch (NullPointerException ex) {
                    Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        openBinary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = FileChooser.fileChooser("Open", "Open", "Open a file", true).getAbsolutePath();
                    if (path.substring(path.lastIndexOf('.')).equals(".bAD")) {
                        auto.points = Export.readBinaryFile(path);
                        auto.tool.history = new History(auto.points);
                        auto.file = new File(path.substring(0, path.lastIndexOf(".")));
                    } else {
                        System.out.println("Invalid file type!");
                    }
                } catch (NullPointerException ex) {
                    Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File save = FileChooser.fileChooser("Save Location", "Save", "Save file to this location", false);

                    //check for proper file type
                    if (save.getName().lastIndexOf('.') == -1) {
                        save = new File(save.getPath() + ".java");
                    } else if (!save.getName().substring(save.getName().lastIndexOf('.')).equals(".java")) {
                        JOptionPane.showMessageDialog(menuBar, "Not a .java file type!");
                        return;
                    }

                    //no accidental overwrites
                    if (save.exists()) {
                        switch (JOptionPane.showConfirmDialog(menuBar, "File specified already exists.  Continue?", "Overwrite Warning", JOptionPane.OK_CANCEL_OPTION)) {
                            case JOptionPane.OK_OPTION:
                                break;
                            default:
                                return;
                        }
                    }

                if (MainFrame.info == null) {
                    JOptionPane.showMessageDialog(menuBar, "Robot editor has not been run!  Please run the robot editor under the robot menu to continue.", "Export Warning", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                    MainFrame.info.setPointArray(auto.points);
                    MainFrame.info.setSaveLocation(auto.file);
                    MainFrame.info.setProgramName(save.getName().substring(0, save.getName().lastIndexOf('.')));

                    String linearOpMode;
                    try {
                        linearOpMode = Export.writeLinearOpMode(MainFrame.info);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(menuBar, "Error generating code: " + e1.getMessage() + ".  Make sure you have run the robot editor.\n"
                                + "If you have run the robot editor, and still get errors, please create an issue at https://github.com/archerD/FTC-autoDrawer.");
                        e1.printStackTrace();
                        return;
                    }

                    //handle the writing of the file
                    try {
                        PrintWriter writer = new PrintWriter(save);
                        writer.print(linearOpMode);
                        writer.flush();
                        writer.close();
                    } catch (FileNotFoundException exe) {
                        JOptionPane.showMessageDialog(menuBar, "Problem writing to file.");
                        exe.printStackTrace();
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(menuBar, "Error generating code: " + e1.getMessage() + ".  \nMake sure you have run the robot editor.\n"
                            + "If you have run the robot editor, and still get errors, please create an issue at \nhttps://github.com/Green-Griffins-10092/FTC-autoDrawer/issues/new");
                    e1.printStackTrace();
                    return;
                }
            }
        });

        //------------View Menu----------------

        menuBar.add(view);

        showRobot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK));
        view.add(showRobot);

        showRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.showRobot = !auto.showRobot;
            }
        });

        //------------Tool Menu----------------

        tool.setMnemonic(KeyEvent.VK_B);
        tool.getAccessibleContext().setAccessibleDescription("Tool Menu");
        menuBar.add(tool);


        toolAdd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        toolAdd.getAccessibleContext().setAccessibleDescription("Change the tool to add");
        tool.add(toolAdd);


        toolDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        toolDelete.getAccessibleContext().setAccessibleDescription("Change the tool to delete");
        tool.add(toolDelete);

        toolEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        toolEdit.getAccessibleContext().setAccessibleDescription("Change the tool to edit");
        tool.add(toolEdit);

        toolAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.tool.toolType = 1;
                //System.out.println("Add");
            }
        });

        toolDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.tool.toolType = 2;
                //System.out.println(auto.tool.toolType);
            }
        });

        toolEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.tool.toolType = 3;
                //System.out.println(auto.tool.toolType);
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //--------Selected point menu-------
        JMenu selectedPoints = new JMenu("Selected point");
        menuBar.add(selectedPoints);

        openText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openText.getAccessibleContext().setAccessibleDescription("Change the extra code");
        selectedPoints.add(changeExtraCode);

        changeExtraCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auto.points.get(auto.points.selectedPoint).extraCode = JOptionPane.showInputDialog("Put your extra code here:",
                        auto.points.get(auto.points.selectedPoint).extraCode);
                auto.tool.history.addVersion(auto.points);
            }
        });

        if (developing) {
            menuBar.add(testing);
            testingGetDistance.getAccessibleContext().setAccessibleDescription("Test the getDistance method\n" +
                    " prints the distance between the selected point and\n " +
                    "the clicked on point to the terminal");
            testing.add(testingGetDistance);

            testingGetDistance.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    auto.tool.toolType = -1;
                    //System.out.println(auto.tool.toolType);
                }
            });


        }
        //--------Robot menu-------
        robot.setMnemonic(KeyEvent.VK_A);
        robot.getAccessibleContext().setAccessibleDescription("Test Main menu");
        menuBar.add(robot);

        openRobotEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        openRobotEditor.getAccessibleContext().setAccessibleDescription("Change the extra code");
        robot.add(openRobotEditor);

        openRobotEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RobotEditorFrame.REF();
            }
        });

        return (menuBar);
    }
}
