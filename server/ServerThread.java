
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    
    ServerSocket server;
    Main_page main;
    boolean keepGoing = true;
    
    public ServerThread(int port, Main_page main){
        main.appendMessage("[Server]: Starting server in port "+ port);
        try {
            this.main = main;
            server = new ServerSocket(port);
            main.appendMessage("[Server]: Server started.!");
        } 
        catch (IOException e) { main.appendMessage("[IOException]: "+ e.getMessage()); }
    }

    @Override
    public void run() {
        try {
            while(keepGoing){
                Socket socket = server.accept();
               
                new Thread(new SocketThread(socket, main)).start();
            }
        } catch (IOException e) {
            main.appendMessage("[ServerThreadIOException]: "+ e.getMessage());
        }
    }
    
    
    public void stop(){
        try {
            server.close();
            keepGoing = false;
            System.out.println("Server is now closed..!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
}