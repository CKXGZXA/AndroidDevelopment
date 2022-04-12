# **简单带存储功能与应用界面的APP设计与实现**

> **学号:** 21940162
>
> **姓名:** 赵希奥

[TOC]



## 简单笔记本

项目地址:<https://gitee.com/ckxgzxa/AndroidDevelopment/tree/notepad/>

### 1. 功能介绍

本应用实现了以下基本功能:

- 通过输入指定主题和内容可以新生成一条笔记记录;
- 可以按照时间顺序将笔记显示在界面中;
- 可以对笔记的主题内容进行修改;
- 可以删除某条笔记.

### 2. 实现细节

1. 首先我构建了一个activity_main 的 layout, 其中包含一个线性布局,在上方有两个EditText和一个保存按钮用于获取笔记内容然后将其写入数据库,下面是一个ListView, 用于将数据库中的笔记显示到屏幕中.

   ![image-20220412235704420](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220412235704420.png)

2. ListView中Item的布局如下, 线性布局中包含一个约束布局和另一个线性布局, 其中约束布局中含有两个TextView, 分别用来显示笔记编号和笔记主题, 线性布局中含有两个垂直布局的TextView用以显示笔记更新时间和部分内容.

   ![image-20220412235721204](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220412235721204.png)

3. 点击ListView可进入笔记详情界面, 在此界面, 线性布局下包含了两个EditText分别显示笔记的主题和具体内容, 界面最下方有两个按钮, 分别用来保存和删除,可以对数据库中的数据进行操作.

   ![image-20220412235732511](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220412235732511.png)

4. 点击保存或删除返回主页面后,界面会刷新数据显示最新结果.

### 3. 

### 4. 应用实际效果截图

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/11231977C403B508DE80334708763F54.jpg)

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/8FDC775F1E78BD30FC1DEB6E1F7F412D.jpg)