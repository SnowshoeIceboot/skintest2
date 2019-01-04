package net.minecraft.skintest.math;

import net.minecraft.skintest.gfx.Bitmap;
import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.Polygon;

/** Simple model renderer. */
public class Model {
    /** Vertex positions. */
    private Vertex[] vertices;

    /** Quads. */
    private Polygon[] polygons;

    /** Texture X coordinate. */
    private int xTexOffs;

    /** Texture Y coordinate. */
    private int yTexOffs;

    /** X coordinate. */
    public double x;

    /** Y coordinate. */
    public double y;

    /** Z coordinate. */
    public double z;

    /** X rotation (Roll). */
    public double xRot;

    /** Y rotation (Pitch). */
    public double yRot;

    /** Z rotation (Yaw). */
    public double zRot;

    /** Flips texture horizontally. */
    public boolean mirror = false;

    /** Inverts quads. */
    public boolean invert = false;

    /**
     * Creates a model with the given texture coordinates.
     * 
     * @param xTexOffs X coordinate
     * @param yTexOffs Y coordinate
     */
    public Model(int xTexOffs, int yTexOffs) {
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }

    /**
     * Sets the texture coordinates for this model.
     * 
     * @param xTexOffs X coordinate
     * @param yTexOffs Y coordinate
     */
    public void setTexOffs(int xTexOffs, int yTexOffs) {
        this.xTexOffs = xTexOffs;
        this.yTexOffs = yTexOffs;
    }

    /**
     * Adds a box to this model.
     * 
     * @param x0 X offset
     * @param y0 Y offset
     * @param z0 Z offset
     * @param w Width
     * @param h Height
     * @param d Depth
     * @param g Scale
     */
    public void addBox(double x0, double y0, double z0, int w, int h, int d, float g) {
        // Boxes have 8 vertices.
        vertices = new Vertex[8];
        // and 6 sides.
        polygons = new Polygon[6];

        double x1 = x0 + w;
        double y1 = y0 + h;
        double z1 = z0 + d;

        // Scale all sides.
        x0 -= g;
        y0 -= g;
        z0 -= g;
        x1 += g;
        y1 += g;
        z1 += g;

        if (mirror) {
            // X flip all sides.
            double tmp = x1;
            x1 = x0;
            x0 = tmp;
        }

        // Create front top left vertex.
        Vertex u0 = new Vertex(x0 - g, y0 - g, z0 - g, 0.0D, 0.0D);
        // Create front top right vertex.
        Vertex u1 = new Vertex(x1 + g, y0 - g, z0 - g, 0.0D, 8.0D);
        // Create front bottom right vertex.
        Vertex u2 = new Vertex(x1 + g, y1 + g, z0 - g, 8.0D, 8.0D);
        // Create front bottom left vertex.
        Vertex u3 = new Vertex(x0 - g, y1 + g, z0 - g, 8.0D, 0.0D);

        // Create back top right vertex.
        Vertex l0 = new Vertex(x0 - g, y0 - g, z1 + g, 0.0D, 0.0D);
        // Create back top left vertex.
        Vertex l1 = new Vertex(x1 + g, y0 - g, z1 + g, 0.0D, 8.0D);
        // Create back bottom left vertex.
        Vertex l2 = new Vertex(x1 + g, y1 + g, z1 + g, 8.0D, 8.0D);
        // Create back bottom right vertex.
        Vertex l3 = new Vertex(x0 - g, y1 + g, z1 + g, 8.0D, 0.0D);

        vertices[0] = u0;
        vertices[1] = u1;
        vertices[2] = u2;
        vertices[3] = u3;
        vertices[4] = l0;
        vertices[5] = l1;
        vertices[6] = l2;
        vertices[7] = l3;

        // Create left quad.
        polygons[0] = new Polygon(new Vertex[] { l1, u1, u2, l2 }, xTexOffs + d + w, yTexOffs + d, xTexOffs + d + w + d, yTexOffs + d + h);
        // Create right quad.
        polygons[1] = new Polygon(new Vertex[] { u0, l0, l3, u3 }, xTexOffs, yTexOffs + d, xTexOffs + d, yTexOffs + d + h);
        // Create top quad.
        polygons[2] = new Polygon(new Vertex[] { l1, l0, u0, u1 }, xTexOffs + d, yTexOffs, xTexOffs + d + w, yTexOffs + d);
        // Create bottom quad.
        polygons[3] = new Polygon(new Vertex[] { u2, u3, l3, l2 }, xTexOffs + d + w, yTexOffs + d, xTexOffs + d + w + w, yTexOffs);
        // polygons[3] = new Polygon(new Vertex[]{u2, u3, l3, l2}, xTexOffs + d + w, yTexOffs, xTexOffs + d + w + w, yTexOffs + d);
        // Create front quad.
        polygons[4] = new Polygon(new Vertex[] { u1, u0, u3, u2 }, xTexOffs + d, yTexOffs + d, xTexOffs + d + w, yTexOffs + d + h);
        // Create back quad.
        polygons[5] = new Polygon(new Vertex[] { l0, l1, l2, l3 }, xTexOffs + d + w + d, yTexOffs + d, xTexOffs + d + w + d + w, yTexOffs + d + h);

        if ((mirror ^ invert)) {
            // Invert all polygons.
            for (int i = 0; i < polygons.length; i++) {
                polygons[i].mirror();
            }
        }
    }

    /**
     * Renders this model with the given texture.
     * 
     * @param m 3D space
     * @param texture The skin
     * @param bitmap 2D space
     */
    public void render(Matrix3 m, Bitmap texture, Bitmap3d bitmap) {
        m = m.clone();
        if ((x != 0.0D) || (y != 0.0D) || (z != 0.0D)) {
            m = m.translate(x, y, z);
        }
        if (zRot != 0.0D) {
            m = m.rotZ(zRot);
        }
        if (yRot != 0.0D) {
            m = m.rotY(yRot);
        }
        if (xRot != 0.0D) {
            m = m.rotX(xRot);
        }

        for (int i = 0; i < vertices.length; i++) {
            vertices[i].transform(m);
        }
        for (int i = 0; i < polygons.length; i++) {
            polygons[i].clipZ(1.0D);
            polygons[i].project();
            bitmap.render(polygons[i], texture, Bitmap3d.Mode.straight, -1);
        }
    }

    /**
     * Sets the position of this model.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
