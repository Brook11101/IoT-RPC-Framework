package serialization;

import java.util.concurrent.ConcurrentHashMap;

public class SerializationManager {

    private final static ConcurrentHashMap<Byte, ISerializer> serializers=new ConcurrentHashMap<Byte, ISerializer>();

    static {
        ISerializer jsonSerializer=new JsonSerializer();
        ISerializer javaSerializer=new JavaSerializer();
        serializers.put(jsonSerializer.getType(),jsonSerializer);
        serializers.put(javaSerializer.getType(),javaSerializer);
    }

    public static ISerializer getSerializer(byte key){
        ISerializer serializer=serializers.get(key);
        if(serializer==null){
            return new JavaSerializer();
        }
        return serializer;
    }
}

