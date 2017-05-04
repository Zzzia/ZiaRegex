# ZiaRegex
利用正则设计的网络爬虫工具，操作简单粗暴，个人感觉很好用，一直这么爬取数据，突然想到封装一下..

[工具类直达列车](https://github.com/Zzzia/ZiaRegex/blob/master/app/src/main/java/com/zia/ziaregex/MyRegex.java)复制到自己的工程内即可享用

[简书（详细说明）]（http://www.jianshu.com/p/d318f0cc817a）



### demo:

解析新浪新闻手机版页面中新闻的例子


```
//首先实例化这个类
        MyRegex myRegex = new MyRegex();
        //开启log输出
        myRegex.setIsLog(true);
        //调用这个类的build方法，接下来的解析在这里面操作
        //解析新浪新闻手机版中新闻
        myRegex.build("https://sina.cn/?vt=3", new MyRegex.BuildCallback() {
            @Override
            public void onFinish(MyRegex myRegex) {
                //调用Regex...方法，第一个参数为解析字段的首字符串，第二个是末尾字符串

                //第一次解析出含<img标签的内容，一张图片外加一个新闻标题
                myRegex.RegexInclude("<img","/>");//id == 1

                //第二次直接把标题解析出来
                    myRegex.RegexExcept("alt=\"","\"");//id == 2


                //再次解析图片地址,第三个参数是将解析内容添加到id为2的集合，第四个参数是对id为1的集合进行解析
                myRegex.RegexExcept("data-src=\"","\"",3,1);//id == 3

                List<String> titleList = myRegex.regexList.get(2);//将id为2的解析内容保存到标题集合
                List<String> imgList = myRegex.regexList.get(3);//将id为3的解析内容保存到图片地址集合
                
                for(i=0;i<titleList.size();i++){
                    a = a + titleList.get(i) + "\n";
                    a = a + imgList.get(i) + "\n";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(a);
                    }
                });
            }
        });
```
