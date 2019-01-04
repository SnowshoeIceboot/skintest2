package net.minecraft.skintest;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JApplet;

public class ModelPreviewApplet extends JApplet {
    private static final long serialVersionUID = 1L;
    ModelPreview modelPreview;

    public void init() {
        String url = getParameter("url");
        modelPreview = new ModelPreview(2, url);

        setLayout(new BorderLayout());
        add((Component) modelPreview, "Center");
    }

    public void start() {
        modelPreview.start();
    }

    public void stop() {
        modelPreview.stop();
    }
}
