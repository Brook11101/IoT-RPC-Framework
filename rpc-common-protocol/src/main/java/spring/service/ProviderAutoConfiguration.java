package spring.service;

import serviceregistry.IRegistryService;
import serviceregistry.RegistryFactory;
import serviceregistry.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(ProtocolServerProperties.class)
public class ProviderAutoConfiguration {

    @Bean
    public SpringProviderBean rpcProviderBean(ProtocolServerProperties rpcServerProperties) throws UnknownHostException {
        //添加注册中心
        IRegistryService registryService= RegistryFactory.createRegistryService(rpcServerProperties.getRegistryAddress(), RegistryType.findByCode(rpcServerProperties.getRegisterType()));
        return new SpringProviderBean(rpcServerProperties.getServicePort(),registryService);
    }
}
