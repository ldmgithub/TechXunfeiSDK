package xunfei.tech.com.techlib.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParser {
    public static String parseGrammarResult(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer();
        try {
            JSONArray localJSONArray1 = new JSONObject(new JSONTokener(paramString)).getJSONArray("ws");
            for (int i = 0; i < localJSONArray1.length(); i++) {
                JSONArray localJSONArray2 = localJSONArray1.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < localJSONArray2.length(); j++) {
                    JSONObject localJSONObject = localJSONArray2.getJSONObject(j);
                    if (localJSONObject.getString("w").contains("nomatch")) {
                        localStringBuffer.append("没有匹配结果.");
                        return localStringBuffer.toString();
                    }
                    localStringBuffer.append("【结果】" + localJSONObject.getString("w"));
                    localStringBuffer.append("【置信度】" + localJSONObject.getInt("sc"));
                    localStringBuffer.append("\n");
                }
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            localStringBuffer.append("没有匹配结果.");
        }
        return localStringBuffer.toString();
    }

    public static String parseIatResult(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer();
        try {
            JSONArray localJSONArray = new JSONObject(new JSONTokener(paramString)).getJSONArray("ws");
            for (int i = 0; i < localJSONArray.length(); i++)
                localStringBuffer.append(localJSONArray.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w"));
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return localStringBuffer.toString();
    }

    public static String parseLocalGrammarResult(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer();
        int i = 0;
        try {
            JSONObject localJSONObject1 = new JSONObject(new JSONTokener(paramString));
            JSONArray localJSONArray1 = localJSONObject1.getJSONArray("ws");
            if (i < localJSONArray1.length()) {
                JSONArray localJSONArray2 = localJSONArray1.getJSONObject(i).getJSONArray("cw");
                for (int j = 0; j < localJSONArray2.length(); j++) {
                    JSONObject localJSONObject2 = localJSONArray2.getJSONObject(j);
                    if (localJSONObject2.getString("w").contains("nomatch")) {
                        localStringBuffer.append("没有匹配结果.");
                        return localStringBuffer.toString();
                    }
                    localStringBuffer.append("【结果】" + localJSONObject2.getString("w"));
                    localStringBuffer.append("\n");
                }
            }
            localStringBuffer.append("【置信度】" + localJSONObject1.optInt("sc"));
            return localStringBuffer.toString();
        } catch (Exception localException) {
            while (true) {
                localException.printStackTrace();
                localStringBuffer.append("没有匹配结果.");
                continue;
            }
        }
    }
}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     hello.tech.com.xunfeilib.utils.JsonParser
 * JD-Core Version:    0.6.0
 */