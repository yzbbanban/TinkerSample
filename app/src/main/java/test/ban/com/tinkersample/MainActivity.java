package test.ban.com.tinkersample;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

import test.ban.com.tinkersample.download.DownloadTask;
import test.ban.com.tinkersample.download.ToastUtil;

public class MainActivity extends AppCompatActivity implements TinkerResultService.PatchComplete, DownloadTask.IDownLoad {
    private ProgressDialog pd;
    private static final String TAG = "TinkerResultService";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_test);
        Button btnTest = (Button) findViewById(R.id.btn_test);
        progressBar = (ProgressBar) findViewById(R.id.pb_test);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadTinker();
                } catch (Exception e) {
                    Log.i(TAG, "error: " + e.getMessage());
                }

            }
        });

        TinkerResultService.setView(this);

        textView.setText("4444");
        textView.setTextColor(Color.RED);
        Toast.makeText(MainActivity.this, "4444", Toast.LENGTH_SHORT).show();
        btnTest.setText("download");
    }

    private void downloadTinker() {
//        String path = "http://1.wxban.applinzi.com/ATMSDriver_1.2.3(t1).apk";
        String path ="http://1.wxban.applinzi.com/patch_signed_7zip.apk";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File savDir = Environment.getExternalStorageDirectory();
            DownloadTask downloadTask = new DownloadTask(MainActivity.this, path, savDir, progressBar);
            new Thread(downloadTask).start();
//                        download(path, savDir);
        } else {
            ToastUtil.showToast(getApplicationContext(),
                    "SD卡错误");
        }
    }

    @Override
    public void patchComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                delFile("/patch_signed_7zip.apk");
                Toast.makeText(MainActivity.this, "修复成功,请重新启动程序", Toast.LENGTH_SHORT).show();
//                restartApplication();
            }
        });

    }

    @Override
    public void patchFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void restartApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        am.restartPackage("test.ban.com.tinkersample");
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void delFile(String fileName) {

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(sdPath + fileName);
        Log.e(TAG, "path: " + sdPath + fileName);
        if (file.isFile()) {
            file.delete();
            Log.e(TAG, "修复成功");
        }
        file.exists();
    }

    @Override
    public void downloadSuccess() {
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/patch_signed_7zip.apk");
        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("提示");
        pd.setMessage("修补中。。。");
        pd.show();
    }

    @Override
    public void downloadfailure() {
        ToastUtil.showToast(this, "获取失败");
    }
}
