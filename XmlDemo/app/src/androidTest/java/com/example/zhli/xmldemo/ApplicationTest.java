package com.example.zhli.xmldemo;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
//        writeXmlToLocal();

        List<Person> personList = parserXmlFromLocal();
        for (Person p : personList) {
            System.out.println(p);
        }
    }

    /**
     * pull 解析 xml
     * @return
     */
    private List<Person> parserXmlFromLocal() {
       try {
            File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
            FileInputStream fis = new FileInputStream(path);

           // 获得 pull 解析器对象
           XmlPullParser parser = Xml.newPullParser();
           parser.setInput(fis, "utf-8");

           // 获得事件的类型
           int eventType = parser.getEventType();
           List<Person> personList = null;
           Person person = null;
           while (eventType != XmlPullParser.END_DOCUMENT) {
               String tagName = parser.getName();   // 获得节点的名称
               switch (eventType) {
                   case XmlPullParser.START_TAG:
                       if(tagName.equals("persons")) {
                           personList = new ArrayList<>();
                       } else if(tagName.equals("person")) {
                           person = new Person();
                           String id = parser.getAttributeValue(null, "id");    // 获得 id
                           person.setId(Integer.parseInt(id));
                       } else if(tagName.equals("name")) {
                           person.setName(parser.nextText());   // 设置 name
                       } else if(tagName.equals("age")) {
                           person.setAge(Integer.parseInt(parser.nextText()));
                       }
                       break;
                   case XmlPullParser.END_TAG:  // </person>
                       if("person".equals(tagName)) {
                           // 将 persson 添加到 list 中
                           personList.add(person);
                       }
                   default:
                       break;
               }
               eventType = parser.next();  // 获得下一个事件的类型
           }
           return personList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写 xml 文件到本地
     * <?xml version="1.0" encoding="utf-8" standalone="yes"?>
     * <persons>
     *      <person id="1">
     *          <name>li</name>
     *          <age>21</age>
     *      </person>
     *      ...
     * <persons/>
     */
    private void writeXmlToLocal() {
        List<Person> personList = getPersonList();
        // 获得序列化对象
        XmlSerializer serializer = Xml.newSerializer();
        try {
            File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
            FileOutputStream fos = new FileOutputStream(path);
            // 指定序列化对象的输出和编码
            serializer.setOutput(fos, "utf-8");

            serializer.startDocument("utf-8", true);    // 头<?xml version="1.0" encoding="utf-8" standalone="yes"?>
            serializer.startTag(null, "persons");   // <persons>
            for (Person p : personList) {
                serializer.startTag(null, "person");    // <person>

                serializer.attribute(null, "id", String.valueOf(p.getId()));

                serializer.startTag(null, "name");  // <name>
                serializer.text(p.getName());
                serializer.endTag(null, "name");    // </name>

                serializer.startTag(null, "age");  // <age>
                serializer.text(String.valueOf(p.getAge()));
                serializer.endTag(null, "age");    // </age>
                serializer.endTag(null, "person");      // </person>
            }
            serializer.endTag(null, "persons");     // </persons>
            serializer.endDocument();   // 结束
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Person> getPersonList() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            personList.add(new Person("li" + i, i, 10 + i));
        }
        return null;
    }
}