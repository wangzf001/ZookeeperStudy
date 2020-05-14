###经典的CAP/BASE理论
CAP  
```
C（一致性 Consistency）: 所有节点上的数据，时刻保持一致
A 可用性（Availability）：每个请求都能够收到一个响应，无论响应成功或者失败
P 分区容错 （Partition-tolerance）：表示系统出现脑裂以后，可能导致某些server与集群中的其他机器失去联系
CP  / AP

CAP理论仅适用于原子读写的Nosql场景，不适用于数据库系统
```
BASE  
```
基于CAP理论，CAP理论并不适用于数据库事务（因为更新一些错误的数据而导致数据出现紊乱，无论什么样的数据库高可用方案都是
徒劳） ，虽然XA事务可以保证数据库在分布式系统下的ACID特性，但是会带来性能方面的影响；

eBay尝试了一种完全不同的套路，放宽了对事务ACID的要求。提出了BASE理论
`Basically available`  ： 数据库采用分片模式， 把100W的用户数据分布在5个实例上。如果破坏了其中一个实例，仍然可以保证
80%的用户可用

`soft-state` ：  在基于client-server模式的系统中，server端是否有状态，决定了系统是否具备良好的水平扩展、负载均衡、故障恢复等特性。
Server端承诺会维护client端状态数据，这个状态仅仅维持一小段时间, 这段时间以后，server端就会丢弃这个状态，恢复正常状态

`Eventually consistent` ：数据的最终一致性
```
###zookeeper
用处:
`数据的发布/订阅（配置中心:disconf）  、 负载均衡（dubbo利用了zookeeper机制实现负载均衡） 、命名服务、
master选举(kafka、hadoop、hbase)、分布式队列、分布式锁`

安装及配置解析:
```zoo.cfg: 
    clientPort 客户端连接的端口

   dataDir中增加myid文件；
   
   server.id=host:port:port； 
   第一个port表示：用于通信的端口
   第二个port表示：用于选举的端口
   
   peerType=observer
   server.1=192.168.11.129:2181:3181:observer 
   ```

数据模型：
```
    持久化节点  ： 节点创建后会一直存在zookeeper服务器上，直到主动删除
    持久化有序节点 ：每个节点都会为它的一级子节点维护一个顺序
    临时节点 ： (临时节点不能有子节点)临时节点的生命周期和客户端的会话保持一致。当客户端会话失效，该节点自动清理
    临时有序节点 ： 在临时节点上多勒一个顺序性特性
```
    
会话：`not_conneted -> connecting -> connected -> closed  `  
watcher:`监听，watcher的通知是一次性，一旦触发一次通知后，该watcher就失效  `  
ACL：`A/C/I/D/admin  `  

####基本命令：
  create [-s] [-e] path data acl  
  get path [watch]  
  set path data [version]  
  delete path [version]  
  
####stat:
    cZxid = 0x500000015 创建事务  
    ctime = 创建时间   
    mZxid = 0x500000016 修改事务  
    mtime = 修改时间  
    pZxid = 0x500000015 子节点修改事务  
    cversion = 0 子节点版本号  version乐观锁的概念
    dataVersion = 1 数据版本号  
    aclVersion = 0 权限版本号，修改节点权限  
    ephemeralOwner = 0x0   创建临时节点的时候，会有一个sessionId 。 该值存储的就是这个sessionid  
    dataLength = 3    数据值长度  
    numChildren = 0  子节点数  
    
java api的使用
权限控制模式
schema：授权对象
ip     : 192.168.1.1
Digest  : username:password
world  : 开放式的权限控制模式，数据节点的访问权限对所有用户开放。 world:anyone
super  ：超级用户，可以对zookeeper上的数据节点进行操作

连接状态
KeeperStat.Expired  在一定时间内客户端没有收到服务器的通知， 则认为当前的会话已经过期了。
KeeperStat.Disconnected  断开连接的状态
KeeperStat.SyncConnected  客户端和服务器端在某一个节点上建立连接，并且完成一次version、zxid同步
KeeperStat.authFailed  授权失败

事件类型
NodeCreated  当节点被创建的时候，触发
NodeChildrenChanged  表示子节点被创建、被删除、子节点数据发生变化
NodeDataChanged    节点数据发生变化
NodeDeleted        节点被删除
None   客户端和服务器端连接状态发生变化的时候，事件类型就是None  
 
 -
 
 curator连接的重试策略
 
 ExponentialBackoffRetry()  衰减重试 
 RetryNTimes 指定最大重试次数
 RetryOneTime 仅重试一次
 RetryUnitilElapsed 一直重试知道规定的时间