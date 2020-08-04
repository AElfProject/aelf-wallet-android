# aelf-wallet-android
### AElf - 分布式云计算区块链网络

AElf 是一个区块链系统，旨在通过使用侧链和灵活的设计实现可扩展性和可扩展性。为了支持多个用例，AElf 通过提供易于使用的工具和框架来扩展/定制系统，以便定制链并编写智能合约，从而尽可能简化。AElf 最终将支持各种语言，让开发人员选择他们最熟悉的语言。

有关更多信息，请访问以下链接：

* [官方网站](https://aelf.io)
* [官方文档](https://docs.aelf.io/v/dev/)
* [白皮书](https://grid.hoopox.com/aelf_whitepaper_EN.pdf?v=1)


## APP 功能

- [x] 创建/导入钱包
- [x] 添加/编辑资产
- [x] AElf 及子链转账/收款
- [x] 导出 AElf 助记词/Keystore/私钥/二维码
- [x] 主流币市场行情/K线
- [x] 交易消息通知

## 技术
- [x] mvp开发模式
- [x] retrofit + rxjava网络框架 (com.squareup.retrofit2:retrofit:2.+;com.squareup.retrofit2:adapter-rxjava:2.+)
- [x] glide图片框架 (com.github.bumptech.glide:glide:4.9.0)
- [x] eventbus (org.greenrobot:eventbus:3.1.1)
- [x] RSA 加密算法
- [x] 缓存加密框架 (com.scottyab:secure-preferences-lib:0.1.4)
- [x] 比特币协议框架 (org.bitcoinj:bitcoinj-core:0.14.7)
- [x] frangmentation 页面管理框架 (me.yokeyword:fragmentation:1.3.6)

## 工具
- [x] [Postman](https://www.getpostman.com) - 用于测试 `Web` 服务的强大客户端
- [x] [fiddler](https://www.telerik.com/fiddler) - 可用于手机端抓包

## 运行与打包
- [x] 运行环境 Android studio 3.0,支持最低sdk 16
- [x] 打包所需keystore请见app/build.gradle signingConfigs
- [x] 项目运行后，访问接口需要 PubKey 加密，在项目的 ServiceGenerator 中配置。如有必要，你可以通过 issue 来申请一个 PubKey。
- [x] 支持服务接口配置，配置文件参考\app\src\main\assets\networkConfig.json


