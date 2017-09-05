package com.xxf.daemon;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    private MyBinder myBinder;
    private MyServiceConnection myServiceConnection;

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, LocalService.class), myServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "LocalService Connect Success");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接出现了异常断开了，RemoteService被杀掉了
            Log.i(TAG, "LocalService killed");
            // 启动RemoteCastielService
            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    private class MyBinder extends IProcessService.Stub {

        @Override
        public String getProcessName() throws RemoteException {
            return TAG;
        }
    }
}
