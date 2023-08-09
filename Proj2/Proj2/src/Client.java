import java.awt.event.ActionListener;
import java.net.*;
import java.awt.*;

import javax.swing.*;

import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.initFrame();
        client.initConnect();
    }

    private static final int PORT = 2708;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    JTextArea textArea;
    JFrame frame;
    private int id;

    void initFrame() {
        frame = new JFrame();
        frame.setSize(800, 600);
        // 不可缩放
        frame.setResizable(false);
        // 退出时关闭
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 输入+发送
        JTextField textField = new JTextField(50);
        textField.setEditable(true);
        JButton button = new JButton("发送");
        ActionListener listener = (event) -> {
            String message = textField.getText();
            if (message != null && !message.equals("")) {
                try {
                    out.println("客户端 " + id + ": " + message);
                    out.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            textField.setText("");
        };
        textField.addActionListener(listener);
        button.addActionListener(listener);

        JPanel south_panel = new JPanel();
        south_panel.add(textField);
        south_panel.add(button);
        frame.add(south_panel, BorderLayout.SOUTH);

        // 消息显示
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setFocusable(false);
        frame.add(BorderLayout.CENTER, scrollPane);

        frame.setVisible(true);
    }

    public void initConnect() throws IOException {
        try {
            socket = new Socket("localhost", PORT);
            // IO流初始化
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);

            // 新建接收线程
            MsgReceiver receiver = new MsgReceiver();
            Thread receiver_thread = new Thread(receiver);
            receiver_thread.start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "无法启动程序!");
            if (socket != null)
                socket.close();
            System.exit(-1);
        }
    }

    public class MsgReceiver implements Runnable {
        @Override
        public void run() {
            String msg;
            try {
                msg = in.readLine();
                id = Integer.parseInt(msg);
                frame.setTitle("刘思锐 3200102708 Java聊天室 客户端ID:" + id);

                while ((msg = in.readLine()) != null) {
                    textArea.append(msg + "\n");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}