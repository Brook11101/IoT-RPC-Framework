package loadbalance;

import serviceregistry.ServiceInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected ServiceInstance<ServiceInfo> doSelect(List<ServiceInstance<ServiceInfo>> servers) {
        int length=servers.size();
        Random random=new Random();
        return servers.get(random.nextInt(length));
    }
}
