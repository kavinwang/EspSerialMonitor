package com.yuemtech.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Tools {

	public static byte high(byte chr) {

		byte temp = (byte) 0x00;

		for (int i = 7; i >= 4; i--) {
			if (((1L << i) & chr) != 0) {
				temp |= (byte) (1L << (i - 4));
			}
		}

		return temp;
	}
	public static int bytes2int(byte[] bytes, int offset) {
		int length1 = ((((int) bytes[offset + 3]) << 24) & 0xff000000);
		int length2 = ((((int) bytes[offset + 2]) << 16) & 0x00ff0000);
		int length3 = ((((int) bytes[offset + 1]) << 8) & 0x0000ff00);
		int length4 = ((((int) bytes[offset])) & 0x000000ff);

		return length1 + length2 + length3 + length4;
	}

	public static short bytes2short(byte[] bytes, int offset) {
		int length1 = ((((int) bytes[offset + 1]) << 8) & 0x0000ff00);
		int length2 = ((((int) bytes[offset])) & 0x000000ff);

		return (short)(length1 + length2) ;
	}

	public static byte[] int2bytes(int value){
		byte[] bytes = new byte[4];

		bytes[0] = (byte) (value & 0x000000ff);
		bytes[1] = (byte) ((value & 0x0000ff00) >> 8);
		bytes[2] = (byte) ((value & 0x00ff0000) >> 16);
		bytes[3] = (byte) ((value & 0xff000000) >> 24);

		return bytes;

	}
	public static byte[] short2bytes(short value){
		byte[] bytes = new byte[2];

		bytes[0] = (byte) (value & 0x000000ff);
		bytes[1] = (byte) ((value & 0x0000ff00) >> 8);

		return bytes;

	}

	public static String bytes2hex(ByteBuffer bb) {
		return bytes2hex(bb,"");
	}
	public static String bytes2hex(ByteBuffer bb,String sep) {
		return bytes2hex(bb.getValue(),sep);
	}
	public static String bytes2hex(byte[] bytes) {
		return bytes2hex(bytes,"");
	}
	public static String bytes2hex(byte[] bytes,String sep) {
		int len = bytes.length;
		boolean hasSep = sep!=null && sep.length()>0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			String temp = Integer.toHexString(bytes[i]);
			switch (temp.length()) {
				case 0:
				case 1:
					temp = "0" + temp;
					break;
				default:
					temp = temp.substring(temp.length() - 2);
					break;
			}
			sb.append(temp);
			if(hasSep && i < (len -1))sb.append(sep);
		}
		return sb.toString().toUpperCase();

	}

	public static int toInt(char a) {
		if (a >= '0' && a <= '9')
			return a - '0';
		if (a >= 'A' && a <= 'F')
			return a - 55;
		if (a >= 'a' && a <= 'f')
			return a - 87;
		return 0;
	}

	public static byte[] hex2bytes(String hex) {
		if (hex.length() % 2 != 0)
			hex = "0" + hex;
		int len = hex.length() / 2;
		byte[] val = new byte[len];
		for (int i = 0; i < len; i++) {
			val[i] = (byte) (toInt(hex.charAt(2 * i)) * 16 + toInt(hex
					.charAt(2 * i + 1)));
		}
		return val;
	}

	/**
	 * 将BCD码转换成字符串型数据.如果待转换的字节流为"\x12\xAB\xCD\xE0", 偏移位置为1,长度为2, 则转换后的结果为"ABCD"
	 *
	 * @param bcddata:待转换的BCD数据
	 * @param offset:BCD数据的偏移位置，转换从此位置开始进行
	 * @param len:待转换数据的长度(压缩BCD码的长度)
	 * @return 返回转换后的字符串
	 */
	public static String bcd2string(byte[] bcddata, int offset, int len) {
		int strlen = len * 2; /* 转换后字符串的长度 */
		byte[] temp = new byte[strlen]; /* 临时缓冲区 */

		/* 将待转换数据的高位和低位进行分离 */
		for (int i = 0, j = 0; i < len; i++) {
			temp[j++] = high(bcddata[offset + i]);
			temp[j++] = (byte) (bcddata[offset + i] & 0x0F);
		}

		for (int i = 0; i < strlen; i++) {
			if (temp[i] >= 0x0A) {
				temp[i] = (byte) ('A' + temp[i] - 0x0A);
			} else {
				temp[i] += (byte) 0x30;
			}
		}

		return (new String(temp));
	}

	public static String bcd2string(byte[] bcddata) {
		return bcd2string(bcddata, 0, bcddata.length);
	}

	/**
	 * string2bcd用到的数值转换函数
	 */
	static byte hxv(byte chr) {

		if (chr > 0x60) {
			return ((byte) (chr - 'a' + 10));
		}
		if (chr > 0x40) {
			return ((byte) (chr - 'A' + 10));
		}
		if (chr == 0) {
			return ((byte) 0);
		}
		return ((byte) (chr - '0'));
	}

	/**
	 * 将字符串型数据转换成BCD码.例如,当要转换的字符串为"1234",而转换后 数据的长度为3时,转换后的结果为"\x00\x12\x34"
	 *
	 * @param str:待转换的BCD数据
	 * @param len:转换数据后数据的长度
	 * @return 返回转换后的缓冲区数据
	 */
	public static byte[] string2bcd(String str, int len) {
		byte[] temp = new byte[len * 2];
		for (int i = 0; i < len * 2; i++) {
			temp[i] = (byte) 0x30;

		}
		StringBuffer sb = new StringBuffer(new String(temp));

		sb = sb.replace(len * 2 - str.length(), sb.length(), str);

		temp = new byte[len];
		int lpi;
		int lpj;
		for (lpi = 0, lpj = 0; lpi < len; lpi++) {
			byte high = (byte) (hxv((byte) (sb.charAt(lpj++))) << 4);
			byte low = hxv((byte) (sb.charAt(lpj++)));
			temp[lpi] = (byte) (high | low);
		}

		return temp;
	}

	/**
	 * 将int 型数据转换成BCD型数据
	 *
	 * @param data;需要转换的整型数据
	 * @param len:转换后缓冲区的长度
	 * @return 返回转换后生成的缓冲区
	 */
	public static byte[] int2bcd(int data, int len) {
		String buf = String.valueOf(data);

		return string2bcd(buf, len);
	}

	/**
	 * 将long型数据转换成BCD型数据
	 *
	 * @param data;需要转换的整型数据
	 * @param len:转换后缓冲区的长度
	 * @return 返回转换后生成的缓冲区
	 */
	public static byte[] long2bcd(long data, int len) {
		String buf = String.valueOf(data);

		return string2bcd(buf, len);
	}
	
	public static int ntohl(byte[] bytes) {
		return ntohl(bytes,0);
	}
	/**
	 * 将网络字节序的数据流转换成一int型数值(从偏移位置开始转换4个字节)
	 *
	 * @param bytes:
	 *            相应的字节流
	 * @param offset:
	 *               字节流中开始转换的偏移位置
	 * @return 返回一个整型数值
	 */
	public static int ntohl(byte[] bytes, int offset) {
		int length1 = ((((int) bytes[offset]) << 24) & 0xff000000);
		int length2 = ((((int) bytes[offset + 1]) << 16) & 0x00ff0000);
		int length3 = ((((int) bytes[offset + 2]) << 8) & 0x0000ff00);
		int length4 = ((((int) bytes[offset + 3])) & 0x000000ff);

		return length1 + length2 + length3 + length4;
	}


	/**
	 * 将一个整型数值转换成网络字节序的字节流(4 个字节长)
	 *
	 * @param value:待转换的整型数值
	 * @return 返回转换所网络字节序的字节流
	 */
	public static byte[] htonl(int value) {
		byte[] bytes = new byte[4];

		bytes[3] = (byte) (value & 0x000000ff);
		bytes[2] = (byte) ((value & 0x0000ff00) >> 8);
		bytes[1] = (byte) ((value & 0x00ff0000) >> 16);
		bytes[0] = (byte) ((value & 0xff000000) >> 24);

		return bytes;
	}

	/**
	 * 将网络字节序的数据流转换成一short型数值(从偏移位置开始转换2个字节)
	 *
	 * @param bytes:
	 *            相应的字节流
	 * @param offset:字节流中开始转换的偏移位置
	 * @return 返回一个短整型数值
	 */
	public static short ntohs(byte[] bytes, int offset) {
		int length = ((((int) bytes[offset + 0]) << 8) & 0x0000ff00)
				+ ((((int) bytes[offset + 1]) << 0) & 0x000000ff);

		return (short) length;
	}

	/**
	 * 将一个短整型数值转换成网络字节序的字节流(2 个字节长)
	 *
	 * @param value:待转换的整型数值
	 * @return 返回转换所网络字节序的字节流
	 */
	public static byte[] htons(short value) {
		byte[] bytes = new byte[2];

		bytes[1] = (byte) (value & 0x000000ff);
		bytes[0] = (byte) ((value & 0x0000ff00) >> 8);

		return bytes;
	}

	/**
	 * 取系统日期
	 *
	 * @return 返回的日期格式为"YYYYMMDD"
	 */
	public static String getSystemDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	/**
	 * 取系统时间
	 *
	 * @return 返回的时间格式为"HHMMSS"
	 */
	public static String getSystemTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(date);
	}

	public static String getSystemDateTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);

	}

	/**
	 * 从网络上读取原始的数据流
	 *
	 * @param size:需要接收的数据的长度
	 * @return 返回接收到的数据
	 */
	public static byte[] read(InputStream in, int size) throws IOException,
			InterruptedIOException {

		byte[] buf = new byte[size];
		int rc; // read方法的返回数据
		int remain; // 剩余的没有接收完的数据长度
		int received; // 已经接收到的数据长度

		remain = size;
		received = 0;
		while (remain > 0) {
			rc = in.read(buf, received, remain);
			if (rc == -1) {
				throw new IOException("网络连接可能出现问题");
			}
			remain = remain - rc;
			received = received + rc;
		}
		return buf;
	}

	/**
	 * 使用于当需要读取的长度大于指定的nio buffer的长度而必须进行多次读取的情况
	 * @param socketChannel
	 * @param readBuffer
	 * @param length
	 * @param timeOut 在这么长的时间内必须把数据读回来
	 * @return
	 * @throws Exception
	 */
	public static ByteBuffer getChannelData(SocketChannel socketChannel,java.nio.ByteBuffer readBuffer,int length,long timeOut)throws Exception{
		ByteBuffer bb = new ByteBuffer();
		while(length>0){
			if(readBuffer.capacity()<length){
				Tools.readChannelData(socketChannel,readBuffer,readBuffer.capacity(),timeOut);
				byte[] data = new byte[readBuffer.capacity()];
				readBuffer.get(data);
				bb.append(data);
				length-=readBuffer.capacity();
			}else{
				Tools.readChannelData(socketChannel,readBuffer,length,timeOut);
				byte[] data = new byte[length];
				readBuffer.get(data);
				bb.append(data);
				length=0;
			}
		}
		return bb;

	}

	/**
	 * 适用于读取的长度不大于nio缓冲的长度，直接返回nio的缓冲，便于简单处理
	 * @param socketChannel
	 * @param readBuffer
	 * @param length
	 * @param timeOut 在这个时间内必须读取完整的包数据回来
	 * @throws Exception
	 */
	public static void readChannelData(SocketChannel socketChannel,java.nio.ByteBuffer readBuffer,int length,long timeOut)throws Exception{
		int count=-1;
		long time = System.currentTimeMillis();
		readBuffer.clear();
		readBuffer.limit(length);
		while (true) {
			count = socketChannel.read(readBuffer);
			if (count < 0)throw new Exception("网络连接已经断开，不能读取更多数据");
//			if (count > 0)time = System.currentTimeMillis();
			if (readBuffer.position() < length) {
				if ((System.currentTimeMillis() - time) > timeOut) {
					throw new Exception("读取网络数据超时:"+length+"-->"+readBuffer.position());
				} else continue;
			} else {
				readBuffer.flip();
				return;
			}
		}

	}

	public static void writeChannel(SocketChannel socketChannel,java.nio.ByteBuffer buffer,byte[] datas)throws Exception{
		int cap = buffer.capacity();
		if(cap==0)throw new Exception("错误的发送缓存空间");
		int dataLen = datas.length;
//		int times = dataLen/cap;
//		if(dataLen%cap>0)times+=1;
		int remain = dataLen;
		while(remain>0){
			buffer.clear();
			int willSend = remain>cap?cap:remain;
			buffer.limit(willSend);
			buffer.put(datas,(dataLen-remain),willSend);
			buffer.flip();
			int lenW = socketChannel.write(buffer);
//			if(lenW!=willSend)System.out.println("应写："+willSend+"  实际写:"+lenW);
			remain-=lenW;//willSend;
//			System.out.println("需写出["+dataLen+"]此次应写["+willSend+"]此次写出["+lenW+"]还有["+remain+"]");
		}

//		for(int i=0;i<times;i++){
//			buffer.clear();
//			int max = remain>cap?cap:remain;
//			buffer.limit(max);
//			buffer.put(datas, i*cap, max);
//			buffer.flip();
//			socketChannel.write(buffer);
//			remain = remain-max;
//		}
	}


	/**
	 * 转换对数据操作的日期信息
	 *
	 * @param: 日期信息，格式为"YYYYMMDD"
	 * @return 返回的对数据库操作的日期形式, "YYYY/MM/DD"
	 */
	public static String getSqlDate(String date) {
		String output = date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
		return output;
	}

	/**
	 * 对两个数组的数进行异域运算,若两个数组长不等，则以长度断的数为准
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static byte[] xor(byte[] b1, byte[] b2) {
		int len = b1.length <= b2.length ? b1.length : b2.length;
		byte[] temp = new byte[len];
		for (int i = 0; i < len; i++) {
			temp[i] = (byte) (b1[i] ^ b2[i]);
		}
		return temp;

	}


	/**
	 * <p>
	 * 将一个YYYYMMDDHHMMSS类型的字符串信息转化为一个时间对象
	 */
	public static Date getDate(String datestr) {
		int year = Integer.parseInt(datestr.substring(0, 4));
		int month = Integer.parseInt(datestr.substring(4, 6));
		int day = Integer.parseInt(datestr.substring(6, 8));
		int hour = Integer.parseInt(datestr.substring(8, 10));
		int minute = Integer.parseInt(datestr.substring(10, 12));
		int second = Integer.parseInt(datestr.substring(12));

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute, second);

		java.sql.Timestamp stamp = new java.sql.Timestamp(cal.getTimeInMillis());
		stamp.setNanos(0);
		return stamp;
	}

	public static String trunkString(int length, String orig) {
		if (length <= 0)
			length = 256;
		int len = orig.length();
		if (len < (2 * length + 3))
			return orig;
		StringBuffer result = new StringBuffer();
		return result.append(orig.substring(0, length)).append("...").append(
				orig.substring(orig.length() - length)).toString();
	}

	/**
	 * <p>
	 * 在一个字符串的前面或后面增加一些填充字符。
	 *
	 * @param value:
	 *            转换前的的字符串
	 * @param fill:
	 *            需要填充的字符
	 * @param length:
	 *            转换后字符串的长度,如果此值比value.length小的话，则返回 value的前length个字符
	 * @param head:
	 *            true:在value前填充字符；false:在value后填充字符
	 * @return 返回转换后的字符串。如果length为0或value为null的话，则返回null
	 * @@deprecated 不能解决汉字的长度问题 use adjustString2
	 */
	public static String adjustString(String value, char fill, int length,
									  boolean head) {
		return adjustString2(value,fill,length,head);
//		if (value == null || length < 0)
//			return null;
//		if (value.length() >= length)
//			return value.substring(0, length);
//		else {
//			StringBuffer sb = new StringBuffer().append(fill);
//			int len = length - value.length();
//			while (len != 1) {
//				len /= 2;
//				sb.append(sb);
//			}
//			sb.append(sb).setLength(length - value.length());
//			if (head)
//				return sb.append(value).toString();
//			else
//				return sb.insert(0, value).toString();
//		}
	}
	/**
	 * <p>
	 * 在一个字符串的前面或后面增加一些填充字符<br>
	 * 字符串长度的判断根据getBytes().length,适用于含汉字字符串的长度调整
	 * @param value:
	 *            转换前的的字符串
	 * @param fill:
	 *            需要填充的字符
	 * @param length:
	 *            转换后字符串的长度,如果此值比value.length小的话，则返回 value的前length个字符
	 * @param head:
	 *            true:在value前填充字符；false:在value后填充字符
	 * @return 返回转换后的字符串。如果length为0或value为null的话，则返回null
	 */
	public static String adjustString2(String value, char fill, int length,
									   boolean head) {
		if (value == null || length < 0)
			return null;

		int srcLen = value.getBytes().length;

		if (srcLen >= length)
			return new String(value.getBytes(), 0, length);
		else {
			StringBuffer sb = new StringBuffer().append(fill);
			int len = length - srcLen;
			while (len != 1) {
				len /= 2;
				sb.append(sb);
			}
			sb.append(sb).setLength(length - srcLen);
			if (head)
				return sb.append(value).toString();
			else
				return sb.insert(0, value).toString();
		}
	}

	public static Object deserialize(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream inn = new ObjectInputStream(bais);
		return inn.readObject();
	}

	public static byte[] serialize(Object object) throws Exception {
		if (!(object instanceof Serializable))
			throw new Exception("data can not serial!");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(baos);
		out.writeObject(object);
		return baos.toByteArray();
	}

	public static byte[] compress(String data) {
		byte[] output = "".getBytes();
		try {
			ByteArrayOutputStream o = new ByteArrayOutputStream();
			DeflaterOutputStream out = new DeflaterOutputStream(o);
			out.write(data.getBytes());
			out.finish();
			output = o.toByteArray();
			out.close();
			o.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return output;
	}
	public static byte[] decompress(byte[] data){
		byte[] output = new byte[512];
		ByteBuffer bb = new ByteBuffer();
		try {
			ByteArrayInputStream i = new ByteArrayInputStream(data);
			InflaterInputStream in = new InflaterInputStream(i);

			while(true){
				int ret = in.read(output);
				if (ret == -1)
					break;
				bb.append(output, 0, ret);
			}

			in.close();
			i.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bb.getValue();

	}
	


	/**
	 * 数字金额字符串转汉字   added wyi 2006-09-29
	 * @param amount
	 * @return
	 */
	public static String NumToRMB(String amount)
	{
		String strNum, strUnit, strAmount, strAmount2;
		int intLen;
		int intData,iLoop;
		float fAnount;

		try
		{
			fAnount = Float.parseFloat(amount);

			amount=String.format("%.2f", fAnount);


			strNum = "零壹贰叁肆伍陆柒捌玖";
			strUnit = "分角元拾佰仟万拾佰仟亿拾佰仟";
			intLen =amount.length();

			if (intLen > 13) return "";  //数字金额过大则返回

			//删除小数点
			strAmount =amount.substring(0,intLen-3)+amount.substring((intLen-2));
			System.out.println(strAmount);
			intLen = intLen - 1;

			strAmount2 = "";
			for (iLoop = 0; iLoop < intLen; iLoop++)
			{
				strAmount2 = strUnit.substring(iLoop,iLoop+1)+strAmount2;
				//System.out.println(strAmount.substring(intLen-iLoop-1,intLen-iLoop));
				intData = Integer.parseInt(strAmount.substring(intLen-iLoop-1,intLen-iLoop));
				strAmount2 = strNum.substring(intData,intData+1)+strAmount2;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}

		return strAmount2;
	}
	public static String toRMB(String value){
		String strNum = "零壹贰叁肆伍陆柒捌玖";
		String strUnit = "分角元拾佰仟万拾佰仟亿拾佰仟万拾佰仟";
		int len = value.length();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<len;i++){
			sb.append(strNum.charAt(value.charAt(i)-48));
			sb.append(strUnit.charAt(len-(i+1)));
		}
		return sb.toString();

	}

	/**
	 * 返回当前主机的ip地址
	 * 私有地址优先，且返回第一个查询到的私有地址如果没有地址则返回localhost
	 * 返回最后一个本机地址
	 * @return
	 * @throws Exception
	 */
	public static String getBaseNetAddress() throws Exception {
		InetAddress addr = InetAddress.getLocalHost();
		String hostName = addr.getHostName();
		String ip = "";
		if (hostName.length() > 0) {
			InetAddress[] addrs = InetAddress.getAllByName(hostName);
			if (addrs.length > 0) {
				for (int i = 0; i < addrs.length; i++) {
					ip = addrs[i].getHostAddress();
					if (ip.startsWith("10") || ip.startsWith("172.16")
							|| ip.startsWith("192.168"))
						return ip;
				}
			}
		}
		if(ip.trim().equals(""))return "127.0.0.1";
		return ip;

	}
}
