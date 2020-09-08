/**
 * Created by Ксения on 25.04.2017.
 */
public class User {
    private String address;
    private int port;
    private String name;
    public User(String name, String address, int port){
        this.name=name;
        this.address=address;
        this.port=port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
}
