package serviceregistry;

import zookeeper.ZookeeperRegistryService;

public class RegistryFactory {

    public static IRegistryService createRegistryService(String address,RegistryType registryType){
        IRegistryService registryService=null;
        try {
            switch (registryType) {
                case ZOOKEEPER:
                    registryService = new ZookeeperRegistryService(address);
                    break;
                case EUREKA:
                    //TODO
                    break;
                default:
                    registryService = new ZookeeperRegistryService(address);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return registryService;
    }
}
