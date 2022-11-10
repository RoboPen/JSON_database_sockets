package server;

import lombok.Data;

@Data
public class Response {
    private String response;
    private Object value;
    private String reason;

}
