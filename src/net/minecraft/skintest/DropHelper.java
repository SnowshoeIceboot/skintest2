package net.minecraft.skintest;

import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.TooManyListenersException;

public class DropHelper {
    private transient DropTargetListener dropListener;
    private static Boolean supportsDnD;

    public DropHelper(final Component c, final Listener listener) {
        if (supportsDnD()) {
            dropListener = new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent evt) {
                }

                public void dragOver(DropTargetDragEvent evt) {
                }

                public void drop(DropTargetDropEvent evt) {
                    try {
                        Transferable tr = evt.getTransferable();

                        if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            evt.acceptDrop(DnDConstants.ACTION_COPY);

                            List fileList = (List) tr
                                    .getTransferData(DataFlavor.javaFileListFlavor);
                            File[] filesTemp = new File[fileList.size()];
                            fileList.toArray(filesTemp);
                            final File[] files = filesTemp;

                            if (listener != null)
                                listener.filesDropped(files);

                            evt.getDropTargetContext().dropComplete(true);
                        }
                        else
                        {
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    evt.acceptDrop(DnDConstants.ACTION_COPY);

                                    Reader reader = flavors[zz].getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(reader);

                                    if (listener != null)
                                        listener.filesDropped(createFileArray(br));

                                    evt.getDropTargetContext().dropComplete(true);
                                    handled = true;
                                    break;
                                }
                            }
                            if (!handled) {
                                evt.rejectDrop();
                            }
                        }
                    }
                    catch (IOException io) {
                        evt.rejectDrop();
                    }
                    catch (UnsupportedFlavorException ufe) {
                        evt.rejectDrop();
                    }
                }

                public void dragExit(DropTargetEvent evt) {
                }

                public void dropActionChanged(DropTargetDragEvent evt) {
                    if (isDragOk(evt)) {
                        evt.acceptDrag(DnDConstants.ACTION_COPY);
                    }
                    else {
                        evt.rejectDrag();
                    }
                }
            };
            makeDropTarget(c);
        }
    }

    private static boolean supportsDnD() {
        if (supportsDnD == null) {
            boolean support = false;
            try {
                support = true;
            }
            catch (Exception e) {
                support = false;
            }
            supportsDnD = new Boolean(support);
        }
        return supportsDnD.booleanValue();
    }

    private static String ZERO_CHAR_STRING = "" + (char) 0;

    private static File[] createFileArray(BufferedReader bReader) {
        try {
            List list = new ArrayList();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    if (ZERO_CHAR_STRING.equals(line))
                        continue;

                    File file = new File(new URI(line));
                    list.add(file);
                } catch (Exception ex) {
                }
            }

            return (File[]) list.toArray(new File[list.size()]);
        } catch (IOException ex) {
        }
        return new File[0];
    }

    private void makeDropTarget(final Component c) {
        final DropTarget dt = new DropTarget();
        try {
            dt.addDropTargetListener(dropListener);
        }
        catch (TooManyListenersException e) {
            e.printStackTrace();
        }

        c.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent evt) {
                Component parent = c.getParent();
                if (parent == null) {
                    c.setDropTarget(null);
                }
                else {
                    new DropTarget(c, dropListener);
                }
            }
        });
        if (c.getParent() != null)
            new DropTarget(c, dropListener);
    }

    private boolean isDragOk(final DropTargetDragEvent evt) {
        boolean ok = false;
        DataFlavor[] flavors = evt.getCurrentDataFlavors();
        int i = 0;
        while (!ok && i < flavors.length) {
            final DataFlavor curFlavor = flavors[i];
            if (curFlavor.equals(DataFlavor.javaFileListFlavor)
                    || curFlavor.isRepresentationClassReader()) {
                ok = true;
            }
            i++;
        }
        return ok;
    }

    public static boolean remove(Component c) {
        return remove(null, c, true);
    }

    public static boolean remove(PrintStream out, Component c, boolean recursive) {
        if (supportsDnD()) {
            c.setDropTarget(null);
            if (recursive && (c instanceof Container)) {
                Component[] comps = ((Container) c).getComponents();
                for (int i = 0; i < comps.length; i++)
                    remove(out, comps[i], recursive);
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    public static interface Listener {
        public abstract void filesDropped(File[] files);
    }

    public static class Event extends EventObject {
        private static final long serialVersionUID = 1L;
        private File[] files;

        public Event(File[] files, Object source) {
            super(source);
            this.files = files;
        }

        public File[] getFiles() {
            return files;
        }
    }

    public static class TransferableObject implements Transferable {
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";
        public final static DataFlavor DATA_FLAVOR = new DataFlavor(DropHelper.TransferableObject.class, MIME_TYPE);
        private Fetcher fetcher;
        private Object data;
        private DataFlavor customFlavor;

        public TransferableObject(Object data) {
            this.data = data;
            this.customFlavor = new DataFlavor(data.getClass(), MIME_TYPE);
        }

        public TransferableObject(Fetcher fetcher) {
            this.fetcher = fetcher;
        }

        public TransferableObject(Class dataClass, Fetcher fetcher) {
            this.fetcher = fetcher;
            this.customFlavor = new DataFlavor(dataClass, MIME_TYPE);
        }

        public DataFlavor getCustomDataFlavor() {
            return customFlavor;
        }

        public DataFlavor[] getTransferDataFlavors() {
            if (customFlavor != null)
                return new DataFlavor[] { customFlavor, DATA_FLAVOR, DataFlavor.stringFlavor };
            else
                return new DataFlavor[] { DATA_FLAVOR, DataFlavor.stringFlavor };
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DATA_FLAVOR))
                return fetcher == null ? data : fetcher.getObject();
            if (flavor.equals(DataFlavor.stringFlavor))
                return fetcher == null ? data.toString() : fetcher.getObject().toString();
            throw new UnsupportedFlavorException(flavor);
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.equals(DATA_FLAVOR))
                return true;
            if (flavor.equals(DataFlavor.stringFlavor))
                return true;
            return false;
        }

        public static interface Fetcher {
            public abstract Object getObject();
        }
    }
}
