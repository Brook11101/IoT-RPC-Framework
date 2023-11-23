package protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RequestHolder {

    public static final AtomicLong REQUEST_ID=new AtomicLong();

    public static final Map<Long,ProtocolFuture> REQUEST_MAP=new ConcurrentHashMap<>();
}
