package air.cleaner.test.client;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

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
	private static AtomicInteger failure = new AtomicInteger(0);
	
	@Override
	public void run() {
        // 创建客户端连接器. 
        NioSocketConnector connector = new NioSocketConnector(); 
        connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new CleanerCodeFactory() )); //设置编码过滤器 
        connector.setHandler(new ClientHandler());//设置事件处理器 
        // 设置接收缓冲区的大小  
	    connector.getSessionConfig().setReceiveBufferSize(128);
	    // 设置输出缓冲区的大小  
	    connector.getSessionConfig().setSendBufferSize(128);
	    // 读写都空闲时间:30秒  
	    //connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30); 
	    // 读(接收通道)空闲时间:40秒 
	    //connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 40);
	    // 写(发送通道)空闲时间:50秒 
	    //connector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, 50);
		byte[] seeds = new byte[]{-0x15,-0x34,0x11,0x11,0x11,0x22,0x00,0x00,0x22,0x00,0x00,0x07};
		byte[] UID = new byte[12];
		for (int i = 0; i < UID.length; i++) {
			UID[i] = seeds[(int) (Math.random() * 11)];
		}

		AtomicInteger count = new AtomicInteger(0);
	    
	    while(count.get() < 1){
	    	//ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8888));//建立连接
	    	ConnectFuture cf = connector.connect(new InetSocketAddress("measure.qingair.net", 8888));//建立连接
	    	cf.awaitUninterruptibly();//等待连接创建完成 
	        
	        byte[] FRH =new byte[]{-0x11};
			byte[] CTF = new byte[]{0x02};
			byte[] CID = new byte[]{0x00};
//			byte[] UID = new byte[]{-0x15,-0x34,0x11,0x11,0x11,0x22,0x00,0x00,0x22,0x00,0x00,0x07};
			byte[] LEN = new byte[]{0x20};
			byte[] DATA = new byte[]{0x00,0x02,0x33,0x22,0x01,0x11,0x11,0x11,0x00,0x11,0x01,0x01,0x01,0x01};
			byte[] reserve = new byte[]{0x68,0x01,0x03,0x11,0x22,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};

			int check = CRC16.CRC_XModem(ByteUtil.concatAll(CTF, CID, UID, LEN,DATA, reserve));
			byte[] CRC = ByteUtil.intToByteArray(check, 2);
			byte[] FRT = new byte[]{-0x12};
	        MCPPacket mcpPacket= new MCPPacket(CTF, CID, UID, LEN, ByteUtil.concatAll(DATA, reserve), CRC);
	        try {
				cf.getSession().write(mcpPacket);
			} catch (Exception e) {
	        	e.printStackTrace();
				failure.getAndIncrement();
			}
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count.getAndIncrement();
	    }


	}

	public static void main(String[] args) {
		ClientTest[] clientArray = new ClientTest[1];
		for (int i = 0; i < clientArray.length; i++) {
			clientArray[i] = new ClientTest();
			clientArray[i].start();
		}
		System.out.println("result: " + failure);
    }
}
