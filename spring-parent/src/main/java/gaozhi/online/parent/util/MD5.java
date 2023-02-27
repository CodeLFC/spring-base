package gaozhi.online.parent.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
  * @description: (MD5字符串加密)
  * @author: gaozhi.online
  * @date: 2021/10/19 12:07
  * @throws:
  */
public class MD5 {
    private static final int length=50;//截取MD5长度,可以自己设置
	private MD5() {}
	/**
	 * 对字符串进行MD5加密
	 * @param plainText  要加密的字符串
	 * @return
	 */
    public static String generateMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");//获取MD5实例
            md.update(plainText.getBytes());//此处传入要加密的byte类型值
            byte[] digest = md.digest();//此处得到的是md5加密后的byte类型值
            /*
                     下边的运算就是自己添加的一些二次小加密，记住这个千万不能弄错乱，
                      否则在解密的时候，你会发现值不对的（举例：在注册的时候加密方式是一种，
                     在我们登录的时候是不是还需要加密它的密码然后和数据库的进行比对，但是
                    最后我们发现，明明密码对啊，就是打不到预期效果，这时候你就要想一下，你是否
                     有改动前后的加密方式）   
            */
            int i;
            StringBuilder sb = new StringBuilder();
            for (int offset = 0; offset < digest.length; offset++) {
                i = digest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append(0);
                sb.append(Integer.toHexString(i));//通过Integer.toHexString方法把值变为16进制
            }
            return sb.substring(0, Math.min(length, digest.length));//从下标0开始，length目的是截取多少长度的值
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
