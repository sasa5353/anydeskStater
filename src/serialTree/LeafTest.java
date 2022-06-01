package serialTree;

import java.io.*;

import static java.lang.Thread.sleep;

public class LeafTest implements Serializable {
//    @Serial
    private static final long serialVersionUID = 1L;
//    public static String xAny="AnyDesk.exe";
    public boolean isFolder;

    public String name;
    public String xID;
    public String xPSW;
    public String comment;

    public LeafTest(){

    }

    LeafTest(String _name, String _xID, String _xPSW, String _comment){
        this.name = _name;
        this.xID = _xID;
        this.xPSW = _xPSW;
        this.comment = _comment;
        this.isFolder = false;
    }

    LeafTest(String _name){
        this.name = _name;
        this.isFolder = true;
    }

    @Override
    public String toString(){
        return name;// + ((!comment.equals(""))?" ("+comment+")":"");
    }

    public String getName(){
        return name;
    }

    public String getxID(){
        return xID;
    }

    public void excAnyDesk() {
        excAnyDesk(false);
    }

    public void excAnyDesk(boolean isFileTransfer){
        if(!isFolder) {
            Runtime rt = Runtime.getRuntime();
            try {
                Process pr;
                if (isFileTransfer) {
                    pr = rt.exec(new String[]{"cmd.exe", "/c", "\"echo " + xPSW + "|\"" + Main.pathToAnyDesk + "\" " + xID + " --with-password --file-transfer\""});
                } else {
                    pr = rt.exec(new String[]{"cmd.exe", "/c", "\"echo " + xPSW + "|\"" + Main.pathToAnyDesk + "\" " + xID + " --with-password\""});
                }
                sleep(3000);
                pr.destroy();
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

