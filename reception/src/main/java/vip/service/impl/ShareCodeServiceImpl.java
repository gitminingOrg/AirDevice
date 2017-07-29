package vip.service.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifThumbnailDirectory;

import utils.PathUtil;
import utils.ResponseCode;
import utils.ResultData;
import vip.service.ShareCodeService;

@Service
public class ShareCodeServiceImpl implements ShareCodeService {
	private Logger logger = LoggerFactory.getLogger(ShareCodeServiceImpl.class);

	private final static String TEMPLATE_FG_PATH = "/material/image/sharecode.png";
	
	@Override
	public ResultData customizeShareCode(String path, String value) {
		ResultData result = new ResultData();
		try {
			StringBuffer pic = new StringBuffer(PathUtil.retrivePath()).append(path);
			StringBuffer fg = new StringBuffer(PathUtil.retrivePath()).append(TEMPLATE_FG_PATH);
			BufferedImage big = ImageIO.read(new File(pic.toString()));
			BufferedImage small = ImageIO.read(new File(fg.toString()));
			Graphics2D g = big.createGraphics();
			g.drawImage(small, big.getWidth() - small.getWidth(), big.getHeight() - small.getHeight(), small.getWidth(), small.getHeight(), null);
			Font font = new Font("arial", Font.PLAIN, 50);
			g.setColor(Color.BLUE);
			g.setFont(font);
			g.drawString(value, big.getWidth() - small.getWidth() + 160, big.getHeight() - small.getHeight() + 195);
			g.dispose();
			ImageIO.write(big, "png", new File(pic.toString()));
			result.setData(path);
		} catch (Exception e) {
			logger.error(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
		}
		return result;
	}
	
	/** 
	* 计算旋转参数 
	*/  
	public static Rectangle CalcRotatedSize(Rectangle src,int angel){  
	    // if angel is greater than 90 degree,we need to do some conversion.  
	    if(angel > 90){  
	        if(angel / 9%2 ==1){  
	            int temp = src.height;  
	            src.height = src.width;  
	            src.width = temp;  
	        }  
	        angel = angel % 90;  
	    }  
	      
	    double r = Math.sqrt(src.height * src.height + src.width * src.width ) / 2 ;  
	    double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
	    double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;    
	    double angel_dalta_width = Math.atan((double) src.height / src.width);    
	    double angel_dalta_height = Math.atan((double) src.width / src.height);    
	  
	    int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha    
	            - angel_dalta_width));    
	    int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha    
	            - angel_dalta_height));    
	    int des_width = src.width + len_dalta_width * 2;    
	    int des_height = src.height + len_dalta_height * 2;    
	    return new java.awt.Rectangle(new Dimension(des_width, des_height));    
	}
	
	/** 
	 * 图片翻转时，计算图片翻转到正常显示需旋转角度  
	 */  
	public int getRotateAngleForPhoto(File file){
	    int angel = 0;  
	    Metadata metadata;
	    try{  
	        metadata = JpegMetadataReader.readMetadata(file);  
	        Directory directory = metadata.getDirectory(ExifThumbnailDirectory.class);  
	        if(directory.containsTag(ExifThumbnailDirectory.TAG_ORIENTATION)){   
	            // Exif信息中方向　　  
	            int orientation = directory.getInt(ExifThumbnailDirectory.TAG_ORIENTATION);   
	            // 原图片的方向信息  
	            if(6 == orientation ){  
	                //6旋转90  
	                angel = 90;  
	            }else if( 3 == orientation){  
	               //3旋转180  
	                angel = 180;  
	            }else if( 8 == orientation){  
	               //8旋转90  
	                angel = 270;  
	            }  
	        }  
	    } catch(Exception e){  
	        logger.error(e.getMessage());
	    }  
	    logger.info("图片旋转角度：" + angel);  
	    return angel;  
	}
}
