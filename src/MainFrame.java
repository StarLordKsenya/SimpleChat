import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Created by Ксения on 25.04.2017.
 */

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 600;
    private static final int FRAME_MINIMUM_HEIGHT = 500;
    private static final int FROM_FIELD_DEFAULT_COLUMNS = 15;
    private static final int TO_FIELD_DEFAULT_COLUMNS = 15;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;
    private final JLabel textFieldFrom;
    private final JLabel textFieldTo;
    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    private  String name=null;
    private ArrayList<String> namesOfUsers= new ArrayList<>(10);
    private JPanel messagePanel;

    private InstantMessenger messenger;
    private JScrollPane scrollPaneIncoming;
    private JLabel labelFrom;
    private JLabel labelTo;
    private JScrollPane scrollPaneOutgoing;
    private JButton sendButton;
    private JButton chooseUserButton;
    private Image jupiter= Toolkit.getDefaultToolkit().getImage("юпитер");
    private Image sirius= Toolkit.getDefaultToolkit().getImage("сириус");
    private Image zafira= Toolkit.getDefaultToolkit().getImage("зафира");
    private Image you1= Toolkit.getDefaultToolkit().getImage("звёздный лорд");
    private Image you2= Toolkit.getDefaultToolkit().getImage("звёздный лорд1");
    private JLabel imageFrom;
    private JLabel imageTo;

    private Font fontForName = new Font("Comic Sans MS", 0, 15);
    private Color colorForName =new Color(0x0000B4);
    private Color colorForGround =new Color(0xE6E6FF);

    private PrivateDialog privateDialog;

    public JTextArea getTextAreaIncoming() {
        return this.textAreaIncoming;
    }

    public JTextArea getTextAreaOutgoing() {
        return textAreaOutgoing;
    }

    @Override
    public String getName() {
        return name;
    }
    public String getNameTo(){return textFieldTo.getText();}

    public void setName(String name, MainFrame This){
        boolean isInMassive=false;

            for(String user : This.namesOfUsers){
                if(user.equals(name)){isInMassive = true; break;}
            }
            if(!isInMassive) {This.namesOfUsers.add(name);This.drawMainFrame();}

    }

    public InstantMessenger getMessenger() {
        return messenger;
    }

    public PrivateDialog getPrivateDialog() {
        return privateDialog;
    }

    public MainFrame(){
        super(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        privateDialog=new PrivateDialog();


// Центрирование окна
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2,
                (kit.getScreenSize().height - getHeight()) / 2);
// Текстовая область для отображения полученных сообщений
        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);
// Контейнер, обеспечивающий прокрутку текстовой области
        scrollPaneIncoming =
                new JScrollPane(textAreaIncoming);
// Подписи полей
        labelFrom = new JLabel("Подпись");

        labelTo = new JLabel("Получатель");
// Поля ввода имени пользователя и адреса получателя
        textFieldFrom = new JLabel("Ваш логин");
        textFieldFrom.setForeground(colorForName);
        textFieldFrom.setFont(fontForName);

        textFieldTo = new JLabel("Имя адресата");
        textFieldTo.setFont(fontForName);
        textFieldTo.setForeground(colorForName);
// Текстовая область для ввода сообщения
        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);
// Контейнер, обеспечивающий прокрутку текстовой области
        scrollPaneOutgoing =
                new JScrollPane(textAreaOutgoing);
// Панель ввода сообщения
        messagePanel = new JPanel();
        messagePanel.setBorder(
                BorderFactory.createTitledBorder("Сообщение"));
// Кнопка отправки сообщения
        sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String message = textAreaOutgoing.getText();
                final String destinationName = textFieldTo.getText();

                if (destinationName.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Введите адрес узла-получателя", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Введите текст сообщения", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                messenger.sendMessage(destinationName,message,false);
            }
        });
        chooseUserButton=new JButton("Выбрать адресата");
        chooseUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) JOptionPane.showInputDialog(MainFrame.this, "Выберете получателя", "Выбирайте", JOptionPane.PLAIN_MESSAGE, null, namesOfUsers.toArray(), "");
                int q = JOptionPane.showConfirmDialog(MainFrame.this, "Хотители создать с ним приватный диалог", null, JOptionPane.YES_NO_OPTION);
                textFieldTo.setText(name);
                if (q == JOptionPane.YES_OPTION) {
                    privateDialog.drawPrivateDialog(MainFrame.this);
                }

            }
        });
        messenger=new InstantMessenger(this);
        try {
            File file = new File("Data.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.read();
            String line = null;
            while ((line = reader.readLine()) != null){
                String name = line;
                reader.readLine();
                reader.readLine();
                setName(name, MainFrame.this);
            }
            reader.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("main"+namesOfUsers);
        //drawMainFrame();

    }
    public void registration(){
        int i=0;
        System.out.println("reg"+namesOfUsers);
        try{
        while (true){

            name=null;
            name=JOptionPane.showInputDialog(MainFrame.this, "Введите логин для регестрации");
            if( name==null){name=""; System.exit(0);}
            System.out.println(name);
            if(name.equals("")) {
                if (i < 3) {i++;
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Введите логин, пожалуйста", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {JOptionPane.showMessageDialog(MainFrame.this,
                        "С этого момента Вы Starman, нравится Вам это или нет", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                name = "Starman";}
            } else
                for(String namee:namesOfUsers){
                if(namee.equals(name)) {JOptionPane.showMessageDialog(MainFrame.this,
                        "Это имя уже занято, попробуйте другое", "Ошибка",
                        JOptionPane.ERROR_MESSAGE); name=""; break;}
            }

            if(!name.equals("") ) {textFieldFrom.setText(name); textFieldFrom.setVisible(true); messenger.sendMessageForServer(name); drawMainFrame(); break;}

        }}catch (NullPointerException e){}



    }
    public void drawMainFrame() {
// Компоновка элементов панели "Сообщение"
        System.out.println("dr"+namesOfUsers);
        final GroupLayout layout2 = new GroupLayout(messagePanel);
        messagePanel.setBackground(colorForGround);
        messagePanel.setLayout(layout2);
        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout2.createSequentialGroup()
                                .addComponent(labelFrom)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldFrom)
                                .addGap(LARGE_GAP)
                                .addComponent(labelTo)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldTo)
                                .addGap(SMALL_GAP)
                                .addComponent(chooseUserButton))
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());
        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addContainerGap().addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelFrom)
                        .addComponent(textFieldFrom)
                        .addComponent(labelTo)
                        .addComponent(textFieldTo)
                        .addComponent(chooseUserButton))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());
// Компоновка элементов фрейма
        getContentPane().setBackground(colorForGround);
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addComponent(scrollPaneIncoming)
                        .addComponent(messagePanel))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(messagePanel)
                .addContainerGap());
// Создание и запуск потока-обработчика запросов

    }

    public static void main(String[] args) { //зачем тут поток?
        SwingUtilities.invokeLater(() -> {
            final MainFrame frame = new MainFrame();
            Server server = new Server(frame);
            frame.setVisible(true);
            frame.registration();
            if(frame.getName()==null) return;
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        });
    }
}