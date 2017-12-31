
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class SocketThread implements Runnable{
    
    Socket socket;
    Main_page main;
    DataInputStream dis;
    StringTokenizer st;
    String client, filesharing_username;
    
    private final int BUFFER_SIZE = 100;
    
    public SocketThread(Socket socket, Main_page main){
        this.main = main;
        this.socket = socket;
        
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            main.appendMessage("[SocketThreadIOException]: "+ e.getMessage());
        }
    }
    
  
    private void createConnection(String receiver, String sender, String filename){
        try {
            main.appendMessage("[createConnection]: creating file sharing connection.");
            Socket s = main.get_clients(receiver);
            if(s != null){ 
                main.appendMessage("[createConnection]: Socket OK");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                main.appendMessage("[createConnection]: DataOutputStream OK");
              
                String format = "CMD_FILE_XD "+sender+" "+receiver +" "+filename;
                dosS.writeUTF(format);
                main.appendMessage("[createConnection]: "+ format);
            }else{
                main.appendMessage("[createConnection]: Client was not found '"+receiver+"'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR "+ "Client '"+receiver+"' was not found in the list, make sure it is on the online list.!");
            }
        } catch (IOException e) {
            main.appendMessage("[createConnection]: "+ e.getLocalizedMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            while(true){
            
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
  
                switch(CMD){
                    case "CMD_JOIN":
                     
                        String clientUsername = st.nextToken();
                        client = clientUsername;
                        main.setClientList(clientUsername);
                        main.setSocketList(socket);
                        main.appendMessage("[Client]: "+ clientUsername +" joins the chatroom.!");
                        break;
                        
                  
                    case "CMD_SHARINGSOCKET":
                        main.appendMessage("CMD_SHARINGSOCKET : Client stablish a socket connection for file sharing...");
                        String file_sharing_username = st.nextToken();
                        filesharing_username = file_sharing_username;
                        main.setclientusername(file_sharing_username);
                        main.setclientsocket(socket);
                        main.appendMessage("CMD_SHARINGSOCKET : Username: "+ file_sharing_username);
                        main.appendMessage("CMD_SHARINGSOCKET : File sharing is now open");
                        break;
                    
                    case "CMD_SENDFILE":
                        main.appendMessage("CMD_SENDFILE : Client sending a file...");
                       
                        String file_name = st.nextToken();
                        String filesize = st.nextToken();
                        String sendto = st.nextToken();
                        String consignee = st.nextToken();
                        main.appendMessage("CMD_SENDFILE : From: "+ consignee);
                        main.appendMessage("CMD_SENDFILE : To: "+ sendto);
                    
                        main.appendMessage("CMD_SENDFILE : preparing connections..");
                        Socket cSock = main.getClientFileSharingSocket(sendto); 
                      
                        if(cSock != null){
                            try {
                                main.appendMessage("CMD_SENDFILE : Connected..!");
                               
                                main.appendMessage("CMD_SENDFILE : Sending file to client...");
                                DataOutputStream cDos = new DataOutputStream(cSock.getOutputStream());
                                cDos.writeUTF("CMD_SENDFILE "+ file_name +" "+ filesize +" "+ consignee);
                            
                                InputStream input = socket.getInputStream();
                                OutputStream sendFile = cSock.getOutputStream();
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int cnt;
                                while((cnt = input.read(buffer)) > 0){
                                    sendFile.write(buffer, 0, cnt);
                                }
                                sendFile.flush();
                                sendFile.close();
                               
                                main.removeClientFileSharing(sendto);
                                main.removeClientFileSharing(consignee);
                                main.appendMessage("CMD_SENDFILE : File was send to client...");
                            } catch (IOException e) {
                                main.appendMessage("[CMD_SENDFILE]: "+ e.getMessage());
                            }
                        }else{ 
                            main.removeClientFileSharing(consignee);
                            main.appendMessage("CMD_SENDFILE : Client '"+sendto+"' was not found.!");
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("CMD_SENDFILEERROR "+ "Client '"+sendto+"' was not found, File Sharing will exit.");
                        }                        
                        break;
                        
                        
                    case "CMD_SENDFILERESPONSE":
                      
                        String receiver = st.nextToken();
                        String rMsg = ""; 
                        main.appendMessage("[CMD_SENDFILERESPONSE]: username: "+ receiver);
                        while(st.hasMoreTokens()){
                            rMsg = rMsg+" "+st.nextToken();
                        }
                        try {
                            Socket rSock = (Socket) main.getClientFileSharingSocket(receiver);
                            DataOutputStream rDos = new DataOutputStream(rSock.getOutputStream());
                            rDos.writeUTF("CMD_SENDFILERESPONSE" +" "+ receiver +" "+ rMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_SENDFILERESPONSE]: "+ e.getMessage());
                        }
                        break;
                        
                        
                    case "CMD_SEND_FILE_XD":                         
                        try {
                            String send_sender = st.nextToken();
                            String send_receiver = st.nextToken();
                            String send_filename = st.nextToken();
                            main.appendMessage("[CMD_SEND_FILE_XD]: Host: "+ send_sender);
                            this.createConnection(send_receiver, send_sender, send_filename);
                        } catch (Exception e) {
                            main.appendMessage("[CMD_SEND_FILE_XD]: "+ e.getLocalizedMessage());
                        }
                        break;
                        
                        
                    case "CMD_SEND_FILE_ERROR": 
                        String eReceiver = st.nextToken();
                        String eMsg = "";
                        while(st.hasMoreTokens()){
                            eMsg = eMsg+" "+st.nextToken();
                        }
                        try {
                            
                            Socket eSock = main.getClientFileSharingSocket(eReceiver); 
                            DataOutputStream eDos = new DataOutputStream(eSock.getOutputStream());
                           
                            eDos.writeUTF("CMD_RECEIVE_FILE_ERROR "+ eMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: "+ e.getMessage());
                        }
                        break;
                        
                    
                    case "CMD_SEND_FILE_ACCEPT":
                        String aReceiver = st.nextToken();
                        String aMsg = "";
                        while(st.hasMoreTokens()){
                            aMsg = aMsg+" "+st.nextToken();
                        }
                        try {
                          
                            Socket aSock = main.getClientFileSharingSocket(aReceiver); 
                            DataOutputStream aDos = new DataOutputStream(aSock.getOutputStream());
                            
                            aDos.writeUTF("CMD_RECEIVE_FILE_ACCEPT "+ aMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: "+ e.getMessage());
                        }
                        break;
                        
                        
                    default: 
                        main.appendMessage("[CMDException]: Unknown Command "+ CMD);
                    break;
                }
            }
        } catch (IOException e) {
         
            System.out.println(client);
            System.out.println("File Sharing: " +filesharing_username);
            main.remove_client(client);
            if(filesharing_username != null){
                main.removeClientFileSharing(filesharing_username);
            }
            main.appendMessage("[SocketThread]: Client connection closed..!");
        }
    }
    
}