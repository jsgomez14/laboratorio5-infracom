package interfaz;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

public class FilesPanel extends JPanel implements ActionListener{


	private static final String VER = "Ver";


	private ClientInterface ventanaPrincipal;


	private JList<String> listaArchivos;

	private JButton botonVer;


	public FilesPanel( ClientInterface ventana )
	{
		ventanaPrincipal = ventana;

		setBorder( BorderFactory.createTitledBorder( "Downloaded Files" ) );
		setLayout( new BorderLayout( ) );

		listaArchivos = new JList<String>( );
		JScrollPane scroll = new JScrollPane( );
		scroll.setViewportView( listaArchivos );
		scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		add( scroll, BorderLayout.CENTER );

		botonVer = new JButton( );
		botonVer.setText( "Ver archivo" );
		botonVer.setActionCommand( VER );
		botonVer.addActionListener( this );
		add(botonVer, BorderLayout.SOUTH);


	}

	public int darIndex( )
	{
		int c = listaArchivos.getSelectedIndex( );
		if( c < 0 )
			return -1;
		return c;
	}
	public String darArchivoSeleccionado( )
	{
		int c = listaArchivos.getSelectedIndex( );
		if( c < 0 )
			return "";
		String archivo = ( String )listaArchivos.getSelectedValue( );
		return archivo;
	}
	public void cerrarConexion(){

	
		this.repaint();
		//textLabel.setText("Inicia conexi�n para ver archivos");
	}

    public void actualizarArchivos( ArrayList<String> files )
    {
    	DefaultListModel listModel = new DefaultListModel();
        for (String st : files)
            listModel.addElement(st);
        listaArchivos.setModel(listModel);
        listaArchivos.setSelectedIndex(0);
        repaint();
    }

	public void actionPerformed( ActionEvent evento )
	{
		String comando = evento.getActionCommand( );
		int archivo = darIndex();

		if( comando.equals( VER ) )
		{
			if( archivo == 0 )
			{
				JOptionPane.showMessageDialog( this, "Debe seleccionar un archivo", "Mostrar Archivo", JOptionPane.ERROR_MESSAGE );
				return;
			}else{
				try {
	                Desktop.getDesktop().open(new File("./Recibidos/" + (String) listaArchivos.getSelectedValue()));
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
			}
		}

	}

	
}
