package spring.reference;


import lombok.Data;

@Data
public class ProtocolClientProperties {
    //前两个属性其实可以删除，因为有注册中心了
    private String serviceAddress;
    private int servicePort;

    private byte registryType;

    private String registryAddress;
}
