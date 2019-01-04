package net.minecraft.skintest.math;

/** 3D vector class. */
public class Vec3 {
    /** X coordinate. */
    public double x;

    /** Y coordinate. */
    public double y;

    /** Z coordinate. */
    public double z;

    /**
     * Creates a new vector with the given coordinates.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Interpolates this vector towards the given target vector, using linear interpolation.
     * 
     * @param t Target vector
     * @param p Interpolation factor (should be in the range of 0..1)
     * @return Result as a new vector
     */
    public Vec3 interpolateTo(Vec3 t, double p) {
        double xt = x + (t.x - x) * p;
        double yt = y + (t.y - y) * p;
        double zt = z + (t.z - z) * p;

        return new Vec3(xt, yt, zt);
    }

    /**
     * Overrides the coordinates of this vector.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
