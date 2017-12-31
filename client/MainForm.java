


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class MainForm extends javax.swing.JFrame {
    
    Socket socket;
    DataOutputStream out;
    public boolean document_open = false;
    private boolean connected = false;
    String userid;
    String host;
    int port;
    
    private String download_folder = "C:\\";
    
  
    public MainForm() {
        initComponents();
    }
    
    public void initFrame(String userid, String host, int port){
        this.userid = userid;
        this.host = host;
        this.port = port;
        setTitle("you are logged in as: " + userid);
      
        connect();
    }
    
    public void connect(){
       
        try {
            socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
       
            out.writeUTF("CMD_JOIN "+ userid);
          
 
            new Thread(new ClientThread(socket, this)).start();
      
            connected = true;
            
        }
        catch(IOException e) {
            connected = false;
            JOptionPane.showMessageDialog(this, "Unable to Connect to Server, please try again later.!","Connection Failed",JOptionPane.ERROR_MESSAGE);
            
        }
    }
  
    public boolean connected(){
        return this.connected;
    }
    
    
   


    public void setMyTitle(String s){
        setTitle(s);
    }

    public String getMyDownloadFolder(){
        return this.download_folder;
    }
  
    public String getMyHost(){
        return this.host;
    }
 
    public int getMyPort(){
        return this.port;
    }
   
    public String getMyUsername(){
        return this.userid;
    }
 
    public void updateAttachment(boolean b){
        this.document_open = b;
    }
  
    public void openFolder(){
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int open = chooser.showDialog(this, "Browse Folder");
        if(open == chooser.APPROVE_OPTION){
            download_folder = chooser.getSelectedFile().toString()+"\\";
        } else {
            download_folder = "C:\\";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        chooser = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(145, 53, 53));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 51, 102));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Shannu\\Desktop\\client_images\\Transfer-Document-icon.png")); // NOI18N
        jButton1.setText("Send a file");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 51, 102));
        jButton3.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Shannu\\Desktop\\client_images\\inside-logout-icon.png")); // NOI18N
        jButton3.setText("logout");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel1.setText("Welcome to the File Sharing Application");
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addComponent(jButton1)
                .addGap(27, 27, 27)
                .addComponent(jButton3)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   

        int confirm = JOptionPane.showConfirmDialog(this, "Close this Application.?");
        if(confirm == 0){
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            this.dispose();
            System.exit(0);
        }
    }                                  

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        if(!document_open){
            SendFile s = new SendFile();
            if(s.prepare(userid, host, port, this)){
                s.setLocationRelativeTo(null);
                s.setVisible(true);
                document_open = true;
            } else {
                JOptionPane.showMessageDialog(this, "Unable to establish file transfer at this moment, please try again later.!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
   
        int confirm = JOptionPane.showConfirmDialog(null, "Logout from your account ?");
        if(confirm == 0){
            try {
                socket.close();
                setVisible(false);
              
                new LoginForm().setVisible(true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

     
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JFileChooser chooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration                   
}