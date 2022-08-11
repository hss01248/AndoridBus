/*
package com.hss01248.bus;

import android.util.Log;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

*/
/**
 * @Despciption todo
 * @Author hss
 * @Date 01/08/2022 14:16
 * @Version 1.0
 *//*

public class GenericClassUtil2 {
    */
/**
     * 从接口声明里获取泛型
     * @param clazz 子类
     * @param idxOfIterfaces 第几个接口,从0开始.
     *                       如果是-1,或者大于接口个数-1,则自动轮询所有接口
     * @param idxOfGeneric  第几个泛型
     * @return
     *//*

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

    */
/**
     * 从父类声明的泛型里获取类
     * @param clazz
     * @param idxOfGeneric
     * @return
     *//*

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

    */
/**
     * 从接口和父类获取泛型
     * @param clazz
     * @param idxOfGeneric
     * @return
     *//*

    public static Class getGeneric(Class clazz,int idxOfGeneric){
        Class genericFromSuperClass = getGenericFromSuperClass(clazz, idxOfGeneric);
        if(genericFromSuperClass != null){
            return genericFromSuperClass;
        }
        return getGenericFromInterfaces(clazz,-1,idxOfGeneric);
    }

    */
/**
     * 获取第一个泛型,不管是接口还是父类
     * @param clazz
     * @return
     *//*

    public static Class getGeneric(Class clazz){
        return getGeneric(clazz,0);
    }


     static GenericTree getClass(Type type) {
        if (type instanceof ParameterizedType) {
            return getGenericClass((ParameterizedType) type);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0);
        } else {
            return new GenericTree((Class) type);
        }
    }

     static GenericTree getGenericClass(ParameterizedType parameterizedType) {
         //List<Class> classes = new ArrayList<>();
        Type[] genericClasses = parameterizedType.getActualTypeArguments();
        if(genericClasses == null || genericClasses.length ==0){
            return null;
        }
         GenericTree tree
         for (int i = 0; i < genericClasses.length; i++) {
             Type genericClass = genericClasses[i];
            // GenericTree tree
             if (genericClass instanceof ParameterizedType) {
                 //todo 取出内层嵌套的泛型, 只适配单个泛型的情况
                 ParameterizedType innerType = (ParameterizedType) genericClass;
                 Type[] typeArguments = innerType.getActualTypeArguments();

                 classes.add((Class) ((ParameterizedType) genericClass).getRawType());
                 while (typeArguments != null && typeArguments.length> 0){
                     //只适配单个泛型的情况, 其实应该用递归获取,本身就是树型结构
                     Type typeArgument = typeArguments[0];
                     classes.add(getClass(typeArgument));
                     typeArguments = null;
                     //再取内层判断
                     // typeArguments = ((ParameterizedType) typeArgument).getActualTypeArguments();
                 }
                 Log.d("class","getActualTypeArguments:"+Arrays.toString(classes.toArray()));
                 //[class java.lang.String]
                 return (Class) ((ParameterizedType) genericClass).getRawType();
             } else if (genericClass instanceof GenericArrayType) {
                 return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
             } else if (genericClass instanceof TypeVariable) {
                 return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
             } else {
                 return (Class) genericClass;
             }
         }

    }

     */
/*static Type getGenericType(ParameterizedType parameterizedType, int i) {
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
    }*//*

}
*/
