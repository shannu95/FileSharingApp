
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class OnlineList implements Runnable {
    
    Main_page main;
    
    public OnlineList(Main_page main){
        this.main = main;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                String msg = "";
                for(int x=0; x < main.client_list.size(); x++){
                    msg = msg+" "+ main.client_list.elementAt(x);
                }
                
                for(int x=0; x < main.socket_list.size(); x++){
                    Socket tsoc = (Socket) main.socket_list.elementAt(x);
                    DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                  
                    if(msg.length() > 0){
                        dos.writeUTF("CMD_ONLINE "+ msg);
                    }
                }
                
                Thread.sleep(1900);
            }
        } catch(InterruptedException e){
            main.appendMessage("[InterruptedException]: "+ e.getMessage());
        } catch (IOException e) {
            main.appendMessage("[IOException]: "+ e.getMessage());
        }
    }
    
    
}