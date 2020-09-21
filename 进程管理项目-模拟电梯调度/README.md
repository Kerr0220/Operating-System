# 进程管理 - 电梯调度_设计方案报告

## 开发环境


* **开发环境:**  Windows 10
* **开发软件:**  Eclipse
* **开发语言:**  JavaSE (jdk1.8.0_241)
* **开发工具包:**  Swing

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

```
│  elevator.jar
│  README.md
│  README.pdf
│  tree.txt
│  进程管理 - 电梯调度_设计方案报告.pdf
│
└─src
    ├─component
    │      Buttons.java
    │      Elevator.java
    │      EventListener.java
    │      Floor.java
    │      MyButton.java
    │
    ├─image
    │      1.png
    │      10.png
    │      10A.png
    │      10h.png
    │      11.png
    │      11A.png
    │      11h.png
    │      12.png
    │      12A.png
    │      12h.png
    │      13.png
    │      13A.png
    │      13h.png
    │      14.png
    │      14A.png
    │      14h.png
    │      15.png
    │      15A.png
    │      15h.png
    │      16.png
    │      16A.png
    │      16h.png
    │      17.png
    │      17A.png
    │      17h.png
    │      18.png
    │      18A.png
    │      18h.png
    │      19.png
    │      19A.png
    │      19h.png
    │      1A.png
    │      1h.png
    │      2.png
    │      20.png
    │      20A.png
    │      20h.png
    │      2A.png
    │      2h.png
    │      3.png
    │      3A.png
    │      3h.png
    │      4.png
    │      4A.png
    │      4h.png
    │      5.png
    │      5A.png
    │      5h.png
    │      6.png
    │      6A.png
    │      6h.png
    │      7.png
    │      7A.png
    │      7h.png
    │      8.png
    │      8A.png
    │      8h.png
    │      9.png
    │      9A.png
    │      9h.png
    │      alarm.png
    │      alarmH.png
    │      close.png
    │      closeA.png
    │      closeH.png
    │      door.png
    │      down.png
    │      downH.png
    │      open.png
    │      openA.png
    │      openH.png
    │      slamDunk.jpg
    │      up.png
    │      upH.png
    │
    └─UI
            MyBuilding.java
```

## 作者

姓名：Kerr

联系方式：email:[kerr99801@gmail.com](mailto:kerr99801@gmail.com)

