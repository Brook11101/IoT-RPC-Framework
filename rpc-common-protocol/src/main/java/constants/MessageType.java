package constants;

public enum MessageType {
    REQUEST((byte)1),
    RESPONSE((byte)2),
    HEARTBEAT((byte)3);

    private byte code;

    private MessageType(byte code) {
        this.code = code;
    }

    public byte code(){
        return this.code;
    }
    public static MessageType findByCode(int code) {
        for (MessageType msgType : MessageType.values()) {
            if (msgType.code() == code) {
                return msgType;
            }
        }
        return null;
    }
}
