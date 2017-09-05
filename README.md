# AndroidDaemon
Android进程守护 进程保活

Google从Android SDK 21之后添加了JobScheduler来执行一些满足特定条件但不紧急的后台任务，我们可以利用JobScheduler来执行这些特殊的后台任务时来减少电量的消耗。JobService则是一个抽象类，其中包含两个抽象方法：

abstract boolean onStartJob(JobParameters params)

// 我们需要重写onStartJob方法在JobService被调度的时候

abstract boolean onStopJob(JobParameters params)

// 如果确定停止系统调度作业，即使调度作业可能被完成，将调用此方法

当我们有以下需求时，可以使用调度作业

   1.APP有可以推迟的非面向用户的工作
   
   2.APP有当插入设备时您希望优先执行的工作
   
   3.APP有需要访问网络或 Wi-Fi 连接的任务
   
   4.APP有希望作为一个批次定期运行的许多任务

使用JobService可以实现APP进程防杀。

首先需要声明权限
```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```
