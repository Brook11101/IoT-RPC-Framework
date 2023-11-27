# IoT-RPC-Framework

``` 
rpc-common-interface提供了通用接口，即consumer需要调用的函数与provider需要提供的函数统一接口
rpc-common-registry提供了zookeeper实现注册中心，用于provider注册服务，consumer寻找服务
rpc-common-protocol提供了基于netty的双方通信渠道，以及基于Spring注解的Bean封装与发布（对Consumer需要远程调用的接口方法封装为bean，对Provider需要提供的函数注解进行注册）
rpc-service-consumer通过restcontroller获得用户请求，并调用远程方法
rpc-service-provider收到请求后调用本地方法，并返回执行结果


