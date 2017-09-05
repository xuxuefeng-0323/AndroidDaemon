package com.xxf.daemon;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocalService extends Service {
    private static final String TAG = "LocalService";
    private MyBinder myBinder = null;
    private MyServiceConnection myServiceConnection;

    public LocalService() {
    }

    @Override
    public void onCreate() {
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        myServiceConnection = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.bindService(new Intent(this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "RemoteService Connect Success");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接出现了异常断开了，RemoteService被杀掉了
            Log.i(TAG, "RemoteService killed");
            // 启动RemoteCastielService
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class),
                    myServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    class MyBinder extends IProcessService.Stub {
        @Override
        public String getProcessName() throws RemoteException {
            return TAG;
        }
    }
}
