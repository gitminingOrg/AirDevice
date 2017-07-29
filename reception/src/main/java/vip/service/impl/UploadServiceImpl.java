package vip.service.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifThumbnailDirectory;

import net.coobird.thumbnailator.Thumbnails;
import utils.IDGenerator;
import utils.ResponseCode;
import utils.ResultData;
import utils.SystemTeller;
import vip.service.UploadService;

/**
 * Created by sunshine on 6/2/16.
 */
@Service
public class UploadServiceImpl implements UploadService {
	private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

	@Override
	public ResultData upload(MultipartFile file, String base) {
		ResultData result = new ResultData();
		try {
			if (file == null || file.getBytes().length == 0) {
				result.setResponseCode(ResponseCode.RESPONSE_NULL);
				return result;
			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
			result.setResponseCode(ResponseCode.RESPONSE_ERROR);
			result.setDescription(e.getMessage());
			return result;
		}
		String PATH = "/material/upload";
		Date current = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(current);
		StringBuilder builder = new StringBuilder(base);
		builder.append(PATH);
		builder.append("/");
		builder.append(time);
		File directory = new File(builder.toString());
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		String key = IDGenerator.generate("TH");
		String name = key + suffix;
		String completeName = builder.append(File.separator).append(name).toString();
		File temp = new File(completeName);
		try {
			file.transferTo(temp);
			int index = temp.getPath().indexOf(SystemTeller.tellPath(PATH + "/" + time));
			result.setData(temp.getPath().substring(index)); 
			int angel = getRotateAngleForPhoto(new File(temp.toString()));
			BufferedImage src = ImageIO.read(temp);
			BufferedImage big = null;
			if(angel != 0){
				int src_width = src.getWidth(null);  
                int src_height = src.getHeight(null);  
                Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);  
                big = new BufferedImage(rect_des.width, rect_des.height,BufferedImage.TYPE_INT_RGB);  
                Graphics2D g2 = big.createGraphics();  
  
                g2.translate((rect_des.width - src_width) / 2,   (rect_des.height - src_height) / 2);  
                g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);  
  
                g2.drawImage(big, null, null);
			}else {
				big = src;
			}
            ImageIO.write(big, suffix, temp);  
            Thumbnails.of(temp.getAbsolutePath()).size(375, 222).outputQuality(0.8f).outputFormat("jpg").toFile(temp.getAbsolutePath());
		} catch (IOException e) {
			logger.debug(e.getMessage());
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
