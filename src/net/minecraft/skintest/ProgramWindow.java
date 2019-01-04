package net.minecraft.skintest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProgramWindow implements ActionListener, WindowListener, MenuListener {
    private JFrame frame = new JFrame();
    private ModelPreview modelPreview = new ModelPreview(2, null);
    private String usernameRegex = "^[a-zA-Z0-9_]{2,16}$";
    private String skinServer = "http://skins.minecraft.net/MinecraftSkins/";
    private JLabel labelPreview = new JLabel();
    private JTextField txtName = new JTextField();
    private JCheckBoxMenuItem chkbxmntmPause = new JCheckBoxMenuItem("Pause");
    private JRadioButtonMenuItem rdbtnmntmClassic = new JRadioButtonMenuItem("Classic Arms");
    private JRadioButtonMenuItem rdbtnmntmSlim = new JRadioButtonMenuItem("Slim Arms");
    private JCheckBoxMenuItem chkbxmntmHair = new JCheckBoxMenuItem("Hat");
    private JCheckBoxMenuItem chkbxmntmBody = new JCheckBoxMenuItem("Jacket");
    private JCheckBoxMenuItem chkbxmntmArm2 = new JCheckBoxMenuItem("Left Sleeve");
    private JCheckBoxMenuItem chkbxmntmArm0 = new JCheckBoxMenuItem("Right Sleeve");
    private JCheckBoxMenuItem chkbxmntmLeg2 = new JCheckBoxMenuItem("Left Pants Leg");
    private JCheckBoxMenuItem chkbxmntmLeg0 = new JCheckBoxMenuItem("Right Pants Leg");
    private ClipboardHelper clipboardHelper = new ClipboardHelper();
    private String suffix = ".png";
    private JFileChooser fileChooser = new JFileChooser() {
        private static final long serialVersionUID = 1L;

        public void approveSelection() {
            File fileToBeSaved = getSelectedFile();
            if (!getSelectedFile().getAbsolutePath().endsWith(suffix)) {
                fileToBeSaved = new File(getSelectedFile() + suffix);
            }
            if (fileToBeSaved.exists() && getDialogType() == SAVE_DIALOG) {
                int result = JOptionPane.showConfirmDialog(this, fileToBeSaved.getName() + " already exists.\nDo you want to replace it?", "Confirm Save", JOptionPane.YES_NO_OPTION);
                switch (result) {
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CLOSED_OPTION:
                    return;
                }
            }
            if (!getSelectedFile().exists() && getDialogType() == OPEN_DIALOG) {
                JOptionPane.showMessageDialog(this, "File not found.\nCheck the file name and try again.", "Open", JOptionPane.WARNING_MESSAGE);
                return;
            }
            super.approveSelection();
        }
    };

    public ProgramWindow() {
        frame.setTitle("SkinTest");

        // 3D preview
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add((Component) modelPreview, "Center");

        // 2D preview
        JPanel panelPreview = new JPanel();
        panelPreview.setLayout(new BorderLayout(0, 0));
        panelPreview.add(labelPreview, BorderLayout.CENTER);
        frame.getContentPane().add(panelPreview, BorderLayout.EAST);

        // Handle drag and drop input.
        new DropHelper((Component) modelPreview, new DropHelper.Listener() {
            public void filesDropped(File[] files) {
                // Change the skin for each file dropped.
                for (int i = 0; i < files.length; i++) {
                    try {
                        setTexture(files[i].getCanonicalPath(), 1);
                        updateInterface();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        updateInterface();

        // Screenshot button
        JButton btnScreenshot = new JButton("Take screenshot");
        btnScreenshot.setActionCommand("savescreenshot");
        btnScreenshot.addActionListener(this);
        frame.getContentPane().add(btnScreenshot, BorderLayout.SOUTH);

        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        // Username toolbar
        JToolBar toolbarName = new JToolBar();
        toolbarName.setFloatable(false);
        frame.getContentPane().add(toolbarName, BorderLayout.NORTH);

        JLabel labelUsername = new JLabel("Username: ");
        toolbarName.add(labelUsername);

        txtName.setActionCommand("setname");
        txtName.addActionListener(this);
        toolbarName.add(txtName);

        setupMenus();

        frame.setResizable(false); // Disable window resizing before pack.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        modelPreview.start();
    }

    /** Creates the menu bar and menu items. */
    private void setupMenus() {
        JMenuBar menuBar = new JMenuBar();

        // Workaround to stop the 3D preview canvas from covering popup menus.
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        JMenu mnFile = new JMenu("File");
        mnFile.setMnemonic('f');
        menuBar.add(mnFile);

        JMenuItem mntmOpen = new JMenuItem("Open...");
        mntmOpen.setMnemonic('o');
        mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        mntmOpen.setActionCommand("open");
        mntmOpen.addActionListener(this);
        mnFile.add(mntmOpen);

        JMenuItem mntmOpenUrl = new JMenuItem("Open URL...");
        mntmOpenUrl.setMnemonic('u');
        mntmOpenUrl.setActionCommand("openurl");
        mntmOpenUrl.addActionListener(this);
        mnFile.add(mntmOpenUrl);

        JMenuItem mntmSave = new JMenuItem("Save As...");
        mntmSave.setMnemonic('s');
        mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        mntmSave.setActionCommand("save");
        mntmSave.addActionListener(this);
        mnFile.add(mntmSave);

        JMenuItem mntmSaveScreenshot = new JMenuItem("Save Screenshot As...");
        mntmSaveScreenshot.setMnemonic('c');
        mntmSaveScreenshot.setActionCommand("savescreenshot");
        mntmSaveScreenshot.addActionListener(this);
        mnFile.add(mntmSaveScreenshot);

        mnFile.addSeparator();

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.setMnemonic('x');
        mntmExit.setActionCommand("exit");
        mntmExit.addActionListener(this);
        mnFile.add(mntmExit);

        JMenu mnEdit = new JMenu("Edit");
        mnEdit.setMnemonic('e');
        menuBar.add(mnEdit);

        JMenuItem mntmCopy = new JMenuItem("Copy");
        mntmCopy.setMnemonic('c');
        mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        mntmCopy.setActionCommand("copy");
        mntmCopy.addActionListener(this);
        mnEdit.add(mntmCopy);

        JMenuItem mntmCopyScreenshot = new JMenuItem("Copy Screenshot");
        mntmCopyScreenshot.setMnemonic('y');
        mntmCopyScreenshot.setActionCommand("copyscreenshot");
        mntmCopyScreenshot.addActionListener(this);
        mnEdit.add(mntmCopyScreenshot);

        JMenuItem mntmPaste = new JMenuItem("Paste");
        mntmPaste.setMnemonic('p');
        mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        mntmPaste.setActionCommand("paste");
        mntmPaste.addActionListener(this);
        mnEdit.add(mntmPaste);

        mnEdit.addSeparator();

        JMenuItem mntmColor = new JMenuItem("Background Color...");
        mntmColor.setActionCommand("color");
        mntmColor.addActionListener(this);
        mnEdit.add(mntmColor);

        // JMenuItem mntmColorHex = new JMenuItem("Background Color (Hex)...");
        // mntmColorHex.setActionCommand("colorhex");
        // mntmColorHex.addActionListener(this);
        // mnEdit.add(mntmColorHex);

        JMenuItem mntmServer = new JMenuItem("Skin Server...");
        mntmServer.setToolTipText("Set a remote location to load skins using minecraft.net usernames.");
        mntmServer.setActionCommand("server");
        mntmServer.addActionListener(this);
        mnEdit.add(mntmServer);

        JMenu mnView = new JMenu("View");
        mnView.setMnemonic('v');
        mnView.addMenuListener(this);
        menuBar.add(mnView);

        JMenu mnCamera = new JMenu("Camera");
        mnView.add(mnCamera);
        mnCamera.setMnemonic('c');

        JMenuItem mntmFront = new JMenuItem("Front");
        mntmFront.setMnemonic('f');
        mntmFront.setActionCommand("front");
        mntmFront.addActionListener(this);
        mnCamera.add(mntmFront);

        JMenuItem mntmBack = new JMenuItem("Back");
        mntmBack.setMnemonic('b');
        mntmBack.setActionCommand("back");
        mntmBack.addActionListener(this);
        mnCamera.add(mntmBack);

        JMenuItem mntmLeft = new JMenuItem("Left");
        mntmLeft.setMnemonic('l');
        mntmLeft.setActionCommand("left");
        mntmLeft.addActionListener(this);
        mnCamera.add(mntmLeft);

        JMenuItem mntmRight = new JMenuItem("Right");
        mntmRight.setMnemonic('r');
        mntmRight.setActionCommand("right");
        mntmRight.addActionListener(this);
        mnCamera.add(mntmRight);

        JMenuItem mntmTop = new JMenuItem("Top");
        mntmTop.setMnemonic('t');
        mntmTop.setActionCommand("top");
        mntmTop.addActionListener(this);
        mnCamera.add(mntmTop);

        JMenuItem mntmBottom = new JMenuItem("Bottom");
        mntmBottom.setMnemonic('o');
        mntmBottom.setActionCommand("bottom");
        mntmBottom.addActionListener(this);
        mnCamera.add(mntmBottom);

        JMenu mnScale = new JMenu("Scale");
        mnView.add(mnScale);
        mnScale.setMnemonic('s');

        mnView.addSeparator();

        chkbxmntmPause.setMnemonic('p');
        chkbxmntmPause.setActionCommand("pause");
        chkbxmntmPause.addActionListener(this);
        mnView.add(chkbxmntmPause);

        ButtonGroup buttonGroupScale = new ButtonGroup();

        JRadioButtonMenuItem rdbtnmntmScale1 = new JRadioButtonMenuItem("1x");
        rdbtnmntmScale1.setMnemonic('1');
        rdbtnmntmScale1.setActionCommand("scale1");
        rdbtnmntmScale1.addActionListener(this);
        mnScale.add(rdbtnmntmScale1);
        buttonGroupScale.add(rdbtnmntmScale1);

        JRadioButtonMenuItem rdbtnmntmScale2 = new JRadioButtonMenuItem("2x");
        rdbtnmntmScale2.setMnemonic('2');
        rdbtnmntmScale2.setActionCommand("scale2");
        rdbtnmntmScale2.addActionListener(this);
        mnScale.add(rdbtnmntmScale2);
        rdbtnmntmScale2.setSelected(true);
        buttonGroupScale.add(rdbtnmntmScale2);

        JRadioButtonMenuItem rdbtnmntmScale4 = new JRadioButtonMenuItem("4x");
        rdbtnmntmScale4.setMnemonic('4');
        rdbtnmntmScale4.setActionCommand("scale4");
        rdbtnmntmScale4.addActionListener(this);
        mnScale.add(rdbtnmntmScale4);
        buttonGroupScale.add(rdbtnmntmScale4);

        JMenu mnModel = new JMenu("Model");
        mnModel.setMnemonic('d');
        menuBar.add(mnModel);

        JMenu mnLayers = new JMenu("Layers");
        mnLayers.setMnemonic('a');
        mnModel.add(mnLayers);

        chkbxmntmHair.setActionCommand("showhair");
        chkbxmntmHair.addActionListener(this);
        mnLayers.add(chkbxmntmHair);

        chkbxmntmBody.setActionCommand("showbody");
        chkbxmntmBody.addActionListener(this);
        mnLayers.add(chkbxmntmBody);

        chkbxmntmArm2.setActionCommand("showarm2");
        chkbxmntmArm2.addActionListener(this);
        mnLayers.add(chkbxmntmArm2);

        chkbxmntmArm0.setActionCommand("showarm0");
        chkbxmntmArm0.addActionListener(this);
        mnLayers.add(chkbxmntmArm0);

        chkbxmntmLeg2.setActionCommand("showleg2");
        chkbxmntmLeg2.addActionListener(this);
        mnLayers.add(chkbxmntmLeg2);

        chkbxmntmLeg0.setActionCommand("showleg0");
        chkbxmntmLeg0.addActionListener(this);
        mnLayers.add(chkbxmntmLeg0);

        mnLayers.addSeparator();

        JMenuItem mntmShowLayers = new JMenuItem("Show All");
        mntmShowLayers.setMnemonic('s');
        mntmShowLayers.setActionCommand("showlayers");
        mntmShowLayers.addActionListener(this);
        mnLayers.add(mntmShowLayers);

        JMenuItem mntmHideLayers = new JMenuItem("Hide All");
        mntmHideLayers.setMnemonic('e');
        mntmHideLayers.setActionCommand("hidelayers");
        mntmHideLayers.addActionListener(this);
        mnLayers.add(mntmHideLayers);

        mnModel.addSeparator();

        ButtonGroup buttonGroupAnim = new ButtonGroup();

        JRadioButtonMenuItem rdbtnmntmRunning = new JRadioButtonMenuItem("Running");
        rdbtnmntmRunning.setMnemonic('r');
        rdbtnmntmRunning.setActionCommand("anim0");
        rdbtnmntmRunning.addActionListener(this);
        mnModel.add(rdbtnmntmRunning);
        rdbtnmntmRunning.setSelected(true);
        buttonGroupAnim.add(rdbtnmntmRunning);

        JRadioButtonMenuItem rdbtnmntmStanding = new JRadioButtonMenuItem("Standing");
        rdbtnmntmStanding.setMnemonic('s');
        rdbtnmntmStanding.setActionCommand("anim1");
        rdbtnmntmStanding.addActionListener(this);
        mnModel.add(rdbtnmntmStanding);
        buttonGroupAnim.add(rdbtnmntmStanding);

        JRadioButtonMenuItem rdbtnmntmSitting = new JRadioButtonMenuItem("Sitting");
        rdbtnmntmSitting.setMnemonic('n');
        rdbtnmntmSitting.setActionCommand("anim2");
        rdbtnmntmSitting.addActionListener(this);
        mnModel.add(rdbtnmntmSitting);
        buttonGroupAnim.add(rdbtnmntmSitting);

        mnModel.addSeparator();

        ButtonGroup buttonGroupModel = new ButtonGroup();

        rdbtnmntmClassic.setMnemonic('c');
        rdbtnmntmClassic.setActionCommand("classic");
        rdbtnmntmClassic.addActionListener(this);
        mnModel.add(rdbtnmntmClassic);
        rdbtnmntmClassic.setSelected(true);
        buttonGroupModel.add(rdbtnmntmClassic);

        rdbtnmntmSlim.setMnemonic('m');
        rdbtnmntmSlim.setActionCommand("slim");
        rdbtnmntmSlim.addActionListener(this);
        mnModel.add(rdbtnmntmSlim);
        buttonGroupModel.add(rdbtnmntmSlim);

        frame.setJMenuBar(menuBar);
    }

    /** Updates menu items and the 2D preview. */
    private void updateInterface() {
        BufferedImage texture = getTexture();
        ImageIcon iconPreview = new ImageIcon(texture);
        int scale = modelPreview.scale;
        int iconWidth = 64 * scale;
        int iconHeight = iconWidth;

        if (!modelPreview.zombie.isNewSkin) {
            // Legacy skins are always 32 pixels tall.
            iconHeight = 32 * scale;
        }

        labelPreview.setIcon(scaleIcon(iconPreview, iconWidth, iconHeight));

        if (modelPreview.zombie.isNewSkin) {
            rdbtnmntmSlim.setEnabled(true);
            rdbtnmntmSlim.setToolTipText("3 pixel arms instead of 4. New skins may require this.");
            rdbtnmntmClassic.setEnabled(true);
            rdbtnmntmClassic.setToolTipText("Traditional 4 pixel arms compatible with most skins.");
            chkbxmntmBody.setEnabled(true);
            chkbxmntmBody.setSelected(modelPreview.zombie.showBody);
            chkbxmntmArm2.setEnabled(true);
            chkbxmntmArm2.setSelected(modelPreview.zombie.showArm2);
            chkbxmntmArm0.setEnabled(true);
            chkbxmntmArm0.setSelected(modelPreview.zombie.showArm0);
            chkbxmntmLeg2.setEnabled(true);
            chkbxmntmLeg2.setSelected(modelPreview.zombie.showLeg2);
            chkbxmntmLeg0.setEnabled(true);
            chkbxmntmLeg0.setSelected(modelPreview.zombie.showLeg0);
        } else {
            // Legacy skins don't have slim arms or extra layers.
            rdbtnmntmSlim.setEnabled(false);
            rdbtnmntmSlim.setSelected(false);
            rdbtnmntmSlim.setToolTipText(null);
            rdbtnmntmClassic.setEnabled(false);
            rdbtnmntmClassic.setSelected(true);
            rdbtnmntmClassic.setToolTipText(null);
            chkbxmntmBody.setEnabled(false);
            chkbxmntmBody.setSelected(false);
            chkbxmntmArm2.setEnabled(false);
            chkbxmntmArm2.setSelected(false);
            chkbxmntmArm0.setEnabled(false);
            chkbxmntmArm0.setSelected(false);
            chkbxmntmLeg2.setEnabled(false);
            chkbxmntmLeg2.setSelected(false);
            chkbxmntmLeg0.setEnabled(false);
            chkbxmntmLeg0.setSelected(false);
            modelPreview.zombie.slim = false;
        }

        chkbxmntmHair.setSelected(modelPreview.zombie.showHair);
    }

    private void setTexture(String name) {
        // Only allow usernames that match valid minecraft.net accounts.
        if (name.matches(usernameRegex)) {
            String url = skinServer + name + suffix;
            modelPreview.zombie.loadTexture(url, false);
        } else {
            modelPreview.zombie.loadDefaultTexture();
        }
    }

    private void setTexture(String url, int type) {
        switch (type) {
        case 1: {
            // Local file
            modelPreview.zombie.loadTexture(url, true);
            break;
        }
        case 2: {
            // URL
            modelPreview.zombie.loadTexture(url, false);
            break;
        }
        default: {
            modelPreview.zombie.loadDefaultTexture();
            break;
        }
        }
    }

    private BufferedImage getTexture() {
        BufferedImage texture = null;

        if (modelPreview.zombie.bi != null) {
            return modelPreview.zombie.bi;
        }
        try {
            texture = ImageIO.read(ModelPreview.class.getResourceAsStream(modelPreview.zombie.defaultSkin));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }

    private void setPreviewScale(int scale) {
        modelPreview.scale = scale;
        int width = ModelPreview.WIDTH * scale;
        int height = ModelPreview.HEIGHT * scale;

        modelPreview.setPreferredSize(new Dimension(width, height));
        modelPreview.setMaximumSize(new Dimension(width, height));
        modelPreview.setMinimumSize(new Dimension(width, height));
    }

    private ImageIcon scaleIcon(ImageIcon imageIcon, int newWidth, int newHeight) {
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();

        if (width != newWidth) {
            width = newWidth;
        }
        if (height != newHeight) {
            height = newHeight;
        }
        return new ImageIcon(imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
    }

    private BufferedImage takeScreenshot(int scale) {
        BufferedImage screenshot = new BufferedImage((ModelPreview.WIDTH * scale), (ModelPreview.HEIGHT * scale), 2);
        screenshot.getGraphics().drawImage(modelPreview.screenBitmap.getImage(), 0, 0, (ModelPreview.WIDTH * scale), (ModelPreview.HEIGHT * scale), null);
        return screenshot;
    }

    public void actionPerformed(ActionEvent e) {
        // Handle commands sent by interface elements.
        String cmd = e.getActionCommand();

        if (cmd.equals("setname")) {
            setTexture(txtName.getText());
            updateInterface();
        }
        if (cmd.equals("open")) {
            int response = fileChooser.showOpenDialog(null);
            if (response == 0) {
                String loaddirString = fileChooser.getSelectedFile().toString();
                setTexture(loaddirString, 1);
                updateInterface();
            }
        }
        if (cmd.equals("openurl")) {
            String url = JOptionPane.showInputDialog(frame, "Enter a URL", "Open URL", JOptionPane.PLAIN_MESSAGE);
            if (url != null) {
                setTexture(url, 2);
                updateInterface();
            }
        }
        if (cmd.equals("save")) {
            if (txtName.getText().matches(usernameRegex)) {
                fileChooser.setSelectedFile(new File(txtName.getText() + suffix));
            } else {
                fileChooser.setSelectedFile(new File("char" + suffix));
            }
            int response = fileChooser.showSaveDialog(null);
            if (response == 0) {
                File fileToBeSaved = fileChooser.getSelectedFile();
                if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(suffix)) {
                    fileToBeSaved = new File(fileChooser.getSelectedFile() + suffix);
                }
                try {
                    BufferedImage image = getTexture();
                    ImageIO.write(image, "png", fileToBeSaved);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (cmd.equals("savescreenshot")) {
            boolean paused = modelPreview.paused;
            modelPreview.paused = true;
            fileChooser.setSelectedFile(new File(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + suffix));
            int response = fileChooser.showSaveDialog(null);
            if (response == 0) {
                File fileToBeSaved = fileChooser.getSelectedFile();
                if (!fileChooser.getSelectedFile().getAbsolutePath().endsWith(suffix)) {
                    fileToBeSaved = new File(fileChooser.getSelectedFile() + suffix);
                }
                try {
                    BufferedImage image = takeScreenshot(2);
                    ImageIO.write(image, "png", fileToBeSaved);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            modelPreview.paused = paused;
        }
        if (cmd.equals("exit")) {
            this.windowClosing(null);
        }
        if (cmd.equals("copy")) {
            BufferedImage bi = getTexture();
            clipboardHelper.CopyImage(bi);
        }
        if (cmd.equals("copyscreenshot")) {
            BufferedImage bi = takeScreenshot(2);
            clipboardHelper.CopyImage(bi);
        }
        if (cmd.equals("paste")) {
            BufferedImage bi = clipboardHelper.PasteImage();
            modelPreview.zombie.loadTexture(bi);
            updateInterface();
        }
        if (cmd.equals("color")) {
            int rgb = new Color(modelPreview.color).getRGB();
            Color colorRGB = new Color(rgb);
            Color selectedColor = JColorChooser.showDialog(null, "Background Color", colorRGB);
            if (selectedColor != null) {
                String hex = Integer.toHexString(selectedColor.getRGB() & 0xffffff);
                // System.out.println(newColorHex);
                modelPreview.color = Integer.parseInt(hex, 16);
            }
        }
        if (cmd.equals("colorhex")) {
            Object hex = JOptionPane.showInputDialog(frame, "Enter a hex color (RRGGBB)", "Background Color", JOptionPane.PLAIN_MESSAGE, null, null, "a0b0e0");
            String hexPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
            if (hex != null && ((String) hex).matches(hexPattern)) {
                modelPreview.color = Integer.parseInt((String) hex, 16);
            }
        }
        if (cmd.equals("server")) {
            Object url = JOptionPane.showInputDialog(frame, "Enter a URL", "Skin Server", JOptionPane.PLAIN_MESSAGE, null, null, "http://skins.minecraft.net/MinecraftSkins/");
            String serverRegex = "^(https?:\\/\\/(?:www\\.|(?!www))[^\\s\\.]+\\.[^\\s]{2,}|www\\.[^\\s]+\\.[^\\s]{2,})\\/$";
            if (url != null && ((String) url).matches(serverRegex)) {
                skinServer = (String) url;
            }
        }
        if (cmd.equals("front")) {
            modelPreview.xRot = 0;
            modelPreview.yRot = 0;
        }
        if (cmd.equals("back")) {
            modelPreview.xRot = 0;
            modelPreview.yRot = 3.1415928f;
        }
        if (cmd.equals("left")) {
            modelPreview.xRot = 0;
            modelPreview.yRot = 1.5707964f;
        }
        if (cmd.equals("right")) {
            modelPreview.xRot = 0;
            modelPreview.yRot = -1.5707964f;
        }
        if (cmd.equals("top")) {
            modelPreview.xRot = 1.5707964f;
            modelPreview.yRot = 0;
        }
        if (cmd.equals("bottom")) {
            modelPreview.xRot = -1.5707964f;
            modelPreview.yRot = 0;
        }
        if (cmd.equals("scale1")) {
            setPreviewScale(1);
            updateInterface();
            frame.pack();
        }
        if (cmd.equals("scale2")) {
            setPreviewScale(2);
            updateInterface();
            frame.pack();
        }
        if (cmd.equals("scale4")) {
            setPreviewScale(4);
            updateInterface();
            frame.pack();
        }
        if (cmd.equals("pause")) {
            modelPreview.paused = !modelPreview.paused;
        }
        if (cmd.equals("anim0")) {
            modelPreview.zombie.anim = 0;
        }
        if (cmd.equals("anim1")) {
            modelPreview.zombie.anim = 1;
        }
        if (cmd.equals("anim2")) {
            modelPreview.zombie.anim = 2;
        }
        if (cmd.equals("classic")) {
            modelPreview.zombie.slim = false;
        }
        if (cmd.equals("slim")) {
            modelPreview.zombie.slim = true;
        }
        if (cmd.equals("showhair")) {
            modelPreview.zombie.showHair = !modelPreview.zombie.showHair;
        }
        if (cmd.equals("showbody")) {
            modelPreview.zombie.showBody = !modelPreview.zombie.showBody;
        }
        if (cmd.equals("showarm0")) {
            modelPreview.zombie.showArm0 = !modelPreview.zombie.showArm0;
        }
        if (cmd.equals("showarm2")) {
            modelPreview.zombie.showArm2 = !modelPreview.zombie.showArm2;
        }
        if (cmd.equals("showleg0")) {
            modelPreview.zombie.showLeg0 = !modelPreview.zombie.showLeg0;
        }
        if (cmd.equals("showleg2")) {
            modelPreview.zombie.showLeg2 = !modelPreview.zombie.showLeg2;
        }
        if (cmd.equals("showlayers")) {
            modelPreview.zombie.showHair = true;
            modelPreview.zombie.showBody = true;
            modelPreview.zombie.showArm0 = true;
            modelPreview.zombie.showArm2 = true;
            modelPreview.zombie.showLeg0 = true;
            modelPreview.zombie.showLeg2 = true;
            modelPreview.zombie.showLeg2 = true;
            updateInterface();
        }
        if (cmd.equals("hidelayers")) {
            modelPreview.zombie.showHair = false;
            modelPreview.zombie.showBody = false;
            modelPreview.zombie.showArm0 = false;
            modelPreview.zombie.showArm2 = false;
            modelPreview.zombie.showLeg0 = false;
            modelPreview.zombie.showLeg2 = false;
            modelPreview.zombie.showLeg2 = false;
            updateInterface();
        }
    }

    public void windowClosing(WindowEvent e) {
        if (modelPreview != null) {
            modelPreview.stop();
        }
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void menuCanceled(MenuEvent e) {
    }

    public void menuDeselected(MenuEvent e) {
    }

    public void menuSelected(MenuEvent e) {
        chkbxmntmPause.setSelected(modelPreview.paused);
    }
}
