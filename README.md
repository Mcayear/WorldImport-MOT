## WorldImport-MOT

需配合 WorldExport 使用，使用ws客户端连接到由 WorldExport 创建的服务端，并发送请求来获取区块数据。

### PacketID

```java
    byte HELLO_PACKET = 0x01; // 无实际作用
    byte WORLD_LIST_RES_PACKET = 0x02;// 获取服务器所有的世界名字
    byte WORLD_INFO_REQUEST_PACKET = 0X03;// 获取世界的 region 列表
    byte WORLD_INFO_RES_PACKET = 0X04;// 响应世界 region 列表
    byte REGION_REQUEST_PACKET = 0X05;// 获取某一 region 的数据
    byte REGION_RES_PACKET = 0X06;// 响应某一 region 的数据，结束时发送的包
    byte REGION_RES_PATCH_PACKET = 0X08;// 响应某一 region 的数据，方块数据的实际负载
```