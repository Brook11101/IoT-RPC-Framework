package protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProtocolRequest implements Serializable {
    private String className;
    private String methodName;
    //使用数组，因为不止一个参数
    private Object[] params;
    private Class<?>[] parameterTypes;
}
