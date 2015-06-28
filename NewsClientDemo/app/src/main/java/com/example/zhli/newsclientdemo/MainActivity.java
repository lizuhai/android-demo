package com.example.zhli.newsclientdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ListView lvNews;
    private static final String TAG = "MainActivity-zhli";
    private final int SUCCESS = 0;
    private final int FAILED = 1;
    private List<NewsInfo> newsInfoList;
    private Handler handler = new Handler() {
        /**
         * 接收消息
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    // 给 ListView 绑定数据
                    newsInfoList = (List<NewsInfo>) msg.obj;

                    MyAdapter adapter = new MyAdapter();
                    lvNews.setAdapter(adapter);
                    break;
                case FAILED:
                    Toast.makeText(MainActivity.this, "No news", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        lvNews = (ListView) findViewById(R.id.tv_news);

        // 抓取新闻数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获得新闻集合
                List<NewsInfo> newsInfos = getNewsFromInternet();

                // 绑定数据
                Message msg = new Message();
                if (newsInfos != null) {
                    msg.what = SUCCESS;
                    msg.obj = newsInfos;
                } else {
                    msg.what = FAILED;
                }
                handler.sendMessage(msg);
            }
        }).start();


    }

    /**
     *返回新闻信息
     */
    private List<NewsInfo> getNewsFromInternet() {
        HttpClient client = null;
        try {
            client= new DefaultHttpClient();
            HttpGet get = new HttpGet("http://192.168.72.1:8080/AndroidNewsServer/news.xml");
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200) {
                InputStream is = response.getEntity().getContent();
                // 解析 xml 流文件
                List<NewsInfo> infos = getNewsListFromInputStream(is);
                return infos;
            } else {
                Log.i(TAG, "访问失败" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client != null)
                client.getConnectionManager().shutdown();
        }
        return null;
    }

    private List<NewsInfo> getNewsListFromInputStream(InputStream is) throws Exception {
        List<NewsInfo> newsInfoList = null;
        NewsInfo newsInfo = null;
        XmlPullParser parser = Xml.newPullParser();     // 创建一个 pull 解析器
        parser.setInput(is, "utf-8");                  // 指定解析流和编码
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {   // 没到文件结尾
            String tagName = parser.getName();      // 节点名称
            switch (eventType) {
                case XmlPullParser.START_TAG:   // <news>
                    if (tagName.equals("news")) {
                        newsInfoList = new ArrayList<>();
                    } else if(tagName.equals("new")) {
                        newsInfo = new NewsInfo();
                    } else if(tagName.equals("title")) {
                        newsInfo.setTitle(parser.nextText());
                    } else if(tagName.equals("detail")) {
                        newsInfo.setDetail(parser.nextText());
                    } else if(tagName.equals("comment")) {
                        newsInfo.setComment(Integer.valueOf(parser.nextText()));
                    } else if(tagName.equals("image")) {
                        newsInfo.setImageUrl(parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:         // </news>
                    if (tagName.equals("new"))
                        newsInfoList.add(newsInfo);
                    break;
                default:
                    break;
            }
            eventType = parser.next();                  // 去下一个事件类型
        }
        return newsInfoList;

    }
    class MyAdapter extends BaseAdapter {

        /**
         * 返回列表的总长度
         * @return
         */
        @Override
        public int getCount() {
            return newsInfoList.size();
        }

        /**
         * 返回一个列表字条目的布局
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.listview_item, null);
            } else {
                view = convertView;
            }
            SmartImageView sivIcon = (SmartImageView) view.findViewById(R.id.siv_listview_item_icon);
            TextView tvTitle = (TextView) view.findViewById(R.id.siv_listview_item_title);
            TextView tvDetail = (TextView) view.findViewById(R.id.siv_listview_item_detail);
            TextView tvComment = (TextView) view.findViewById(R.id.siv_listview_item_comment);

            NewsInfo newInfo = newsInfoList.get(position);
            sivIcon.setImageUrl(newInfo.getImageUrl());
            tvTitle.setText(newInfo.getTitle());
            tvDetail.setText(newInfo.getDetail());
            tvComment.setText(newInfo.getComment() + " 跟帖");
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
