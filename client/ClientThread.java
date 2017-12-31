
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;


public class ClientThread implements Runnable{
    MainForm main_f;
    StringTokenizer st;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    
    protected DecimalFormat decimal = new DecimalFormat("##,#00");
    
    public ClientThread(Socket socket, MainForm main_f){
        this.main_f = main_f;
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
           
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
            
                   case "CMD_FILE_XD": 
                        String sender = st.nextToken();
                        String receiver = st.nextToken();
                        String fname = st.nextToken();
                        int confirm = JOptionPane.showConfirmDialog(main_f, "From: "+sender+"\nFilename: "+fname+"\nwould you like to Accept.?");
                       
                        if(confirm == 0){ 
                            main_f.openFolder();
                            try {
                                out = new DataOutputStream(socket.getOutputStream());
                            
                                String format = "CMD_SEND_FILE_ACCEPT "+sender+" accepted";
                                out.writeUTF(format);
                                
                                
                                Socket fSoc = new Socket(main_f.getMyHost(), main_f.getMyPort());
                                DataOutputStream fout = new DataOutputStream(fSoc.getOutputStream());
                                fout.writeUTF("CMD_SHARINGSOCKET "+ main_f.getMyUsername());
                               
                                new Thread(new ReceivingFileThread(fSoc, main_f)).start();
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        } else { 
                            try {
                                out = new DataOutputStream(socket.getOutputStream());
                               
                                String format = "CMD_SEND_FILE_ERROR "+sender+" Client rejected your request or connection was lost.!";
                                out.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        }                       
                        break; 
                                   
                    case "CMD_ONLINE":
                        Vector online = new Vector();
                        while(st.hasMoreTokens()){
                            String list = st.nextToken();
                            if(!list.equalsIgnoreCase(main_f.userid)){
                                online.add(list);
                            }
                        }
                        
                        break;   
                    
                        
                    default: 
                        
                    break;
                }
            }
        } catch(IOException e){
            
        }
    }
}