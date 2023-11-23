package spring.reference;


import lombok.Data;

@Data
public class ProtocolClientProperties {
    private String serviceAddress="192.168.50.116";

    private int servicePort=20880;

    private byte registryType;

    private String registryAddress;
}
