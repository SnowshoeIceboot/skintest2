package net.minecraft.skintest;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.ImageBitmap;
import net.minecraft.skintest.math.Matrix3;
import net.minecraft.skintest.math.Zombie;

public class ModelPreview extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    static final int WIDTH = 160;
    static final int HEIGHT = 160;
    int scale;
    private Thread thread;
    private volatile boolean running = false;
    ImageBitmap screenBitmap;
    Zombie zombie;
    boolean paused = false;
    private int xOld;
    private int yOld;
    long lastTime = System.nanoTime();
    float time = 0.0F;
    float yRot = 0.0F;
    float xRot = 0.0F;
    int color = 0xA0B0E0;

    /** Creates the canvas. */
    public ModelPreview(int scale, String url) {
        // Set the size of the canvas.
        this.scale = scale;
        Dimension size = new Dimension(WIDTH * scale, HEIGHT * scale);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);

        // Set up rendering stuff.
        screenBitmap = new ImageBitmap(WIDTH, HEIGHT);
        zombie = new Zombie(this);

        // Handle mouse input.
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                // Toggle pause state when clicked.
                paused = !paused;
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                xOld = e.getX();
                yOld = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                // Move the camera when the mouse is dragged.
                yRot -= (e.getX() - xOld) / 80.0f;
                xRot += (e.getY() - yOld) / 80.0f;
                xOld = e.getX();
                yOld = e.getY();

                float max = 1.5707964f;
                if (xRot < -max) {
                    xRot = -max;
                }
                if (xRot > max) {
                    xRot = max;
                }

                // System.out.println("xRot: " + String.valueOf(xRot) + " yRot: " + String.valueOf(yRot));
            }

            public void mouseMoved(MouseEvent e) {
            }
        });

        // If we have a url already (applet?), try to load it as a skin.
        if (url != null) {
            zombie.loadTexture(url, false);
        }
    }

    public void render(Bitmap3d bitmap) {
        // Measure elapsed time for animation.
        long now = System.nanoTime();
        if (!paused) {
            time -= (now - lastTime) / 1.0E9f;
        }
        lastTime = now;

        // Clear any depth information from the previous frame.
        bitmap.clearZBuffer();

        // Add a background to cover up the previous frame.
        bitmap.fill(0, 0, bitmap.width, bitmap.height, color);

        // Add the 3D matrix.
        Matrix3 m = new Matrix3().translate(0.0, 0.0, 30.0).rotX(xRot).rotY(yRot);

        zombie.render(m, bitmap, time);
    }

    public void paint(Graphics g) {
        // If no frames have been drawn on top, we use this to show an error message.
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH * scale, HEIGHT * scale);

        FontMetrics fm = getFontMetrics(g.getFont());
        String error = "Preview broken";
        g.setColor(Color.red);
        g.drawString(error, WIDTH * scale / 2 - fm.stringWidth(error) / 2, HEIGHT * scale / 2 + fm.getHeight() / 2);
    }

    public void update(Graphics g) {
    }

    public synchronized void start() {
        // Start the ModelPreview thread.
        if (thread == null) {
            thread = new Thread(this);
            running = true;
            thread.start();
        }
    }

    public synchronized void stop() {
        // Stop the ModelPreview thread.
        if (thread != null) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread = null;
        }
    }

    public void run() {
        // ModelPreview thread loop.
        while (running) {
            render(screenBitmap);

            // Draw the last rendered frame onto the canvas.
            Graphics g = getGraphics();
            g.drawImage(screenBitmap.getImage(), 0, 0, WIDTH * scale, HEIGHT * scale, 0, 0, WIDTH, HEIGHT, null);
            g.dispose();

            // Idle the thread to reduce CPU usage.
            try {
                Thread.sleep(5);
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ProgramWindow();
    }
}
