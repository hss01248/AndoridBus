# AndroidBus

> 结合Android lifecycle的bus工具

# 使用

[![](https://jitpack.io/v/hss01248/AndoridBus.svg)](https://jitpack.io/#hss01248/AndoridBus)

## gradle

```groovy

api "com.github.hss01248.AndoridBus:bus:2.0.3"
```

## java调用

提供两种匹配Observer的方式:

* ByTag

* ByType

> 两种方式数据隔离,必须成对使用.

### byType

> 根据BusObserver上的泛型来作为key存储observer,  post的event必须和泛型class严格一致.

```java
AndroidBus.post(new LoginEvent2("user detail..."));



//void observer( @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer)
//<T>--> 根据T的类型严格匹配. 子类无效.
//once: 只observer一次,然后自动移除
//lifecycleOwner onDestory时自动移除
AndroidBus.observer( this, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","once, with life "+ obj);
            }
        });
```

### byTag

> 以tag字符串为key

```java
  AndroidBus.postByTag("userAction",LoginLogOutEvent.successLogout());


 AndroidBus.observerByTag("userAction",null,new BusObserver<LoginLogOutEvent>(){

            @Override
            public void observer(LoginLogOutEvent obj) {
                Log.e(AndroidBus.TAG,"tag -> userAction:"+ obj);
            }
        });
```



## lifecycler的更简洁api

第二个参数要传lifecyclerOwner,

此处提供一个更简洁的自适应api:   

>  ContextBusObserver(Object contextOrFragment): 自动从contextOrFragment解析出activity 或fragment.

```java
AndroidBus.observerByTag("userAction",  new ContextBusObserver<LoginLogOutEvent>(this) {
            @Override
            protected void doObserverReally(LoginLogOutEvent obj) {
                
            }
        });

AndroidBus.observer(false, new ContextBusObserver<LoginLogOutEvent>(this) {
            @Override
            protected void doObserverReally(LoginLogOutEvent obj) {
                
            }
        });
```



# 移除监听

> 传了lifecycler/activity/fragment/context时,不用管监听的移除. 
>
> 没有传,默认永远监听. 这时可以手动移除

```java
 //AndroidBus类里:

/**
     * 提供手动移除observer的api. 适用于多页面长流程结束的移除
     * @param observer
     * @param <T>
     */
    public static <T> void removeObserverMannually(BusObserver<T> observer) 
      
       public static  void removeObserverByTag(String tag)
      
       public static  void removeObserverByType(Class clazz)
      
   //在BusObserver里也有移除自己的方法:
      default void unSubscribeSelf(){
            AndroidBus.removeObserverMannually(this);
        }
```



# 日志

![image-20220801154458748](https://cdn.jsdelivr.net/gh/shuiniuhss/myimages@main/imagemac2/1659339904596-image-20220801154458748.jpg)



# 对login logout通用事件的封装

```groovy
api "com.github.hss01248.AndoridBus:login:2.0.3"
```



```java
 //登录成功时发送
AndroidBus.post( LoginLogOutEvent.successLogin("user detail...."));

// 取消登录时
AndroidBus.post(LoginLogOutEvent.fromCancelLogin());

//主动点击退出登录按钮退出
  AndroidBus.post(LoginLogOutEvent.successLogout());

//监听
 LoginLogoutObserver.observer( null, new LoginLogoutObserver<Object>() {
            @Override
            public void login(Object userDetail) {
                
            }

            @Override
            public void logout(boolean isFromLoginPage) {

            }
        });
```

