import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.NXTCommand;

public class USBCommunicator {
	public static void main(String[] args) throws NXTCommException, InterruptedException {
		NXTComm comm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		System.out.println("comm created");
		// NXTInfo nxtInfo = comm.search(null)[0];

		// System.out.println("info found");

		NXTConnector conn = new NXTConnector();
		conn.connectTo(NXTComm.LCP);
		NXTCommandConnector.setNXTCommand(new NXTCommand(conn.getNXTComm()));

		System.out.println("command set");

		Motor.A.forward();
		Thread.sleep(2000);
		Motor.A.stop();
		System.out.println("done");
	}
}