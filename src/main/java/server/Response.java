package server;

public class Response {
    String value;
    String response;
    String reason;

    public String getValue() {
        return value;
    }

    public String getResponse() {
        return response;
    }

    public String getReason() {
        return reason;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
