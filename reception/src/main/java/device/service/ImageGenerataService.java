package device.service;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import utils.PathUtil;
import bean.Word;

@Service
public class ImageGenerataService {
	public String generateLineChart(List<Integer> outside, List<Integer> inside, List<String> weekdays, String userID){
		String payLoadLogin = "{\"infile\":\"{\\\"xAxis\\\": { \\\"categories\\\": [ \\\"一月\\\", \\\"二月\\\", \\\"三月\\\",\\\"四月\\\" ] },\\\"series\\\": [{ \\\"data\\\": [1,3,2,4],\\\"type\\\": \\\"line\\\" }, {\\\"data\\\": [5,3,4,2],\\\"type\\\":\\\"line\\\"} ]}\",\"width\":false,\"scale\":false,\"constr\":\"Chart\",\"callback\":\"function(chart) {chart.renderer.label('This label is added in the callback', 100, 100)\\n    .attr({      fill : '#90ed7d',padding: 10,r: 10,zIndex: 10}).css({color: 'black',width: '400px'}) .add();}\",\"styledMode\":false,\"type\":\"image/png\"}";
    	String start = "{\"infile\":\"{XXXXX}\",\"width\":false,\"scale\":false,\"constr\":\"Chart\",\"styledMode\":false,\"width\":1400}";

    	String credits = "'credits': {'enabled': false},";
    	String exporting = "'exporting': {'enabled': false},";
    	String chart = "'chart': {'type': 'spline'},";
    	String title = "'title': {'align': 'left','text': '近日空气质量对比', 'style':{'fontSize': '13px'}},";
    	String colors = "'colors': ['#11c1f3','#f282aa'],";
    	String xAxis = "'xAxis': {'categories': weekdays ,'crosshair': true},";
    	String yAxis = "'yAxis': {'min': 0,'title': {'text': '空气污染指数'}},";
    	String legend = "'legend': {'align': 'right','verticalAlign': 'top'},";
    	String series = "'series': [{'name': '室内','data': insideData}, {'name': '室外','data': outsideData}]";
    	
    	
    	xAxis =xAxis.replaceAll("weekdays", weekdays.toString().replaceAll(",", "','").replaceAll("\\[", "\\['").replaceAll("\\]", "'\\]"));
    	series = series.replaceAll("insideData", inside.toString());
    	series = series.replaceAll("outsideData", outside.toString());
    	String data = credits+exporting+chart+title+colors+xAxis+yAxis+legend+series;
    	String content = start.replaceAll("XXXXX", data);
    	
    	HttpPost post = new HttpPost("http://export.highcharts.com");


        HttpClient client = HttpClientBuilder.create().build();
        post.setEntity(new StringEntity(content, 
                ContentType.create("application/json",Charset.forName("UTF-8"))));
        try{
            HttpResponse response = client.execute(post);
            InputStream is = response.getEntity().getContent();
            String filePath = PathUtil.retrivePath()+"/material/img/chart_"+userID+".png";
            FileOutputStream fos = new FileOutputStream(new File(filePath));

            int inByte;
            while((inByte = is.read()) != -1) fos.write(inByte);
            is.close();
            fos.close();
            return filePath;
        }catch(Exception e){
        	e.printStackTrace();
        }
    	return null;
	}
	
	public BufferedImage loadImageLocal(String imgName) {  
        try {  
            return ImageIO.read(new File(imgName));  
        } catch (IOException e) {  
            System.out.println(e.getMessage());  
        }  
        return null;  
    }  
	
    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {  
    	Graphics2D g = null;  
        try {  
            int w = b.getWidth();  
            int h = b.getHeight();  
              
  
            g = d.createGraphics();  
            g.drawImage(b, 50,2050, w, h, null);  
            g.dispose();  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
  
        return d;  
    }  
    
    public void writeImageLocal(String newImage, BufferedImage img) {  
        if (newImage != null && img != null) {  
            try {  
                File outputfile = new File(newImage);  
                ImageIO.write(img, "png", outputfile);  
            } catch (IOException e) {  
                System.out.println(e.getMessage());  
            }  
        }  
    }
    
    public BufferedImage modifyImage(BufferedImage img, List<Word> contents){
    	Graphics2D g = null;
    	try {  
    		
            int w = img.getWidth();  
            int h = img.getHeight();  
            g = img.createGraphics();
            for (Word word : contents) {
            	g.setColor(word.getColor());//设置字体颜色 
            	Font font = new Font("宋体", word.getWeight(), word.getSize());
            	g.setFont(font);
            	int x = word.getX();
            	int y = word.getY();
            	// 验证输出位置的纵坐标和横坐标  
                if (y >= h || x >= w) {  
                    y = h - word.getSize() + 2;  
                    x = w;  
                } 
                if (word.getContent() != null) {  
                    g.drawString(word.getContent(), x, y);  
                }  
			}
            g.dispose();  
        } catch (Exception e) {  
            System.out.println(e.getMessage());  
        }  
    	
    	return img;
    }
}
