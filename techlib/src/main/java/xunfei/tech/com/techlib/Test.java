package xunfei.tech.com.techlib;

import java.util.HashMap;

import xunfei.tech.com.techlib.utils.XFStringUtil;

/*************************************
 功能：
 创建者： kim_tony
 创建日期：2018/4/16
 版权所有：深圳市亿车科技有限公司
 *************************************/

public class Test {
    public static void main(String[] args) {
        HashMap<String, String[]> map = new HashMap<>();

        String[] arg1 = new String[]{"guanjia" , "shui" ,"nimen"};
        String[] arg2 = new String[]{"kaizha", "tingche"};
        String[] arg3 = new String[]{"ceshi", "zhangsan"};

        map.put("类型1", arg1);
        map.put("类型2", arg2);
        map.put("类型3", arg3);

        XFStringUtil.setTypes(map);
        
        System.out.println(XFStringUtil.getType("你们这里谁是管家君"));
        System.out.println(XFStringUtil.getType("你们这里谁是管家君"));
        System.out.println(XFStringUtil.getType("你们这里谁是管家君"));

        System.out.println(XFStringUtil.getType("请帮我开闸我要出去"));
        System.out.println(XFStringUtil.getType("请帮我停车我要办事"));

        System.out.println(XFStringUtil.getType("这位是我们的测试人员李四"));
        System.out.println(XFStringUtil.getType("这位是我们的开发人员张三"));


    }
}
