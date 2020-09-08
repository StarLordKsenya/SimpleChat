import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Ксения on 25.04.2017.
 */
public class Server {

    private static final int SERVER_PORT = 4567;
    private ArrayList<User> users = new ArrayList<>(10);
    MainFrame This;

    private void addUser(String name, String address, int port){
        User user = new User(name, address, port);
        synchronized (users){
            users.add(user);
        }

        if (users.size()>1){
            for(int i=0; i<users.size()-1;i++){
                try{
                    final Socket socket = new Socket(users.get(i).getAddress(), users.get(i).getPort());
                    final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("Letter from server");
                    out.writeUTF(user.getName());
                    socket.close();
                }catch (IOException e){}
            }
        }
    }
    private void removeUser(User user){
        synchronized (users){
            users.remove(user);
        }
    }
    private User findUserByName(String name){
        for(User user: users) {
            if(user.getName().equals(name)) return user;
        }
        return null;
    }
    private User findUserByAddress(String address){
        for(User user: users) {
            if(user.getAddress().equals(address)) return user;
        }
        return null;
    }

    public Server(MainFrame This){this.This=This;
    openData();

    startServer();}
    private void openData() {
        try {
            File file = new File("Data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            reader.read();
            line = reader.readLine();
            while (line != null){
                String name = line;
                line = reader.readLine();
                String address = line;
                line = reader.readLine();
                Integer port = new Integer(line);
                synchronized (users){
                    users.add(new User(name, address, port));
                }
                line = reader.readLine();
            }
            reader.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                    while (!Thread.interrupted()) {
                        Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        final String destinationName = in.readUTF();
                        if (destinationName.equals("Letter for server")) {
                            addUser(in.readUTF(), in.readUTF(), new Integer(in.readUTF()));
                            socket.close();
                        } else {
                            final String message = in.readUTF();
                            socket.close();
                            User userTo = findUserByName(destinationName);
                            User userFrom = findUserByAddress(((InetSocketAddress) socket.getRemoteSocketAddress())
                                    .getAddress().getHostAddress());
                            send(userTo,userFrom,message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(This,
                            "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }catch (NullPointerException e){
                    JOptionPane.showMessageDialog(This,
                            "Не найден адресат", "Ошибка сервера",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        }).start();
    }
    public void send(User userTo, User userFrom, String message){
        try {
            System.out.println(userTo.getAddress()+userTo.getPort());
            final Socket socketTo = new Socket(userTo.getAddress(),userTo.getPort());
            final DataOutputStream out = new DataOutputStream(socketTo.getOutputStream());
            out.writeUTF(userFrom.getName()); // Записываем в поток имя
            out.writeUTF(message); // Записываем в поток сообщение
            socketTo.close(); // Закрываем сокет
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение: узел-адресат не найден",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(This,
                    "Не удалось отправить сообщение", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
