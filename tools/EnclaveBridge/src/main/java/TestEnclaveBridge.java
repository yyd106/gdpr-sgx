import Messages.Messages;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.UnsupportedEncodingException;

public class TestEnclaveBridge
{
    public static void main(String[] args) {
        EnclaveBridge eb = new EnclaveBridge();
        String para[] = new String[2];
        //System.out.println(Messages.Type.RA_VERIFICATION.ordinal());
        Messages.InitialMessage init_msg = Messages.InitialMessage.newBuilder()
                .setType(Messages.Type.RA_VERIFICATION)
                .build();
        Messages.AllInOneMessage aio_msg = Messages.AllInOneMessage.newBuilder()
                .setType(Messages.Type.RA_VERIFICATION)
                .setInitMsg(init_msg)
                .build();
        para[0] = String.valueOf(Messages.Type.RA_VERIFICATION_VALUE);
        para[1] = init_msg.toString();
        byte[] res = eb.handleMessages(eb.messageHandlerOBJ, aio_msg.toByteArray());
        //String res = eb.handleVerification(eb.messageHandlerOBJ);
        //byte[] res = eb.handleVerification(eb.messageHandlerOBJ);
        //System.out.println((res.length()));
        //System.out.println("("+res+")");
        //byte[] res = eb.handleVerification(eb.messageHandlerOBJ);
        try {
            //byte[] data = res.getBytes();
            //System.out.println("byte size:" + res.length());
            //String iosString = new String(data, "ISO-8859-1");
            //String iosString = new String(data, "GB2312");
            //Messages.MessageMsg0 result = Messages.MessageMsg0.parseFrom(iosString.getBytes());
            Messages.MessageMSG0 result = Messages.MessageMSG0.parseFrom(res);
            System.out.println("type is:" + result.getType());
            System.out.println("epid is:" + result.getEpid());
            System.out.println("status :" + result.getStatus());
        //} catch (InvalidProtocolBufferException | UnsupportedEncodingException e) {
        } catch (InvalidProtocolBufferException  e) {
            System.out.println(e.getMessage());
        }
        /*
        */
    }
}
