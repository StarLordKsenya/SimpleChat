import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Ксения on 29.04.2017.
 */
public class PrivateDialog extends JFrame{
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 600;
    private static final int FRAME_MINIMUM_HEIGHT = 500;
    private static final int INCOMING_AREA_DEFAULT_ROWS = 10;
    private static final int OUTGOING_AREA_DEFAULT_ROWS = 5;
    private static final int SMALL_GAP = 5;
    private static final int MEDIUM_GAP = 10;
    private static final int LARGE_GAP = 15;
    private JLabel textFieldFrom;
    private JLabel textFieldTo;
    private final JTextArea textAreaIncoming;
    private final JTextArea textAreaOutgoing;
    private InstantMessenger messenger;
    private JScrollPane scrollPaneIncoming;
    private JLabel labelFrom;
    private JLabel labelTo;
    private JScrollPane scrollPaneOutgoing;
    private JButton sendButton;

    private Font fontForName = new Font("Comic Sans MS", 0, 15);
    private Color colorForName =new Color(0x0000B4);
    private Color colorForGround =new Color(0xE6E6FF);

    public JTextArea getTextAreaIncoming() {
        return textAreaIncoming;
    }

    public JTextArea getTextAreaOutgoing() {
        return textAreaOutgoing;
    }

    public PrivateDialog() {
        super(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));

        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2,
                (kit.getScreenSize().height - getHeight()) / 2);

        textAreaIncoming = new JTextArea(INCOMING_AREA_DEFAULT_ROWS, 0);
        textAreaIncoming.setEditable(false);

        scrollPaneIncoming =
                new JScrollPane(textAreaIncoming);

        labelFrom = new JLabel("Подпись");
        labelTo = new JLabel("Получатель");

        textAreaOutgoing = new JTextArea(OUTGOING_AREA_DEFAULT_ROWS, 0);

        scrollPaneOutgoing =
                new JScrollPane(textAreaOutgoing);

        sendButton = new JButton("Отправить");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String message = textAreaOutgoing.getText();
                final String destinationName = textFieldTo.getText();

                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(PrivateDialog.this,
                            "Введите текст сообщения", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                messenger.sendMessage(destinationName, message, true);
            }
        });


    }

    public void drawPrivateDialog(MainFrame This) {
// Компоновка элементов панели "Сообщение"
        System.out.println(55);
        messenger = This.getMessenger();
        textFieldFrom = new JLabel(This.getName());
        textFieldFrom.setForeground(colorForName);
        textFieldFrom.setFont(fontForName);

        textFieldTo = new JLabel(This.getNameTo());
        textFieldTo.setFont(fontForName);
        textFieldTo.setForeground(colorForName);
// Компоновка элементов фрейма
        getContentPane().setBackground(colorForGround);
        final GroupLayout layout1 = new GroupLayout(getContentPane());
        setLayout(layout1);
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup()
                        .addGroup(layout1.createSequentialGroup()
                                .addComponent(labelFrom)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldFrom)
                                .addGap(LARGE_GAP)
                                .addComponent(labelTo)
                                .addGap(SMALL_GAP)
                                .addComponent(textFieldTo))
                        .addComponent(scrollPaneIncoming)
                        .addComponent(scrollPaneOutgoing)
                        .addComponent(sendButton))
                .addContainerGap());
        layout1.setVerticalGroup(layout1.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(labelFrom)
                        .addComponent(textFieldFrom)
                        .addComponent(labelTo)
                        .addComponent(textFieldTo))
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneIncoming)
                .addGap(MEDIUM_GAP)
                .addComponent(scrollPaneOutgoing)
                .addGap(MEDIUM_GAP)
                .addComponent(sendButton)
                .addContainerGap());
        setVisible(true);
// Создание и запуск потока-обработчика запросов

    }
}




