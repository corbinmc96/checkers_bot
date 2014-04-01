import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.NXTCommand;

import java.io.IOException;

public class USBCommunicator {
	public static void main(String[] args) throws NXTCommException, InterruptedException, IOException {
		NXTConnector conn = new NXTConnector();
		conn.connectTo(NXTComm.LCP);
		NXTCommandConnector.setNXTCommand(new NXTCommand(conn.getNXTComm()));

		System.out.println("command set");

		Motor.A.forward();
		Thread.sleep(2000);
		Motor.A.stop();
		System.out.println("done");

		conn.close();
	}
}