# AndroidAOPClick
Android 使用AOP方式防止多次点击事件，支持Lambda，布局中设置，butterknife注解

注意事项

1、Lambda表达式，butterknife，布局中设置不支持Except忽略注解
2、Except忽略注解支持setOnClickListener()方式


一、依赖
1). 在根部目录的 build.gradle添加maven仓库



    allprojects {
    
	 repositories {
	 
		...
		maven { url 'https://jitpack.io' }
		
		}  
		
     }


2). gradle 版本使用3.5.2，在项目的build.gradle中的allprojects中的repositories添加，


	dependencies {
	
  	  ...
  	   classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.0'
 	   classpath "com.android.tools.build:gradle:3.5.2"
	   
	}


3). 在app的build.gradle中的最上面添加

	apply plugin: 'android-aspectjx'

4). 在模块下方添加


  	 dependencies {
     	   implementation 'com.github.yedona:AndroidAOPClick:1.0.0'
  	 }
   
   
   
二、使用方式

本项目依赖之后会自动使用

取消方式：


        AopClickUtils.stop();


重新启用方式：

        AopClickUtils.start();


设置点击间隔时间：

        AopClickUtils.setCheckTime(1000L);

忽略拦截，加上注解Except

	new View.OnClickListener() {
            @Except
            @Override
            public void onClick(View v) {
                
            }
        });

