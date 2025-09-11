# lcap-kafka-connector

可连接第三方Kafka服务并执行操作

# 配置参数

- bootstrapServers: 集群地址,如127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094
- securityProtocol: 安全协议(PLAINTEXT、SSL、SASL_PLAINTEXT、SASL_SSL)
- saslMechanism: 协议机制(GASAPI、PLAIN、SCRAM-SHA-256、SCRAM-SHA-512)
- username: 用户名,选填
- password: 密码,选填

# 接口详情

## lcap-kafka-connector.send

将消息发送给kafka执行的主题

入参：

- topic: 消息主题
- data: 发送的数据

出参：返回true

## lcap-kafka-connector.sendByKey

按key分组，将消息发送给kafka执行的主题

入参：

- topic: 消息主题
- key: 发送的key
- data: 发送的数据

出参：返回true

## lcap-kafka-connector.createTopic

新增主题

入参：

- topic: 主题名称
- partition: 分区数量,默认为1
- replication: 副本数量,默认为1

出参：返回true

## lcap-kafka-connector.getTopic

获取主题信息

入参：

- topic: 主题名称，多个主题名称使用,分隔,为空时查询全部主题

出参：返回String，格式为：[{"name":"topic1","partitionsCount":1,"replicaCount":1,"totalLogSize":446,"brokers":[{"id":0,"
host":"127.0.0.1","port":9092,"leaderPartitions":[0],"followerPartitions":[0]}],"consumerGroups":null}]

- topic: 主题名称
- partitionsCount: 分区数量
- replication: 副本数量
- totalLogSize: 当前主题数据大小，单位byte
- brokers: 主机信息
- consumerGroups: 消费者组名称集合

## lcap-kafka-connector.deleteTopic

删除主题，里面的消息也会清空

入参：

- topic: 主题名称

出参：返回true

# 订阅详情

## lcap-kafka-connector.subscribe

订阅消息

入参：

- topic: 主题名称
- group: 消费者分组名称,在单个消费者组中,每个主题的每个分区只能由消费者组中的一个消费者实例进行消费
- callback：ide处理消息的逻辑，逻辑的入参为string(客户端收到的消息)，出参为string()

出参：无

## 使用方式

1. 在平台的资产中心-连接器界面上传该压缩包，进入IDE，点击页面上的集成进入集成中心并导入连接器
2. 配置连接器参数，点击连通性测试，测试成功后添加连接器
3. 在调试页面调用getTopic，获取主题信息
4. 在调试页面发送消息，页面发送成功后登录kafka控制台查看消息是否发送成功
5. 添加消息订阅，使用IDE编写收到消息之后的逻辑
6. 将制品进行发布，发布后查看登录kafka控制台查看消费者组是否已订阅对应主题信息，IDE是否正常接收消息

## 注意事项

1. 在集成中心里，无法测试subscribe方法，其余方法均可测试
2. 安全协议为PLAINTEXT时，用户名和密码可不填，连接器对应配置填空格
3. 安全协议为SASL_SSL时，目前只支持阿里云证书，其他证书需要手动上传至源码里的resources目录
4. 连接器需配置Broker服务端JAAS（kafka_server_jaas.conf文件）中的admin账号，否则连通性测试和获取主题信息会失败
5. 订阅逻辑的方法名只支持小写字母开头，需要有String格式的出参入参，否则会报编译错误
