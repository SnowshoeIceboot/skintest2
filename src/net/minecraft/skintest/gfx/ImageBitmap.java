package net.minecraft.skintest.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageBitmap extends Bitmap3d {
    private BufferedImage image;
    private static Map<String, Bitmap> loadedBitmaps = new HashMap<String, Bitmap>();

    public ImageBitmap(int width, int height) {
        super(width, height);
        image = new BufferedImage(width, height, 1);
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
    }

    public BufferedImage getImage() {
        return image;
    }

    public static Bitmap load(String name) {
        Bitmap bm = (Bitmap) loadedBitmaps.get(name);
        if (bm != null)
            return bm;

        try {
            BufferedImage img = ImageIO.read(ImageBitmap.class.getResource(name));
            Bitmap bitmap = new Bitmap(img.getWidth(), img.getHeight());
            int[] pixels = new int[img.getWidth() * img.getHeight()];
            bitmap.setPixels(img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth()));
            loadedBitmaps.put(name, bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap load(BufferedImage img) {
        Bitmap bitmap = new Bitmap(img.getWidth(), img.getHeight());
        int[] pixels = new int[img.getWidth() * img.getHeight()];
        bitmap.setPixels(img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth()));
        return bitmap;
    }
}
