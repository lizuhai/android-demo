#include <stdio.h>
 #include <jni.h>

 //public native String HelloWorldFromC();
 jstring Java_com_example_zhli_helloworldfromc_MainActivity.HelloWorldFromC(JNIEnv* env,jobject obj) {
     // 返回 java string类型的字符串
     // return (**env).NewStringUTF(env, "helloworldfromc");
     // 2. 实现方法
     return (*env)->NewStringUTF(env, "helloworldfromc");
     // 3. 把 c 代码打包成函数库
     // android.mk
 }