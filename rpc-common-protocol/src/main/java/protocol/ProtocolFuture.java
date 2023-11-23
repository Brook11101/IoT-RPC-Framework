package protocol;

import io.netty.util.concurrent.Promise;
import lombok.Data;

@Data
public class ProtocolFuture<T> {
    // Netty通过 Promise对 Future进行扩展,用于设置IO操作的结果
    private Promise<T> promise;
    public ProtocolFuture(Promise<T> promise) {
        this.promise = promise;
    }
}