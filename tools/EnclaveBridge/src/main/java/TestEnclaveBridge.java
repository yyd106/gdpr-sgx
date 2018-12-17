import Messages.Messages;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.UnsupportedEncodingException;

public class TestEnclaveBridge
{
    public static void main(String[] args) {
        EnclaveBridge eb = new EnclaveBridge();
        //String res = eb.handleVerification(eb.messageHandlerOBJ);
        byte[] res = eb.handleVerification(eb.messageHandlerOBJ);
        //System.out.println((res.length()));
        //System.out.println("("+res+")");
        //byte[] res = eb.handleVerification(eb.messageHandlerOBJ);
        try {
            //byte[] data = res.getBytes();
            //System.out.println("byte size:" + res.length());
            //String iosString = new String(data, "ISO-8859-1");
            //String iosString = new String(data, "GB2312");
            //Messages.MessageMsg0 result = Messages.MessageMsg0.parseFrom(iosString.getBytes());
            Messages.MessageMsg0 result = Messages.MessageMsg0.parseFrom(res);
            System.out.println(result.getEpid());
            System.out.println(result.getType());
            System.out.println(result.getStatus());
        //} catch (InvalidProtocolBufferException | UnsupportedEncodingException e) {
        } catch (InvalidProtocolBufferException  e) {
            System.out.println(e.getMessage());
        }
    }
}
