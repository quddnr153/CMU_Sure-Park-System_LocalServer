package com.localsurepark.cmu.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.domain.OauthToken;
public class HttpRestFulClient {
	
	private OauthToken oauthToken;
	
	
	public void OauthToken() throws ClientProtocolException, IOException, ParseException
	{
		HttpPost post = new HttpPost("http://localhost:8080/surepark-restful/oauth/token");
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("password", "123456"));
        nvps.add(new BasicNameValuePair("username", "ckanstnzja"));
        nvps.add(new BasicNameValuePair("grant_type", "password"));
        nvps.add(new BasicNameValuePair("scope", "read write"));
        nvps.add(new BasicNameValuePair("client_secret", "123456"));
        nvps.add(new BasicNameValuePair("client_id", "owner"));

        String enc = "owner:123456";
        post.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(enc.getBytes()));
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        /*
        for(int i =0; i< post.getAllHeaders().length;i++)
        {
           System.out.println(post.getAllHeaders()[i].toString());
        }
         */
        //System.out.println(post.toString() + post.getAllHeaders().toString()+post.getEntity());
        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        
        
        HttpResponse response = httpClient.execute(post);
        
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(responseString);
        JSONObject responseJsonObject = (JSONObject) obj;
        
        System.out.println(responseJsonObject.get("access_token"));
        
        oauthToken = new OauthToken(responseJsonObject.get("access_token").toString(), responseJsonObject.get("token_type").toString(), responseJsonObject.get("refresh_token").toString(), Integer.parseInt(responseJsonObject.get("expires_in").toString()), responseJsonObject.get("scope").toString());

        
	}
	
	public JSONObject paymentRestfulPost(String phoneNumber, int reservationID) throws ClientProtocolException, IOException, ParseException
	{
		HttpPost post = new HttpPost("http://localhost:8080/surepark-restful//payment/"+phoneNumber+"/"+reservationID);

        
        post.setHeader("Authorization",oauthToken.getToken_type()+" "+oauthToken.getAccess_token());
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        /*
        for(int i =0; i< post.getAllHeaders().length;i++)
        {
           System.out.println(post.getAllHeaders()[i].toString());
        }
        */

        //System.out.println(post.toString() + post.getAllHeaders().toString()+post.getEntity());
        DefaultHttpClient httpClient = new DefaultHttpClient();

        
        HttpResponse response = httpClient.execute(post);
        
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(responseString);
        JSONObject responseJsonObject = (JSONObject) obj;
        
        
        
        return responseJsonObject;
        
       
	}
	
	
	
	

}
