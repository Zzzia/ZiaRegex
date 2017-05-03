package com.zia.ziaregex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.tv);
        //首先实例化这个类
        MyRegex myRegex = new MyRegex();
        //开启log输出
        myRegex.setIsLog(true);
        //调用这个类的build方法，接下来的解析在这里面操作
        //这里面是教务在线的班级名单，需要内网
        myRegex.build("http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=bjStu", new MyRegex.BuildCallback() {
            @Override
            public void onFinish(MyRegex myRegex) {
                //调用该方法，第一个参数为解析字段的首字符串，第二个是末尾字符串
                // 第三个是操作序号，第一次为1，每增加一次解析加1
                myRegex.RegexInclude("<tbo","tbody>");
                final List<String> list = myRegex.RegexExcept("bj=","'");
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
    }
}
