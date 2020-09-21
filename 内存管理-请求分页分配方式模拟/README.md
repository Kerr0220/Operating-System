# 内存管理-请求分页分配方式

## 开发环境

* **开发环境**   Windows 10
* **开发软件**  Eclipse
* **开发语言**  JavaSE (jdk1.8.0_241)
* **开发工具包**  Swing

##  项目结构

```
│  memory.exe
│  memory.jar
│  README.md
│  内存管理 - 请求分页分配方式模拟.md
│  内存管理 - 请求分页分配方式模拟.pdf
│
└─src
    ├─Component
    │      AlgSelectBar.java
    │      Block.java
    │      EventListener.java
    │      Memory.java
    │      Moniter.java
    │      Page.java
    │      SpeedSlider.java
    │      WaitingList.java
    │
    └─UI
            MainFrame.java
```



## 操作说明

* 双击目录下的`memory.jar`(或`memory.exe`)文件进入模拟界面

  * 点击exe文件可能出现如下警告 -> 点击确定即可

    ![img](https://uploader.shimo.im/f/WMjA5j8uQaz16MPz.png!thumbnail)

![img](https://uploader.shimo.im/f/Wb9Du28hegYd4iBW.png!thumbnail)

* 在右上角的选项条中选择置换算法

  * FIFO-先进先出算法（默认值）
  * LRU-最近最少使用页面淘汰算法

  ![img](https://uploader.shimo.im/f/5vT4rdGNLBIdAJYA.png!thumbnail)

* 点击开始模拟

  ![img](https://uploader.shimo.im/f/DANXQZobU4ClFBS7.png!thumbnail)

* 滑动调节速度的滑块可以调整模拟速度

  ![img](https://uploader.shimo.im/f/bgd2ujVC3xchhQZa.png!thumbnail)

  * 慢速条件下可以看清**模拟调页的过程**

  ![img](https://uploader.shimo.im/f/bRYrzN3h0wqrKgI5.png!thumbnail)

  * 快速条件下可以快速得到最终的结果——**缺页率**

  ![img](https://uploader.shimo.im/f/DLLk4Cw7sC7dXKfv.png!thumbnail)

* 点击数据清零可以清除本轮模拟产生的数据以进行下一轮模拟

![img](https://uploader.shimo.im/f/kOOsFunG6K1tzPJG.png!thumbnail)



## 作者

**姓名**  Kerr

**联系方式**  email:kerr99801@gmail.com