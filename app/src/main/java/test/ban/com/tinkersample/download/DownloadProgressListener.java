package test.ban.com.tinkersample.download;

/**
 * Created by YZBbanban on 16/7/20.
 */
public interface DownloadProgressListener {
    /**
     * 下载进度监听方法 获取和处理下载点数据的大小
     *
     * @param size 数据大小
     */
    void onDownloadSize(int size);
}
