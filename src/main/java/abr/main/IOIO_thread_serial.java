package abr.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread_serial extends IOIO_thread
{
    private PwmOutput pwm_pan_output, pwm_tilt_output;
    private int move_value, turn_value;

    InputStream in;
    OutputStream out;

    static final int DEFAULT_PWM = 1500, MAX_PWM = 2000, MIN_PWM = 1000;

    @Override
    public void setup() throws ConnectionLostException
    {
        try
        {
            pwm_pan_output = ioio_.openPwmOutput(5, 50); //motor channel 1: back right;
            pwm_tilt_output = ioio_.openPwmOutput(6, 50); //motor channel 1: back right;

            Uart uart = ioio_.openUart(3,4,230400, Uart.Parity.NONE,Uart.StopBits.ONE);
            in = uart.getInputStream();
            out = uart.getOutputStream();

            pwm_pan_output.setPulseWidth(1500);
            pwm_tilt_output.setPulseWidth(1500);
            move(1500);
            turn(1500);
            move_value = 0;
            turn_value = 0;
        }
        catch (ConnectionLostException e){throw e;}
    }

    public synchronized void sendPacket(int cmd, int byteVal){
        try
        {
            byte[] buffer = new byte[5];
            buffer[0] = (byte) 128;
            buffer[1] = (byte) cmd;
            buffer[2] = (byte) byteVal;

            int crc16int = crc16(Arrays.copyOfRange(buffer,0,3));
            byte[] crcBuff = ByteBuffer.allocate(4).putInt(crc16(Arrays.copyOfRange(buffer,0,3))).array();

            buffer[3] = crcBuff[2];
            buffer[4] = crcBuff[3];

            if(out != null && buffer !=null)
                out.write(buffer,0,5);
        }
        catch (IOException e) {}
    }

    //Calculates CRC16 of nBytes of data in byte array message
    static int crc16(final byte[] buffer) {
        int crc = 0x0000;
        for (int b = 0; b < buffer.length; b++) {
            crc = crc ^ ((int)buffer[b] << 8);
            for (int bit = 0; bit < 8; bit++) {
                if ((crc & 0x8000) > 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc = crc << 1;
                }
            }
        }
        return (int)crc;
    }

    public synchronized void move(int value)
    {
        int translated_value = (int)((value - 1500) * 64 / (float) 1000 * 4 + 64);
        if (translated_value < 0)
            translated_value = 0;
        if (translated_value > 128)
            translated_value = 128;
        boolean success = false;
        //if(move_value != translated_value){
            //move_value = translated_value;
            //while(!success) {
                sendPacket(12, translated_value);
                try {
                    int byte1 = in.read() & 0xFF;
                    //if (byte1 == 255)
                        //success = true;
                } catch (Exception e) {
                }
            //}
        //}
    }

    public synchronized void turn(int value)
    {
        int translated_value = (int)((value - 1500) * 64 / (float) 1000 * 4 + 64);
        if (translated_value < 0)
            translated_value = 0;
        if (translated_value > 128)
            translated_value = 128;
        boolean success = false;
        //if(turn_value != translated_value){
            //turn_value = translated_value;
            //while(!success) {
                sendPacket(13, translated_value);
                try {
                    int byte1 = in.read() & 0xFF;
                    //if (byte1 == 255)
                        //success = true;
                } catch (Exception e) {
                }
            //}
        //}
    }


    public synchronized void pan(int value)
    {
        try {
            if(value > MAX_PWM)
                pwm_pan_output.setPulseWidth(MAX_PWM);
            else if(value < MIN_PWM)
                pwm_pan_output.setPulseWidth(MIN_PWM);
            else
                pwm_pan_output.setPulseWidth(value);
        } catch (ConnectionLostException e) {
            ioio_.disconnect();
        }

    }

    public synchronized void tilt(int value)
    {
        try {
            if(value > MAX_PWM)
                pwm_tilt_output.setPulseWidth(MAX_PWM);
            else if(value < MIN_PWM)
                pwm_tilt_output.setPulseWidth(MIN_PWM);
            else
                pwm_tilt_output.setPulseWidth(value);
        } catch (ConnectionLostException e) {
            ioio_.disconnect();
        }
    }

    /*
    public synchronized String read_temp(){
        byte[] buffer = new byte[2];
        buffer[0] = (byte) 128;
        buffer[1] = (byte) 82;
        try {
            out.write(buffer, 0, 2);
        } catch (IOException e) {
            return "";
        }
        final Thread looperThread = Thread.currentThread();
        Timer t = new Timer();
        TimerTask freqtask = new TimerTask() {
            public void run() {
                looperThread.interrupt();
            }
        };
        t.schedule(freqtask, 50);
        boolean success = false;
        String returnValue = "";
        while(!success) {
            try {
                byte bytes[] = new byte[4];
                bytes[0] = (byte) in.read();
                bytes[1] = (byte) in.read();
                bytes[2] = (byte) in.read();
                bytes[3] = (byte) in.read();

                byte checksum[] = new byte[4];
                checksum[0] = (byte) 128;
                checksum[1] = (byte) 82;
                checksum[2] = bytes[0];
                checksum[3] = bytes[1];

                int crc16 = crc16(checksum);
                byte crc16bytes[] = ByteBuffer.allocate(4).putInt(crc16).array();
                if ((crc16bytes[2] == bytes[2]) && (crc16bytes[3] == bytes[3])) {
                    success = true;
                    returnValue =  "" + ((int) (bytes[0] & 0xFF) * 256 + (int) (bytes[1] & 0xFF));
                }
            } catch (IOException e) {
                return "";
            }
        }
        return returnValue;
    }

    public synchronized String read_current(){
        Long tm = System.currentTimeMillis();
        byte[] buffer = new byte[2];
        buffer[0] = (byte) 128;
        buffer[1] = (byte) 49;
        try {
            out.write(buffer, 0, 2);
        } catch (IOException e) {
            return ",";
        }
        final Thread looperThread = Thread.currentThread();
        Timer t = new Timer();
        TimerTask freqtask = new TimerTask() {
            public void run() {
                looperThread.interrupt();
            }
        };
        t.schedule(freqtask, 50);
        boolean success = false;
        String returnValue = ",";
        while(!success) {
            try {
                byte bytes[] = new byte[6];
                bytes[0] = (byte) in.read();
                bytes[1] = (byte) in.read();
                bytes[2] = (byte) in.read();
                bytes[3] = (byte) in.read();
                bytes[4] = (byte) in.read();
                bytes[5] = (byte) in.read();

                byte checksum[] = new byte[6];
                checksum[0] = (byte) 128;
                checksum[1] = (byte) 49;
                checksum[2] = (byte) bytes[0];
                checksum[3] = (byte) bytes[1];
                checksum[4] = (byte) bytes[2];
                checksum[5] = (byte) bytes[3];
                int crc16 = crc16(checksum);
                byte crc16bytes[] = ByteBuffer.allocate(4).putInt(crc16).array();
                if ((crc16bytes[2] == bytes[4]) && (crc16bytes[3] == bytes[5])) {
                    success = true;
                    returnValue = "" + ((int) (bytes[0] & 0xFF) * 256 + (int) (bytes[1] & 0xFF)) + "," + ((int) (bytes[2] & 0xFF) * 256 + (int) (bytes[3] & 0xFF));
                } else
                    return ",";

            } catch (IOException e) {
                return ",";
            }
        }
        return returnValue;
    }

    public synchronized String read_voltage(){
        Long tm = System.currentTimeMillis();
        byte[] buffer = new byte[2];
        buffer[0] = (byte) 128;
        buffer[1] = (byte) 24;
        try {
            out.write(buffer, 0, 2);
        } catch (IOException e) {
            return "";
        }
        final Thread looperThread = Thread.currentThread();
        Timer t = new Timer();
        TimerTask freqtask = new TimerTask() {
            public void run() {
                looperThread.interrupt();
            }
        };
        t.schedule(freqtask, 50);
        boolean success = false;
        String returnValue = "";
        while(!success) {
            try {
                byte bytes[] = new byte[4];
                bytes[0] = (byte) in.read();
                bytes[1] = (byte) in.read();
                bytes[2] = (byte) in.read();
                bytes[3] = (byte) in.read();

                byte checksum[] = new byte[4];
                checksum[0] = (byte) 128;
                checksum[1] = (byte) 24;
                checksum[2] = bytes[0];
                checksum[3] = bytes[1];
                int crc16 = crc16(checksum);
                byte crc16bytes[] = ByteBuffer.allocate(4).putInt(crc16).array();
                if ((crc16bytes[2] == bytes[2]) && (crc16bytes[3] == bytes[3])) {
                    success = true;
                    returnValue = "" + bytes[1];
                } else
                    return "";
            } catch (IOException e) {
                return "";
            }
        }
        return returnValue;
    }
    */
}