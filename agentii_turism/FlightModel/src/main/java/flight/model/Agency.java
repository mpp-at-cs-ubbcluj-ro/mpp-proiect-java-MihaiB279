package flight.model;

public class Agency extends Entity<String> {

    private String name;
    private String password;

    public Agency(){};

    public Agency(String username, String name, String password) {
        this.setId(username);
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
}
