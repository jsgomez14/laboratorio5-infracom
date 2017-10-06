package interfaz;


import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import client.*;

public class ClientInterface extends JFrame {

	private Client client; 

	FilesPanel filesPanel;
	DownloadPanel downloadPanel;
	ConnectionPanel connectionPanel;
	private ArrayList<String> filesList;

	public ClientInterface( )
	{
		setLayout( new BorderLayout( ) );

		connectionPanel = new ConnectionPanel( this );
		add( connectionPanel, BorderLayout.NORTH );
		downloadPanel = new DownloadPanel( this );
		filesPanel = new FilesPanel( this );
		JPanel midPanel = new JPanel( new BorderLayout( ) );
		midPanel.add( downloadPanel, BorderLayout.WEST );
		midPanel.add( filesPanel, BorderLayout.EAST );
		add( midPanel, BorderLayout.SOUTH );
		pack( );
		setTitle( "File Downloader" );
		setResizable( false );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	}


	public void startConnection()
	{
		JOptionPane.showMessageDialog(null, "Iniciando conexión");
		client = new Client(this);
		//TODO filesList = client.init();
		JOptionPane.showMessageDialog(null, "Se ha establecido conexi�n");
		connectionPanel.changeState();
		downloadPanel.actualizarArchivos(filesList);
		System.out.println("Files are up to date");
	}

	public void closeConnection(){
		//TODO client.closeConn();
		filesPanel.cerrarConexion();
		connectionPanel.setDesconnected();
		JOptionPane.showMessageDialog(null, "Ha cerrado la conexi�n");
	}

	public Client getClient(){
		return client;
	}

	public void descargar(){
		System.out.println("entrada descargar");
		if(!client.getConnectionState())
		{
			JOptionPane.showMessageDialog(null, "Debe iniciar sesi�n primero");
			return;
		}
		String name = downloadPanel.getSelectedFile();
		int selected = downloadPanel.getIndex();
		System.out.println("despues de name y select"); //TODO borrar

		if (selected==-1) JOptionPane.showMessageDialog(this, "Select a file", "Error", JOptionPane.ERROR_MESSAGE);
		if (selected==0) JOptionPane.showMessageDialog(this, "Select just one file", "Error", JOptionPane.ERROR_MESSAGE);
		else{
			client.fileData(name, selected);
			
			client.start();
			System.out.println("started client thread");
		}


	}

	public void displayDownloads(){
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./downloads"));
		int result = fc.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = fc.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			try {
				java.awt.Desktop.getDesktop().open(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop(){
		client.stopDownload();
		JOptionPane.showMessageDialog(null, "Ha detenido la descarga");
		filesPanel.cerrarConexion();
	}

	public void downloadFinished(){
		JOptionPane.showMessageDialog(null, "El archivo solicitado ha sido correctamente descargado");
	}

	
	public static void main( String[] args )
	{
		try
		{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );
		}
		catch( Exception e )
		{
			//Ignora el look & feel
		}
		ClientInterface ci = new ClientInterface( );
		ci.setVisible( true );
	}

}