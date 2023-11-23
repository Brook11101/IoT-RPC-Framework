package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import protocol.Header;
import protocol.ProtocolConstruction;
import serialization.ISerializer;
import serialization.SerializationManager;


/*  netty通信报文每个报文统一header格式如下：
    +----------------------------------------------+
    | 魔数 2byte | 序列化算法 1byte | 请求类型 1byte  |
    +----------------------------------------------+
    | 消息 ID 8byte     |      数据长度 4byte       |
    +----------------------------------------------+
*/
@Slf4j
public class ProtocolEncoder extends MessageToByteEncoder<ProtocolConstruction<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolConstruction<Object> msg, ByteBuf out) throws Exception {
        log.info("==================== 出站消息编码处理 =======================");
        Header header=msg.getHeader();
        out.writeShort(header.getMagic()); //写入魔数
        out.writeByte(header.getSerialType()); //写入序列化类型
        out.writeByte(header.getReqType());//写入请求类型
        out.writeLong(header.getRequestId()); //写入请求id
        ISerializer serializer= SerializationManager.getSerializer(header.getSerialType());
        byte[] data=serializer.serialize(msg.getContent()); //序列化
        header.setLength(data.length);
        out.writeInt(data.length); //写入消息长度
        out.writeBytes(data);
    }
}
