import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    private static final int PORT = 2708;
    private static final LinkedList<PrintWriter> writer_lst = new LinkedList<>();

    public synchronized void start() {
        try (ServerSocket s = new ServerSocket(PORT)) {
            System.out.println("Server started");

            int client_cnt = 0;
            // 死循环处理连接请求
            while (true) {
                Socket client_sock = s.accept();
                ServerClient client = new ServerClient(client_sock);
                new Thread(client).start();

                PrintWriter writer = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        client_sock.getOutputStream())), true);
                writer.println(client_cnt++);
                writer.flush();
                writer_lst.add(writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String msg) {
        try {
            for (PrintWriter writer : writer_lst) {
                writer.println(msg);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ServerClient implements Runnable {
        Socket socket;
        BufferedReader in;

        public ServerClient(Socket s) {
            this.socket = s;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void run() {
            String msg;
            try {
                while ((msg = in.readLine()) != null) {
                    broadcast(msg);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}