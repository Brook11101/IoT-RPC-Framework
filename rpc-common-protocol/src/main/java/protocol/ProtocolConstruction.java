package protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProtocolConstruction<T> implements Serializable {
    private Header header;
    private T content;
}
