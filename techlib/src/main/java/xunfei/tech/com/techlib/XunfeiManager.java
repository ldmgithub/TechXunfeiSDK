package xunfei.tech.com.techlib;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import xunfei.tech.com.techlib.utils.JsonParser;
import xunfei.tech.com.techlib.utils.XFStringUtil;


public class XunfeiManager {
    public static final String PRIVATE_SETTING = "com.iflytek.setting";
    private static final String TAG = "xunfei";
    static XunfeiManager xunfeiManager;
    Activity activity;
    boolean inited;
    private String mEngineType = "cloud";
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    private HashMap<String, String> mIatResults = new LinkedHashMap();
    private InitListener mInitListener = new InitListener() {
        public void onInit(int paramInt) {
            Log.d("xunfei", "SpeechRecognizer init() code = " + paramInt);
            if (paramInt != 0)
                XunfeiManager.this.showTip("讯飞语音初始化失败，错误码：" + paramInt);
        }
    };
    private RecognizerDialogListener mRecognizerDialogListener;
    private RecognizerListener mRecognizerListener;
    private SharedPreferences mSharedPreferences;
    private Toast mToast;
    int ret = 0;

    public static XunfeiManager getInstance() {
        if (xunfeiManager == null)
            xunfeiManager = new XunfeiManager();
        return xunfeiManager;
    }

    //设置默认城市
    public void setDefaultCity(String defaultCity) {
        XFStringUtil.setDefaultCity(defaultCity);
    }

    private void initIat() {
        this.mIat = SpeechRecognizer.createRecognizer(this.activity, this.mInitListener);
        this.mIatDialog = new RecognizerDialog(this.activity, this.mInitListener);
        this.mSharedPreferences = this.activity.getSharedPreferences("com.iflytek.setting", 0);
        this.mToast = Toast.makeText(this.activity, "", 0);
    }

    private void initializeIflytek() {
        if (!this.inited) {
            StringBuffer localStringBuffer = new StringBuffer();
            localStringBuffer.append("appid=" + BuildConfig.xunfeiId);
            localStringBuffer.append(",");
            localStringBuffer.append("engine_mode=msc");
            SpeechUtility.createUtility(this.activity, localStringBuffer.toString());
        }
    }

    /****************************************
     方法描述：开始监听
     @param  paramBoolean true 显示系统dialog false 不显示系统dialog
     @return
     ****************************************/
    public void beGinListner(boolean paramBoolean) {
        getmIatResults().clear();
        if (paramBoolean) {
            this.mIatDialog.setListener(this.mRecognizerDialogListener);
            this.mIatDialog.show();
//            showTip(this.activity.getString(R.string.text_begin));
            return;
        }
        this.ret = this.mIat.startListening(this.mRecognizerListener);
        if (this.ret != 0) {
            Log.d("xunfei", "听写失败,错误码：" + this.ret);
            return;
        }
        showTip(this.activity.getString(R.string.text_begin));
    }


    public String getContent(RecognizerResult paramRecognizerResult) {
        String str1 = JsonParser.parseIatResult(paramRecognizerResult.getResultString());
        StringBuffer localStringBuffer;
        String str2 = "";
        try {
            String str4 = new JSONObject(paramRecognizerResult.getResultString()).optString("sn");
            str2 = str4;
            xunfeiManager.getmIatResults().put(str2, str1);
            localStringBuffer = new StringBuffer();
            Iterator localIterator = xunfeiManager.getmIatResults().keySet().iterator();
            while (localIterator.hasNext()) {
                String str3 = (String) localIterator.next();
                localStringBuffer.append((String) xunfeiManager.getmIatResults().get(str3));
            }
        } catch (JSONException localJSONException) {
            while (true) {
                localJSONException.printStackTrace();
                str2 = null;
            }
        }
        return localStringBuffer.toString().replace("。", "".trim()).replace("，", "".trim()).replace("-", "".trim());
    }

    public HashMap<String, String> getmIatResults() {
        return this.mIatResults;
    }

    public XunfeiManager init(Activity paramActivity, RecognizerDialogListener paramRecognizerDialogListener, RecognizerListener paramRecognizerListener) {
        this.mRecognizerListener = paramRecognizerListener;
        this.mRecognizerDialogListener = paramRecognizerDialogListener;
        this.activity = paramActivity;
        initializeIflytek();
        initIat();
        setParam();
        return xunfeiManager;
    }

    public void setParam() {
        this.mIat.setParameter("params", null);
        this.mIat.setParameter("engine_type", this.mEngineType);
        this.mIat.setParameter("result_type", "json");
        String str = this.mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (str.equals("en_us"))
            this.mIat.setParameter("language", "zh_cn");
        while (true) {
            this.mIat.setParameter("vad_bos", this.mSharedPreferences.getString("iat_vadbos_preference", "4000"));
            this.mIat.setParameter("vad_eos", this.mSharedPreferences.getString("iat_vadeos_preference", "1000"));
            this.mIat.setParameter("asr_ptt", this.mSharedPreferences.getString("iat_punc_preference", "1"));
            this.mIat.setParameter("audio_format", "wav");
            this.mIat.setParameter("asr_audio_path", Environment.getExternalStorageDirectory() + "/msc/iat.wav");
            this.mIat.setParameter("language", "zh_cn");
            this.mIat.setParameter("accent", str);
            return;
        }
    }

    public void setmRecognizerListener(RecognizerListener paramRecognizerListener) {
        this.mRecognizerListener = paramRecognizerListener;
    }

    public void showTip(String paramString) {
        this.mToast.setText(paramString);
        this.mToast.show();
    }
    public void clear(){
        this.activity=null;
        this.mIat =null;
        this.mIatDialog = null;
        this.mSharedPreferences = null;
        this.mRecognizerListener = null;
        this.mRecognizerDialogListener = null;
    }

}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     hello.tech.com.xunfeilib.XunfeiManager
 * JD-Core Version:    0.6.0
 */