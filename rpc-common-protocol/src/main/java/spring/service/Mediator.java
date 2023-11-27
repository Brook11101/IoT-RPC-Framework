package spring.service;

import protocol.ProtocolRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Mediator {
    public static Map<String,BeanMethod> beanMethodMap=new ConcurrentHashMap<>();

    private volatile static Mediator instance=null;

    private Mediator(){
    }

    //这里使用了双重锁定检查，用于在创建单例模式时防止被重复创建
    public static Mediator getInstance(){
        if(instance==null){
            synchronized (Mediator.class){
                if(instance==null){
                    instance=new Mediator();
                }
            }
        }
        return instance;
    }
    //mediator是给server端反射调用函数使用的
    public Object processor(ProtocolRequest rpcRequest){
        String key=rpcRequest.getClassName()+"."+rpcRequest.getMethodName();
        BeanMethod beanMethod=beanMethodMap.get(key);
        if(beanMethod==null){
            return null;
        }
        Object bean=beanMethod.getBean();
        Method method=beanMethod.getMethod();
        try {
            System.out.println(Arrays.toString(rpcRequest.getParams()));
            return method.invoke(bean,rpcRequest.getParams());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
