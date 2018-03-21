package xunfei.tech.com.techlib;

public class WakeUpManager
{
//  private static final int MAX = 60;
//  private static final int MIN = -20;
//  static WakeUpManager wakeUpManager;
//  private final String CALLBACK_GAMEOBJECT = "IFlyManager";
//  private final String TAG = "Unity";
//  WakeCallBack callBack;
//  private Context context;
//  private int curThresh = 10;
//  private String gameObjectName;
//  private InitListener initListener = new InitListener()
//  {
//    public void onInit(int paramInt)
//    {
//      Log.d("Unity", "onInit code:" + paramInt);
//    }
//  };
//  private String ivwNetMode = "0";
//  private String keep_alive = "1";
//  private VoiceWakeuper mIvw = VoiceWakeuper.createWakeuper(this.context, this.initListener);
//  private WakeuperListener mWakeuperListener = new WakeuperListener()
//  {
//    public void onBeginOfSpeech()
//    {
//      Log.d("Unity", "onBeginOfSpeech");
//      WakeUpManager.this.SendMsgToUnity("onBeginOfSpeech", "onBeginOfSpeech");
//    }
//
//    public void onError(SpeechError paramSpeechError)
//    {
//      String str = paramSpeechError.getErrorCode() + "";
//      Log.d("Unity", "errorCode:SpeechError" + str);
//      WakeUpManager.this.SendMsgToUnity("onError", str);
//    }
//
//    public void onEvent(int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
//    {
//      switch (paramInt1)
//      {
//      default:
//        return;
//      case 21003:
//      }
//      byte[] arrayOfByte = paramBundle.getByteArray("data");
//      Log.d("Unity", "ivw audio length: " + arrayOfByte.length);
//    }
//
//    public void onResult(WakeuperResult paramWakeuperResult)
//    {
//      Log.d("Unity", "onResult");
//      if (!"1".equalsIgnoreCase(WakeUpManager.this.keep_alive));
//      try
//      {
//        String str = paramWakeuperResult.getResultString();
//        JSONObject localJSONObject = new JSONObject(str);
//        StringBuffer localStringBuffer = new StringBuffer();
//        localStringBuffer.append("【RAW】 " + str);
//        localStringBuffer.append("\n");
//        localStringBuffer.append("【操作类型】" + localJSONObject.optString("sst"));
//        localStringBuffer.append("\n");
//        localStringBuffer.append("【唤醒词id】" + localJSONObject.optString("id"));
//        localStringBuffer.append("\n");
//        localStringBuffer.append("【得分】" + localJSONObject.optString("score"));
//        localStringBuffer.append("\n");
//        localStringBuffer.append("【前端点】" + localJSONObject.optString("bos"));
//        localStringBuffer.append("\n");
//        localStringBuffer.append("【尾端点】" + localJSONObject.optString("eos"));
//        WakeUpManager.access$102(WakeUpManager.this, localStringBuffer.toString());
//        Log.d("Unity", "resultString:" + WakeUpManager.this.resultString);
//        WakeUpManager.this.SendMsgToUnity("onResult", "" + localJSONObject.optString("id"));
//        WakeUpManager.this.callBack.sucess();
//        return;
//      }
//      catch (JSONException localJSONException)
//      {
//        WakeUpManager.access$102(WakeUpManager.this, "结果解析出错");
//        Log.d("Unity", WakeUpManager.this.resultString);
//        localJSONException.printStackTrace();
//      }
//    }
//
//    public void onVolumeChanged(int paramInt)
//    {
//      Log.d("Unity", "onVolumeChanged:" + paramInt);
//      WakeUpManager.this.SendMsgToUnity("onVolumeChanged", paramInt + "");
//    }
//  };
//  private String resultString;
//
//  private void SendMsgToUnity(String paramString1, String paramString2)
//  {
//    Log.d(paramString1, paramString2);
//  }
//
//  public static WakeUpManager getInstance()
//  {
//    if (wakeUpManager == null)
//      wakeUpManager = new WakeUpManager();
//    return wakeUpManager;
//  }
//
//  private String getResource()
//  {
//    String str = ResourceUtil.generateResourcePath(this.context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5a793fe7.jet");
//    Log.d("Unity", "resPath:" + str);
//    return str;
//  }
//
//  public void StartWakeUpSpeechListener(WakeCallBack paramWakeCallBack, Context paramContext)
//  {
//    this.context = paramContext;
//    this.callBack = paramWakeCallBack;
//    this.mIvw = VoiceWakeuper.getWakeuper();
//    if (this.mIvw == null)
//    {
//      Log.d("Unity", "VoiceWakeuper.createWakeuper faild!!!");
//      return;
//    }
//    this.resultString = "";
//    this.mIvw.setParameter("params", null);
//    this.mIvw.setParameter("ivw_threshold", "0:" + this.curThresh);
//    this.mIvw.setParameter("sst", "wakeup");
//    this.mIvw.setParameter("keep_alive", this.keep_alive);
//    this.mIvw.setParameter("ivw_net_mode", this.ivwNetMode);
//    this.mIvw.setParameter("ivw_res_path", getResource());
//    this.mIvw.setParameter("ivw_audio_path", Environment.getExternalStorageDirectory().getPath() + "/ivw.wav");
//    this.mIvw.setParameter("audio_format", "wav");
//    int i = this.mIvw.startListening(this.mWakeuperListener);
//    if (i != 0)
//    {
//      Log.d("Unity", "StartWakeUpSpeechListener status:" + i);
//      return;
//    }
//    Log.d("Unity", "StartWakeUpSpeechListener start wakeup Listening");
//  }
//
//  public void StopWakeUpSpeechListener()
//  {
//    if (this.mIvw != null)
//      this.mIvw.stopListening();
//    Log.d("Unity", "StopWakeUpSpeechListener");
//  }
//
//  public static abstract interface WakeCallBack
//  {
//    public abstract void sucess();
//  }
}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     hello.tech.com.xunfeilib.WakeUpManager
 * JD-Core Version:    0.6.0
 */