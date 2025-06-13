package egovframework.com.fivemlist.web;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;


public class encrypt {
    @Resource(name = "ARIACryptoService")
    private static EgovCryptoService egovCryptoService;

    @Resource(name = "passwordEncoder")
    private EgovPasswordEncoder passwordEncoder;

    public static String doencrypt(String pwd) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub

        // 암호화/복호화에 사용될 key 생성
        EgovPasswordEncoder pe = new EgovPasswordEncoder();

        String str = pe.encryptPassword(pwd);
        System.out.println(str);
        System.out.println(pwd + " / " + str + " | " + pe.checkPassword(pwd, str));
        return str;
    }
}
