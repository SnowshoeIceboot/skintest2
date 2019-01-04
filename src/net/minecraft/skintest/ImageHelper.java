package net.minecraft.skintest;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

public class ImageHelper {
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage)
            return (BufferedImage) image;

        image = new ImageIcon(image).getImage();
        boolean hasAlpha = ImageHelper.hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = 1;
            if (hasAlpha) {
                transparency = 2;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException transparency) {
            // empty catch block
        }

        if (bimage == null) {
            int type = 1;
            if (hasAlpha) {
                type = 2;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        Graphics2D g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException var2_3) {
            // empty catch block
        }

        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    public static Image toImage(BufferedImage bufferedImage) {
        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }
}
