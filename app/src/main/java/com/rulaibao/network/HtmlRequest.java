package com.rulaibao.network;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rulaibao.bean.Collection2B;
import com.rulaibao.bean.CommissionNewsList1B;
import com.rulaibao.bean.HomeIndex2B;
import com.rulaibao.bean.InsuranceDetail1B;
import com.rulaibao.bean.InsuranceProduct1B;
import com.rulaibao.bean.InsuranceProduct2B;
import com.rulaibao.bean.InteractiveNewsList1B;
import com.rulaibao.bean.MineData2B;
import com.rulaibao.bean.MyAskList1B;
import com.rulaibao.bean.MyCollectionList1B;
import com.rulaibao.bean.MyTopicList1B;
import com.rulaibao.bean.NewMembersCircleList1B;
import com.rulaibao.bean.OK2B;
import com.rulaibao.bean.Plan2B;
import com.rulaibao.bean.PlatformBulletinList1B;
import com.rulaibao.bean.PolicyBookingDetail1B;
import com.rulaibao.bean.PolicyBookingList1B;
import com.rulaibao.bean.PolicyPlan2B;
import com.rulaibao.bean.PolicyRecordDetail1B;
import com.rulaibao.bean.PolicyRecordList1B;
import com.rulaibao.bean.Recommend1B;
import com.rulaibao.bean.RecommendRecordList1B;
import com.rulaibao.bean.RecommendRecordList2B;
import com.rulaibao.bean.RenewalReminderList1B;
import com.rulaibao.bean.Repo;
import com.rulaibao.bean.TrackingDetail1B;
import com.rulaibao.bean.TrackingList1B;
import com.rulaibao.bean.ResultAnswerDetailsBean;
import com.rulaibao.bean.ResultAskDetailsAnswerBean;
import com.rulaibao.bean.ResultAskDetailsBean;
import com.rulaibao.bean.ResultAskIndexBean;
import com.rulaibao.bean.ResultAskTypeBean;
import com.rulaibao.bean.ResultClassDetailsCatalogBean;
import com.rulaibao.bean.ResultClassDetailsDiscussBean;
import com.rulaibao.bean.ResultClassDetailsIntroductionBean;
import com.rulaibao.bean.ResultClassDetailsPPTBean;
import com.rulaibao.bean.ResultClassIndexBean;
import com.rulaibao.bean.UnreadNewsCount2B;
import com.rulaibao.bean.UserInfo2B;
import com.rulaibao.bean.ResultCircleDetailsBean;
import com.rulaibao.bean.ResultCircleDetailsTopicCommentListBean;
import com.rulaibao.bean.ResultCircleDetailsTopicDetailsBean;
import com.rulaibao.bean.ResultCircleDetailsTopicListBean;
import com.rulaibao.bean.ResultCircleIndexBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.common.Urls;
import com.rulaibao.bean.ResultCycleIndex2B;
import com.rulaibao.bean.ResultHotAskBean;
import com.rulaibao.network.http.SimpleHttpClient;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.uitls.encrypt.MD5;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HtmlRequest<T> extends BaseRequester<T> {
    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        // cookieManager.removeSessionCookie();// 移除
        try {
            cookieManager.setCookie(url, DESUtil.decrypt(PreferenceUtil.getCookie()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 对入参进行升序排列
     *
     * @param map 无序的入参
     * @return （升序：如：a,b,c....）排序后的入参
     */
    public static Map<String, Object> sortMap(Map<String, Object> map) {
        if (map == null) {
            return new HashMap<>();
        } else if (map.isEmpty()) {
            return map;
        }

        Map<String, Object> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        sortMap.putAll(map);

        return sortMap;
    }

    public static String getResult(Map<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
//        Log.i("hh", "排序后的入参为：" + sortMap);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);

        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
//            Log.i("hh", "传给后台的入参为：" + encrypt);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getResult(ArrayMap<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
//        Log.i("hh", "排序后的入参为：" + sortMap);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);

        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
//            Log.i("hh", "传给后台的入参为：" + encrypt);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String getResultLinked(LinkedHashMap<String, Object> param) {
        Gson gson = new Gson();
        Map<String, Object> sortMap = sortMap(param);
        String str_md5 = gson.toJson(sortMap);
        String md5 = MD5.stringToMD5(str_md5);
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", sortMap);
            String encrypt = gson.toJson(map);
            result = DESUtil.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResultNoEncrypt(Map<String, Object> param) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(param));
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
            result = gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResultNoEncryptLinked(LinkedHashMap<String, Object> param) {
        Gson gson = new Gson();
        String md5 = MD5.stringToMD5(gson.toJson(param));
        String result = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("check", md5);
            map.put("data", param);
            result = gson.toJson(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     *注册
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void signup(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SIGN_UP;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 登出
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void loginoff(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_LOGINOFF;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    /**
     * 找回密码
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void findPassword(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_FINDPASSWORD;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    /**
     * 发送验证码
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void sendSMS(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_SENDSMS;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 首页
     *
     * @param context  上下文
     * @param listener 监听
     * @return 返回数据
     */
    public static void getHomeData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<HomeIndex2B> b = json.fromJson(data, new TypeToken<Repo<HomeIndex2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 首页---保险产品数据
    public static void getInsuranceProductData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INSURANCE_PRODUCT;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<InsuranceProduct1B> b = json.fromJson(data, new TypeToken<Repo<InsuranceProduct1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 保险产品搜索
    public static void getInsuranceProductSearch(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INSURANCE_PRODUCT_SEARCH;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<InsuranceProduct1B> b = json.fromJson(data, new TypeToken<Repo<InsuranceProduct1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 首页---计划书数据
    public static void getPlanData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_PLAN;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<Plan2B> b = json.fromJson(data, new TypeToken<Repo<Plan2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 计划书搜索
    public static void getPlanSearch(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_PLAN_SEARCH;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<Plan2B> b = json.fromJson(data, new TypeToken<Repo<Plan2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 首页---保单规划数据
    public static void getPolicyPlanData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_POLICY_PLAN;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<PolicyPlan2B> b = json.fromJson(data, new TypeToken<Repo<PolicyPlan2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 保险详情
    public static void getInsuranceDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_INSURANCE_DETAILS;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<InsuranceDetail1B> b = json.fromJson(data, new TypeToken<Repo<InsuranceDetail1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    // 保险详情--收藏取消收藏
    public static void collection(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_COLLECTION;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<Collection2B> b = json.fromJson(data, new TypeToken<Repo<Collection2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    // 保险详情--预约
    public static void apponitmentAdd(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APPOINTMENT_ADD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);

                    Repo<Collection2B> b = json.fromJson(data, new TypeToken<Repo<Collection2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 测试
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTest(final Context context, LinkedHashMap<String, Object> param1, OnRequestListener listener) {
        final String data = getResult(param1);
        final String url = Urls.URL_HOME_ADVERTISE;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    entity = new StringEntity(data);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<MouldList<ResultCycleIndex2B>> b = json.fromJson(data, new TypeToken<Repo<MouldList<ResultCycleIndex2B>>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 我的页面数据
     * @param context
     * @param param
     * @param listener
     */
    public static void getMineData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MINE_DATA;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我的页面数据：" + data);

                    Repo<MineData2B> b = json.fromJson(data, new TypeToken<Repo<MineData2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取个人信息/销售认证等两个页面数据
     * @param context
     * @param param
     * @param listener
     */
    public static void getAppUserInfoData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APP_USER_INFO;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "个人信息/销售认证页面数据：" + data);

                    Repo<UserInfo2B> b = json.fromJson(data, new TypeToken<Repo<UserInfo2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  (销售认证页)提交认证
     * @param context
     * @param param
     * @param listener
     */
    public static void UserInfoSubmit(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APP_USER_INFO_SUBMIT;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "提交认证接口：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取未读消息数
     * @param context
     * @param param
     * @param listener
     */
    public static void getUnreadNewsCount(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MESSAGE_TYPE_COUNT;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "未读消息数口：" + data);

                    Repo<UnreadNewsCount2B> b = json.fromJson(data, new TypeToken<Repo<UnreadNewsCount2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取佣金/保单消息列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getMessageListData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MESSAGES_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "佣金/保单消息列表：" + data);

                    Repo<CommissionNewsList1B> b = json.fromJson(data, new TypeToken<Repo<CommissionNewsList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取互动消息列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getInteractiveNewsList(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_USER_INTERACT_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "互动消息列表：" + data);

                    Repo<InteractiveNewsList1B> b = json.fromJson(data, new TypeToken<Repo<InteractiveNewsList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  同意新成员加入申请
     * @param context
     * @param param
     * @param listener
     */
    public static void getCircleApplyAgreeData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CIRCLE_APPLY_AGREE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "同意新成员加入申请：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  删除成员申请信息
     * @param context
     * @param param
     * @param listener
     */
    public static void getDeletCircleApplyData(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CIRCLE_APPLY_DELETE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "删除成员申请信息：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取圈子新成员列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getNemMembersCircleList(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_CIRCLE_APPLY_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "圈子新成员列表：" + data);

                    Repo<NewMembersCircleList1B> b = json.fromJson(data, new TypeToken<Repo<NewMembersCircleList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  推荐app给好友
     * @param context
     * @param param
     * @param listener
     */
    public static void getRecommendInfo(final Context context, HashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_SET_RECOMMENDAppTo;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "推荐app给好友：" + data);

                    Repo<Recommend1B> b = json.fromJson(data, new TypeToken<Repo<Recommend1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  获取推荐记录数据
     * @param context
     * @param param
     * @param listener
     */
    public static void getRecommendRecordData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_SET_RECOMMEND_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "推荐记录：" + data);

                    Repo<RecommendRecordList1B> b = json.fromJson(data, new TypeToken<Repo<RecommendRecordList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 我的 - 累计佣金 - 交易记录
     * @param context
     * @param param
     * @param listener
     */
    public static void getTradeRecordData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_TRADE_RECORD;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "交易记录：" + data);

                    Repo<TrackingList1B> b = json.fromJson(data, new TypeToken<Repo<TrackingList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  交易记录-明细
     * @param context
     * @param param
     * @param listener
     */
    public static void getTradeRecordDetail(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_TRADE_RECORD_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "交易记录--明细：" + data);

                    Repo<TrackingDetail1B> b = json.fromJson(data, new TypeToken<Repo<TrackingDetail1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  保单列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getPolicyRecordListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_ORDER_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "保单列表：" + data);

                    Repo<PolicyRecordList1B> b = json.fromJson(data, new TypeToken<Repo<PolicyRecordList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  保单详情
     * @param context
     * @param param
     * @param listener
     */
    public static void getPolicyRecordDetail(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_ORDER_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "保单详情：" + data);

                    Repo<PolicyRecordDetail1B> b = json.fromJson(data, new TypeToken<Repo<PolicyRecordDetail1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  续保提醒列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getRenewalReminderData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_ORDER_RENEWAL_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "续保提醒列表：" + data);

                    Repo<RenewalReminderList1B> b = json.fromJson(data, new TypeToken<Repo<RenewalReminderList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  (我的)预约列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getPolicyBookingListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_APPOINTMENT_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我的--预约列表：" + data);

                    Repo<PolicyBookingList1B> b = json.fromJson(data, new TypeToken<Repo<PolicyBookingList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  预约详情
     * @param context
     * @param param
     * @param listener
     */
    public static void getPolicyBookingDetailData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_APPOINTMENT_DETAIL;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "预约详情：" + data);

                    Repo<PolicyBookingDetail1B> b = json.fromJson(data, new TypeToken<Repo<PolicyBookingDetail1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     *  取消预约
     * @param context
     * @param param
     * @param listener
     */
    public static void getPolicyBookingCanceled(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_APPOINTMENT_DELETE;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "取消预约：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }
        });
    }

    /**
     *  我的提问列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getMyAskListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MY_ASK_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我的提问列表：" + data);

                    Repo<MyAskList1B> b = json.fromJson(data, new TypeToken<Repo<MyAskList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }
    /**
     *  我参与的（提问列表）
     * @param context
     * @param param
     * @param listener
     */
    public static void getMyPartakeAskListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APPQUESTION_MYJOIN_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我参与的---提问列表：" + data);

                    Repo<MyAskList1B> b = json.fromJson(data, new TypeToken<Repo<MyAskList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 我参与的（话题列表）
     * @param context
     * @param param
     * @param listener
     */
    public static void getMyPartakeTopicListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_APPTOPIC_MYJOIN_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我参与的---话题列表：" + data);

                    Repo<MyTopicList1B> b = json.fromJson(data, new TypeToken<Repo<MyTopicList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     *  我的话题列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getMyTopicListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_MY_TOPIC_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我的话题列表：" + data);

                    Repo<MyTopicList1B> b = json.fromJson(data, new TypeToken<Repo<MyTopicList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  平台公告列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getBulletinListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_BULLETIN_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "公告列表：" + data);

                    Repo<PlatformBulletinList1B> b = json.fromJson(data, new TypeToken<Repo<PlatformBulletinList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  联系客服
     * @param context
     * @param param
     * @param listener
     */
    public static void getFeedbackData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_FEEDBACK_ADD;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "联系客服：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  我的收藏列表
     * @param context
     * @param param
     * @param listener
     */
    public static void getCollectionListData(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_COLLECTION_LIST;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "我的收藏列表：" + data);

                    Repo<MyCollectionList1B> b = json.fromJson(data, new TypeToken<Repo<MyCollectionList1B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     *  修改登录密码
     * @param context
     * @param param
     * @param listener
     */
    public static void modifyPassword(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_ACCOUNT_SET_PASSWORD_MODIFY;

        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                String data=null;
                Gson json = new Gson();
                if (isCancelled() || result == null) {
                    return null;
                }
                try {
                    data = DESUtil.decrypt(result);
                    Log.i("hh", "修改登录密码：" + data);

                    Repo<OK2B> b = json.fromJson(data, new TypeToken<Repo<OK2B>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /************************************************* 研修模块start *****************************************************************/

    /**
     * 研修首页课程
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingIndexClass(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_RESEARCH;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassIndexBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassIndexBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修首页热门问答
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingHotAskClass(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_HOT_ASK;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultHotAskBean> b = json.fromJson(data, new TypeToken<Repo<ResultHotAskBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修首页 - 换一换
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingRefreshClass(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_REFRESH;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassIndexBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassIndexBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 研修课程首页
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingClassList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_LIST;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassIndexBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassIndexBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }



    /**
     * 研修圈子首页
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleIndex(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_INDEX;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleIndexBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleIndexBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }



    /**
     * 研修圈子详情
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_DETAILS;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleDetailsBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleDetailsBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }



    /**
     * 研修圈子详情话题列表
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleDetailsTopic(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_DETAILS_TOPIC;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleDetailsTopicListBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleDetailsTopicListBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子详情---话题详情
     *
     * @param context  上下文
     * @param listener 监听事件
     *
     */
    public static void getTrainingCircleDetailsTopicDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_DETAILS_TOPIC_DETAILS;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleDetailsTopicDetailsBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleDetailsTopicDetailsBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }



    /**
     * 研修圈子详情---话题详情 评论列表
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleCommentList(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_COMMENT_LIST;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleDetailsTopicCommentListBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleDetailsTopicCommentListBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子详情---回复评论
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleReply(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_REPLY;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 研修圈子详情---点赞
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleZan(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_LIKE;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子---发布话题
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleAddTopic(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_ADD_TOPIC;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子---设置权限
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleSetAuthority(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_SET_AUTHORITY;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子---话题置顶或者取消
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingCircleSetTop(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_SET_TOP;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子---加入圈子
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAddCircle(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_JOIN;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子---退出圈子
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingOutCircle(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CIRCLE_OUT;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修问答类型
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAskType(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ASK_TYPE;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
//                client.get(url);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultAskTypeBean> b = json.fromJson(data, new TypeToken<Repo<ResultAskTypeBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修问答首页
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAskIndex(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ASK_INDEX;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultAskIndexBean> b = json.fromJson(data, new TypeToken<Repo<ResultAskIndexBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修问答详情
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAskDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ASK_DETAILS;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultAskDetailsBean> b = json.fromJson(data, new TypeToken<Repo<ResultAskDetailsBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修问答详情回答列表
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAskDetailsAnswer(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ASK_DETAILS_ANSWER;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultAskDetailsAnswerBean> b = json.fromJson(data, new TypeToken<Repo<ResultAskDetailsAnswerBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修回答详情
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAnswerDetails(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ANSWER_DETAILS;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultAnswerDetailsBean> b = json.fromJson(data, new TypeToken<Repo<ResultAnswerDetailsBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 研修问答详情评论列表
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingDetailsAnswerConmment(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ASK_DETAILS_ANSWER_COMMENT;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultCircleDetailsTopicCommentListBean> b = json.fromJson(data, new TypeToken<Repo<ResultCircleDetailsTopicCommentListBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 发布提问
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingToAsk(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_TOASK;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 回答
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingToAnswer(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_TOANSWER;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修圈子详情---回复评论
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAnswerReply(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ANSWER_REPLY;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 研修回答详情---点赞
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getTrainingAnswerZan(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_ANSWER_LIKE;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 课程详情--简介
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getClassDetailsDesc(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_DETAILS_DESC;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassDetailsIntroductionBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassDetailsIntroductionBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 课程详情--目录
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getClassDetailsCatalog(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_DETAILS_CATALOG;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassDetailsCatalogBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassDetailsCatalogBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }


    /**
     * 课程详情--研讨
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getClassDetailsDiscuss(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_DETAILS_DISCUSS;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassDetailsDiscussBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassDetailsDiscussBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }



    /**
     * 课程详情--研讨 回复
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getClassDetailsDiscussReply(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_DETAILS_DISCUSS_REPLY;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultInfoBean> b = json.fromJson(data, new TypeToken<Repo<ResultInfoBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }

    /**
     * 课程详情--PPT
     *
     * @param context  上下文
     * @param listener 监听事件
     */
    public static void getClassDetailsPPT(final Context context, LinkedHashMap<String, Object> param, OnRequestListener listener) {
        final String data = getResult(param);
        final String url = Urls.URL_TRAINING_CLASS_DETAILS_PPT;
//        final String url = Urls.URL_INDEX;
        getTaskManager().addTask(new MyAsyncTask(buildParams(context, listener, url)) {
            @Override
            public Object doTask(BaseParams params) {
                SimpleHttpClient client = new SimpleHttpClient(context, SimpleHttpClient.RESULT_STRING);
                HttpEntity entity = null;
                try {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    nvps.add(new BasicNameValuePair("requestKey", data));
                    entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                client.post(url, entity);
                String result = (String) client.getResult();
                Gson json = new Gson();

                String data = null;
                try {
                    if (isCancelled() || result == null) {
                        return null;
                    }
                    data = DESUtil.decrypt(result);

                    Repo<ResultClassDetailsPPTBean> b = json.fromJson(data, new TypeToken<Repo<ResultClassDetailsPPTBean>>() {
                    }.getType());

                    return b.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void onPostExecute(Object result, BaseParams params) {
                params.result = result;
                params.sendResult();
            }

        });
    }




    /************************************************* 研修模块end *****************************************************************/






}
