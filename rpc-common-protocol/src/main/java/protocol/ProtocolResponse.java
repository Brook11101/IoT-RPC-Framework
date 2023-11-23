package protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProtocolResponse implements Serializable {
    private Object data;
    private String msg;
}
