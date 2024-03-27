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
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProtocolInvokerProxy implements InvocationHandler {

    IRegistryService registryService;

    public ProtocolInvokerProxy(IRegistryService registryService) {
        this.registryService = registryService;
    }


    //直接调用get方法，等于说将异步的方式变为了同步的方式


    //@Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        log.info("开始远程调用Service Provider的函数");
//        //组装参数
//        ProtocolConstruction<ProtocolRequest> protocol = new ProtocolConstruction<>();
//        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
//        //在经过encoder的时候被content：request被转为字节码，length也被设置了正确的长度
//        Header header = new Header(MessageConstant.MAGIC, SerializationType.JSON_Serialization.code(), MessageType.REQUEST.code(), requestId, 0);
//        protocol.setHeader(header);
//        ProtocolRequest request = new ProtocolRequest();
//        request.setClassName(method.getDeclaringClass().getName());
//        request.setMethodName(method.getName());
//        request.setParameterTypes(method.getParameterTypes());
//        request.setParams(args);
//        protocol.setContent(request);
//        //发送请求
//        NettyClient nettyClient = new NettyClient();
//        //构建异步数据处理
//        ProtocolFuture<ProtocolResponse> future = new ProtocolFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
//        RequestHolder.REQUEST_MAP.put(requestId, future);
//        nettyClient.sendRequest(protocol, this.registryService);
//        return future.getPromise().get().getData();
//    }

    //可以使用监听器或者CompletableFuture+addListener的方法，对结果的处理以thenApply的方式采取异步回调
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("开始远程调用Service Provider的函数");
        // 组装参数
        ProtocolConstruction<ProtocolRequest> protocol = new ProtocolConstruction<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        Header header = new Header(MessageConstant.MAGIC, SerializationType.JSON_Serialization.code(), MessageType.REQUEST.code(), requestId, 0);
        protocol.setHeader(header);
        ProtocolRequest request = new ProtocolRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setContent(request);

        // 发送请求
        NettyClient nettyClient = new NettyClient();
        ProtocolFuture<ProtocolResponse> future = new ProtocolFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
        RequestHolder.REQUEST_MAP.put(requestId, future);
        nettyClient.sendRequest(protocol, this.registryService);

        // 构建异步数据处理，添加监听器等待异步操作完成
        CompletableFuture<ProtocolResponse> completableFuture = new CompletableFuture<>();
        future.getPromise().addListener(f -> {
            if (f.isSuccess()) {
                // 异步操作成功，解析响应
                completableFuture.complete(future.getPromise().getNow());
            } else {
                // 异步操作失败，处理异常
                completableFuture.completeExceptionally(future.getPromise().cause());
            }
        });

        // 阻塞等待异步结果完成，并返回结果。这里我们使用CompletableFuture.get()来阻塞和等待结果。
        // 注意：实际生产环境中应尽量避免在主线程中直接调用get()，以避免阻塞主线程。
        // 可以考虑使用CompletableFuture的thenApply、thenAccept等非阻塞方法来处理异步结果。
        return completableFuture.thenApply(ProtocolResponse::getData).get();
    }

}
