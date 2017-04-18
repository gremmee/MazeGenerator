package nl.gremmee.mazegenerator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Stack;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterResolution;

public class MazeGenerator extends Canvas implements Runnable {
    public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static int cols;
    public static int rows;
    public static Handler handler;
    public static int defaultCell = 50;
    private Cell current;
    private Cell[] grid;
    private Stack<Cell> stack;
    private boolean running = false;
    private int frames = 0;
    private Thread thread;

    public MazeGenerator() {
        handler = new Handler();
        cols = Math.floorDiv(WIDTH, defaultCell);
        rows = Math.floorDiv(HEIGHT, defaultCell);
        grid = new Cell[cols * rows];
        stack = new Stack<>();

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                handler.addObject(new Cell(i + (j * cols), i, j, (j == 0 && i == 0), (j == rows - 1 && i == cols - 1)));
            }
        }

        current = handler.getGameObject(0);

        new Window(WIDTH, HEIGHT, "MazeGenerator", this);
    }

    public static void main(String[] aArgs) {
        new MazeGenerator();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        frames = 0;
        while (running) {
            if (Window.getPrintMaze()) {
                PrinterJob job = PrinterJob.getPrinterJob();
                double dpi = 300.0;
                double cmPx300 = dpi / 2.54;
                Paper paper = new Paper();
                paper.setSize(21.3 * cmPx300, 29.7 * cmPx300);
                paper.setImageableArea(0, 0, 21.3 * cmPx300, 29.7 * cmPx300);
                PageFormat format = new PageFormat();
                format.setOrientation(PageFormat.LANDSCAPE);
                format.setPaper(paper);
                // Assign a new print renderer and the paper size of our choice !
                job.setPrintable(handler, format);
                if (job.printDialog()) {
                    try {
                        HashPrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
                        set.add(MediaSizeName.ISO_A4);
                        PrinterResolution pr = new PrinterResolution((int) (dpi), (int) (dpi), ResolutionSyntax.DPI);
                        set.add(pr);
                        set.add(new MediaPrintableArea(4, 4, 210 - 4, 297 - 4, MediaPrintableArea.MM));
                        job.setJobName("Maze");
                        job.print();
                    } catch (PrinterException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            if (running) {
                render();
            }
            frames++;

            if ((System.currentTimeMillis() - timer) > 1000) {
                timer += 1000;
                int cells = handler.getCells();
                System.out.println("W x H : " + WIDTH + " x " + HEIGHT + " FPS: " + frames + " : Cells " + cells + "|"
                        + cols + " " + rows + "|");
                frames = 0;
            }
        }
        stop();

    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeWalls(Cell aCurrent, Cell aNext) {
        int x = current.i - aNext.i;
        if (x == 1) {
            aCurrent.walls[3] = false;
            aNext.walls[1] = false;
        } else if (x == -1) {
            aCurrent.walls[1] = false;
            aNext.walls[3] = false;
        }
        int y = current.j - aNext.j;
        if (y == 1) {
            aCurrent.walls[0] = false;
            aNext.walls[2] = false;
        } else if (y == -1) {
            aCurrent.walls[2] = false;
            aNext.walls[0] = false;
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(0, 0, 0, 15));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);

        current.visited = true;
        current.highlight(g);
        // step 1.1
        Cell next = current.checkNeighbors();
        if (next != null) {
            next.visited = true;
            // Step 1.2

            stack.push(current);

            // step 1.3
            removeWalls(current, next);

            // step 1.4
            current = next;
        } else if (stack.size() > 0) {
            current = stack.pop();
        }

        g.dispose();
        bs.show();
    }

    private void update() {
        handler.update();
    }
}
