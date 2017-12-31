
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class SendFile extends javax.swing.JFrame {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String userid;
    private String host;
    private int port;
    private StringTokenizer st;
    private String send_to;
    private String file;
    private MainForm main;
  
    public SendFile() {
        initComponents();
        progressbar.setVisible(false);
    }
    
  
    public boolean prepare(String u, String h, int p, MainForm m){
        this.host = h;
        this.userid = u;
        this.port = p;
        this.main = m;

        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
     
            String format = "CMD_SHARINGSOCKET "+ userid;
            dos.writeUTF(format);
            System.out.println(format);
            
     
            new Thread(new SendFileThread(this)).start();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    
    

    class SendFileThread implements Runnable{
        private SendFile form;
        public SendFileThread(SendFile form){
            this.form = form;
        }
        
        private void closeMe(){
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[closeMe]: "+e.getMessage());
            }
            dispose();
        }
        
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()){
                    String data = dis.readUTF(); 
                    st = new StringTokenizer(data);
                    String cmd = st.nextToken();
                    switch(cmd){
                        case "CMD_RECEIVE_FILE_ERROR": 
                            String msg = "";
                            while(st.hasMoreTokens()){
                                msg = msg+" "+st.nextToken();
                            }
                            form.updateAttachment(false);
                            JOptionPane.showMessageDialog(SendFile.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                            this.closeMe();
                            break;
                            
                        case "CMD_RECEIVE_FILE_ACCEPT": 
                            new Thread(new SendingFileThread(socket, file, send_to, userid, SendFile.this)).start();
                            break;
                            
                        case "CMD_SENDFILEERROR":
                            String emsg = "";
                            while(st.hasMoreTokens()){
                                emsg = emsg +" "+ st.nextToken();
                            }                                                     
                            System.out.println(emsg);                            
                            JOptionPane.showMessageDialog(SendFile.this, emsg,"Error", JOptionPane.ERROR_MESSAGE);
                            form.updateAttachment(false);
                            form.disableGUI(false);
                            form.updateBtn("Send File");
                            break;
                        
                        
                        case "CMD_SENDFILERESPONSE":
                           
                            String rReceiver = st.nextToken();
                            String rMsg = "";
                            while(st.hasMoreTokens()){
                                rMsg = rMsg+" "+st.nextToken();
                            }
                            form.updateAttachment(false);
                            JOptionPane.showMessageDialog(SendFile.this, rMsg, "Error", JOptionPane.ERROR_MESSAGE);
                            dispose();
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        chooser = new javax.swing.JFileChooser();
        txtSendTo = new javax.swing.JTextField();
        sendfile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        browse = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        progressbar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Send a File");
        setAlwaysOnTop(true);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        sendfile.setBackground(new java.awt.Color(0, 51, 102));
        sendfile.setForeground(new java.awt.Color(255, 255, 255));
        sendfile.setText("Send File");
        sendfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendfileActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel1.setText("Select a File:");

        txtFile.setEditable(false);
        txtFile.setBackground(new java.awt.Color(255, 255, 255));
        txtFile.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFileActionPerformed(evt);
            }
        });

        browse.setBackground(new java.awt.Color(0, 51, 102));
        browse.setForeground(new java.awt.Color(255, 255, 255));
        browse.setText("Browse");
        browse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 14)); // NOI18N
        jLabel2.setText("Send to a specified user:");

        progressbar.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(230, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSendTo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sendfile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(browse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(41, 41, 41))))
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSendTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendfile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>                        

    private void sendfileActionPerformed(java.awt.event.ActionEvent evt) {                                         
     
        send_to = txtSendTo.getText();
        file = txtFile.getText();
        
        if((send_to.length() > 0) && (file.length() > 0)){
            try {
               
                txtFile.setText("");
                String fname = getThisFilename(file);
                String format = "CMD_SEND_FILE_XD "+userid+" "+send_to+" "+fname;
                dos.writeUTF(format);
                System.out.println(format);
                updateBtn("Sending...");
                sendfile.setEnabled(false);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(this, "Incomplete Form.!","Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                        

    private void formWindowClosing(java.awt.event.WindowEvent evt) {                                   
   
        main.updateAttachment(false);
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }                                  

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {                                       
        
        showOpenDialog();
    }                                      

    private void txtFileActionPerformed(java.awt.event.ActionEvent evt) {                                        
      
    }                                       
    
    

    public void showOpenDialog(){
        int intval = chooser.showOpenDialog(this);
        if(intval == chooser.APPROVE_OPTION){
            txtFile.setText(chooser.getSelectedFile().toString());
        }else{
            txtFile.setText("");
        }
    }
    
    

    public void disableGUI(boolean d){
        if(d){ // Disable
            txtSendTo.setEditable(false);
            browse.setEnabled(false);
            sendfile.setEnabled(false);
            txtFile.setEditable(false);
            progressbar.setVisible(true);
        } else { // Enable
            txtSendTo.setEditable(true);
            sendfile.setEnabled(true);
            browse.setEnabled(true);
            txtFile.setEditable(true);
            progressbar.setVisible(false);
        }
    }
    

    public void setMyTitle(String s){
        setTitle(s);
    }

    protected void closeThis(){
        dispose();
    }
 
    public String getThisFilename(String path){
        File p = new File(path);
        String fname = p.getName();
        return fname.replace(" ", "_");
    }
 
    public void updateAttachment(boolean b){
        main.updateAttachment(b);
    }

    public void updateBtn(String str){
        sendfile.setText(str);
    }
  
    public void updateProgress(int val){
        progressbar.setValue(val);
    }
    
    
    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SendFile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton browse;
    private javax.swing.JFileChooser chooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar progressbar;
    private javax.swing.JButton sendfile;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextField txtSendTo;
    // End of variables declaration                   
}