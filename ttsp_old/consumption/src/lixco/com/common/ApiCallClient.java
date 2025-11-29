package lixco.com.common;


import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by phuc on 15/07/2019.
 */

public class ApiCallClient extends OkHttpClient{
	static OkHttpClient instance;
    static{
    	instance=new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build();
    	
    }
    
    public static Call loginApi(String url,String username,String password){
        try{
            RequestBody formBody = new FormBody.Builder(StandardCharsets.UTF_8)
                    .add("user", username)
                    .add("pass", password)
                    .add("brand","HO CHI MINH")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Call call = instance.newCall(request);
            return call;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Call getListObjectWithParam(String url,String table,String command,String dataJson){
        try{
            RequestBody formBody = RequestBody.create("cm="+command+"&dt="+escapeJavascript(dataJson),MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url+table)
                    .post(formBody)
                    .build();
            Call call = instance.newCall(request);
            return call;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Call getListObjectWithParamMethodGet(String url,String table,String command,String data){
    	try{
	    	HttpUrl.Builder urlBuilder = HttpUrl.parse(url +table).newBuilder();
	        urlBuilder.addQueryParameter("cm", command);
	        urlBuilder.addQueryParameter("dt", data);
	        String urlRender = urlBuilder.build().toString();
	        Request request = new Request.Builder()
	        .url(urlRender)
	        .build();
	       Call call = instance.newCall(request);
	       return call;
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	return null;
    }
    public static Call updateObject(String url,String table,String command,String dataJson){
        try{
            RequestBody formBody = RequestBody.create("cm="+command+"&dt="+escapeJavascript(dataJson),MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(url+table)
                    .post(formBody)
                    .build();
            Call call = instance.newCall(request);
            return call;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Call addObject(String url,String table,String dataJson){
    	try{
    		RequestBody body=RequestBody.create("cm=add&dt="+escapeJavascript(dataJson),MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
    		Request request=new Request.Builder()
    				.url(url+table)
    				.post(body)
    				.build();
    		Call call= instance.newCall(request);
    		return call;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    public static Call deleteObject(String url,String table,String dataJson){
    	try{
    		RequestBody formBody=RequestBody.create("cm=delete&dt="+dataJson,MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
    		Request request=new Request.Builder()
    				.url(url+table)
    				.post(formBody)
    				.build();
    		Call call= instance.newCall(request);
    		return call;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    public static String escapeJavascript(String data) {
        try {
        	  data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
              data = data.replaceAll("\\+", "%2B");
              return data;
        } catch (Exception ex) {
        	ex.printStackTrace();
            return null;
        }
    }

    public static Call apiCommand(String url,String table,String command,String dataJson,String headerJson){
    	try{
    		/*headerJson:{authorization:'',content_type:''}*/
    	  JsonObject  header=JsonParserUtil.getGson().fromJson(headerJson, JsonObject.class);	  
   		  RequestBody body = RequestBody.create("cm="+command+"&dt="+dataJson,MediaType.parse(header.get("content_type").getAsString()));
          Request request = new Request.Builder().addHeader("Authorization",header.get("authorization").getAsString())
           		  .addHeader("Content-Type", header.get("content_type").getAsString())
                     .url(url+table)
                     .post(body)
                     .build();
             Call call = instance.newCall(request);
             return call;
    	}catch (Exception e) {
		}
    	return null;
    }
    public static Call apiInsertUpdateInvoice(String url,String table,String dataJson,String headerJson){
    	try{
    		  /*headerJson:{authorization:'',tax_code:'',content_type:''}*/
    		  /*dataJson:{data:{}}*/
    		  JsonObject  header=JsonParserUtil.getGson().fromJson(headerJson, JsonObject.class);	  
    		  RequestBody body = RequestBody.create(dataJson,MediaType.parse(header.get("content_type").getAsString()));
              Request request = new Request.Builder().addHeader("Authorization",header.get("authorization").getAsString())
            		  .addHeader("TaxCode",header.get("tax_code").getAsString())
            		  .addHeader("Content-Type", header.get("content_type").getAsString())
                      .url(url+table)
                      .post(body)
                      .build();
              Call call = instance.newCall(request);
              return call;
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    public static Call getEInvoiceByID(String url,String table,String guiid,String headerJson){
    	try{
    		JsonObject  header=JsonParserUtil.getGson().fromJson(headerJson, JsonObject.class);	  
            Request request = new Request.Builder().addHeader("Authorization",header.get("authorization").getAsString())
          		  .addHeader("TaxCode",header.get("tax_code").getAsString())
          		  .addHeader("Content-Type", header.get("content_type").getAsString())
          		  .url(url+table+"/"+guiid)
                  .get()
                  .build();
            Call call = instance.newCall(request);
            return call;
    	}catch (Exception e) {
		}
    	return null;
    }
    public static Call deleteEInvoiceByID(String url,String table,String guiid,String headerJson){
    	try{
    		JsonObject  header=JsonParserUtil.getGson().fromJson(headerJson, JsonObject.class);
            Request request = new Request.Builder().addHeader("Authorization",header.get("authorization").getAsString())
          		  .addHeader("TaxCode",header.get("tax_code").getAsString())
          		  .addHeader("Content-Type", header.get("content_type").getAsString())
          		  .url(url+table+"/"+guiid)
                  .delete()
                  .build();
            Call call = instance.newCall(request);
            return call;
    	}catch (Exception e) {
		}
    	return null;
    }
    public static Call getToken(String url,String table,String hearderJson,String formData){
    	try{
    		/*headerJson:{content_type:'application/x-www-form-urlencoded',tax_code:'0101243150-938'}*/
    		/*formData:grant_type=password&username=hieunguyen.ht3@gmail.com&password=12345678a*/
    		JsonObject header=JsonParserUtil.getGson().fromJson(hearderJson,JsonObject.class);
    		RequestBody formBody = RequestBody.create(formData,MediaType.parse(header.get("content_type").getAsString()));
            Request request = new Request.Builder()
            		.addHeader("TaxCode",header.get("tax_code").getAsString())
                    .url(url+table)
                    .post(formBody)
                    .build();
            Call call = instance.newCall(request);
            return call;
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
}
