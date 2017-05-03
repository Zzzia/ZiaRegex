package com.zia.ziaregex;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zia on 2017/5/3.
 */

public class MyRegex {
    //Log标签
    private final static String TAG = "MyRegex: ";
    //正则解析的数据保存在这里
    public static List<List<String>> regexList = new ArrayList<>();
    //解析的目标网址，获取该网址页面所有html内容
    private String RegexUrl;
    //url地址中的html内容
    public static String html;
    //保存下这个类
    private MyRegex myRegex;
    //用户设置是否显示log,默认不显示
    private static boolean isLog = false;
    //保存用户上一次操作的集合id
    private static int Id = 0;
    //解析方式
    private final static int TYPE_INCLUDE = 1;
    private final static int TYPE_EXCEPT = 2;

    //包含正则表达式
    private String Include(String start,String end){
        return start + "[\\s\\S]*?" + end;
    }

    //不包含正则表达式
    private String Except(String start,String end){
        return "(?<=" + start + ")[\\s\\S]*?(?=" + end + ")";
    }

    //解析逻辑
    private List<String> regex(String start,String end,int id,int target,int type){
        List<String> list = new ArrayList<>();
        String regEx;
        //设置解析方式
        if(type == TYPE_EXCEPT){
            regEx = Except(start,end);
        }
        else regEx = Include(start,end);
        Pattern pattern = Pattern.compile(regEx);
        //将目标集合里的所有内容解析出来，放到这里的list里
        for (String data : regexList.get(target)) {
            Matcher matcher = pattern.matcher(data);
            while (matcher.find()){
                if(matcher.group() != null){
                    list.add(matcher.group());
                    log("group"+id+": "+"get:  "+matcher.group());
                }
            }
        }
        regexList.add(list);
        return list;
    }

    public void build(String url,BuildCallback callback){
        RegexUrl = url;
        myRegex = this;
        getHTML(url,callback);
    }

    /**
     * 功能：获取包扩限定字符在内的所有html内容
     * start 字符串起始位置
     * end 字符串末尾位置
     * id 设置此次操作所获得的解析内容放置的集合序号，多层解析时需要,从1开始，0为html
     * return 正则提取的内容
     */
    public List<String> RegexInclude(String start,String end){
        Id++;
        //默认目标id为上一个id
        return regex(start,end,Id,Id-1,TYPE_INCLUDE);
    }

    /**
     * 功能：更灵活地再次解析已解析的数据，可以正则解析指定集合中所有内容，id不自增
     * @param start 字符串起始位置
     * @param end 字符串末尾位置
     * @param id 设置此次操作所获得的解析内容的id,从1开始
     * @param target 再次解析的字符串id
     * @return 正则提取的内容
     */
    public List<String> RegexInclude(String start,String end,int id,int target){
        return regex(start,end,id,target,TYPE_INCLUDE);
    }

    /**
     * 同理，解析除了指定字符串外的字符到集合
     * @param start start
     * @param end end
     * @return 集合
     */
    public List<String>RegexExcept(String start,String end){
        Id++;
        return regex(start,end,Id,Id-1,TYPE_EXCEPT);
    }

    /**
     * 灵活用法，id不自增
     * @param start
     * @param end
     * @param id
     * @param target
     * @return
     */
    public List<String>RegexExcept(String start,String end,int id,int target){
        return regex(start,end,id,target,TYPE_EXCEPT);
    }


    public void setIsLog(Boolean b){
        isLog = b;
    }

    /**
     * log输出
     * @param log log
     */
    private void log(String log){
        if(isLog)
        Log.d(TAG,log);
    }





    //----------------->
    //以下是从网络中获取html的方法，模板类，没有意义
    public interface BuildCallback {
        void onFinish(MyRegex myRegex);
    }
    /**
     * 从网络中加载图片
     * @param address
     */
    private void getHTML(final String address, final BuildCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setDoInput(true);
                    if(connection.getResponseCode()==200) {
                        InputStream in = connection.getInputStream();
                        String response = getStringFromInputStream(in);
                        log("http getOK: " + response);
                        html  = response;
                        List<String> s = new ArrayList<String>();
                        s.add(html);
                        regexList.add(s);
                        callback.onFinish(myRegex);
                    }
                    else{
                        log("get html error: " + connection.getResponseCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private static String getStringFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while((len = is.read(buffer)) != -1){
            outputStream.write(buffer,0,len);
        }
        is.close();
        String state = outputStream.toString();
        outputStream.close();
        return state;
    }
}
