package test.ban.com.tinkersample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity implements TinkerResultService.PatchComplete {
    private ProgressDialog pd;
    private static final String TAG = "TinkerResultService";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.tv_test);
        Button btnTest = (Button) findViewById(R.id.btn_test);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                }catch (Exception e){

                }
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                + "/patch_signed_7zip.apk");
                pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("提示");
                pd.setMessage("修补中。。。");
                pd.show();
            }
        });

        TinkerResultService.setView(this);

        textView.setText("22222");
        textView.setTextColor(Color.RED);
        Toast.makeText(MainActivity.this, "33333", Toast.LENGTH_SHORT).show();
        btnTest.setText("55555");
    }

    @Override
    public void patchComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                delFile("/patch_signed_7zip.apk");
                Toast.makeText(MainActivity.this, "修复成功,请重新启动程序", Toast.LENGTH_SHORT).show();
                restartApplication();
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
//        System.exit(0);
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public static void delFile(String fileName) {

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(sdPath + fileName);
        Log.e(TAG, "path: "+sdPath + fileName);
        if (file.isFile()) {
            file.delete();
            Log.e(TAG, "修复成功");
        }
        file.exists();
    }
}
