

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;



public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

//constructor
    public Client(){

       try {

            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",7778);
            System.out.println("connection done..");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());

              createGUI();
              handleEvent();
              startReading();
//            startWriting();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvent() {
       messageInput.addKeyListener(new KeyListener() {
           @Override
           public void keyTyped(KeyEvent keyEvent) {

           }

           @Override
           public void keyPressed(KeyEvent keyEvent) {

           }

           @Override
           public void keyReleased(KeyEvent keyEvent) {
            //   System.out.println("key released"+ keyEvent.getKeyCode());
               if(keyEvent.getKeyCode()==10){

                 //  System.out.println("you have pressed enter button");
                   String contentTosend=messageInput.getText();
                   messageArea.append("Me :"+contentTosend+"\n");
                   out.println(contentTosend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();
               }
           }
       });

    }

    private void createGUI(){
        //GUI code...

        this.setTitle("Client Messenger[END]");
        this.setSize(400,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("src/clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        //messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //frame ka layout set karenge
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    //start reading (method)
    public void startReading(){

        //thread read karke deta rahega

        Runnable r1=()->{

            System.out.println("reader started...");

            try {
            while (true){


                String msg = br.readLine();
                    if (msg.equals("exit")){

                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server terminated the chat");
                        messageInput.setEnabled(true);
                        socket.close();
                        break;
                    }

                   // System.out.println("Server : "+msg);
                messageArea.append("Server : "+msg+"\n");

                }
            }catch (Exception e){
               // e.printStackTrace();
               System.out.println("Connection closed");
            }

        };

        new Thread(r1).start();
    }

    //start writing send (method)
    public void startWriting(){

        //thread - data user se lega and send karega client tak

        Runnable r2=()->{
            System.out.println("writer started...");

            try {
            while (!socket.isClosed()){

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                if (content.equals("exit")){

                    socket.close();
                    break;
                }

                }

                  System.out.println("Connection is closed");

            }catch (Exception e){
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
    }
}
