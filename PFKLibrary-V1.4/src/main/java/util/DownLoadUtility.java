package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadUtility {

//	private String urlStr="http://172.17.54.91:8080/download/down.txt";
	public DownLoadUtility() {
		// TODO Auto-generated constructor stub
	}

	public String downLoad(String urlStr) {
		try {  
			/* 
			 * 通过URL取得HttpURLConnection 
			 * 要网络连接成功，需在AndroidMainfest.xml中进行权限配置 
			 * <uses-permission android:name="android.permission.INTERNET" /> 
			 */  
			URL url=new URL(urlStr);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			//取得inputStream，并进行读取  
			InputStream input=conn.getInputStream();
			BufferedReader in=new BufferedReader(new InputStreamReader(input));
			String line=null;
			StringBuffer sb=new StringBuffer();
			while((line=in.readLine())!=null){  
				sb.append(line);  
			}  
			System.out.println(sb.toString());
			return sb.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();  
		}  
		return "false";
	}
	
	/**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public  void  downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);    

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+ File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData); 
        if(fos!=null){
            fos.close();  
        }
        if(inputStream!=null){
            inputStream.close();
        }



    }
    
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    } 

}
