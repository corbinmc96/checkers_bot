import java.util.ArrayList;

public class ArraysHelper {
	public static ArrayList<Byte> asArrayList(byte[] array) {
		ArrayList<Byte> result = new ArrayList<Byte>();

		for (byte item : array) {
			result.add(item);
		}

		return result;
	}

	public static ArrayList<Piece> asArrayList(Piece[] array) {
		ArrayList<Piece> result = new ArrayList<Piece>();

		for (Piece item : array) {
			result.add(item);
		}

		return result;
	}

	public static byte[][] copyOfRange(byte[][] original, int startIndex, int endIndex) {
		byte[][] result = new byte[endIndex-startIndex][];

		for (int i = 0; i<endIndex-startIndex; i++) {
			result[i] = original[startIndex+i];
		}
		return result;
	}

	
}