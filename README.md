# Operating-System

操作系统课程设计

[TOC]

## 开发环境


* **开发环境:**Windows 10
* **开发软件:**Eclipse
* **开发语言：**JavaSE (jdk1.8.0_241)
* **开发工具包：**Swing

## 操作说明


* 在**文件夹内**双击运行`MyElevator.exe`,进入电梯模拟系统如下图(注，双击后可能会出现如下警告，点击确定即可运行)

<img src="https://uploader.shimo.im/f/pxaKjS5S2N9xRoo3.png!thumbnail" alt="图片" style="zoom:50%;" />

* **一开始电梯都停在1层，数码显示器显示01**

<img src="https://uploader.shimo.im/f/9ggcL9H2RWwhgoGK.png!thumbnail" alt="图片" style="zoom:50%;" />


* 点击每部电梯的**功能键**(*开/关键*,*报警器*,*楼层按钮*), 进行**单部电梯内命令处理**模拟

<img src="https://uploader.shimo.im/f/WbuQrcay2Y3IIiQY.jpg!thumbnail" alt="图片" style="zoom:50%;" />

点击左侧楼层上下按钮，进行**多部电梯外命令处理**模拟。


* 注：由于空间有限无法给每一个电梯每一层都做上下按钮，故只在每层设置一组上下按钮，**表示该层有上行或下行请求**。其中20层不能继续上行、1层不能继续下行，故不设对应按钮。

## 项目结构

```plain
 │  elevator.jar   
 │  README.pdf
 │  进程管理-电梯调度_设计方案报告.pdf
 │ 
 └─src   
 │  ├─component   
 │  │    Buttons.java
 │  │    Elevator.java
 │  │    EventListener.java
 │  │    Floor.java
 │  │    MyButton.java
 │  └─UI 
 │       MyBuilding.java
 └─image
         i.png   (0<i<21)
         ih.png  (0<i<21)
         iA.png  (0<i<21)
         door.png
         open.png
         openH.png
         openA.png
         close.png
         closeH.png
         closeA.png
         alarm.png
         alarmH.png
         up.png
         upH.png
         down.png
         downH.pngs
```

## 作者

**姓名：**Kerr

**联系方式：**emial:[kerr99801@gmail.com](mailto:kerr99801@gmail.com)

