package cardealershipapp.client.ui.response;


import cardealershipapp.client.communication.Communication;
import cardealershipapp.common.transfer.Operation;
import cardealershipapp.common.transfer.Request;
import cardealershipapp.common.transfer.Response;

/**
 * @author Miroslav Kološnjaji
 */
public interface Responsive {

    default Response getResponse(Operation operation, Object argument) throws Exception {
        Request request = new Request(operation, argument);
        Communication.getInstance().getSender().writeObject(request);
       Response response = (Response) Communication.getInstance().getReceiver().readObject();

        if (response.getException() != null) {
            throw response.getException();
        }
        return response;
    }

}
