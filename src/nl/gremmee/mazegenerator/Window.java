package nl.gremmee.mazegenerator;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Window extends Canvas {
    private static final long serialVersionUID = 1522602361354624706L;

    public static int x;
    public static int y;
    public static boolean printMaze = false;

    public Window(int aWidth, int aHeight, String aTitle, MazeGenerator aMain) {
        JFrame frame = new JFrame(aTitle);
        aMain.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        aMain.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MouseEvent.BUTTON1 == e.getButton()) {
                    printMaze = true;
                } else if (MouseEvent.BUTTON2 == e.getButton()) {
                    // Nothing
                } else if (MouseEvent.BUTTON3 == e.getButton()) {
                    System.exit(0);
                }
            }
        });

        frame.setPreferredSize(new Dimension(aWidth, aHeight));
        frame.setMaximumSize(new Dimension(aWidth, aHeight));
        frame.setMinimumSize(new Dimension(aWidth, aHeight));

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(aMain);
        frame.setVisible(true);

        aMain.start();
    }

    public static boolean getPrintMaze() {
        return printMaze;
    }

    public static int getMouseX() {
        return x;
    }

    public static int getMouseY() {
        return y;
    }
}
