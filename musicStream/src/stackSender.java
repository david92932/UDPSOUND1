import java.io.IOException;
import java.net.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class stackSender
{

    public byte[] buffer;
    private int port;
    static AudioInputStream ais;

    public static void main(String[] args)
    {

        System.setProperty("java.net.preferIPv4Stack", "true");

        TargetDataLine line;
        DatagramPacket dgp;

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = false;
        InetAddress addr;
        int port = 50005;

        System.out.println("Server started at port:" + port);

        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        try
        {
            line = (TargetDataLine) AudioSystem.getLine(info);

            line.open(format);

            line.start();

            byte[] data = new byte[4096];

            addr = InetAddress.getByName("225.6.7.8");
            MulticastSocket socket = new MulticastSocket();
            while (true) {
                // Read the next chunk of data from the TargetDataLine.
                line.read(data, 0, data.length);
                // Save this chunk of data.
                dgp = new DatagramPacket (data,data.length,addr,port);

                socket.send(dgp);
            }

        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
            // TODO: handle exception
        } catch (SocketException e) {
            // TODO: handle exception
        } catch (IOException e2) {
            // TODO: handle exception
        }
    }
}