package serialTree;

import jfork.nproperty.Cfg;
import jfork.nproperty.ConfigParser;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Enumeration;

@Cfg
public class Main {
    public static String baseName = "base.bin";
    public static String nameOfFont = "Arial";
    public static int sizeOfFont = 20;
    public static String pathToAnyDesk="AnyDesk.exe";
    public static int mainFrameWidth=600;
    public static int mainFrameHeight=600;
    public static int dialogFolderFrameWidth=800;
    public static int dialogFolderFrameHeight=110;
    public static int dialogHostFrameWidth=800;
    public static int dialogHostFrameHeight=200;
    public static boolean dialogAutoResize =true;
    public static String pathToCsv = "base.csv";
    @Cfg(ignore = true)
    public static String pathToConfig = "base.ini";
    @Cfg(ignore = true)
    public static TreeExample treeExample;
    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }
    public static void main(String[] args) throws Exception
    {
        UIManager
                .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        if (args.length==1) pathToConfig = String.valueOf(Path.of(args[0]));
        try {
            ConfigParser.parse(Main.class, pathToConfig,false);
            baseName=new String(baseName.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            pathToCsv=new String(pathToCsv.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            pathToAnyDesk=new String(pathToAnyDesk.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
            File file = new File(pathToConfig);
            try{
                if(file.createNewFile()){
                    Writer writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(pathToConfig), StandardCharsets.UTF_8));
                    writer.write("baseName=base.bin\r\n"+
                            "nameOfFont=Arial\r\n" +
                            "sizeOfFont=20\r\n" +
                            "pathToAnyDesk=AnyDesk.exe\r\n"+
                            "mainFrameWidth=600\r\n"+
                            "mainFrameHeight=600\r\n"+
                            "dialogFolderFrameWidth=800\r\n"+
                            "dialogFolderFrameHeight=110\r\n"+
                            "dialogHostFrameWidth=800\r\n"+
                            "dialogHostFrameHeight=200\r\n"+
                            "dialogAutoResize=true\r\n"+
                            "pathToCsv=base.csv");
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    setUIFont(new FontUIResource(new Font(nameOfFont, 0, sizeOfFont)));
                    treeExample = new TreeExample();
                            treeExample.deserialize(baseName);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
//                try {
//                    TreeExample treeExample = TreeExample.deserialize(tmpName);
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                    try {
//                        TreeExample treeExample = new TreeExample();
//                    } catch (IOException | ClassNotFoundException ex) {
//                        ex.printStackTrace();
//                    }
//                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    treeExample.serialize(baseName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
