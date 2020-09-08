import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Created by Ксения on 25.04.2017.
 */
public class InstantMessenger {
    private MainFrame This;
    private static final int SERVER_PORT = 4567;
    private final InetAddress SERVER_ADDRESS = InetAddress.getLoopbackAddress();
    private final int MY_PORT=4568;
    private Boolean isPrivate=false;


    public InstantMessenger(MainFrame This){this.This=This;startServer();}
    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket =
                            new ServerSocket(MY_PORT);
                    while (!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());

                        final String senderName = in.readUTF();
                        if(senderName.equals("Letter from server")){
                            final String newName = in.readUTF();
                            This.setName(newName, This);
                            This.getTextAreaIncoming().append("К сети присоеденился новый пользователь: "+newName+"\n");
                            socket.close();

                        }
                        else {
                            final String message = in.readUTF();
                            socket.close();

                            if(isPrivate){
                                This.getPrivateDialog().getTextAreaIncoming().append(senderName + " : " +
                                        message + "\n");
                            }else{
                            This.getTextAreaIncoming().append(senderName + " : " +
                                    message + "\n");}
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(This,
                            "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public void sendMessage(String destinationName, String message, Boolean isPrivate) {
        this.isPrivate=isPrivate;
        try {
            final Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Создаем сокет для соединения
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // Открываем поток вывода данных
            out.writeUTF(destinationName); // Записываем в поток имя
            out.writeUTF(message); // Записываем в поток сообщение
            socket.close(); // Закрываем сокет
// Помещаем сообщения в текстовую область вывода
            if(isPrivate){This.getPrivateDialog().getTextAreaIncoming().append("Я -> " + destinationName + ": "
                    + message + "\n");
// Очищаем текстовую область ввода сообщения
                This.getPrivateDialog().getTextAreaOutgoing().setText("");

            }else{
            This.getTextAreaIncoming().append("Я -> " + destinationName + ": "
                    + message + "\n");
// Очищаем текстовую область ввода сообщения
            This.getTextAreaOutgoing().setText("");}
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение серверу",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void sendMessageForServer( String name) {
        try {
            final Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Letter for server");
            out.writeUTF(name);
            //out.writeUTF((InetAddress.getLocalHost()).getHostAddress());
            out.writeUTF(((InetSocketAddress) socket.getLocalSocketAddress())
                    .getAddress().getHostAddress());
            out.writeUTF(String.valueOf(MY_PORT));
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение серверу",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
