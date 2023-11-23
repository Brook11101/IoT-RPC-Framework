package spring.reference;

import serviceregistry.IRegistryService;
import constants.MessageConstant;
import constants.MessageType;
import constants.SerializationType;
import entry.NettyClient;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import protocol.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class ProtocolInvokerProxy implements InvocationHandler {

    IRegistryService registryService;

    public ProtocolInvokerProxy(IRegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("开始远程调用Service Provider的函数");
        //组装参数
        ProtocolConstruction<ProtocolRequest> protocol = new ProtocolConstruction<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        //在经过encoder的时候被content：request被转为字节码，length也被设置了正确的长度
        Header header = new Header(MessageConstant.MAGIC, SerializationType.JSON_Serialization.code(), MessageType.REQUEST.code(), requestId, 0);
        protocol.setHeader(header);
        ProtocolRequest request = new ProtocolRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setContent(request);
        //发送请求
        NettyClient nettyClient = new NettyClient();
        //构建异步数据处理
        ProtocolFuture<ProtocolResponse> future = new ProtocolFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId, future);
        nettyClient.sendRequest(protocol, this.registryService);
        return future.getPromise().get().getData();
    }
}
