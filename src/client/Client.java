package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import interfaz.ClientInterface;

public class Client {
	
	private Socket cs;
	private boolean isDownloading;
	private ClientInterface ci;
	private DataInputStream din;
	private DataOutputStream dout;
	private int index;
	private String fileName;
	private File currentFile;
	
	
	public Client(ClientInterface clientInterface) {
		try {
			Client client = new Client(clientInterface);
			cs = new Socket(InetAddress.getByName("127.0.0.1"), 6578);
			din = new DataInputStream(cs.getInputStream());
			dout = new DataOutputStream(cs.getOutputStream());
			//TODO byte[] buffer =client.createDataPacket("124".getBytes("UTF8"), currentFile.getName().getBytes("UTF8"));
			//TODO dout.write(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	}
	
	private byte[] createDataPacket(byte[] cmd,byte[] data) {
		byte[] packet = null;
		try {
			byte[] initialize = new byte[1];
			initialize[0]=2;
			byte[] separator = new byte[1];
			separator[0]=4;
			byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
			packet = new byte[initialize.length +cmd.length+ separator.length + data_length.length];
			
			System.arraycopy(initialize, 0, packet, 0, initialize.length);
			System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);

			System.arraycopy(data_length, 0, packet, initialize.length+cmd.length, data_length.length);
			System.arraycopy(separator, 0, packet, initialize.length+cmd.length+data_length.length, separator.length);
			System.arraycopy(data, 0, packet, initialize.length+cmd.length+data_length.length +separator.length, data.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return packet;
	}
	
	private byte[] ReadStream(DataInputStream din) {
		byte[] data = null;
		try {
			int b = 0;
			String lengthBuff = "";
			while((b=din.read()) != 4){
				lengthBuff+=  String.valueOf(b);
			}
			int dataLenght= Integer.parseInt(lengthBuff);
			data = new byte[dataLenght];
			int byteRead = 0;
			int byteOffset=0;
			while(byteOffset < dataLenght) {
				din.read(data, byteOffset, dataLenght-byteOffset);
				byteOffset+= byteRead;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public boolean getConnectionState() {
		// TODO Auto-generated method stub
		return false;
	}

	public void fileData(String name, int i) {
		fileName = name;
		index = i;
	}

	public void start() {
		if(fileName != null && cs != null && index !=-1)
		{
			isDownloading= true;
	    	File file = null;
	    	long startTime = System.currentTimeMillis();
		}
	}

	public void stopDownload() {
		// TODO Auto-generated method stub
		
	}

}
