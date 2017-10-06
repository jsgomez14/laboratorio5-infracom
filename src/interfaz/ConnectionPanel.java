package interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectionPanel extends JPanel implements ActionListener {
	
	private ClientInterface ci;
	private JLabel request;
	private JButton connect;
	public static String CONNECT="Connect";
	private JLabel state;
	private JTextField stateC;
	
	public ConnectionPanel(ClientInterface ci){
		
		this.ci = ci;
	    setLayout(new GridLayout(2,2));

        setBorder( BorderFactory.createTitledBorder( "Server Connection" ) );

        request = new JLabel( "Request connection" );
        add( request );
        
        connect = new JButton( );
        connect.setText( "CONNECT" );
        connect.setActionCommand( CONNECT );
        connect.addActionListener( this );
        add( connect );
		
        state = new JLabel("Connection state: ");
        add(state);
        
        stateC = new JTextField();
        stateC.setEditable(false);
        stateC.setText("No connection");
        add(stateC);
	}
	
	public void changeState(){
		stateC.setText("Connected");
	}
	public void setDesconnected(){
		stateC.setText("Sin conexion");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		if(CONNECT.equals(command)){
			System.out.println("connect");//TODO Borrar
			ci.startConnection();
		}
		
	}

}
