package client;

import lombok.Data;

@Data
public class Request {
    private String type;
    private Object key;
    private Object value;

}