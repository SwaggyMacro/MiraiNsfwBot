# 🐧MiraiNsfwBot

A plugin for QQBot based on Mirai.

一个通过Mirai框架实现的QQ机器人鉴黄插件。

---

## ⌨️使用说明

**⚠️所有撤回和禁言操作需要机器人设置为管理员！！！**

1. 使用本插件，你可能需要了解一下 [GitHub - mamoe/mirai: 高效率 QQ 机器人支持库](https://github.com/mamoe/mirai) 。
  
2. 你可以直接使用Mirai的一键安装工具，相关使用说明：[mirai/UserManual.md at dev · mamoe/mirai · GitHub](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md#%E4%BD%BF%E7%94%A8%E7%BA%AF%E6%8E%A7%E5%88%B6%E5%8F%B0%E7%89%88%E6%9C%AC)
  
3. 若你已经安装好Mirai Console(MCL)了，那你直接把编译出来的Jar包放进MCL根目录的 "plugins" 文件夹后重启MCL即可。
  
  ![](https://raw.githubusercontent.com/SwaggyMacro/MiraiNsfwBot/master/preview/2022-03-24-10-51-21-image.png)
  

**已编译好Jar包下载：**

- [🔗Github Release 下载](https://github.com/SwaggyMacro/MiraiNsfwBot/releases/download/1.1/nsfw-1.1.mirai.jar)
- [🔗MiraiNsfwBot 阿里云盘](https://www.aliyundrive.com/s/1wwVbw9X9SG)
- [🔗MiraiNsfwBot 百度网盘](https://pan.baidu.com/s/1j-TwhD0HgnpJ4sNRRQz-Ng?pwd=9agy)

## 🦾指令说明

**⚠以下指令直接在群聊中发送即可**

- **!NSFW设置主人:1194142028**
  
  - 首次运行需要设置主人QQ号，未设置主人QQ前所有人都可以设置。
- **!NSFW设置阈值:sexy-0.6**
  
  - 总共有sexy、hentai、porn三种图片类型会触发，后面的小数是图片疑似度百分比（只能填写小数），上面是设置sexy的例子。
- **!NSFW设置处罚:sexy-1**
  
  - 处罚类型有三种，分别是 0=不作为 1=禁言 2=撤回消息 3=撤回消息+禁言，上面的例子就是设置处罚sexy类型图片后禁言发图片的人，另外有hentai和porn两种的处罚。
- **!NSFW设置禁言时间:sexy-600**
  
  - 注意禁言时间的单位是秒，上面的例子就是当处罚sexy图片类型，sexy的处罚类型是1或者3的时候禁言的时间为600秒也就是10分钟，另外有hentai和porn两种的处罚。
- **!NSFW添加群:660684836**
  
  - 添加群号为660684836的群，只有添加群之后机器人才会进行这个群的图片鉴黄处理，后面写的是QQ群号码。
- **!NSFW删除群:660684836**
  
  - 同理，这是删除群号为660684836的QQ群。
- **!NSFW切换模型:3**
  
  - 将NSFW CNN模型切换至resnet50模型，总共有三种模型，分别是：3和其他任意字符代表resnet50、1代表inceptionV3、2代表mobilenet。mobilenet识别速度最快，识别率也最差。默认使用resnet50。
    
  - 所以以上指令的意思就是让鉴黄插件切换至resnet50模型。
    
- **!NSFW设置回复:禁言-讨厌, hentai！少发点涩图好吗？这就先给你禁言了，下次私发给我。**
  
  - 以上指令的意思就是当有人发了敏感图并且被插件检测到了，而且对其进行了禁言操作的时候就会发送这条消息并 At TA。
    
  - 总共有7种回复设置，分别为：禁言、撤回、撤回禁言或者禁言撤回、群主、管理员、不作为、权限(当机器人不是群主也不是管理员的时候回复)
    
  - 其中群主和管理员的意思就是当群主或者管理员发送敏感度但由于机器人权限不足的时候发送的话。
    
  - 例子：!NSFW设置回复:群主-完了，权限不够，处理不了群主
    
- **!NSFW加入白名单:1194142028**
  
  - 将QQ号为1194142028的账号加入白名单，白名单内账号不会参与检测。
- **!NSFW移除白名单:1194142028**
  
  - 将QQ号为1194142028的账号移除白名单。
- **!NSFW查看白名单列表**
  
  - 查看当前白名单列表
    
- **!NSFW清空白名单**
  
  - 清空白名单
    
- **!NSFW查看群列表**
  
  - 查看当前所有群列表
    
- **!NSFW清空群列表**
  
  - 清空所有已经添加的群
    

## 🖼️使用截图

![](https://raw.githubusercontent.com/SwaggyMacro/MiraiNsfwBot/master/preview/2022-03-24-11-13-36-image.png)
