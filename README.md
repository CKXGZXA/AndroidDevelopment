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

1. 首先介绍主页面的布局([**activity_main.xml**](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/activity_main.xml)), 该页面使用线性布局, 其内组件垂直排布,从上到下分别为一个主题输入EditView, 一个内容输入EditView, 主题最大允许字符为30, 内容最大能够输入300个字符, 之后是一个保存按钮, 界面下方是一个ListView, 该ListView用于显示各条笔记, 各条笔记之间用一条褐色分界线线分隔;

   ![](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220413004905832.png)

2. 随后创建 LIstView 中 Item 的布局([**note.xml**)](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/note.xml), 此布局可以显示笔记的基本信息, 包括笔记的序号, 主题, 和部分内容预览, 该垂直线性布局中含有一个约束布局和一个线性布局, 约束布局中包含两项: 笔记序号和笔记主题, 在界面偏左 $\frac{1}{5}$ 处, 使用引导线将序号和主题分离, 序号字体具有阴影效果, 主题的背景色改为了绿色, 线性布局中含有两个TextView, 第一个TextView显示的是最后修改或者创建笔记的时间, 第二个TextView 只有一行,用来预览笔记的内容, 北京设置为一个红色边框XML, 因此跟其他组件划分开来,  

   ![](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220413005049446.png)

3. 为了实现笔记内容查看, 和更新删除功能, 创建了笔记详情布局([**activity_note_detail.xml**)](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/activity_note_detail.xml), 该布局为线性布局, 其中从上到下包含了一个垂直线性布局和一个水平线性布局, 垂直线性布局中包含两个EditText, 

### 3. 特色之处

### 4. 应用实际效果截图

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/11231977C403B508DE80334708763F54.jpg)

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/8FDC775F1E78BD30FC1DEB6E1F7F412D.jpg)