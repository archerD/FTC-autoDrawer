package animationtest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.File;

public class FTCauto extends JFrame {

    public FTCauto(boolean developing) {
        add(new MainGraphicsPanel(developing));

    }


    public static class MainGraphicsPanel extends JPanel {


        static final int FIELD_X_OFFSET = 0;
        static final int FIELD_Y_OFFSET = 0;
        //Picture stuff  !!You have to use png so you can have transparency
        private static final Image field = new ImageIcon(ClassLoader.getSystemResource("Field.png")).getImage();
        private static final Image autoDrawer = new ImageIcon(ClassLoader.getSystemResource("autoDrawer.png")).getImage();
        private static final double FIELD_HEIGHT_IN_INCHES = 144;
        final Color background = new Color(200, 200, 200);
        final Color sidePanelDark = new Color(0, 0, 0, 50);
        final Color sidePanel = new Color(0, 90, 33);
        final Color sidePanelLight = new Color(200, 200, 200);
        final Color warningRed = new Color(250, 0, 0);
        private final boolean developing;
        //Variables
        public int mouseX = 0;
        public int mouseY = 0;
        public boolean mouseOver = true;
        //This file stores the currently opened file
        public File file;
        protected boolean showRobot = false;
        PointArray points = new PointArray();
        double fieldSize = 10;
        ToolMouseListener tool;
        private int openingTrans = 255;
        private int openingTextTrans = 255;
        private int frames = 0;
        private double inchesToPixels = FIELD_HEIGHT_IN_INCHES / fieldSize;

        public MainGraphicsPanel(boolean developing) {


            //Getting mouse location when moved
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();

                }
            });

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    mouseOver = true;
                }
            });

            addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent e) {
                    mouseOver = false;
                }
            });

            //The animation timer
            Timer timer = new Timer(10, new TimerListener());
            timer.start();
            
            tool = new ToolMouseListener(this, developing);
            addMouseListener(tool);

            this.developing = developing;
        }

        public double getInchesToPixels() {
            return inchesToPixels;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            //the ratio of inches to pixels in the field
            inchesToPixels = FIELD_HEIGHT_IN_INCHES / fieldSize;

            Graphics2D g2 = (Graphics2D) g;
            
            //Update the dragged point
            if (tool.pointDragging != -1) {
                if (mouseX > FIELD_X_OFFSET + fieldSize) {
                    points.get(tool.pointDragging).setX((int) (FIELD_X_OFFSET + fieldSize), inchesToPixels);
                } else if (mouseX < FIELD_X_OFFSET) {
                    points.get(tool.pointDragging).setX(FIELD_X_OFFSET, inchesToPixels);
                } else {
                    points.get(tool.pointDragging).setX(mouseX, inchesToPixels);
                }

                if (mouseY > FIELD_Y_OFFSET + fieldSize) {
                    points.get(tool.pointDragging).setY((int) (FIELD_Y_OFFSET + fieldSize), inchesToPixels);
                } else if (mouseY < FIELD_Y_OFFSET) {
                    points.get(tool.pointDragging).setY(FIELD_Y_OFFSET, inchesToPixels);
                } else {
                    points.get(tool.pointDragging).setY(mouseY, inchesToPixels);
                }

            }


            //Main frame
            g.setColor(background);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(sidePanel);
            g.fillRect(0, 0, FIELD_X_OFFSET, getHeight());


            g.setColor(sidePanelLight);
            g.fillRect(0, 0, FIELD_X_OFFSET, FIELD_Y_OFFSET);


            //Resizing the field
            if (getWidth() - FIELD_X_OFFSET < getHeight() - FIELD_Y_OFFSET) {
                fieldSize = getWidth() - FIELD_X_OFFSET;
            } else {
                fieldSize = getHeight() - FIELD_Y_OFFSET;
            }

            g.drawImage(field, FIELD_X_OFFSET, FIELD_Y_OFFSET, (int) fieldSize, (int) fieldSize, null);


            //Menu Shadow
            g.setColor(sidePanelDark);
            //g.fillRect(90, 0, 10, getHeight());

            int eighteenInchesInPixels = (int) (18 / inchesToPixels);
            g.setColor(new Color(0, 0, 0, 50));
            if (Math.abs((mouseX) - (FIELD_X_OFFSET + (fieldSize / 2))) > (fieldSize - eighteenInchesInPixels) / 2 || Math.abs((mouseY) - (FIELD_Y_OFFSET + (fieldSize / 2))) > (fieldSize - eighteenInchesInPixels) / 2) {
                g.setColor(new Color(255, 0, 0, (int) ((Math.sin((double) frames / 10) * 110) + 120)));
            }


            //draw a robot outline
            if (showRobot) {
                g.drawRect(mouseX - eighteenInchesInPixels / 2, mouseY - eighteenInchesInPixels / 2, eighteenInchesInPixels, eighteenInchesInPixels);
                double sqrtTwo = Math.sqrt(2);
                g.drawOval((int) (mouseX - eighteenInchesInPixels*sqrtTwo / 2), (int) (mouseY - eighteenInchesInPixels*sqrtTwo / 2), (int) (eighteenInchesInPixels*sqrtTwo), (int) (eighteenInchesInPixels*sqrtTwo));
            }

            //drawing the lines
            for (int i = 1; i < points.size(); i++) {
                g2.setColor(new Color(0, 200, 50, 250));
                g2.setStroke(new BasicStroke(5));
                g2.draw(new Line2D.Float((float) points.get(i).getX(inchesToPixels) + FIELD_X_OFFSET, (float) points.get(i).getY(inchesToPixels) + FIELD_Y_OFFSET,
                        (float) points.get(i - 1).getX(inchesToPixels) + FIELD_X_OFFSET, (float) points.get(i - 1).getY(inchesToPixels) + FIELD_Y_OFFSET));
            }

            if (points.size() > 0) { //dealing with the first point
                //Making it into a more usable form
                int size = points.get(0).size;

                Color point = new Color(0, 200, 50, points.get(0).transparency);

                //set color
                g.setColor(point);

                //Changing the size
                if (size > 10) {
                    points.get(0).size -= points.get(0).sizeSpeed;

                    points.get(0).sizeSpeed += 0.5;
                }
                //And the transparency
                if (points.get(0).transparency < 250) {
                    points.get(0).transparency += 2;
                }

                g.setColor(new Color(10, 127, 25, points.get(0).transparency));

                int transMax = 250;
                //if the point is selected, then set color to be blue, if not green.
                if (points.selectedPoint == 0) {
                    point = new Color(0, FIELD_Y_OFFSET, 150, points.get(0).transparency);
                    g.setColor(point);
                    transMax = 150;
                }

                //Draw the point
                g.fillOval((int) points.get(0).getX(inchesToPixels) + FIELD_X_OFFSET - (size / 2), (int) points.get(0).getY(inchesToPixels) + FIELD_Y_OFFSET - (size / 2),
                        size, size);

                if (!points.get(0).extraCode.equals("")) {
                    g.setColor(new Color(0, 0, 0));
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new Line2D.Float((float) points.get(0).getX(inchesToPixels) + FIELD_X_OFFSET, (float) points.get(0).getY(inchesToPixels) + FIELD_Y_OFFSET,
                            (float) points.get(0).getX(inchesToPixels) + 70, (float) points.get(0).getY(inchesToPixels) - 10));

                    String outCode = "";

                    if (points.get(0).extraCode.length() > 15) {
                        for (int j = 0; j < 15; j++) {
                            outCode += points.get(0).extraCode.charAt(j);

                        }
                        outCode += "...";
                    } else {
                        outCode = points.get(0).extraCode;
                    }

                    g.setColor(new Color(0, 0, 0, 50));
                    g.fillRect((int) points.get(0).getX(inchesToPixels) + 63, (int) points.get(0).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);
                    g.drawRect((int) points.get(0).getX(inchesToPixels) + 63, (int) points.get(0).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);

                    g.setColor(new Color(0, 0, 0));
                    g.drawString(outCode, (int) points.get(0).getX(inchesToPixels) + 65, (int) points.get(0).getY(inchesToPixels) - 15);
                }


                //Changing the size
                if (size > 10) {
                    points.get(0).size -= points.get(0).sizeSpeed;
                    
                    points.get(points.size() - 1).sizeSpeed += 0.1;
                }

                //And the transparency
                if (points.get(0).transparency < transMax) {
                    points.get(0).transparency += 2;
                }
            }

            //Draw the middle points
            for (int i = 1; i < points.size() - 1; i++) {
                //Making it into a more usable form
                int size = points.get(i).size;

                Color point = new Color(0, 200, 50, points.get(i).transparency);

                //set color
                g.setColor(point);

                //Draw the point
                g.fillOval((int) points.get(i).getX(inchesToPixels) + FIELD_X_OFFSET - (size / 2), (int) points.get(i).getY(inchesToPixels) + FIELD_Y_OFFSET - (size / 2),
                        size, size);

                //Changing the size
                if (size > 10) {
                    points.get(i).size -= points.get(i).sizeSpeed;

                    points.get(i).sizeSpeed += 0.5;
                }
                //And the transparency
                if (points.get(i).transparency < 250) {
                    points.get(i).transparency += 2;
                }

                int transMax = 250;
                //if the point is selected, then set color to be blue, if not green.
                if (points.selectedPoint == i) {
                    point = new Color(0, FIELD_Y_OFFSET, 150, points.get(i).transparency);
                    g.setColor(point);
                    transMax = 150;
                }

                //Draw the point
                g.fillOval((int) points.get(i).getX(inchesToPixels) + FIELD_X_OFFSET - (size / 2), (int) points.get(i).getY(inchesToPixels) + FIELD_Y_OFFSET - (size / 2),
                        size, size);

                //Draw the angle
                if (i != 0 && i != points.size() - 1) {
                    g.setColor(new Color(0, 0, 0, points.get(i).transparency));
                    String angle = String.valueOf((int) (points.getAngle(i)+0.5));
                    g.drawString(angle,
                            (int) (points.get(i).getX(inchesToPixels) + FIELD_X_OFFSET), (int) points.get(i).getY(inchesToPixels) + FIELD_Y_OFFSET);

                }

                //Draw the distance between points
                g.setColor(new Color(0, 0, 0, points.get(i).transparency));
                String distance = String.valueOf((int) (points.getDistance(i-1)+0.5));
                g.drawString(distance,
                        (int) ((points.get(i).getX(inchesToPixels)+points.get(i-1).getX(inchesToPixels))/2 + FIELD_X_OFFSET),
                        (int) ((points.get(i).getY(inchesToPixels)+points.get(i-1).getY(inchesToPixels))/2 + FIELD_Y_OFFSET));

                if (!points.get(i).extraCode.equals("")) {
                    g.setColor(new Color(0, 0, 0));
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new Line2D.Float((float) points.get(i).getX(inchesToPixels) + FIELD_X_OFFSET, (float) points.get(i).getY(inchesToPixels) + FIELD_Y_OFFSET,
                            (float) points.get(i).getX(inchesToPixels) + 70, (float) points.get(i).getY(inchesToPixels) - 10));

                    String outCode = "";

                    if (points.get(i).extraCode.length() > 15) {
                        for (int j = 0; j < 15; j++) {
                            outCode += points.get(i).extraCode.charAt(j);

                        }
                        outCode += "...";
                    } else {
                        outCode = points.get(i).extraCode;
                    }

                    g.setColor(new Color(0, 0, 0, 50));
                    g.fillRect((int) points.get(i).getX(inchesToPixels) + 63, (int) points.get(i).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);
                    g.drawRect((int) points.get(i).getX(inchesToPixels) + 63, (int) points.get(i).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);

                    g.setColor(new Color(0, 0, 0));
                    g.drawString(outCode, (int) points.get(i).getX(inchesToPixels) + 65, (int) points.get(i).getY(inchesToPixels) - 15);
                }


                //Changing the size
                if (size > 10) {
                    points.get(i).size -= points.get(i).sizeSpeed;

                    points.get(i).sizeSpeed += 0.1;
                }

                //And the transparency
                if (points.get(i).transparency < transMax) {
                    points.get(i).transparency += 2;
                }
            }

            if (points.size() > 1) { //Dealing with the last point
                //Making it into a more usable form
                int size = points.get(points.size() - 1).size;

                Color point = new Color(0, 200, 50, points.get(points.size() - 1).transparency);

                //set color
                g.setColor(point);

                //Draw the point
                g.fillOval((int) points.get(points.size() - 1).getX(inchesToPixels) + FIELD_X_OFFSET - (size / 2), (int) points.get(points.size() - 1).getY(inchesToPixels) + FIELD_Y_OFFSET - (size / 2),
                        size, size);

                //Changing the size
                if (size > 10) {
                    points.get(points.size() - 1).size -= points.get(points.size() - 1).sizeSpeed;

                    points.get(points.size() - 1).sizeSpeed += 0.5;
                }

                //And the transparency
                if (points.get(points.size() - 1).transparency < 250) {
                    points.get(points.size() - 1).transparency += 2;
                }

                g.setColor(new Color(250, 0, 0, points.get(points.size() - 1).transparency));

                int transMax = 250;
                //if the point is selected, then set color to be blue, if not red.
                if (points.selectedPoint == points.size() - 1) {
                    point = new Color(0, FIELD_Y_OFFSET, 150, points.get(points.size() - 1).transparency);
                    g.setColor(point);
                    transMax = 150;
                }

                //Draw the point
                g.fillOval((int) points.get(points.size() - 1).getX(inchesToPixels) + FIELD_X_OFFSET - (size / 2), (int) points.get(points.size() - 1).getY(inchesToPixels) + FIELD_Y_OFFSET - (size / 2),
                        size, size);

                if (!points.get(points.size() - 1).extraCode.equals("")) {
                    g.setColor(new Color(0, 0, 0));
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(new Line2D.Float((float) points.get(points.size() - 1).getX(inchesToPixels) + FIELD_X_OFFSET, (float) points.get(points.size() - 1).getY(inchesToPixels) + FIELD_Y_OFFSET,
                            (float) points.get(points.size() - 1).getX(inchesToPixels) + 70, (float) points.get(points.size() - 1).getY(inchesToPixels) - 10));

                    String outCode = "";

                    if (points.get(points.size() - 1).extraCode.length() > 15) {
                        for (int j = 0; j < 15; j++) {
                            outCode += points.get(points.size() - 1).extraCode.charAt(j);

                        }
                        outCode += "...";
                    } else {
                        outCode = points.get(points.size() - 1).extraCode;
                    }

                    g.setColor(new Color(0, 0, 0, 50));
                    g.fillRect((int) points.get(points.size() - 1).getX(inchesToPixels) + 63, (int) points.get(points.size() - 1).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);
                    g.drawRect((int) points.get(points.size() - 1).getX(inchesToPixels) + 63, (int) points.get(points.size() - 1).getY(inchesToPixels) - 26, (8 * outCode.length()) + 4, 14);

                    g.setColor(new Color(0, 0, 0));
                    g.drawString(outCode, (int) points.get(points.size() - 1).getX(inchesToPixels) + 65, (int) points.get(points.size() - 1).getY(inchesToPixels) - 15);
                }


                //Changing the size
                if (size > 10) {
                    points.get(points.size() - 1).size -= points.get(points.size() - 1).sizeSpeed;

                    points.get(points.size() - 1).sizeSpeed += 0.1;
                }

                //And the transparency
                if (points.get(points.size() - 1).transparency < transMax) {
                    points.get(points.size() - 1).transparency += 2;
                }

                //Draw the distance between points
                g.setColor(new Color(0, 0, 0, points.get(points.size()-1).transparency));
                String distance = String.valueOf((int) (points.getDistance(points.size()-2)+0.5));
                g.drawString(distance,
                        (int) ((points.get(points.size()-1).getX(inchesToPixels)+points.get(points.size()-2).getX(inchesToPixels))/2 + FIELD_X_OFFSET),
                        (int) ((points.get(points.size()-1).getY(inchesToPixels)+points.get(points.size()-2).getY(inchesToPixels))/2 + FIELD_Y_OFFSET));
            }

            //Changing the tool color

            if (tool.toolType == 1) {
                Color point = new Color(0, 200, 50, (int) ((Math.sin((double) frames / 20) * 50) + 130));
                g.setColor(point);
            } else if (tool.toolType == 2) {
                Color point = new Color(200, 50, 0, (int) ((Math.sin((double) frames / 20) * 50) + 130));
                g.setColor(point);
            } else if (tool.toolType == 3) {
                Color point = new Color(50, 225, 200, (int) ((Math.sin((double) frames / 20) * 50) + 130));
                g.setColor(point);
            } else if (developing && tool.toolType == -1) {
                g.setColor(warningRed);
            }
            
            
            //Mouse stuff
            if (mouseOver) {
                g2.setStroke(new BasicStroke(2));
                g.drawLine(mouseX, 0, mouseX, getHeight());
                g.drawLine(0, mouseY, getWidth(), mouseY);

                g.drawLine(mouseX + 1, 0, mouseX + 1, getHeight());
                g.drawLine(0, mouseY + 1, getWidth(), mouseY + 1);
            }

            //Opening credits & stuff
            if (!developing) {
                if (openingTrans > 0 && frames > 100) {
                    openingTrans -= 5;
                }
                
                if (frames > 30 && openingTextTrans > 10) {
                    openingTextTrans -= 10;
                }
                
                Color OpeningBackground = new Color(255, 255, 255, openingTrans);
                g.setColor(OpeningBackground);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                if (frames < 100) {
                    g.drawImage(autoDrawer, ((getWidth() - 100) / 2) - 310, (getHeight() / 2) - 100, 629, 236, null);
                }
                
                
                OpeningBackground = new Color(10, 10, 10, openingTextTrans);
                g.setColor(OpeningBackground);
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(warningRed);
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 35));
                g.drawString("Developing!", 5, 20);
            }
            frames++;
        }


        class TimerListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }
    }

}
