package spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ReferenceAutoConfiguration implements EnvironmentAware {

    @Bean
    public SpringReferencePostProcessor postProcessor(){
        String address=environment.getProperty("serviceAddress");
        int port=Integer.parseInt(environment.getProperty("servicePort"));
        ProtocolClientProperties rc=new ProtocolClientProperties();

        //有注册中心，client无需知道provider service的地址
        //rc.setServiceAddress(address);
        //rc.setServicePort(port);

        rc.setRegistryType(Byte.parseByte(environment.getProperty("registryType")));
        rc.setRegistryAddress(environment.getProperty("registryAddress"));
        return new SpringReferencePostProcessor(rc);
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}
