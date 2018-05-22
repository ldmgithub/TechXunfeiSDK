package xunfeisdk.tech.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tech.xunfei.R;

import xunfei.tech.com.techlib.XunfeiManager;
import xunfei.tech.com.techlib.utils.Cn2Spell;
import xunfei.tech.com.techlib.utils.XFStringUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BasicIatActivity";
    private EditText mContent;
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onError(SpeechError paramSpeechError) {
            MainActivity.this.showTip(paramSpeechError.getPlainDescription(true));
        }

        @Override
        public void onResult(RecognizerResult paramRecognizerResult, boolean paramBoolean) {
            Log.d("BasicIatActivity", paramRecognizerResult.getResultString());
            if (!paramBoolean) {
                MainActivity.this.printResult(paramRecognizerResult, MainActivity.this.mContent);
                return;
            }
        }
    };
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            MainActivity.this.showTip("开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            MainActivity.this.showTip("结束说话");
        }

        @Override
        public void onError(SpeechError paramSpeechError) {
            MainActivity.this.showTip(paramSpeechError.getPlainDescription(true));
        }

        @Override
        public void onEvent(int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle) {
        }

        @Override
        public void onResult(RecognizerResult paramRecognizerResult, boolean paramBoolean) {
            Log.d("BasicIatActivity", paramRecognizerResult.getResultString());
            if (!paramBoolean) {
                MainActivity.this.printResult(paramRecognizerResult, MainActivity.this.mContent);
                return;
            }
        }

        @Override
        public void onVolumeChanged(int paramInt, byte[] paramArrayOfByte) {
            MainActivity.this.showTip("当前正在说话，音量大小：" + paramInt);
            Log.d("BasicIatActivity", "返回音频数据：" + paramArrayOfByte.length);
        }
    };
    private Toast mToast;
    private XunfeiManager xunfeiManager;

    private void printResult(RecognizerResult paramRecognizerResult, EditText paramEditText) {
        String str1 = this.xunfeiManager.getContent(paramRecognizerResult);
        Log.d("yuanyin1", str1);
        String pinyin = "";
        for (int i = 0; i < str1.length(); i++) {
            pinyin = pinyin + "_" + Cn2Spell.getPinYin(str1.substring(i, i + 1));
        }
        Log.d("yuanyin2", pinyin);
        Log.d("yuanyin3", XFStringUtil.castToCarNum("车牌" + str1, "失败", true));


        String str2 = XFStringUtil.parseNoneCity(str1, paramEditText.getText().toString(), false);
        paramEditText.setText(str2);
        paramEditText.setSelection(str2.length());
    }

    public void clickMethod() {
        this.xunfeiManager.beGinListner(false);
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        this.mToast = Toast.makeText(this, "", 0);
        this.mContent = ((EditText) findViewById(R.id.et_number));
        findViewById(R.id.btn_recog).setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
//                MainActivity.this.clickMethod();
            }
        });
        findViewById(R.id.btn_tts).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.xunfeiManager.ttsPlay(mContent.getText().toString());
            }
        });
//        this.xunfeiManager = XunfeiManager.getInstance().init(this, this.mRecognizerDialogListener, this.mRecognizerListener);
        this.xunfeiManager = XunfeiManager.getInstance().initTts(this, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public void showTip(String paramString) {
        this.mToast.setText(paramString);
        this.mToast.show();
    }
}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     com.example.andy.testvoice.basic.MainActivity
 * JD-Core Version:    0.6.0
 */