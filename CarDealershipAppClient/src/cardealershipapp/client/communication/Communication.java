package cardealershipapp.client.communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Miroslav Kološnjaji
 */
public class Communication {
    
    private static Communication instance;
    private final Socket socket;
    private final ObjectInputStream receiver;
    private final ObjectOutputStream sender;
    
    private Communication() throws Exception{
        socket = new Socket("localhost", 9000);        
        this.receiver = new ObjectInputStream(this.socket.getInputStream());
        this.sender = new ObjectOutputStream(this.socket.getOutputStream());   
    }
    
    public static Communication getInstance() throws Exception{
        if(instance == null){
            instance = new Communication();
        }
        return instance;
    }

    public ObjectInputStream getReceiver() {
        return receiver;
    }

    public ObjectOutputStream getSender() {
        return sender;
    }

    public Socket getSocket() {
        return socket;
    }
    
    
    
}
