之前写过一篇[Android打开本地pdf文件](http://blog.csdn.net/u010046908/article/details/53008310)的文章，最后总结的时候说，后面一定要拓展库，让其也能打开网络的的pdf文件。今天终于可以兑现承诺了。frok一份代码https://github.com/JoanZapata/android-pdfview，开始改造一番。

##1.基本思路：

 - 打来网络pdf 思路整体还是来源与图片的加载。
 - android中加载网络图片的框架有很多个。如image-laoder， fresco、glide等，首先都是从内存中找图片，如果内存中没有，接着从本地找，本地没有在从网络下载。
 - android中加载pdf也是类似，首先从本地找pdf文件，如果本地存在该pdf文件，直接打开，如果本地不存在，将该pdf文件下载到本地在打开。
 - 下载文件用到了retrofit2的库，已经封装到android_pdf中了。
 
##2.依赖android_pdf库方法
###2.1 在项目的gradle中增加如下代码：

```
compile 'com.lidong.pdf:android_pdf:1.0.1'
```

###2.2 一句代码就可以加载网络pdf。

```
  pdfView.fileFromLocalStorage(this,this,this,fileUrl,fileName);   //设置pdf文件地址
```
###2.3对fileFromLocalStorage(this,this,this,fileUrl,fileName)的解析
   /**
 

```
    *  加载pdf文件
     * @param onPageChangeListener
     * @param onLoadCompleteListener
     * @param onDrawListener
     * @param fileUrl
     * @param fileName
     */
    public  void  fileFromLocalStorage(
    final OnPageChangeListener onPageChangeListener,
                                       final OnLoadCompleteListener onLoadCompleteListener,
                                       final OnDrawListener onDrawListener,
                                       String fileUrl,
                                       final String fileName)
```

 1. OnPageChangeListener onPageChangeListener  ：翻页回调 
 2. OnLoadCompleteListener onLoadCompleteListener:加载完成的回调 
 3. OnDrawListener：页面绘制的回调 
 4. String fileUrl  ： 文件的网络地址
 5.  String fileName 文件名称

##3.使用android_pdf库方法

###3.1写一个布局文件
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.lidong.pdf.androidpdf.MainActivity">

    <com.lidong.pdf.PDFView
        android:id="@+id/pdfView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
</RelativeLayout>
```

###3.2在MainActivity中加载

```
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lidong.pdf.PDFView;
import com.lidong.pdf.api.ApiManager;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;
import com.lidong.pdf.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener
        ,OnLoadCompleteListener, OnDrawListener {
    private PDFView pdfView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfView = (PDFView) findViewById( R.id.pdfView );
        displayFromFile1("http://file.chmsp.com.cn/colligate/file/00100000224821.pdf", "00100000224821.pdf");

    }
    /**
     * 获取打开网络的pdf文件
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1( String fileUrl ,String fileName) {
        pdfView.fileFromLocalStorage(this,this,this,fileUrl,fileName);   //设置pdf文件地址

    }

    /**
     * 翻页回调
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText( MainActivity.this , "page= " + page +
                " pageCount= " + pageCount , Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载完成回调
     * @param nbPages  总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText( MainActivity.this ,  "加载完成" + nbPages  , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( MainActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }
```

[代码地址](https://github.com/lidong1665/AndroidPDF)
-----


效果实现：
![这里写图片描述](http://img.blog.csdn.net/20161102151756760)

代码已经奉上，请大家伙给点建议。一起交流（1561281670）