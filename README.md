
# 文章浏览器

博客园Android客户端内嵌网页WebView，整合网页与Android端之间的交互，用来解耦网页和逻辑处理的紧密交互。

实现通用的文章浏览，适合：博客、新闻、知识库等等类型的文章。


## 一、编译网页源文件

内嵌的网页是基于`vue`框架来构建的，对此需要一定的基础了解。`vue` `npm` 的环境搭建具体百度一下。

> 安装依赖包：

切换到项目的根目录 `vue-source` 

```
cd vue-source
```

然后执行：

``` shell
npm install
# 或者
yarn install
```

> 编译

``` shell
npm run build
# 或者
yarn run build
```
> 完成

请检查当前目录下的 `./library/assets` 目录生成了编译后的网页代码。

## 二、项目引用

``` gradle
allprojects {
    repositories {
	 maven { url 'https://maven.raeblog.com/repository/maven-public/' }
    }
}
```

``` gradle
dependencies {
       implementation 'com.cnblogs:article:1.0.0'
}
```

## 三、开始使用

``` gradle

```
