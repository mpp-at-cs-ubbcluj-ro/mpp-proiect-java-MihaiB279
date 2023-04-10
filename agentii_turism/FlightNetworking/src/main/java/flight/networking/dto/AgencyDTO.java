package flight.networking.dto;

import java.io.Serializable;

public class AgencyDTO implements Serializable {
    private String name;
    private String password;
    public String id;

    public AgencyDTO(String username, String name, String password) {
        this.id = username;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
