## 启用配置中心

1. 需要配置中心配置

```yaml
#application.yaml
server:
  port: 8888

spring:
  application:
    name: springcloud-config-server #client bootstrap中的serviceId保持一致
  cloud:
    config:
      server:
        git:
          uri: your git url # 配置git仓库的地址
          search-paths: auth-realname-api # git仓库地址下的相对地址，可以配置多个，用,分割。
          username: xx
          password: xx

#注册中心
eureka:
  instance:
    hostname: ${spring.cloud.client.ip-address}
  client:
    service-url:
      defaultZone: http://127.0.0.1:1110/eureka/
```

2. 将doc下auth-realname-api-mysql.properties（或.yml）和auth-realname-api-redis.properties（或.yml）放到git地址auth-realname-api目录下。
3. 启动项目指定参数`--spring.profiles.active=redis,mysql`

> 1. 使用本地配置，启动时指定--spring.profiles.active=native
>
> 2. 也可以直接把application.yml整个放到git上（这里只把mysql和redis配置放到git上）。