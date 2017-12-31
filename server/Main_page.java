



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;


public class Main_page extends javax.swing.JFrame {
    
    SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
    Thread t;
    ServerThread st;
    public Vector socket_list = new Vector();
    public Vector client_list = new Vector();
    public Vector client_username = new Vector();
    public Vector client_socket = new Vector();
    ServerSocket server;
   
    public Main_page() {
        initComponents();
    }
    
    
    public void appendMessage(String msg){
        Date date = new Date();
        Display_area.append(s.format(date) +": "+ msg +"\n");
        Display_area.setCaretPosition(Display_area.getText().length() - 1);
    }
 
    public void setSocketList(Socket socket){
        try {
            socket_list.add(socket);
            appendMessage("Socket: connected");
        } catch (Exception e) { appendMessage("Socket: "+ e.getMessage()); }
    }
    public void setClientList(String client){
        try {
            client_list.add(client);
            appendMessage("A client has been added");
        } catch (Exception e) { appendMessage("Client: "+ e.getMessage()); }
    }
    public void setclientusername(String user){
        try {
            client_username.add(user);
        } catch (Exception e) { }
    }
    
    public void setclientsocket(Socket soc){
        try {
            client_socket.add(soc);
        } catch (Exception e) { }
    }

    public Socket get_clients(String client){
        Socket tsoc = null;
        for(int x=0; x < client_list.size(); x++){
            if(client_list.get(x).equals(client)){
                tsoc = (Socket) socket_list.get(x);
                break;
            }
        }
        return tsoc;
    }
    
    
    public void remove_client(String client){
        try {
            for(int x=0; x < client_list.size(); x++){
                if(client_list.elementAt(x).equals(client)){
                    client_list.removeElementAt(x);
                    socket_list.removeElementAt(x);
                    appendMessage("[Removed]: "+ client);
                    break;
                }
            }
        } catch (Exception e) {
            appendMessage("[RemovedException]: "+ e.getMessage());
        }
    }
    
    public Socket getClientFileSharingSocket(String username){
        Socket tsoc = null;
        for(int x=0; x < client_username.size(); x++){
            if(client_username.elementAt(x).equals(username)){
                tsoc = (Socket) client_socket.elementAt(x);
                break;
            }
        }
        return tsoc;
    }
    
 
    public void removeClientFileSharing(String username){
        for(int x=0; x < client_username.size(); x++){
            if(client_username.elementAt(x).equals(username)){
                try {
                    Socket rSock = getClientFileSharingSocket(username);
                    if(rSock != null){
                        rSock.close();
                    }
                    client_username.removeElementAt(x);
                    client_socket.removeElementAt(x);
                    appendMessage("[FileSharing]: Removed "+ username);
                } catch (IOException e) {
                    appendMessage("[FileSharing]: "+ e.getMessage());
                    appendMessage("[FileSharing]: Unable to Remove "+ username);
                }
                break;
            }
        }
    }
    
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        server_start = new javax.swing.JButton();
        server_stop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Display_area = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel1.setText("Enter the port number :");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        server_start.setBackground(new java.awt.Color(0, 51, 102));
        server_start.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        server_start.setForeground(new java.awt.Color(255, 255, 255));
        server_start.setText("Start Server");
        server_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                server_startActionPerformed(evt);
            }
        });

        server_stop.setBackground(new java.awt.Color(0, 51, 102));
        server_stop.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        server_stop.setForeground(new java.awt.Color(255, 255, 255));
        server_stop.setText("Stop Server");
        server_stop.setEnabled(false);
        server_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                server_stopActionPerformed(evt);
            }
        });

        Display_area.setEditable(false);
        Display_area.setColumns(20);
        Display_area.setRows(5);
        jScrollPane1.setViewportView(Display_area);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(server_start, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(server_stop, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(server_start)
                        .addGap(18, 18, 18)
                        .addComponent(server_stop)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>                        

    private void server_startActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
        int port = Integer.parseInt(jTextField1.getText());
        st = new ServerThread(port, this);
        t = new Thread(st);
        t.start();
        
       new Thread(new OnlineList(this)).start();
       
       server_start.setEnabled(false);
       server_stop.setEnabled(true);
        
     
    }                                            

    private void server_stopActionPerformed(java.awt.event.ActionEvent evt) {                                            
        
        int confirm = JOptionPane.showConfirmDialog(null, "Close server ??");
        if(confirm == 0){
            st.stop();
        }
    }                                           

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea Display_area;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton server_start;
    private javax.swing.JButton server_stop;
    // End of variables declaration                   


}