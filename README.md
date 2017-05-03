# ZiaRegex
利用正则设计的网络爬虫工具，操作简单粗暴，个人感觉很好用，一直这么爬取数据，突然想到封装一下..

[工具类直达列车](https://github.com/Zzzia/ZiaRegex/blob/master/app/src/main/java/com/zia/ziaregex/MyRegex.java)

### demo:

```
//首先实例化这个类
MyRegex myRegex = new MyRegex();
//开启log输出
myRegex.setIsLog(true);
//这里面是教务在线的班级名单，测试似乎需要内网
myRegex.build("http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=bjStu", new MyRegex.BuildCallback() {
            @Override
            public void onFinish(MyRegex myRegex) {
                myRegex.RegexInclude("<tbo","tbody>");
                final List<String> list = myRegex.RegexExcept("bj=","'");
                //所有班级号就这么两步解析出来了，现在只需要显示在textview上
                //其实只需要myRegex.RegexExcept("bj=","'");这一步就够了，分两步是为了保险，避免误判
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String a = "";
                        for (String s : list) {
                            a = a + s + "\n";
                        }
                        textView.setText(a);
                    }
                });
            }
        });
```

