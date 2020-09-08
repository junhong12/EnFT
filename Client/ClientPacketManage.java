import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ClientPacketManage
 */
public class ClientPacketManage {

    public InputStream fileInputStream;
    public String fileName;
    public int packetSize;
    public int dataTotalLength;

    public ClientPacketManage() {
        packetSize = 1024;
    }

    // read file that going to send to server
    public void readFile () throws FileNotFoundException, IOException {
        File inputFile = new File(fileName.trim());
        fileInputStream = new FileInputStream(inputFile);
        dataTotalLength = fileInputStream.available();
        // System.out.println("Read file length: " + dataTotalLength);

    }

    // generate the whole packet list
    public byte[][] getPackeList() throws IOException {
        byte[][] p = new byte[dataTotalLength/packetSize +1][];
        byte[] readData = new byte[packetSize];
        int readByte = 0;
        int packetCount = 0;
        while ((readByte = fileInputStream.read(readData)) != -1) {
            // System.out.println("read byte: " + readByte);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream w = new DataOutputStream(baos);
            if (readByte == packetSize) {
                w.writeChar('p');
            } else {
                w.writeChar('e');
            }
            w.writeInt(packetCount);
            w.write(readData);
            w.flush();
            byte[] result = baos.toByteArray();
            p[packetCount] = result;
            // System.out.println(p[packetCount].length);
            packetCount++;
        }
        return p;
    }

    // generate the first message with file info
    public String getFileInfo() {
        return fileName.trim() + ',' +
                String.valueOf(packetSize) + ',' + String.valueOf(dataTotalLength);

    }

    public char getPacketLossMessageHeader(byte[] b) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream i = new DataInputStream(bais);
        return  i.readChar();
    }

    public int getLossSeqenceNumber(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream i = new DataInputStream(bais);
        i.skipBytes(2);
        return i.readInt();
    }
}
