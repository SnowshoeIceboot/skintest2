package net.minecraft.skintest.gfx;

public class Bitmap {
    public final int width;
    public final int height;
    public int[] pixels;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    protected void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void draw(Bitmap bitmap, int xPos, int yPos) {
        for (int y = 0; y < bitmap.height; y++) {
            int yPix = y + yPos;
            if (yPix < 0 || yPix >= height)
                continue;

            for (int x = 0; x < bitmap.width; x++) {
                int xPix = x + xPos;
                if (xPix < 0 || xPix >= width)
                    continue;

                int src = bitmap.pixels[x + y * bitmap.width];
                pixels[xPix + yPix * width] = src;
            }
        }
    }

    public void fill(int x0, int y0, int x1, int y1, int color) {
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                pixels[x + y * width] = color;
            }
        }
    }
}
