package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class SlsAlert implements ThirdPartAlert{
    private static final long serialVersionUID = -5136469378192983192L;
//    {
//        "appName": "business-user-biz",
//            "className": "c.p.b.u.b.u.impl.UnifyUserServiceImpl    :",
//            "content": " createUserDTO = {\"callingPrefix\":\"+86\",\"email\":\"741458077@QQ.COM\",\"identity\":\"FLOWGOLD\",\"inviteDTO\":{\"channel\":\"flowmore\",\"inviteCode\":\"FMmarketing\"},\"kycLocation\":\"\",\"needCheckUnique\":true,\"password\":\"a711291cdcd3f276a97cf1a5a7a7e046d0dd42783a4f443f385afffeb6663e51\",\"phoneNo\":\"15170865027\",\"signupLocation\":\"hz\",\"url\":\"https://flowmore.pingpongx.com/entrance/signup?location=&phone=15170865027&inviteCode=FMmarketing[cookie]:FMmarketing\",\"userType\":\"NORMAL\"} 统一用户注册出现异常：\n\ncom.pingpongx.business.common.exception.BizException: 邮箱已被注册！\n\tat com.pingpongx.business.common.exception.Assert.isTrue(Assert.java:50)\n\tat com.pingpongx.business.common.exception.Assert.warnIsTrue(Assert.java:18)\n\tat com.pingpongx.business.user.behaviour.uums.meta.MetaUserHelper.checkUserUniqueByEmail(MetaUserHelper.java:287)\n\tat com.pingpongx.business.user.behaviour.uums.impl.UnifyUserServiceImpl.createUser(UnifyUserServiceImpl.java:132)\n\tat com.pingpongx.business.user.behaviour.uums.impl.UnifyUserServiceI",
//            "hostName": "B2BNEW-192.168.138.123-Hangzhou",
//            "ip": "192.168.138.123",
//            "level": "ERROR",
//            "traceId": "gadaee0e78606466096666bc70088d8ca"
//    }
    String appName;
    String className;
    String content;
    String hostName;
    String ip;
    String level;
    String traceId;

    @Override
    public IdentityPath<String> getCountPath() {
        return IdentityPath.of(Stream.of(appName,className).collect(Collectors.toList()));
    }

//    @Override
//    public Set<String> countTags() {
//        boolean isDubboTimeOut;
//        return Stream.of("sls",appName,isDubboTimeOut);
//    }
}
