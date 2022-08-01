# AndroidBus

> 结合Android lifecycle的bus工具

# 使用

```java
AndroidBus.post(new LoginEvent2("user detail..."));



//void observer(boolean once, @Nullable LifecycleOwner lifecycleOwner, @NonNull BusObserver<T> observer)
//<T>--> 根据T的类型严格匹配. 子类无效.
//once: 只observer一次,然后自动移除
//lifecycleOwner onDestory时自动移除
AndroidBus.observer(true, this, new BusObserver<LoginEvent2>() {
            @Override
            public void observer(LoginEvent2 obj) {
                Log.i("observer","once, with life "+ obj);
            }
        });
```