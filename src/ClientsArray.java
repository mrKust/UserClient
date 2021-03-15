import java.util.HashMap;
import java.util.Map;

public  class ClientsArray {
    private Map<String, WorkWithClient> clients = null;

    public ClientsArray() {
        this.clients = new HashMap<>();
    }

    public void addNewClient (String name, WorkWithClient thread){
        clients.put(name, thread);
        thread.update(this);
    }

    public void sendAll (String message){
        for(Map.Entry<String, WorkWithClient> entry : clients.entrySet()) {
            String tmp = entry.getKey();
            if (entry.getKey().equals(entry))
                continue;
            entry.getValue().sendMessage(message);
        }
    }

    public void sendUser (String user, String message) {
        clients.get(user).sendMessage(message);
    }


}