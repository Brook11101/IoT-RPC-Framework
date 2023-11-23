package constants;

public enum SerializationType {
    JSON_Serialization((byte) 0), JAVA_SERIALSerialization((byte) 1);

    private byte code;

    SerializationType(byte code) {
        this.code = code;
    }

    public byte code() {
        return this.code;
    }
}
