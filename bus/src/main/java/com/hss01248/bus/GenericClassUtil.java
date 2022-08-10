package com.hss01248.bus;

import android.util.Log;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 14:16
 * @Version 1.0
 */
public class GenericClassUtil {
    /**
     * 从接口声明里获取泛型
     * @param clazz 子类
     * @param idxOfIterfaces 第几个接口,从0开始.
     *                       如果是-1,或者大于接口个数-1,则自动轮询所有接口
     * @param idxOfGeneric  第几个泛型
     * @return
     */
    public static Class getGenericFromInterfaces(Class clazz,int idxOfIterfaces,int idxOfGeneric){
        Type[] types = clazz.getGenericInterfaces();
        Log.d("class","getGenericInterfaces: "+ Arrays.toString(types));
        if(types == null || types.length ==0){
            return null;
        }
        if(idxOfIterfaces >= types.length){
            Log.w("class","idxOfIterfaces >= types.length:"+idxOfIterfaces+"/"+types.length);
            idxOfIterfaces = -1;
            //return null;
        }
        if(idxOfIterfaces <0){
            for (Type type : types) {
                try {
                    Class cl = getClass(type,idxOfGeneric);
                    if(cl != null){
                        return cl;
                    }
                }catch (Throwable throwable){
                    throwable.printStackTrace();
                }
            }
        }else {
            Type type = types[idxOfIterfaces];
            try {
                Class cl = getClass(type,idxOfGeneric);
                if(cl != null){
                    return cl;
                }
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从父类声明的泛型里获取类
     * @param clazz
     * @param idxOfGeneric
     * @return
     */
    public static Class getGenericFromSuperClass(Class clazz,int idxOfGeneric){
        Type type0 = clazz.getGenericSuperclass();
        try {
            Class cl = getClass(type0,idxOfGeneric);
            if(cl != null){
                return cl;
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 从接口和父类获取泛型
     * @param clazz
     * @param idxOfGeneric
     * @return
     */
    public static Class getGeneric(Class clazz,int idxOfGeneric){
        Class genericFromSuperClass = getGenericFromSuperClass(clazz, idxOfGeneric);
        if(genericFromSuperClass != null){
            return genericFromSuperClass;
        }
        return getGenericFromInterfaces(clazz,-1,idxOfGeneric);
    }

    /**
     * 获取第一个泛型,不管是接口还是父类
     * @param clazz
     * @return
     */
    public static Class getGeneric(Class clazz){
        return getGeneric(clazz,0);
    }


     static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) {
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);
        } else {
            return (Class) type;
        }
    }

     static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Type genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) {
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

     static Type getGenericType(ParameterizedType parameterizedType, int i) {
        Type genericType = parameterizedType.getActualTypeArguments()[i];
        if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getRawType();
        } else if (genericType instanceof GenericArrayType) {
            return ((GenericArrayType) genericType).getGenericComponentType();
        } else if (genericType instanceof TypeVariable) {
            return getClass(((TypeVariable) genericType).getBounds()[0], 0);
        } else {
            return genericType;
        }
    }
}
