package utils;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Constant {
	public static final int CTF_QUERY = 0x00;
	public static final int CTF_SET = 0x01;
	public static final int CTF_HEART = 0x02;
	public static final String AUTO = "auto";
	public static final String SLEEP = "sleep";
	public static final String MANUAL = "manual";
	
	public static final String PM25 = "pm25";
	public static final String TEMPERATURE = "temperature";
	public static final String HUMIDITY = "humidity";
	public static final String HCHO = "hcho";
	public static final String CO2 = "co2";
	public static final String VELOCITY = "velocity";
	public static final String POWER = "power";
	public static final String WORKMODE = "workMode";
	public static final String UV = "uv";
	public static final String HEAT = "heat";
	public static final String LIGHT = "light";
	public static final String VOC = "voc";
	public static final String SIGNAL = "signal";
	
	public static final String SERVER_IP = "serverIP";
	public static final String SERVER_PORT = "serverPort";
	public static final String HEARTBEAT_GAP = "heartbeatGap";
	public static final String CYCLE = "cycle";
	public static final String DEVICETYPE = "deviceType";
	public static final String FIRMWARE = "firmware";
	public static final String HARDWARE = "hardware";
	public static final String RESTART = "restart";
	public static final String RUNDAY = "runday";
	
	public static final Set<Integer> deviceSet = new ImmutableSet.Builder<Integer>().add(0x01).add(0x02).add(0x03).add(0xFC).add(0xFD).add(0xFE).add(0xFF).build();
    public static final Set<Integer> statusSet = new ImmutableSet.Builder<Integer>().add(0x04).add(0x05).add(0x06).add(0x07).add(0x08).add(0x09).add(0x0A).build();

}
