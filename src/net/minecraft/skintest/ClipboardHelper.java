package net.minecraft.skintest;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipboardHelper implements ClipboardOwner {
    public void CopyImage(BufferedImage bi) {
        try {
            TransferableImage trans = new TransferableImage(bi);
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(trans, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage PasteImage() {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = c.getContents(null);
        if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return ImageHelper.toBufferedImage((Image) trans.getTransferData(DataFlavor.imageFlavor));
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void lostOwnership(Clipboard clip, Transferable trans) {
        System.out.println("Lost clipboard ownership");
    }

    private class TransferableImage implements Transferable {
        Image i;

        public TransferableImage(Image i) {
            this.i = i;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
                return i;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (flavor.equals(flavors[i])) {
                    return true;
                }
            }
            return false;
        }
    }
}
