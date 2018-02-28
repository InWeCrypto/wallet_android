package com.inwecrypto.wallet.common.http.callback;

import com.google.gson.stream.JsonReader;
import com.inwecrypto.wallet.R;
import com.lzy.okgo.convert.Converter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.inwecrypto.wallet.common.Constant;
import com.inwecrypto.wallet.common.http.Convert;
import com.inwecrypto.wallet.common.http.LzyResponse;
import com.inwecrypto.wallet.common.http.SimpleResponse;
import com.inwecrypto.wallet.common.util.ToastUtil;
import com.inwecrypto.wallet.event.BaseEventBusBean;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;

    public JsonConvert() {
    }

    public JsonConvert(Type type) {
        this.type = type;
    }

    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用


        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response, type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else {
            T t = Convert.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = Convert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null) return null;
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != LzyResponse.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = Convert.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
                response.close();
                //noinspection unchecked
                return (T) simpleResponse.toLzyResponse();
            } else {
                String bodys=body.string().toString();
                if (bodys.contains("4000")){
                    // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                    LzyResponse lzyResponse = Convert.fromJson(bodys, type);
                    response.close();
                    return (T) lzyResponse;
                }else if (bodys.contains("4001")||bodys.contains("4010")) {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_TOKEN));
                    throw new IllegalStateException("4001:Please login");
                }else if (bodys.contains("4010")) {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_TOKEN));
                    throw new IllegalStateException("4010:Please login");
                } else if (bodys.contains("4002")) {
                    ToastUtil.show("Privilege access");
                    throw new IllegalStateException("4002:wallet_error:No permission");
                } else if (bodys.contains("4003")) {
                    throw new IllegalStateException("4003:wallet_error:Routing does not exist");
                }else if (bodys.contains("4004")) {
                    throw new IllegalStateException("4004:wallet_error:Validation does not pass");
                }else if (bodys.contains("4005")) {
                    throw new IllegalStateException("4005:wallet_error:Query data does not exist");
                }else if (bodys.contains("4006")) {
                    throw new IllegalStateException("4006:wallet_error:Request execution failure");
                }else if (bodys.contains("4007")) {
                    throw new IllegalStateException("4007:wallet_error:The request succeeds and is about to jump");
                }else if (bodys.contains("4008")) {
                    throw new IllegalStateException("4008:wallet_error:unregistered");
                }else if (bodys.contains("4009")) {
                    EventBus.getDefault().post(new BaseEventBusBean(Constant.EVENT_TOKEN));
                    throw new IllegalStateException("4009:Token expired");
                }else if (bodys.contains("5201")) {
                    throw new IllegalStateException("5201:wallet_error");
                }else {
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException("wallet_error:unknown error");
                }
            }
        }
    }
}
