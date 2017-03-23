package nl.gremmee.mazegenerator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.Stack;

public class MazeGenerator extends Canvas implements Runnable {
    public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int NUM_STARS = WIDTH;

    public static int speed = 1;

    public static int cols;
    public static int rows;
    public static Handler handler;

    public static int w = 30;

    Cell current;

    Cell[] grid;

    Stack<Cell> stack;

    private boolean running = false;
    private int frames = 0;

    private Thread thread;

    public MazeGenerator() {
        handler = new Handler();
        cols = Math.floorDiv(WIDTH, w);
        rows = Math.floorDiv(HEIGHT, w);
        grid = new Cell[cols * rows];
        stack = new Stack<>();

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                handler.addObject(new Cell(i + (j * cols), i, j, ID.Cell));
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
                int stars = handler.getStars();
                System.out.println("W x H : " + WIDTH + " x " + HEIGHT + " FPS: " + frames + " : Cells " + stars);
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
