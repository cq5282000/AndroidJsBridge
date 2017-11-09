# AndroidJsBridge

## android学习 && AndroidJSBridge开发日志

### 2017年10月28日

- 工程搭建，今天完成了工程的搭建

### 2017年11月8日

最近花时间把安卓撸了一下，发现学习一样东西的时候漫无目的的乱看是最没有效率的，得不偿失。安卓的JSBridge的开发环境已经
搭建起来，接下来详细说明一下搭建过程

- 使用安卓原生框架内嵌网页开发有以下优点：

  第一，版本的热更新，每次更新功能的时候，网页具有热更新的功能，不用每次发布版本， 所以大多数公司会采取这种形式。
  
  第二，当涉及到复杂的交互以及展示界面时候，相比于使用安卓原生开发，网页开发更加快捷方便。
  
  第三，当你的安卓应提供给用户的数据需要实时的网络连接来获取相应的数据，例如电子邮件这种，采用潜入网页开发更好。
 
- 因为安卓的屏幕大小和像素密度都不同，首先设置模版文件的viewport属性，让你的网页永远显示在合适的大小，viewport是安
卓为你的网页提供的方形可绘制区域，你可以定义它的大小和初始的缩放值，其中最重要的是viewport的width属性，width定义了
viewport绘制网页时水平方向可用的像素点大小

```bash
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
```
- 安卓布局添加webview布局，用来加载网页

- <manifest>添加网络访问权限

```bash
<uses-permission android:name="android.permission.INTERNET" />
```

- 使能JS，在安卓的webview中，JS默认是被禁止的，必须开启

```bash
WebView myWebView = (WebView) findViewById(R.id.webview);
WebSettings webSettings = myWebView.getSettings();
webSettings.setJavaScriptEnabled(true);
```

- 当LOAD有SSL层的HTTPS界面时，如果这个网站的安全证书在安卓无法得到认证，webview就会变成一个空白页，并不会像PC浏览
器一样跳出一个风险提示框。因为必须对这种情况做出处理，

```bash
webview.setWebViewClient(new WebViewClient(){
    @override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
    //handler.cancel(); 默认的处理方式，WebView变成空白页
      handler.process();接受证书
    
    //handleMessage(Message msg); 其他处理
    }
    // 这行代码一定加上否则效果不会出现  
 webView.getSettings().setJavaScriptEnabled(true); 
```

安卓的端的设置到此完成，接下来说一下如何搭建开发平台，开发的形式采用的是在PC端开发，安卓端LOAD网页测试

- 如果公司Wi-Fi没有限制手机端和PC端接入同一Wi-Fi，PC端启动node服务，手机端直接打开 "IP:PORT/网页地址" 进行调试
 
- 情况2，大部分公司加了限制，移动端无法自由自在的通过公共IP访问node服务，解决办法如下： 首先，PC端链接一个Wi-Fi1，
对应为IP1和port1，然后移动端链接另外一个Wi-Fi2，对应为IP2，port2，然后在移动端使用移动代理，代理为IP1和PORT1，这
样手机端就可以自由访问了。。。。

好，开发环境的搭建到此就完成了，接下来就可以进行安卓端JSBridge的开发了

### 2017年11月9日

当开发在安卓客户端使用到webview的网络应用的时候，你可以在JS代码和安卓端代码之间创建接口，你的JS代码可以调用一
个安卓原生代码封装的method来实现特定功能，例如你可以调用原生Toast功能来实现吐司而不是使用JS的alert，接下来简单介绍
一下如何把JS绑定到安卓上

- 声明实现特定功能的安卓接口，例如，你可以在安卓应用中声明以下接口：

```bash
public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
```

这个接口的功能是实现一个简单的吐司功能,如果你的安卓使用的安卓SDK的版本大于或者等于17，你应当给接口里所有的声明的方法
添加 @JavascriptInterface 注释。如果不添加的话，当你的页面运行在安卓4.2或者更高的版本上时，安卓会不支持。

- 使用addJavascriptInterface()方法把这个接口绑定到你的JS，并且第二个参数可以声明定义JS调用时所使用的接口名称

```bash
webView.addJavascriptInterface(new WebAppInterface(this), "Android");
```

- 接下来在JS代码中直接调用方法，当网页运行在安卓的webview的时候，会调用你在安卓webview中定义的接口。

```bash
componentWillMount() {
    Android.showToast('hello jsbridge');
}
```



