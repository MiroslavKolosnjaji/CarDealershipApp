package cardealershipapp.server.communication;

import cardealershipapp.server.database.DataBase;
import cardealershipapp.server.session.ServerSession;
import cardealershipapp.server.thread.ProcessClientRequests;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            DataBase.getInstance().connectToDb();
            while (!isInterrupted()) {
                System.out.println("Waiting for connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected!");
                handleClient(socket);
            }
            serverSocket.close();
        } catch (SocketException soe) {
            System.out.println("Socket closed!");
            try {
                DataBase.getInstance().disconnectFromDb();
                System.out.println("Disconnected from the db!");
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stopServer() {
        try {
            closeAllConnections();
            interrupt();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void handleClient(Socket socket) {
        ProcessClientRequests processClientRequests = new ProcessClientRequests(socket);
        processClientRequests.start();
    }

    private void closeAllConnections() {
        List<ProcessClientRequests> allConnections = ServerSession.getInstance().getConnections();
        if (allConnections != null && !allConnections.isEmpty()) {

            for (ProcessClientRequests pcr : allConnections) {
                try {
                    pcr.interrupt();
                    pcr.closeClient();
                } catch (IOException ex) {
                    System.out.println("Clossing connection: " + pcr.getName());
                }
            }
            ServerSession.getInstance().getConnectedClients().clear();
        }
    }

}
