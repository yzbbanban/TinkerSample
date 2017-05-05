package test.ban.com.tinkersample.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import java.io.File;

/**
 * Created by brander on 2017/4/5.
 */
public class DownloadTask implements Runnable {
    private String path;
    private File saveDir;
    private FileDownloader loader;
    private static final int PROCESSING = 1;
    private static final int FAILURE = -1;
    private Handler handler = new UIHandler();
    private ProgressBar progressBar;
    private Context context;
    private IDownLoad iDownLoad;

    public DownloadTask(Context context, String path, File saveDir, ProgressBar progressBar) {
        this.path = path;
        this.context = context;
        this.saveDir = saveDir;
        this.progressBar = progressBar;
        iDownLoad = (IDownLoad) context;
    }


    /**
     * 退出下载
     */
    public void exitLoader() {
        if (loader != null)
            loader.exit();
    }

    DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
        @Override
        public void onDownloadSize(int size) {
            Message msg = new Message();
            msg.what = PROCESSING;
            msg.getData().putInt("size", size);
            handler.sendMessage(msg);
        }
    };

    public void run() {
        try {
            // 实例化一个文件下载器
            loader = new FileDownloader(context.getApplicationContext(), path,
                    saveDir, 3);
            // 设置进度条最大值
            progressBar.setMax(loader.getFileSize());
            loader.download(downloadProgressListener);
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i(TAG, "run: " + e.getMessage());
            handler.sendMessage(handler.obtainMessage(FAILURE));  // 发送一条空消息对象
        }
    }


    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING: // 更新进度
                    progressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float) progressBar.getProgress()
                            / (float) progressBar.getMax();
                    int result = (int) (num * 100); // 计算进度
//                    resultView.setText(result + "%");
                    ToastUtil.showToast(context.getApplicationContext(), result + "%");
                    if (progressBar.getProgress() == progressBar.getMax()) { // 下载完成
                        ToastUtil.showToast(context.getApplicationContext(), "下载完成");
                        iDownLoad.downloadSuccess();
                    }
                    break;
                case FAILURE: // 下载失败

                    ToastUtil.showToast(context.getApplicationContext(), "失败");
                    iDownLoad.downloadSuccess();
                    break;
            }
        }
    }

    private DownloadTask task;

    private void exit() {
        if (task != null)
            task.exitLoader();
    }


    public interface IDownLoad {
        void downloadSuccess();

        void downloadfailure();
    }
}
