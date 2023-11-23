package spring.service;

import serviceregistry.IRegistryService;
import serviceregistry.ServiceInfo;
import entry.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import spring.annotation.RemoteServiceReference;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class SpringProviderBean implements InitializingBean, BeanPostProcessor {

    private final int serverPort;
    private final String serverAddress;
    private final IRegistryService registryService; //修改部分,增加注册中心实现
    public SpringProviderBean(int serverPort,IRegistryService registryService) throws UnknownHostException {
        this.serverPort = serverPort;
        InetAddress address=InetAddress.getLocalHost();
        this.serverAddress=address.getHostAddress();
        this.registryService=registryService; //修改部分,增加注册中心实现
    }

    @Override
    //先开启server，然后向zookeeper注册。这个函数会在bean初始化时被自动调用
    public void afterPropertiesSet() throws Exception {
        log.info("Netty Server 被部署在 host {}, port {}",this.serverAddress,this.serverPort);
        new Thread(()->{
            try {
                new NettyServer(this.serverAddress,this.serverPort).startNettyServer();
            } catch (Exception e) {
                log.error("启动Netty Server出错,",e);
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean.getClass().isAnnotationPresent(RemoteServiceReference.class)){
            //针对存在该注解的服务进行发布
            Method[] methods=bean.getClass().getDeclaredMethods();
            for(Method method: methods){
                String serviceName=bean.getClass().getInterfaces()[0].getName();
                String key=serviceName+"."+method.getName();
                BeanMethod beanMethod=new BeanMethod();
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                Mediator.beanMethodMap.put(key,beanMethod);
                try {
                    ServiceInfo serviceInfo = new ServiceInfo();
                    serviceInfo.setServiceAddress(this.serverAddress);
                    serviceInfo.setServicePort(this.serverPort);
                    serviceInfo.setServiceName(serviceName);
                    registryService.register(serviceInfo);
                }catch (Exception e){
                    log.error("注册内容为 {} 的服务失败",serviceName,e);
                }
            }
        }
        return bean;
    }
}
