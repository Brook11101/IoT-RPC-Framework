package entry;

import handler.RpcServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer{
    private String serverAddress; //地址
    private int serverPort; //端口

    public NettyServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startNettyServer() throws Exception {
        log.info("Service Provider Netty客户端开始启动");
        EventLoopGroup bossGroup=new NioEventLoopGroup();  //处理连接请求
        EventLoopGroup workGroup=new NioEventLoopGroup();  //处理连接上的io操作
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInitializer());
            ChannelFuture channelFuture = bootstrap.bind(this.serverAddress, this.serverPort).sync();
            log.info("Service Provider Netty客户端启动 Port:{}", this.serverPort);
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("Service Provider Netty客户端启动错误",e);
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

