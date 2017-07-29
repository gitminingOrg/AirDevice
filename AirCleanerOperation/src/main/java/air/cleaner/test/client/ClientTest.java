package air.cleaner.test.client;

import java.net.InetSocketAddress;

import model.MCPPacket;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.ByteUtil;
import utils.CRC16;
import air.cleaner.mina.CleanerCodeFactory;

public class ClientTest extends Thread{
	public static Logger LOG = LoggerFactory.getLogger(ClientTest.class);
	
	
	@Override
	public void run() {
        // 创建客户端连接器. 
        NioSocketConnector connector = new NioSocketConnector(); 
        connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new CleanerCodeFactory() )); //设置编码过滤器 
        connector.setHandler(new ClientHandler());//设置事件处理器 
        // 设置接收缓冲区的大小  
	    connector.getSessionConfig().setReceiveBufferSize(10240);
	    // 设置输出缓冲区的大小  
	    connector.getSessionConfig().setSendBufferSize(10240);
	    // 读写都空闲时间:30秒  
	    //connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30); 
	    // 读(接收通道)空闲时间:40秒 
	    //connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 40);
	    // 写(发送通道)空闲时间:50秒 
	    //connector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 50); 
	    
	    
	    while(true){
	    	//ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8888));//建立连接
	    	ConnectFuture cf = connector.connect(new InetSocketAddress("commander.qingair.net", 8888));//建立连接 
	    	cf.awaitUninterruptibly();//等待连接创建完成 
	        
	        byte[] FRH =new byte[]{-0x11};
			byte[] CTF = new byte[]{0x02};
			byte[] CID = new byte[]{0x00};
			byte[] UID = new byte[]{-0x15,-0x34,0x11,0x11,0x11,0x22,0x00,0x00,0x22,0x00,0x00,0x03};
			byte[] LEN = new byte[]{0x20};
			byte[] DATA = new byte[]{0x00,0x02,0x33,0x22,0x01,0x11,0x11,0x11,0x00,0x11,0x01,0x01,0x01,0x01};
			byte[] reserve = new byte[]{0x68,0x01,0x03,0x11,0x22,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

			int check = CRC16.CRC_XModem(ByteUtil.concatAll(CTF, CID, UID, LEN,DATA, reserve));
			byte[] CRC = ByteUtil.intToByteArray(check, 2);
			byte[] FRT = new byte[]{-0x12};
	        MCPPacket mcpPacket= new MCPPacket(CTF, CID, UID, LEN, ByteUtil.concatAll(DATA, reserve), CRC);
	        cf.getSession().write(mcpPacket);
	        try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	    
        

	}


	public static void main(String[] args) {
		new ClientTest().start();
    }
}
