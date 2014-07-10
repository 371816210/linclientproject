package com.inhuasoft.smart.server;

import java.util.regex.Pattern;

/**
 * ���򹤾���
 * �ṩ��֤���䡢�ֻ�š��绰���롢���֤���롢���ֵȷ���
 */
public final class RegexUtils {
 
    /**
     * ��֤Email
     * @param email email��ַ����ʽ��zhangsan@sina.com��zhangsan@xxx.com.cn��xxx����ʼ�������
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }
     
    /**
     * ��֤���֤����
     * @param idCard �������֤����15λ��18λ�����һλ���������ֻ���ĸ
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }
     
    /**
     * ��֤�ֻ���루֧�ֹ�ʸ�ʽ��+86135xxxx...���й��ڵأ���+00852137xxxx...���й���ۣ���
     * @param mobile �ƶ�����ͨ��������Ӫ�̵ĺ����
     *<p>�ƶ��ĺŶΣ�134(0-8)��135��136��137��138��139��147��Ԥ������TD����
     *��150��151��152��157��TDר�ã���158��159��187��δ���ã���188��TDר�ã�</p>
     *<p>��ͨ�ĺŶΣ�130��131��132��155��156�������ר�ã���185��δ���ã���186��3g��</p>
     *<p>���ŵĺŶΣ�133��153��180��δ���ã���189</p>
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }
     
    /**
     * ��֤�̶��绰����
     * @param phone �绰���룬��ʽ����ң�����绰���� + ��ţ����д��룩 + �绰���룬�磺+8602085588447
     * <p><b>��ң����� ���� ��</b>��ʶ�绰����Ĺ�ң�����ı�׼��ң�������롣���� 0 �� 9 ��һλ���λ���֣�
     *  ����֮���ǿո�ָ��Ĺ�ң�������롣</p>
     * <p><b>��ţ����д��룩��</b>����ܰ�һ�������� 0 �� 9 �����֣��������д������Բ���š���
     * �Բ�ʹ�õ������д���Ĺ�ң�������ʡ�Ը������</p>
     * <p><b>�绰���룺</b>���� 0 �� 9 ��һ���������� </p>
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }
     
    /**
     * ��֤����������͸�����
     * @param digit һλ���λ0-9֮�������
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex,digit);
    }
     
    /**
     * ��֤����͸�����������������
     * @param decimals һλ���λ0-9֮��ĸ������磺1.23��233.30
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex,decimals);
    } 
     
    /**
     * ��֤�հ��ַ�
     * @param blankSpace �հ��ַ�������ո�\t��\n��\r��\f��\x0B
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex,blankSpace);
    }
     
    /**
     * ��֤����
     * @param chinese �����ַ�
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }
     
    /**
     * ��֤���ڣ������գ�
     * @param birthday ���ڣ���ʽ��1992-09-03����1992.09.03
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex,birthday);
    }
     
    /**
     * ��֤URL��ַ
     * @param url ��ʽ��http://blog.csdn.net:80/xyang81/article/details/7705960? �� http://www.csdn.net:80
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }
     
    /**
     * ƥ���й���������
     * @param postcode ��������
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
     
    /**
     * ƥ��IP��ַ(��ƥ�䣬��ʽ���磺192.168.1.1��127.0.0.1��û��ƥ��IP�εĴ�С)
     * @param ipAddress IPv4��׼��ַ
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
    
    /**
     * ����û���
     * @param �û���
     * @retur n��֤�ɹ�����true����֤ʧ�ܷ���false
     */
	public static boolean checkUserName(String username) {   
		int lenght = username.length();
		if (lenght < 4 || lenght > 16) {
			return false;
		} else {
			return true;
		}
	}
    /**
     * �������
     * @param ����
     * @return ��֤�ɹ�����true����֤ʧ�ܷ���false
     */
    public static boolean checkPassWord(String password) {
    	int lenght = password.length();
    	if (lenght < 6 || lenght > 16) {
			return false;
		} else {
			return true;
		}
	}
    
}