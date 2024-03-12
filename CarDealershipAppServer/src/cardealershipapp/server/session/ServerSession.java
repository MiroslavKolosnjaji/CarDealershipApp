package cardealershipapp.server.session;

import cardealershipapp.common.exception.InputValidationException;
import cardealershipapp.server.thread.ProcessClientRequests;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class ServerSession {

    private static final ServerSession instance = new ServerSession();
    private final List<ProcessClientRequests> connectedClients;
    private final List<ProcessClientRequests> connections;

    private ServerSession() {
        this.connectedClients = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public static ServerSession getInstance() {
        return instance;
    }

    public void addConnectedClient(ProcessClientRequests client) throws Exception {
        if (connectedClients != null && !connectedClients.isEmpty()) {
            
            for (ProcessClientRequests connectedClient : connectedClients) {
                if (connectedClient.getConnectedUser().getEmail().equals(client.getConnectedUser().getEmail())) {
                    throw new InputValidationException("This user is already connected!");
                }
            }
        }

        connectedClients.add(client);
    }

    public List<ProcessClientRequests> getConnectedClients() {
        return connectedClients;
    }

    public List<ProcessClientRequests> getConnections() {
        return connections;
    }

    public void setConnection(ProcessClientRequests connection) {
        connections.add(connection);
    }
    
    

}
