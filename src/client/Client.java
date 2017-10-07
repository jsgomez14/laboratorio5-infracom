package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import interfaz.ClientInterface;

public class Client {
	public final static int TAM_BUFFER=16000;
	public final static String PATH = "./Recibidos";

	private Socket cs;
	private boolean isDownloading;
	private ClientInterface ci;
	private PrintWriter pw;
	private int index;
	private String fileName;
	private File currentFile;
	private BufferedReader br;




	public Client(ClientInterface clientInterface) {
		isDownloading=true;
		ci=clientInterface;

		try {
			cs = new Socket(InetAddress.getByName("127.0.0.1"), 6578);
			br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			pw = new PrintWriter( cs.getOutputStream(), true);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated constructor stub
	}

	public String[] receiveFilesList()
	{
		try {
			return br.readLine().split(":");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void downloadFile(String nombre)
	{
		CompletableFuture.runAsync(
				new Runnable() {

					@Override
					public void run() {
						pw.println(nombre);
						try {
							long tamArchivo = Long.parseLong (br.readLine());
							FileOutputStream fos = new FileOutputStream("./Recibidos/" + nombre);
							BufferedOutputStream bos = new BufferedOutputStream(fos);
							InputStream is = cs.getInputStream();
							int recibido = 0;
							byte[] paquete = new byte[TAM_BUFFER];
							//Recibir el archivo
							while (recibido < tamArchivo && isDownloading) {
								int tamPaquete = is.read(paquete);
								System.out.println("Paquete recibido: "+ Arrays.toString(paquete));
								bos.write(paquete, 0, tamPaquete);
								recibido += tamPaquete;
							}
							System.out.println("Se terminó de recibir el archivo "+nombre); 
							ArrayList<String> files=actualizarDescargas();
							ci.actualizarDescargas(files);
							bos.close();
							fos.close();
						} catch (Exception e){
							e.printStackTrace();
						}
					}
				});

	}

	public static ArrayList<String> actualizarDescargas( )
	{
		File folder = new File(PATH);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> files= new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()){
				files.add(listOfFiles[i].getName());
			}
		}
		return files;
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

	public void stopDownload() 
	{
		isDownloading=false;
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

}
