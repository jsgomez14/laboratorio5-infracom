package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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
	public final static int TIME_OUT = 180000;


	
	private Socket targetSocket;
	private PrintWriter mout;
	private BufferedReader min;
	public final static int TAM_BUFFER=8184;
	
	public ClientWorker(Socket socket) {
		try {
			targetSocket = socket;
			targetSocket.setSoTimeout(TIME_OUT);
			mout = new PrintWriter(targetSocket.getOutputStream(), true);
			min = new BufferedReader(new InputStreamReader(targetSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ArrayList<String> files = getFiles(PATH);
		mout.println(getNames(files));
		try {
			String nameArch=min.readLine();
			enviarArchivo(nameArch);
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public void enviarArchivo(String name)
	{
		try {
			File archivo = new File("./data/" + name);
			FileInputStream fis = new FileInputStream(archivo);
			//Tamaño del archivo a enviar
			long fileLength = archivo.length();
			System.out.println("size "+fileLength);
			
			//Envia el tamaño del archivo
			mout.println(fileLength);
			
			//Definir el tamaño del Buffer en el socket
			targetSocket.setReceiveBufferSize(TAM_BUFFER);
			
			//Crea el buffer
		    byte[] bytesBuffer = new byte[TAM_BUFFER];
		    
		    int count;
		    OutputStream os=targetSocket.getOutputStream();
		    while ((count = fis.read(bytesBuffer)) > -1) {
		        os.write(bytesBuffer, 0, count);
		    }
		    System.out.println("ARCHIVO ENVIADO");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private String getNames(ArrayList<String> files) {
		String response = "";
		for (int i = 0; i < files.size(); i++) {
			response+=(files.get(i))+':';
		}
		return response;
	}
	
}