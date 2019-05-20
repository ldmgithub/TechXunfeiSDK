package xunfei.tech.com.techlib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
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
    Context activity;
    boolean inited;
    private String mEngineType = "cloud";
    private SpeechRecognizer mIat;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener;

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
        this.mToast = Toast.makeText(this.activity, "", Toast.LENGTH_SHORT);
    }


    public void ttsPlay(String voiceText) {
        ttsPlay(voiceText, false);
    }

    /**
     * 语音合成
     *
     * @param voiceText isNumber true 非数字拆分读  false  数字拆分
     */
    public void ttsPlay(String voiceText, boolean isNumber) {
        setTtsParam(isNumber);
        int code = mTts.startSpeaking(voiceText, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.ico";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            Log.d(TAG, "语音合成失败,错误码: " + code);
        }
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

    public void stopListning() {
        if (this.mIat != null) {
            this.mIat.stopListening();
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

    //isEng 是否是英语
    public XunfeiManager init(Context paramActivity, RecognizerDialogListener paramRecognizerDialogListener, RecognizerListener paramRecognizerListener, boolean isEng) {
        this.mRecognizerListener = paramRecognizerListener;
        this.mRecognizerDialogListener = paramRecognizerDialogListener;
        this.activity = paramActivity;
        initializeIflytek();
        initIat();
        setParam(isEng);
        return xunfeiManager;
    }


    //语音合成
    public XunfeiManager initTts(Context paramActivity, SynthesizerListener synthesizerListener) {
        this.activity = paramActivity;
        this.mTtsListener = synthesizerListener;
        initializeIflytek();
        initTts();
        return xunfeiManager;
    }

    //初始化语音合成
    private void initTts() {
        // 初始化合成对象
        this.mSharedPreferences = this.activity.getSharedPreferences("com.iflytek.setting", 0);
        mTts = SpeechSynthesizer.createSynthesizer(this.activity, this.mInitListener);
    }

    /**
     * 参数设置
     *
     * @param isNumber true 数字连读 false 数字拆分读
     * @return
     */
    private void setTtsParam(boolean isNumber) {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter("rdn", isNumber ? "1" : "2"); //设置数字读法

       /* 0 //自动，不确定按照值的读法合成
        1 //按照值的读法合成
        2 //按照串的读法合成
        3 //自动，不确定时按照串读法合成*/
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "80"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    public void stopTts() {
        if (this.mTts != null)
            this.mTts.stopSpeaking();
    }

    public void destroyTts() {
        stopTts();
        mTtsListener = null;
        mTts = null;
    }

    public void setVolume(Integer volume) {
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(this.activity, this.mInitListener);
        }
        if (volume >= 0 && volume <= 100 && mTts != null) {
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", volume.toString()));
        }
    }

    public void setParam(boolean isEng) {
        if (this.mIat == null) return;
        this.mIat.setParameter("params", null);
        this.mIat.setParameter("engine_type", this.mEngineType);
        this.mIat.setParameter("result_type", "json");
        this.mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_MIX);
        String str = this.mSharedPreferences.getString("iat_language_preference", "mandarin");
        if (str.equals("en_us"))
            this.mIat.setParameter("language", "zh_cn");
        else {
            this.mIat.setParameter("vad_bos", this.mSharedPreferences.getString("iat_vadbos_preference", "4000"));
            this.mIat.setParameter("vad_eos", this.mSharedPreferences.getString("iat_vadeos_preference", "1000"));
            this.mIat.setParameter("asr_ptt", this.mSharedPreferences.getString("iat_punc_preference", "1"));
            this.mIat.setParameter("audio_format", "wav");
            this.mIat.setParameter("asr_audio_path", Environment.getExternalStorageDirectory() + "/msc/iat.wav");
            this.mIat.setParameter("language", "zh_cn");
            this.mIat.setParameter("accent", str);
        }

        this.mIat.setParameter("language", isEng ? "en_us" : "zh_cn");

    }

    public void setmRecognizerListener(RecognizerListener paramRecognizerListener) {
        this.mRecognizerListener = paramRecognizerListener;
    }

    public void showTip(String paramString) {
        this.mToast.setText(paramString);
        this.mToast.show();
    }


    public void clear() {
        this.activity = null;
        this.mIat = null;
        this.mIatDialog = null;
        this.mSharedPreferences = null;
        this.mRecognizerListener = null;
        this.mRecognizerDialogListener = null;
        this.mTts = null;
        this.mTtsListener = null;


    }

    public boolean isEmpty() {
        stopListning();
        if (this.activity == null) return true;
        if (this.mIat == null) return true;
        if (this.mIatDialog == null) return true;
        if (this.mSharedPreferences == null) return true;
        if (this.mRecognizerListener == null) return true;
        if (this.mRecognizerDialogListener == null) return true;
        return false;
    }

}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     hello.tech.com.xunfeilib.XunfeiManager
 * JD-Core Version:    0.6.0
 */