package serviceregistry;

public interface IRegistryService {

    /**
     * 注册服务
     * @param serviceInfo
     * @throws Exception
     */
    void register(ServiceInfo serviceInfo) throws Exception;

    /**
     * 取消注册
     * @param serviceInfo
     * @throws Exception
     */
    void unRegister(ServiceInfo serviceInfo) throws Exception;

    /**
     * 动态发现服务
     * @param serviceName
     * @return
     * @throws Exception
     */
    ServiceInfo discovery(String serviceName) throws Exception;
}
