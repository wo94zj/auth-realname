## 实名认证服务

#### 简介

提供http接口，校验居民身份证信息。

> 也可自行提供接口，及认证实现。只需要引入auth-realname-service和对应的实现包即可。

#### 架构

##### 1. auth-realname-api

http实名认证接口项目。会保留一份认证记录，用于重复认证请求的去重处理。

> 引入redis做重复请求并发控制。

##### 2. auth-realname-service

支持SPI规范的实名认证API，并提供基础的身份证号校验，及默认实现。

##### 3. auth-realname-alicloud-cy

阿里云市场（畅游）实名认证接口调用实现。如需实现其他厂商的接口，自行建立module并实现接口即可。

> `https://market.aliyun.com/products/57000002/cmapi028936.html?spm=5176.10695662.1996646101.searchclickresult.2c575f970kIwOj#sku=yuncode2293600001`

#### 使用说明

1. 如果要使用畅游的认证服务，需要填写`com.auth.alicloud.common.RequestUtil.APPCODE`——阿里云申请。
2. 将自己的实现按SPI规范打包，并引入到auth-realname-api。
3. 创建表
4. 启动auth-realname-api即可。



#### 数据结构

##### 实名认证记录表

```sql
DROP TABLE IF EXISTS `xw_realname_auth`;
CREATE TABLE `xw_realname_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idcard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `area` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` tinyint(2) NOT NULL,
  `update_time` bigint(13) NOT NULL,
  `create_time` bigint(13) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_idcard`(`idcard`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic STORAGE DISK;
```

| 属性        | 类型       | 备注                                |
| ----------- | ---------- | ----------------------------------- |
| id          | int        | 自增                                |
| idcard      | String     |                                     |
| name        | String     |                                     |
| sex         | tinyint(2) | 女：0；男：1                        |
| area        | String     |                                     |
| birthday    | String     |                                     |
| status      | tinyint(2) | 0：待认证；1：认证通过；2：认证失败 |
| update_time | bigint(20) |                                     |
| create_time | bigint(20) |                                     |



#### 接口

##### 响应码

| 响应码 | 备注             |
| ------ | ---------------- |
| 2000   | 认证成功         |
| 2001   | 认证失败         |
| 2002   | 服务繁忙         |
| 4000   | 请求信息校验失败 |
| 4050   | 请求方法不支持   |
| 4040   | 未找到API        |
| 4150   | MediaType ERROR  |
| 5000   | ERROR            |

示例：

```json
{
    "code": 2000,
    "data": "",
    "msg": ""
}
```

