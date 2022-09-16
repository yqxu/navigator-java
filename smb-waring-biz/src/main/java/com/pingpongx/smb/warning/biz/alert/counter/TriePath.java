package com.pingpongx.smb.warning.biz.alert.counter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TriePath {
    //POST:/v3/b2b/dingDing/createAlertWorkOrder|200|119.23.150.175| 165ms | [] | >>>>:{"appName":"flowmore-kyc","content":"null","message":"o.a.d.remoting.transport.AbstractCodec   : Data length too large: 8431278, max payload: 8388608, channel: NettyChannel [channel=[id: 0x113de861, L:/10.20.29.57:42742 - R:/10.20.29.45:20881]]\n\norg.apache.dubbo.remoting.transport.ExceedPayloadLimitException: Data length too large: 8431278, max payload: 8388608, channel: NettyChannel [channel=[id: 0x113de861, L:/10.20.29.57:42742 - R:/10.20.29.45:20881]]\n\tat org.apache.dubbo.remoting.transport.AbstractCodec.checkPayload(AbstractCodec.java:44)\n\tat org.apache.dubbo.remoting.exchange.codec.ExchangeCodec.encodeRequest(ExchangeCodec.java:247)\n\tat org.apache.dubbo.remoting.exchange.codec.ExchangeCodec.encode(ExchangeCodec.java:69)\n\tat org.apache.dubbo.rpc.protocol.dubbo.DubboCountCodec.encode(DubboCountCodec.java:38)\n\tat org.apache.dubbo.remoting.transport.netty4.NettyCodecAdapter$InternalEncoder.encode(NettyCodecAdapter.java:70)\n\tat io.netty.handler.codec.MessageToByteEncoder.write(MessageToByteEncoder.java:107)\n\tat io.netty.channel.AbstractChannelHandlerContext.invo","hostName":"flowmore-kyc-c7c59f87-q9x2p","ip":"10.20.29.57","level":"ERROR","traceId":"-"} | <<<<:{"receivers":[{"code":"86","phone":"15557125616","email":"lusn@pingpongx.com","domainAccount":"lusn"},{"code":"86","phone":"15810035573","email":"penghp@pingpongx.com","domainAccount":"penghp"}]}
    // \tat org.apache.dubbo.remoting.transport.AbstractCodec.checkPayload(AbstractCodec.java:44)\n
    //org.apache.dubbo.remoting.transport.AbstractCodec.checkPayload(AbstractCodec.java:44)
    List<String> pathList ;

    public static TriePath of(String path){
        TriePath p = new TriePath();
        String[] pArr = path.split(".");
        p.pathList = Arrays.stream(pArr).collect(Collectors.toList());
        return p;
    }

    public String top(){
        if (pathList.size()==0){
            return null;
        }
        return pathList.get(0);
    }

    public TriePath pop(){
        if (pathList.size()==0){
            return this;
        }
        pathList.remove(0);
        return this;
    }


}
