package com.yedona.aopclick;


import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;


@Aspect
public class AopClickAspect {


    private static final String TAG = AopClickAspect.class.getSimpleName();


    /**
     * 拦截之前进行的操作
     */
    @Before("dealWithNormal()||dealWithLambda()||onClickInXmlPointcuts()")
    public void beforePoint(JoinPoint joinPoint) {
        //拦截时的日志打印
    }

    @Pointcut("execution(void com..lambda*(android.view.View))")
    public void dealWithLambda() {
    }

    @Pointcut("execution(void android.view.View.OnClickListener.onClick(..))")
    public void dealWithNormal() {
    }

    @Pointcut("execution(* android.support.v7.app.AppCompatViewInflater.DeclaredOnClickListener.onClick(..))")
    public void onClickInXmlPointcuts() {
    }

    @Around("dealWithNormal()||dealWithLambda()||onClickInXmlPointcuts()")
    public void onViewClicked(ProceedingJoinPoint joinPoint) {
        try {

            Log.d("yedona", "onViewClicked: 有点击事件进来了");

            if (!AopClickUtils.isFilter) {
                //如果关闭，就不拦截，直接执行
                joinPoint.proceed();
                return;
            }

            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method method = methodSignature.getMethod();
                // 如果有 Except 注解，就不需要处理
                boolean isExcept = method != null && method.isAnnotationPresent(Except.class);
                if (isExcept) {
                    joinPoint.proceed();
                    Log.d("yedona", "onViewClicked: 有注解");
                    return;
                }
            }
            Object[] args = joinPoint.getArgs();
            View view = getViewFromArgs(args);
            if (view == null) {
                //位置类型，直接执行点击事件
                joinPoint.proceed();
                Log.d("yedona", "onViewClicked: 未知类型");
                return;
            }
            Long lastClickTime = getTimeLeack(view);
            if (lastClickTime == null) {
                //第一次点击，直接执行点击事件
                Log.d("yedona", "onViewClicked: 第一次点击");
                //给View设置点击时的时间
                setTime(view);
                joinPoint.proceed();
                return;
            }
            //判断两次点击之间的时间
            if (canClick(lastClickTime)) {
                //超过限定时间，直接执行点击事件
                Log.d("yedona", "onViewClicked: 超过时间");
                setTime(view);
                joinPoint.proceed();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //用来缓存最近点击过的
    private LinkedHashMap<String, Long> map = new LinkedHashMap<String, Long>() {

        @Override
        protected boolean removeEldestEntry(Entry<String, Long> eldest) {
            return size() > 10;
        }
    };

    private Long getTimeLeack(View view) {

        if (view == null) {
            return null;
        }
        if (map.containsKey(String.valueOf(view.hashCode()))) {
            return map.get(String.valueOf(view.hashCode()));
        } else {
            return null;
        }
    }

    private void setTime(View view) {

        if (view == null) {
            return;
        }
        map.put(String.valueOf(view.hashCode()), SystemClock.elapsedRealtime());

    }


    private View getViewFromArgs(Object[] args) {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }


    private boolean canClick(long lastClickTime) {
        return SystemClock.elapsedRealtime() - lastClickTime >= AopClickUtils.sCheckTime;
    }


}
