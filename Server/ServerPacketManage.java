import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ServerPacketManage
 */
class ServerPacketManage {
    public String fileName;
    public int packetSize;
    public int dataTotalLength;
    public byte[][] packetsList;

    // constructor
    public ServerPacketManage () {
        fileName = "";
        packetSize = 0;
        dataTotalLength = 0;
    }

    // update info after server get first message
    public void setUpDataInfo(String fileInfo) {
        String[] sl = fileInfo.split(",");
        fileName = sl[0].trim();
        packetSize = Integer.parseInt(sl[1]);
        dataTotalLength = Integer.parseInt(sl[2].trim());
        packetsList = new byte[dataTotalLength / packetSize + 1][];
    }

    // receive client packets and store in local
    public void updatePackeList(byte[] b) throws IOException {
        // System.out.println("get data: ");
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream i = new DataInputStream(bais);
        i.skipBytes(2);
        // System.out.println("each packet size: " + b.length);
        packetsList[i.readInt()] = getDataFromPac(b);
    }

    // generate file after receive all packets
    public void generateFile () throws FileNotFoundException, IOException  {
        System.out.println("Start generate file");
        File outFile = new File (fileName);
        OutputStream fileOutputStream = new FileOutputStream(outFile);
        for (int i = 0; i < packetsList.length; i++) {
            // System.out.println("Writing file at: " + i + "th packet");
            fileOutputStream.write(packetsList[i], 0, packetsList[i].length);
        }
        System.out.println("Finished gerate file ");
        fileOutputStream.close();
    }

    // take off the packe management header
    private byte[] getDataFromPac(byte[] receiveData) {
        byte t[] = new byte[packetSize];
        System.arraycopy(receiveData, 6, t, 0, packetSize);
        return t;
    }

    public boolean checkPacketLoss () {
        boolean packeLost = false;
        int dropCount = 0;
        for (int i = 0; i < packetsList.length; i++) {
            if (packetsList[i] == null) {
                // System.out.println("empty spot: " + i);
                dropCount++;
                packeLost = true;
            }
        }
        System.out.println("Drop Rate: " + (double)dropCount/(double)packetsList.length + " Number of packets drop: " + dropCount + "/" + packetsList.length);
        return packeLost;
    }

    public byte[] packetLossRequest(int seq) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream w = new DataOutputStream(baos);
        w.writeChar('l');
        w.writeInt(seq);
        w.flush();
        return baos.toByteArray();
    }

    public byte[] getAllPacketMessage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream w = new DataOutputStream(baos);
        w.writeChar('a');
        w.flush();
        return baos.toByteArray();
    }
}
