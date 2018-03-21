package xunfei.tech.com.techlib.utils;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XFStringUtil {
    private static String[] mProvinceChines;  //城市汉字
    private static String[] mProvincesPin; //城市拼音

    private static String[] numsPins; //数字拼音
    private static String[] nums;    //数字

    private static String[] EngPins; //英文拼音
    private static String[] Engs;    //英文

    public static StringBuilder sb = new StringBuilder();
    public static final String IS_NONE_CARNUM = "识别失败，请说\"车牌xxxxxx\"";

    private static String defaultCity = "粤";

    //设置默认城市
    public static void setDefaultCity(String defaultCity) {
        XFStringUtil.defaultCity = defaultCity;
    }


    static {
        mProvinceChines = new String[]{"京", "沪", "浙", "苏", "粤", "鲁", "晋", "冀", "豫", "川", "渝", "辽", "吉", "黑", "皖", "鄂", "湘", "赣", "闽", "陕", "甘", "宁", "蒙", "津", "贵", "云", "桂", "琼", "青", "新", "藏"};
        mProvincesPin = new String[mProvinceChines.length];

        numsPins = new String[]{       //数字拼音
                "ling", "lin", "li",
                "yi", "yao",
                "er", "e",
                "sa", "san", "shan",
                "si", "shi",
                "wu", "hu",
                "liu", "lu",
                "qi", "ji",
                "ba", "pa",
                "jiu", "ju"
        };
        nums = new String[]{     //对应数字
                "0", "0", "0",
                "1", "1",
                "2", "2",
                "3", "3", "3",
                "4", "4",
                "5", "5",
                "6", "6",
                "7", "7",
                "8", "8",
                "9", "9"
        };

        EngPins = new String[]{       //英语拼音
                "e",
                "bi",
                "xi",
                "di", "de",
                "yi",
                "fu", "fo",
                "ji",
                "ci", "chi",
                "ai",
                "zei",
                "ke",
                "lu",
                "mu",
                "en",
                "ou", "ao",
                "pi",
                "yu",
                "ar",
                "si",
                "ti", "te",
                "you",
                "wei",
                "liu",
                "X",
                "wai", "wan",
                "Z"
        };
        Engs = new String[]{     //对应数字
                "A",
                "B",
                "C",
                "D", "D",
                "E",
                "F", "F",
                "G",
                "H", "H",
                "I",
                "J",
                "K",
                "L",
                "M",
                "N",
                "O", "O",
                "P",
                "Q",
                "R",
                "S",
                "T", "T",
                "U",
                "V",
                "W",
                "X",
                "Y", "Y",
                "Z"
        };
        castToPin(mProvinceChines);
    }

    private static StringBuffer localStringBuffer;

    /****************************************
     方法描述： 识别车牌
     @param
     srcStr   输入字符
     paramString2   默认字符
     hasProve   true 有省代号  false    无省代号
     @return
     ****************************************/
    public static String parseNoneCity(String srcStr, String defaultStr, boolean hasProve) {
        if ((!TextUtils.isEmpty(srcStr.toUpperCase())) && (srcStr.contains("车牌")))
            return castToCarNum(srcStr, defaultStr, hasProve).toUpperCase();
        if ((!TextUtils.isEmpty(srcStr.toUpperCase())) && ((srcStr.startsWith("要把")) || (srcStr.startsWith("修改"))))
            return editCarNum(srcStr.toUpperCase(), defaultStr.toUpperCase()).toUpperCase();
        return defaultStr.toUpperCase();
    }


    public static String castToCarNum(String paramString, String defaultString, boolean hasProve) {
        String str1 = "";
        if ("".equals(paramString) ||
                (!paramString.contains("车牌")) ||
                paramString.length() == 2 ||
                "".equals(str1 = subNumber(paramString, hasProve)))//位数不够
            return defaultString;
        //剪切长度
        return str1;
    }


    private static String subNumber(String number, boolean hasProve) {
        int index = number.lastIndexOf("车牌");
        String newNum = "";
        try {
            newNum = number.substring(index + 2);  //将车牌去掉
            if (isProvince((newNum.substring(0, 1)))) {   //是否是城市名开头
                if (!hasProve) {
                    newNum = newNum.substring(1);//去掉第一位
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        //位数控制 控制在8位以内
        int length = newNum.length();
        int size = hasProve ? 8 : 7;
        if (length > size) {
            newNum = newNum.substring(0, size);
        }
        //解析位车牌  1.汉字取首拼音 2.数字转
        sb = new StringBuilder();
        for (int i = 0; i < newNum.length(); i++) {
            String str2 = newNum.substring(i, i + 1);
            if (hasProve && i == 0) {
                //首字母处理
                if (isProvince(str2)) {
                    sb.append(getProvince(str2));
                } else {
                    sb.append(defaultCity);
                    sb.append(pinyin2single(str2));
                    continue;
                }
            } else {
                sb.append(pinyin2single(str2));
            }

        }

        return sb.toString();
    }


    //获取城市
    private static String getProvince(String pinyin) {
        pinyin = Cn2Spell.getPinYin(pinyin);
        String proStr = pinyin.substring(0, 1);
        for (int k = 0; k < mProvincesPin.length; k++) {
            if (mProvincesPin[k].equals(pinyin)) {
                proStr = mProvinceChines[k];
                break;
            }
        }
        return proStr;
    }

    /****************************************
     方法描述：单拼音转换为英文或者数字
     @param content:文字内容
     @return
     ****************************************/
    private static String pinyin2single(String content) {
        content = Cn2Spell.getPinYin(content);
        if (isProvince(content)) {
            return getProvince(content);
        }
        if (isNumber(content)) {
            return getNumber(content);
        }
        if (isEnglish(content)) {
            return getEnglish(content);
        }
        return content.substring(0, 1);
    }


    //获取拼音对应的英语
    private static String getEnglish(String pinyin) {
        pinyin = Cn2Spell.getPinYin(pinyin);
        String engStr = pinyin.substring(0, 1);
        for (int k = 0; k < EngPins.length; k++) {
            if (EngPins[k].equals(pinyin)) {
                engStr = EngPins[k];
            }
            break;
        }
        return engStr;
    }

    //获取拼音对应的数字
    private static String getNumber(String pinyin) {
        pinyin = Cn2Spell.getPinYin(pinyin);
        String numStr = pinyin.substring(0, 1);
        for (int k = 0; k < numsPins.length; k++) {
            if (numsPins[k].equals(pinyin)) {
                numStr = numsPins[k];
                break;
            }
        }
        return numStr.substring(0, 1);
    }

    //是否是英文
    private static boolean isEnglish(String pinyin) {
        return Arrays.asList(EngPins).contains(Cn2Spell.getPinYin(pinyin));
    }

    //是否是数字
    private static boolean isNumber(String pinyin) {
        return Arrays.asList(numsPins).contains(Cn2Spell.getPinYin(pinyin));
    }

    //是否是城市code
    public static boolean isProvince(String str3) {
        return Arrays.asList(mProvincesPin).contains(Cn2Spell.getPinYin(str3));
    }

    public static void main(String[] args) {
        System.out.println(subNumber("范德萨车牌x1233克劳车牌福德吉萨烦的就是卡了减肥的撒啊车牌敏敏一p23432六七", true));
    }

    private static void castToPin(String[] paramArrayOfString) {
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++)
            mProvincesPin[j] = Cn2Spell.getPinYin(paramArrayOfString[j]);
    }


    public static String editCarNum(String paramString1, String paramString2) {
        return "";
    }

    /**
     * 根据中文unicode范围判断u4e00 ~ u9fa5,是否是汉字
     */
    private static boolean isChinese(String str) {
        String regEx1 = "[\\u4e00-\\u9fa5]+";
        String regEx2 = "[\\uFF00-\\uFFEF]+";
        String regEx3 = "[\\u2E80-\\u2EFF]+";
        String regEx4 = "[\\u3000-\\u303F]+";
        String regEx5 = "[\\u31C0-\\u31EF]+";
        Pattern p1 = Pattern.compile(regEx1);
        Pattern p2 = Pattern.compile(regEx2);
        Pattern p3 = Pattern.compile(regEx3);
        Pattern p4 = Pattern.compile(regEx4);
        Pattern p5 = Pattern.compile(regEx5);
        Matcher m1 = p1.matcher(str);
        Matcher m2 = p2.matcher(str);
        Matcher m3 = p3.matcher(str);
        Matcher m4 = p4.matcher(str);
        Matcher m5 = p5.matcher(str);
        if (m1.find() || m2.find() || m3.find() || m4.find() || m5.find())
            return true;
        else
            return false;
    }


}

/* Location:           C:\Users\dell\Desktop\base_dex2jar.jar
 * Qualified Name:     hello.tech.com.xunfeilib.utils.XFStringUtil
 * JD-Core Version:    0.6.0
 */