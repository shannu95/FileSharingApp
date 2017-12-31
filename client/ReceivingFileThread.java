

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;


public class ReceivingFileThread implements Runnable {
    
    protected Socket socket;
    protected DataInputStream in;
    protected DataOutputStream out;
    protected MainForm main;
    protected StringTokenizer st;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    
    public ReceivingFileThread(Socket soc, MainForm m){
        this.socket = soc;
        this.main = m;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String data = in.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                
                switch(CMD){
         
                    case "CMD_SENDFILE":
                        String consignee = null;
                            try {
                                String filename = st.nextToken();
                                int filesize = Integer.parseInt(st.nextToken());
                                consignee = st.nextToken();
                                main.setMyTitle("Downloading File....");
                                System.out.println("Downloading File....");
                                System.out.println("From: "+ consignee);
                                String path = main.getMyDownloadFolder() + filename;                                
                             
                                FileOutputStream fos = new FileOutputStream(path);
                                InputStream input = socket.getInputStream();                                
                              
                                ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(main, "Downloading file please wait...", input);
                             
                                BufferedInputStream bis = new BufferedInputStream(pmis);
                              
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int count, percent = 0;
                                while((count = bis.read(buffer)) != -1){
                                    percent = percent + count;
                                    int p = (percent / filesize);
                                    main.setMyTitle("Downloading File  "+ p +"%");
                                    fos.write(buffer, 0, count);
                                }
                                fos.flush();
                                fos.close();
                                main.setMyTitle("You are logged in as: " + main.getMyUsername());
                                JOptionPane.showMessageDialog(null, "File has been downloaded to \n'"+ path +"'");
                                System.out.println("File was saved: "+ path);
                            } catch (IOException e) {
                                
                                DataOutputStream eDos = new DataOutputStream(socket.getOutputStream());
                                eDos.writeUTF("CMD_SENDFILERESPONSE "+ consignee + " Connection was lost, please try again later.!");
                                
                                System.out.println(e.getMessage());
                                main.setMyTitle("you are logged in as: " + main.getMyUsername());
                                JOptionPane.showMessageDialog(main, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                                socket.close();
                            }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }
}