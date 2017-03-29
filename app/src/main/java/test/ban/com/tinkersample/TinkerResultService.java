package test.ban.com.tinkersample;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * Created by brander on 2017/3/27.
 */
public class TinkerResultService extends DefaultTinkerResultService {
    private static final String TAG = "TinkerResultService";
    private static PatchComplete context;

    public static void setView(PatchComplete context) {
        TinkerResultService.context = context;
    }

    @Override
    public void onPatchResult(PatchResult result) {
//        super.onPatchResult(result);   把这行注释掉，屏蔽掉父类中的方法
        if (result == null) {
            Log.e(TAG, "TinkerResultService received null result!!!!");
            return;
        }
        Log.e(TAG, "TinkerResultService receive result: %s" + result.toString());
        //first, we want to kill the recover process
        if (result.isSuccess) {
            //修复成功
            context.patchComplete();
            Log.e(TAG, "修复成功");
//            deleteRawPatchFile(new File(result.rawPatchFilePath));  //删除补丁包
            Log.e(TAG, "删除补丁");
//            TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());
        } else {
            context.patchFailed();
        }

    }

    /**
     * 完成或失败的回调
     */
    interface PatchComplete {
        void patchComplete();

        void patchFailed();
    }


}
