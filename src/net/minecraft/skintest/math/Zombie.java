package net.minecraft.skintest.math;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import net.minecraft.skintest.ImageHelper;
import net.minecraft.skintest.ModelPreview;
import net.minecraft.skintest.gfx.Bitmap;
import net.minecraft.skintest.gfx.Bitmap3d;
import net.minecraft.skintest.gfx.ImageBitmap;

public class Zombie {
    /** Head. */
    public Model head = new Model(0, 0);

    /** Hat. */
    public Model hair = new Model(32, 0);

    /** Hat inverted. */
    public Model iHair = new Model(32, 0);

    /** Body. */
    public Model body = new Model(16, 16);

    /** Right arm. */
    public Model arm0 = new Model(40, 16);

    /** Left arm (Legacy). */
    public Model arm1 = new Model(40, 16);

    /** Right leg. */
    public Model leg0 = new Model(0, 16);

    /** Left leg (Legacy). */
    public Model leg1 = new Model(0, 16);

    /** Jacket. */
    public Model bodyClothes = new Model(16, 32);

    /** Jacket inverted. */
    public Model iBodyClothes = new Model(16, 32);

    /** Right arm (Slim). */
    public Model slimArm0 = new Model(40, 16);

    /** Right sleeve (Slim). */
    public Model slimArm0Clothes = new Model(40, 32);

    /** Right sleeve inverted (Slim). */
    public Model iSlimArm0Clothes = new Model(40, 32);

    /** Right sleeve. */
    public Model arm0Clothes = new Model(40, 32);

    /** Right sleeve inverted. */
    public Model iArm0Clothes = new Model(40, 32);

    /** Left arm (Slim). */
    public Model slimArm2 = new Model(32, 48);

    /** Left sleeve (Slim). */
    public Model slimArm2Clothes = new Model(48, 48);

    /** Left sleeve inverted (Slim). */
    public Model iSlimArm2Clothes = new Model(48, 48);

    /** Left arm. */
    public Model arm2 = new Model(32, 48);

    /** Left sleeve. */
    public Model arm2Clothes = new Model(48, 48);

    /** Left sleeve inverted. */
    public Model iArm2Clothes = new Model(48, 48);

    /** Right pants leg. */
    public Model leg0Clothes = new Model(0, 32);

    /** Right pants leg inverted. */
    public Model iLeg0Clothes = new Model(0, 32);

    /** Left leg. */
    public Model leg2 = new Model(16, 48);

    /** Left pants leg. */
    public Model leg2Clothes = new Model(0, 48);

    /** Left pants leg inverted. */
    public Model iLeg2Clothes = new Model(0, 48);

    /** Model texture. */
    public Bitmap texture;

    /** 2D Preview texture. */
    public BufferedImage bi;

    /** Default skin path. */
    public String defaultSkin = "/char.png";

    /** X position. */
    public double x;

    /** Y position. */
    public double y;

    /** Z position. */
    public double z;

    /** Y rotation (Pitch). */
    public double rot = 0.0;

    /** Scale factor. */
    public double size = 1.0;

    public double timeOffs = 0.0;
    public double speed = 0.800000011920929;

    /** Animation to be played. */
    public int anim = 0;

    /** Player model type. */
    public boolean slim = false;

    /** 1.8 skin. */
    public boolean isNewSkin = false;

    /** Hat region has transparency. */
    public boolean hasHair = false;

    /** Hat visibility. */
    public boolean showHair = true;

    /** Body visibility. */
    public boolean showBody = true;

    /** Right sleeve visibility. */
    public boolean showArm0 = true;

    /** Left sleeve visibility. */
    public boolean showArm2 = true;

    /** Right pants leg visibility. */
    public boolean showLeg0 = true;

    /** Left pants leg visibility. */
    public boolean showLeg2 = true;

    public Zombie(ModelPreview main) {
        // Standard skin model
        head.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.0f);

        hair.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.25f);

        iHair.invert = true;
        iHair.addBox(-4.0, -8.0, -4.0, 8, 8, 8, 0.25f);

        body.addBox(-4.0, 0.0, -2.0, 8, 12, 4, 0.0f);

        arm0.addBox(-3.0, -2.0, -2.0, 4, 12, 4, 0.0f);
        arm0.setPos(-5.0, 2.0, 0.0);

        arm1.mirror = true;
        arm1.addBox(-1.0, -2.0, -2.0, 4, 12, 4, 0.0f);
        arm1.setPos(5.0, 2.0, 0.0);

        leg0.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.0f);
        leg0.setPos(-2.0, 12.0, 0.0);

        leg1.mirror = true;
        leg1.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.0f);
        leg1.setPos(2.0, 12.0, 0.0);

        // 1.8 skin model
        bodyClothes.addBox(-4.0, 0.0, -2.0, 8, 12, 4, 0.1f);

        iBodyClothes.invert = true;
        iBodyClothes.addBox(-4.0, 0.0, -2.0, 8, 12, 4, 0.1f);

        slimArm0.addBox(-2.0, -2.0, -2.0, 3, 12, 4, 0.0f);
        slimArm0.setPos(-5.0, 2.0, 0.0);

        slimArm0Clothes.addBox(-2.0, -2.0, -2.0, 3, 12, 4, 0.1f);
        slimArm0Clothes.setPos(-5.0, 2.0, 0.0);

        iSlimArm0Clothes.invert = true;
        iSlimArm0Clothes.addBox(-2.0, -2.0, -2.0, 3, 12, 4, 0.1f);
        iSlimArm0Clothes.setPos(-5.0, 2.0, 0.0);

        arm0Clothes.addBox(-3.0, -2.0, -2.0, 4, 12, 4, 0.1f);
        arm0Clothes.setPos(-5.0, 2.0, 0.0);

        iArm0Clothes.invert = true;
        iArm0Clothes.addBox(-3.0, -2.0, -2.0, 4, 12, 4, 0.1f);
        iArm0Clothes.setPos(-5.0, 2.0, 0.0);

        slimArm2.addBox(-1.0, -2.0, -2.0, 3, 12, 4, 0.0f);
        slimArm2.setPos(5.0, 2.0, 0.0);

        slimArm2Clothes.addBox(-1.0, -2.0, -2.0, 3, 12, 4, 0.1f);
        slimArm2Clothes.setPos(5.0, 2.0, 0.0);

        iSlimArm2Clothes.invert = true;
        iSlimArm2Clothes.addBox(-1.0, -2.0, -2.0, 3, 12, 4, 0.1f);
        iSlimArm2Clothes.setPos(5.0, 2.0, 0.0);

        arm2.addBox(-1.0, -2.0, -2.0, 4, 12, 4, 0.0f);
        arm2.setPos(5.0, 2.0, 0.0);

        arm2Clothes.addBox(-1.0, -2.0, -2.0, 4, 12, 4, 0.1f);
        arm2Clothes.setPos(5.0, 2.0, 0.0);

        iArm2Clothes.invert = true;
        iArm2Clothes.addBox(-1.0, -2.0, -2.0, 4, 12, 4, 0.1f);
        iArm2Clothes.setPos(5.0, 2.0, 0.0);

        leg0Clothes.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.1f);
        leg0Clothes.setPos(-2.0, 12.0, 0.0);

        iLeg0Clothes.invert = true;
        iLeg0Clothes.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.1f);
        iLeg0Clothes.setPos(-2.0, 12.0, 0.0);

        leg2.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.0f);
        leg2.setPos(2.0, 12.0, 0.0);

        leg2Clothes.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.1f);
        leg2Clothes.setPos(2.0, 12.0, 0.0);

        iLeg2Clothes.invert = true;
        iLeg2Clothes.addBox(-2.0, 0.0, -2.0, 4, 12, 4, 0.1f);
        iLeg2Clothes.setPos(2.0, 12.0, 0.0);
    }

    public void loadTexture(String url, boolean local) {
        Image i = null;
        try {
            if (local) {
                i = Toolkit.getDefaultToolkit().createImage(new String(url));
            } else {
                i = Toolkit.getDefaultToolkit().createImage(new URL(url));
            }

            BufferedImage bi = ImageHelper.toBufferedImage(i);
            if (checkValidSkin(bi)) {
                System.out.println("Loading texture for " + url);
                texture = ImageBitmap.load(bi);
                hasHair = checkHair(texture);
                isNewSkin = checkNewSkin(texture);
                this.bi = bi;
            } else {
                System.out.println("Invalid texture size for " + url);
                loadDefaultTexture();
            }
        } catch (Exception e) {
            System.out.println("Failed to load texture for " + url);
            loadDefaultTexture();
        }
    }

    public void loadTexture(BufferedImage bi) {
        try {
            if (checkValidSkin(bi)) {
                System.out.println("Loading texture");
                texture = ImageBitmap.load(bi);
                hasHair = checkHair(texture);
                isNewSkin = checkNewSkin(texture);
                this.bi = bi;
            } else {
                System.out.println("Invalid texture size");
                loadDefaultTexture();
            }
        } catch (Exception e) {
            System.out.println("Failed to load texture");
            loadDefaultTexture();
        }
    }

    public void loadDefaultTexture() {
        texture = ImageBitmap.load(defaultSkin);
        hasHair = checkHair(texture);
        isNewSkin = checkNewSkin(texture);
        bi = null;
    }

    private boolean checkValidSkin(BufferedImage bi) {
        return (bi.getWidth() == 64 && bi.getHeight() == 32 || bi.getHeight() == 64);
    }

    private boolean checkNewSkin(Bitmap b) {
        return b.width == b.height;
    }

    private boolean checkHair(Bitmap b) {
        for (int x = 32; x < 64; x++)
            for (int y = 0; y < 16; y++) {
                int a = b.pixels[(x + y * 64)] >> 24 & 0xFF; // fourth byte
                if (a < 128) {
                    // hair region is transparent
                    return true;
                }
            }
        return false;
    }

    private Bitmap removeAlpha(Bitmap b) {
        for (int x = 0; x < 64; ++x) {
            for (int y = 0; y < 32; ++y) {
                // int blue = b.pixels[(x + y * 64)] & 0xFF; // first byte
                // int green = b.pixels[(x + y * 64)] >> 8 & 0xFF; // second byte
                // int red = b.pixels[(x + y * 64)] >> 16 & 0xFF; // third byte
                int a = b.pixels[(x + y * 64)] >> 24 & 0xFF; // fourth byte
                // String hexRGBA = (Integer.toHexString(red & 0xFF) + Integer.toHexString(green & 0xFF) + Integer.toHexString(blue & 0xFF) +
                // Integer.toHexString(a & 0xFF));

                // if (a < 128) {
                // System.out.println("Transparent pixel at: " + x + ", " + y);
                // } else {
                // System.out.println("Opaque pixel at: " + x + ", " + y + " Color: " + hexRGBA);
                // }

                // if (x >= 32 && y >= 8 && y < 16 || x >= 40 && x < 56 && y < 8 || a > 128) {
                if (a > 128)
                    continue;

                b.fill(x, y, 1, 1, 0x000000);
            }
        }
        return b;
    }

    public void render(Matrix3 m, Bitmap3d bitmap, double time) {
        if (texture == null)
            loadDefaultTexture();

        m = m.clone();

        time = time * 10.0 * speed + timeOffs;

        switch (anim) {
        case 1: {
            // Standing
            y = -7.0;

            iHair.yRot = hair.yRot = head.yRot = 0;
            iHair.xRot = hair.xRot = head.xRot = 0;

            iArm0Clothes.xRot = arm0Clothes.xRot = arm0.xRot = Math.sin(time * 0.067) * 0.05;
            iArm0Clothes.zRot = arm0Clothes.zRot = arm0.zRot = Math.cos(time * 0.09) * 0.05 + 0.05;

            iSlimArm0Clothes.xRot = slimArm0Clothes.xRot = slimArm0.xRot = arm0.xRot;
            iSlimArm0Clothes.zRot = slimArm0Clothes.zRot = slimArm0.zRot = arm0.zRot;

            iArm2Clothes.xRot = arm2Clothes.xRot = arm2.xRot = arm1.xRot = Math.sin(time * 0.067) * 0.05;
            iArm2Clothes.zRot = arm2Clothes.zRot = arm2.zRot = arm1.zRot = Math.cos(time * 0.09) * -0.05 + -0.05;

            iSlimArm2Clothes.xRot = slimArm2Clothes.xRot = slimArm2.xRot = arm1.xRot;
            iSlimArm2Clothes.zRot = slimArm2Clothes.zRot = slimArm2.zRot = arm1.zRot;

            iLeg0Clothes.xRot = leg0Clothes.xRot = leg0.xRot = 0;
            iLeg0Clothes.yRot = leg0Clothes.yRot = leg0.yRot = 0;

            iLeg2Clothes.xRot = leg2Clothes.xRot = leg2.xRot = leg1.xRot = 0;
            iLeg2Clothes.yRot = leg2Clothes.yRot = leg2.yRot = leg1.yRot = 0;

            break;
        }
        case 2: {
            // Sitting
            y = -7.0;

            iHair.yRot = hair.yRot = head.yRot = 0;
            iHair.xRot = hair.xRot = head.xRot = 0;

            iArm0Clothes.xRot = arm0Clothes.xRot = arm0.xRot = Math.sin(time * 0.067) * 0.05;
            iArm0Clothes.zRot = arm0Clothes.zRot = arm0.zRot = Math.cos(time * 0.09) * 0.05 + 0.05;

            iSlimArm0Clothes.xRot = slimArm0Clothes.xRot = slimArm0.xRot = arm0.xRot;
            iSlimArm0Clothes.zRot = slimArm0Clothes.zRot = slimArm0.zRot = arm0.zRot;

            iArm2Clothes.xRot = arm2Clothes.xRot = arm2.xRot = arm1.xRot = Math.sin(time * 0.067) * 0.05;
            iArm2Clothes.zRot = arm2Clothes.zRot = arm2.zRot = arm1.zRot = Math.cos(time * 0.09) * -0.05 + -0.05;

            iSlimArm2Clothes.xRot = slimArm2Clothes.xRot = slimArm2.xRot = arm1.xRot;
            iSlimArm2Clothes.zRot = slimArm2Clothes.zRot = slimArm2.zRot = arm1.zRot;

            iArm0Clothes.xRot = arm0Clothes.xRot = arm0.xRot += -0.63;
            iSlimArm0Clothes.xRot = slimArm0Clothes.xRot = slimArm0.xRot = arm0.xRot;

            iArm2Clothes.xRot = arm2Clothes.xRot = arm2.xRot = arm1.xRot += -0.63;
            iSlimArm2Clothes.xRot = slimArm2Clothes.xRot = slimArm2.xRot = arm1.xRot;

            iLeg0Clothes.xRot = leg0Clothes.xRot = leg0.xRot = -1.3;
            iLeg0Clothes.yRot = leg0Clothes.yRot = leg0.yRot = 0.31;

            iLeg2Clothes.xRot = leg2Clothes.xRot = leg2.xRot = leg1.xRot = -1.3;
            iLeg2Clothes.yRot = leg2Clothes.yRot = leg2.yRot = leg1.yRot = -0.31;

            break;
        }
        default: {
            // Running
            y = (-Math.abs(Math.sin(time * 0.6662))) * 5.0 - 24.0 + 20.0;

            iHair.yRot = hair.yRot = head.yRot = Math.sin(time * 0.23) * 1.0;
            iHair.xRot = hair.xRot = head.xRot = Math.sin(time * 0.1) * 0.8;

            iArm0Clothes.xRot = arm0Clothes.xRot = arm0.xRot = Math.sin(time * 0.6662 + 3.141592653589793) * 2.0;
            iArm0Clothes.zRot = arm0Clothes.zRot = arm0.zRot = (Math.sin(time * 0.2312) + 1.0) * 1.0;

            iSlimArm0Clothes.xRot = slimArm0Clothes.xRot = slimArm0.xRot = arm0.xRot;
            iSlimArm0Clothes.zRot = slimArm0Clothes.zRot = slimArm0.zRot = arm0.zRot;

            iArm2Clothes.xRot = arm2Clothes.xRot = arm2.xRot = arm1.xRot = Math.sin(time * 0.6662) * 2.0;
            iArm2Clothes.zRot = arm2Clothes.zRot = arm2.zRot = arm1.zRot = (Math.sin(time * 0.2812) - 1.0) * 1.0;

            iSlimArm2Clothes.xRot = slimArm2Clothes.xRot = slimArm2.xRot = arm1.xRot;
            iSlimArm2Clothes.zRot = slimArm2Clothes.zRot = slimArm2.zRot = arm1.zRot;

            iLeg0Clothes.xRot = leg0Clothes.xRot = leg0.xRot = Math.sin(time * 0.6662) * 1.4;
            iLeg0Clothes.yRot = leg0Clothes.yRot = leg0.yRot = 0;

            iLeg2Clothes.xRot = leg2Clothes.xRot = leg2.xRot = leg1.xRot = Math.sin(time * 0.6662 + 3.141592653589793) * 1.4;
            iLeg2Clothes.yRot = leg2Clothes.yRot = leg2.yRot = leg1.yRot = 0;

            break;
        }
        }

        m = m.translate(x, y, z).rotY(rot).scale(size, size, size);

        // Render each model.
        head.render(m, texture, bitmap);

        if (hasHair && showHair) {
            hair.render(m, texture, bitmap);
            iHair.render(m, texture, bitmap);
        }

        body.render(m, texture, bitmap);

        if (isNewSkin && slim) {
            slimArm0.render(m, texture, bitmap);
        } else {
            arm0.render(m, texture, bitmap);
        }

        if (isNewSkin && slim) {
            slimArm2.render(m, texture, bitmap);
        } else if (isNewSkin) {
            arm2.render(m, texture, bitmap);
        } else {
            arm1.render(m, texture, bitmap);
        }

        leg0.render(m, texture, bitmap);

        if (isNewSkin) {
            leg2.render(m, texture, bitmap);
        } else {
            leg1.render(m, texture, bitmap);
        }

        if (isNewSkin && hasHair) {
            if (showBody) {
                bodyClothes.render(m, texture, bitmap);
                iBodyClothes.render(m, texture, bitmap);
            }
            if (slim) {
                if (showArm0) {
                    slimArm0Clothes.render(m, texture, bitmap);
                    iSlimArm0Clothes.render(m, texture, bitmap);
                }
                if (showArm2) {
                    slimArm2Clothes.render(m, texture, bitmap);
                    iSlimArm2Clothes.render(m, texture, bitmap);
                }
            } else {
                if (showArm0) {
                    arm0Clothes.render(m, texture, bitmap);
                    iArm0Clothes.render(m, texture, bitmap);
                }
                if (showArm2) {
                    arm2Clothes.render(m, texture, bitmap);
                    iArm2Clothes.render(m, texture, bitmap);
                }
            }
            if (showLeg0) {
                leg0Clothes.render(m, texture, bitmap);
                iLeg0Clothes.render(m, texture, bitmap);
            }
            if (showLeg2) {
                leg2Clothes.render(m, texture, bitmap);
                iLeg2Clothes.render(m, texture, bitmap);
            }
        }
    }
}
