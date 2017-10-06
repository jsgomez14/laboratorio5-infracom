package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static void main(String[] args) {
		ServerSocket ss;
		try {
			ss = new ServerSocket(6578);
			while(true) {
				new Thread(new ClientWorker(ss.accept())).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class ClientWorker implements Runnable{
	
	public final static String PATH = "./data";

	
	private Socket targetSocket;
	private DataInputStream din;
	private DataOutputStream dout;
	private PrintWriter mout;
	private BufferedReader min;
	private ArrayList<String> filesList;
	
	public ClientWorker(Socket socket) {
		try {
			targetSocket = socket;
			din = new DataInputStream(targetSocket.getInputStream());
			dout = new DataOutputStream(targetSocket.getOutputStream());
			mout = new PrintWriter(targetSocket.getOutputStream(), true);
			min = new BufferedReader(new InputStreamReader(targetSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ArrayList<String> files = getFiles(PATH);
		mout.println("OK "+ getNames(files));
		try {
			String i = min.readLine();
			while(i==null){
				i=min.readLine();
			}
			File targetFile = new File(PATH+"/"+files.get(Integer.parseInt(i)));
			dout.write(createDataPacket("124".getBytes("UTF8"), targetFile.getName().getBytes("UTF8")));
			dout.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//TODO AQUÍ AGREGAR LA LOGICA DE MANDAR EL ARCHIVO QUE EL CLIENTE PIDE
		RandomAccessFile rw;
		long currentFilePointer = 0;
		while(true) {
			byte[] initialize = new byte[1];
			try {
				din.read(initialize, 0 , initialize.length);
				if(initialize[0] == 2) {
					byte[] cmdBuff = new byte[3];
					din.read(cmdBuff,0,cmdBuff.length);
					byte[] rcvData = ReadStream();
					switch(Integer.parseInt(new String(cmdBuff))){
					case 124:
						rw= new RandomAccessFile(PATH+"/"+ new String(rcvData), "rw");
						dout.write(createDataPacket("125".getBytes("UTF8"), String.valueOf(currentFilePointer).getBytes("UTF8")));
						dout.flush();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<String> getFiles(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files= new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()){
				files.add(listOfFiles[i].getName());
			}
		}
		return files;
	}
	
	private String getNames(ArrayList<String> files) {
		String response = "";
		for (int i = 0; i < files.size(); i++) {
			response+=(files.get(i))+':';
		}
		return response;
	}
	
	private byte[] ReadStream() {
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
	
	private byte[] createDataPacket(byte[] cmd, byte[] data) {
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
	
}