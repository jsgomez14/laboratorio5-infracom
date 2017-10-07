package interfaz;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


public class DownloadPanel extends JPanel implements ActionListener {
	
	
	 private JList filesList;
	    private JButton downloadButton;
	    private JButton stopButton;
	    private ClientInterface ci;
	    public static String DOWNLOAD="Download";
	    public static String STOP="Stop";

	 
	    public DownloadPanel( ClientInterface ci )
	    {
	        this.ci = ci;

	        setBorder( BorderFactory.createTitledBorder( "Files" ) );
	        setLayout( new BorderLayout( ) );

	        filesList = new JList( );
	        JScrollPane scroll = new JScrollPane( );
	        scroll.setViewportView( filesList );
	        scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
	        scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
	        add( scroll, BorderLayout.CENTER );

	        downloadButton = new JButton( );
	        downloadButton.setText( "Download" );
	        downloadButton.setActionCommand( DOWNLOAD );
	        downloadButton.addActionListener( this );

	        stopButton = new JButton( );
	        stopButton.setText( "Stop" );
	        stopButton.setActionCommand( STOP );
	        stopButton.addActionListener( this );

	        JPanel panel = new JPanel( );
	        panel.add( downloadButton );
	        panel.add( stopButton );
	        add( panel, BorderLayout.SOUTH );
	    }

	  
	    public void actualizarArchivos( String[] files )
	    {
	    	DefaultListModel listModel = new DefaultListModel();
	        for (String st : files)
	            listModel.addElement(st);
	        filesList.setModel(listModel);
	        filesList.setSelectedIndex(0);
	        repaint();
	    }

	   
	    public int getIndex( )
		{
			int c = filesList.getSelectedIndex( );
			if( c < 0 )
				return -1;
			return c;
		}
		public String getSelectedFile( )
		{
			int c = filesList.getSelectedIndex( );
			if( c < 0 )
				return "";
			String archivo = ( String )filesList.getSelectedValue( );
			return archivo;
		}

	    public void actionPerformed( ActionEvent event )
	    {
	        String command = event.getActionCommand( );
	        String fileName = getSelectedFile( );
	        if( command.equals( DOWNLOAD ) )
	        {
	            if( fileName == null )
	            {
	                JOptionPane.showMessageDialog( this, "You have to select a file", "Show file", JOptionPane.ERROR_MESSAGE );
	                return;
	            }
	            System.out.println(fileName);
	            ci.descargar();
	        }
	        else if( command.equals( STOP ) )
	        {
	            ci.stop();
	        }
	    }
}
