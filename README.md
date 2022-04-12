# **简单带存储功能与应用界面的APP设计与实现**

> **学号:** 21940162
>
> **姓名:** 赵希奥

[TOC]



## 简单笔记本

项目地址:<https://gitee.com/ckxgzxa/AndroidDevelopment/tree/notepad/>

### 1. 功能介绍

本应用实现了以下基本功能:  (斜体为4月13号新增内容)

- 通过输入指定主题和内容可以新生成一条笔记记录;
- 可以按照时间顺序将笔记显示在界面中;
- 可以对笔记的主题内容进行修改;
- 可以删除某条笔记.
- *打开笔记本需要进行密码验证*





---------------

### 2. 实现细节

1. 首先介绍主页面的布局([**activity_main.xml**](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/activity_main.xml)), 该页面使用线性布局, 其内组件垂直排布,从上到下分别为一个主题输入EditView, 一个内容输入EditView, 主题最大允许字符为30, 内容最大能够输入300个字符, 之后是一个保存按钮(单击之后可以将上述两个EditText中的内容写入数据库, 随后将其清空刷新ListView), 界面下方是一个ListView, 该ListView用于显示各条笔记, 各条笔记之间用一条褐色分界线线分隔;

   ![](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220413004905832.png)

2. 随后创建 LIstView 中 Item 的布局([**note.xml**)](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/note.xml), 此布局可以显示笔记的基本信息, 包括笔记的序号, 主题, 和部分内容预览, 该垂直线性布局中含有一个约束布局和一个线性布局, 约束布局中包含两项: 笔记序号和笔记主题, 在界面偏左 $\frac{1}{5}$ 处, 使用引导线将序号和主题分离, 序号字体具有阴影效果, 主题的背景色改为了绿色, 线性布局中含有两个TextView, 第一个TextView显示的是最后修改或者创建笔记的时间, 第二个TextView 只有一行,用来预览笔记的内容, 北京设置为一个红色边框XML, 因此跟其他组件划分开来,  

   ![](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/image-20220413005049446.png)

3. 为了实现笔记内容查看, 和更新删除功能, 创建了笔记详情布局([**activity_note_detail.xml**)](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/res/layout/activity_note_detail.xml), 该布局为线性布局, 其中从上到下包含了一个垂直线性布局和一个水平线性布局, 垂直线性布局中包含两个EditText, 可以对笔记的主题和内容进行修改, 下面的水平线性布局中包含两个按钮,分别是保存和删除按钮, 可以对数据库中的笔记进行更新和删除操作,点击之后页面会返回到主页面,随后刷新ListView.

4. 接下来叙述代码逻辑部分.

5. **[MyDatabaseHelper.java](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/java/top/ckxgzxa/notepad/MyDatabaseHelper.java)**

   数据库工具类, 该方法继承了**SQLiteOpenHelper**抽象类, 

   ```java
   // 实现此构造方法
   public MyDatabaseHelper(@Nullable Context context, @Nullable String name,int version) 
   ```

   另外重写了OnCreate()方法, 如下:

   ```java
       @Override
       public void onCreate(SQLiteDatabase sqLiteDatabase) {
           sqLiteDatabase.execSQL("create table if not exists notes(" + "_id integer primary key autoincrement,topic varchar(30),note varchar(1000)," + "time varchar(20),password varchar(20))");
       }
   ```

   此方法定义了一个SQL语句并执行, 作用是建立数据表, 主键为_id的递增主键, 还有笔记的主题和内容对应的键

6. [**MainActivity.java**](https://gitee.com/ckxgzxa/AndroidDevelopment/blob/notepad/app/src/main/java/top/ckxgzxa/notepad/MainActivity.java)

   该类关联`activity_main.xml`, 

   首先介绍含有的方法:

   ```java
   private boolean insertData(String topic, String note);
   ```

   该方法用于想数据库中插入数据, 即保存按钮点击之后需要调用该方法, 其中首先判断两个编辑框内是否有为空的,若为空,则使用Toast提醒用户哪一个编辑框内未填充字符串,  若均含内容返回`false`随后获取当前时间, 和主题, 内容一起写入数据库保存,提示"保存成功", 返回`true`.

   ```java
   private void showNotes();
   ```

   这是在ListView中展示笔记的重要方法, 该方法首先创建数据库相关对象, 执行查询的SQL语句, 这个方法中, 我并没有使用SQL语句而是使用`db`的`query()`方法, 对返回结果, 我们首先按时间倒序排列, 再按照序号倒序排列,随后我们以查询出的结果分别赋给note布局中的各个组件, 随后将`ListView`的adapter设置好关闭数据库,就完成了既定功能.

   ```java
       @Override
       protected void onDestroy() {
           super.onDestroy();
           // 退出程序时关闭数据库
           dbHelper.close();
       }
   ```

   退出程序时关闭数据库.

   ```java
   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l);
   ```

   该方法获取`ListView`中被点击的条目, 将当前`view`中存序号的`TextView`内容也就是笔记在数据库中的主键取出封装成Bundle对象,在使用`Intent`携带此`Bundle`对象,   再使用`startActivityForResult()`方法等待返回特定结果, 下面重写的方法`protected void onActivityResult(int requestCode, int resultCode, Intent data)`定义了如果返回结果为*`RESULT_OK`* 时, 页面会再调用一次`showNotes()` 方法刷新数据,   
   
   最后我们看最主要的`onCreate()`方法, 在此方法中, 我们首先使用`findViewById`获取到主页面的`listView`, 为其设置监听器 :
   
   ```java
           // 给listView设置点击事件
           listView.setOnItemClickListener(this);
   ```
   
   随后我们打开新建数据库帮助类打开数据库, 若存在数据库则读取其中的数据展示到页面当中, 若不存在数据库, 则新建.随后调用`showNotes()`方法刷新页面, 这样可以做到首次进入页面的时候, 数据会展示出来.
   
   下面给按钮增加点击事件监听器, 将两个EditText中的内容分别存入两个`String`变量 topic 和 note 中 ,然后调用`insertData(topic, note)` 方法将编辑框中的数据写入到数据库中, 再次刷新数据:
   
   ```java
           btn.setOnClickListener(view -> {
               // 获取用户输入的数据
               String topic = topicEt.getText().toString();
               String note = noteEt.getText().toString();
               // 将数据插入数据库
   
               if (insertData(topic, note)) {
                   // 清空输入框
                   topicEt.setText("");
                   noteEt.setText("");
               }
               showNotes();
           });
   ```
   
   7. 



-----------

### 3. 特色之处



-------------

### 4. 应用实际效果截图

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/11231977C403B508DE80334708763F54.jpg)

![img](https://zxastaticpages.oss-cn-beijing.aliyuncs.com/blogpictures/8FDC775F1E78BD30FC1DEB6E1F7F412D.jpg)