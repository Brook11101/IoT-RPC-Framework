package entry;

import serviceregistry.IRegistryService;
import serviceregistry.ServiceInfo;
import handler.RpcClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import protocol.ProtocolConstruction;
import protocol.ProtocolRequest;

@Slf4j
public class NettyClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
    public NettyClient(){
        log.info("begin init NettyClient");
        bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }

    public void sendRequest(ProtocolConstruction<ProtocolRequest> protocol, IRegistryService registryService) throws Exception {

        //这里客户端通过在zookeeper上找到需要的class与method的service provider的地址后，consumer再根据地址与provider通信
        ServiceInfo serviceInfo=registryService.discovery(protocol.getContent().getClassName());
        ChannelFuture future=bootstrap.connect(serviceInfo.getServiceAddress(),serviceInfo.getServicePort()).sync();
        future.addListener(listener->{
            if(future.isSuccess()){
                log.info("根据注册中心获取地址的Service Provider {} 连接成功",serviceInfo.getServiceAddress());
            }else{
                log.error("根据注册中心获取地址的Service Provider {} 连接失败",serviceInfo.getServiceAddress());
                future.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        log.info("客户端开始发起远程调用请求");
        //会依次经过很多handler
        future.channel().writeAndFlush(protocol);
    }
}

