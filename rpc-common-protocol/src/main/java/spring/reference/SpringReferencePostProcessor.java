package spring.reference;

import constants.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import spring.annotation.RemoteCallReference;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpringReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private ApplicationContext context;
    private ClassLoader classLoader;
    private ProtocolClientProperties clientProperties;

    public SpringReferencePostProcessor(ProtocolClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    //保存发布的引用bean信息
    private final Map<String, BeanDefinition> rpcRefBeanDefinitions=new ConcurrentHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionname:beanFactory.getBeanDefinitionNames()){
            //遍历bean定义，然后获取到加载的bean，遍历这些bean中的字段，是否携带RemoteCallReference注解
            //如果有，则需要构建一个动态代理实现
            BeanDefinition beanDefinition=beanFactory.getBeanDefinition(beanDefinitionname);
            String beanClassName=beanDefinition.getBeanClassName();
            if(beanClassName!=null){
                //这里使用反射实例化这个类，从而用于parseRpcReference的判断，判断是否存在RemoteCallReference这个注解
                Class<?> clazz= ClassUtils.resolveClassName(beanClassName,this.classLoader);
                ReflectionUtils.doWithFields(clazz,this::parseRpcReference);
            }
        }
        //将RemoteCallReference注解的bean，构建一个动态代理对象
        BeanDefinitionRegistry registry=(BeanDefinitionRegistry)beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName,beanDefinition)->{
            if(context.containsBean(beanName)){
                log.warn("SpringContext already register bean {}",beanName);
                return;
            }
            registry.registerBeanDefinition(beanName,beanDefinition);
            log.info("注册了bean方法 {} 成功.",beanName);
        });
    }
    private void parseRpcReference(Field field){
        //这段话就是判断这个bean class是否有RemoteCallReference注解
        RemoteCallReference remoteCallReference= AnnotationUtils.getAnnotation(field,RemoteCallReference.class);
        //说明这个bean存在这个注解
        if(remoteCallReference!=null) {
            BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition(SpringReferenceBean.class);
            log.info("Spring 测试：BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition(SpringRpcReferenceBean.class) {}",SpringReferenceBean.class);
            builder.setInitMethodName(MessageConstant.INIT_METHOD_NAME);
            log.info("Spring 测试：setInitMethodName(RpcConstant.INIT_METHOD_NAME) {}",MessageConstant.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass",field.getType());
            log.info("Spring 测试：addPropertyValue(\"interfaceClass\",field.getType()); {}",field.getType());
            builder.addPropertyValue("registryType",clientProperties.getRegistryType());
            builder.addPropertyValue("registryAddress",clientProperties.getRegistryAddress());
            BeanDefinition beanDefinition=builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(),beanDefinition);
            log.info("Spring 对 Client请求方法的Bean对象生成成功");
        }
    }
}
