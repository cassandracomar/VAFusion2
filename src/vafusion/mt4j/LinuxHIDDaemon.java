package vafusion.mt4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LinuxHIDDaemon {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//figure out which file to read input from...
		File eventFD = new File("/dev/input/by-id/usb-Cando_Corporation_Cando_10.1_Multi_Touch_Panel_with_Controller_20091003.001-event-if00");
		InputStream in = new FileInputStream(eventFD);
		
		//Open a socket
		Socket sock = new Socket(new InetSocketAddress("localhost", 9999).getAddress(), 9999);
		OutputStream out = sock.getOutputStream();
		
		// begin publishing data from the file to the socket
		//read data in 24 byte chunks...
		byte[] event = new byte[24];
		
		while(true) {
			
			in.read(event, 0, 24);
			out.write(event);
			
		}

	}

}
