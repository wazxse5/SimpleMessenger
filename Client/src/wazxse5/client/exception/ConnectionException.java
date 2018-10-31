package wazxse5.client.exception;

public class ConnectionException extends Exception {
    private String info;

    public ConnectionException(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
