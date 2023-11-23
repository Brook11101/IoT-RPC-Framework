package serviceregistry;

import lombok.Data;

@Data
public class ServiceInfo {
    private String serviceName;
    private String serviceAddress;
    private int servicePort;
}
