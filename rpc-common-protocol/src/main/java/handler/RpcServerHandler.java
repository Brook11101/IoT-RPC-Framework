package handler;

import constants.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.Header;
import protocol.ProtocolConstruction;
import protocol.ProtocolRequest;
import protocol.ProtocolResponse;
import spring.service.Mediator;

public class RpcServerHandler extends SimpleChannelInboundHandler<ProtocolConstruction<ProtocolRequest>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolConstruction<ProtocolRequest> msg) throws Exception {
        ProtocolConstruction resProtocol=new ProtocolConstruction<>();
        Header header=msg.getHeader();
        //复用并更改header里面的请求/回复信息
        header.setReqType(MessageType.RESPONSE.code());

        //这里通过java的反射机制实现了结果
        Object result= Mediator.getInstance().processor(msg.getContent());
        resProtocol.setHeader(header);
        ProtocolResponse response=new ProtocolResponse();
        response.setData(result);
        response.setMsg("服务器端成功调用了客户端请求的方法");
        resProtocol.setContent(response);

        //在最后一个handler将返回的消息发送回去，开始掉头出栈
        ctx.writeAndFlush(resProtocol);
    }

//    @Deprecated
//    private Object invoke(ProtocolRequest request){
//        try {
//            Class<?> clazz=Class.forName(request.getClassName());
//            Object bean= SpringBeansManager.getBean(clazz); //获取实例对象
//            Method declaredMethod=clazz.getDeclaredMethod(request.getMethodName(),request.getParameterTypes());
//            return declaredMethod.invoke(bean,request.getParams());
//        } catch (ClassNotFoundException | NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

