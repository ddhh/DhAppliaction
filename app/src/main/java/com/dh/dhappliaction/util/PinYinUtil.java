package com.dh.dhappliaction.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

	public static String getPinyinOfHanyu(String chineseStr){
        StringBuilder zhongWenPinYin = new StringBuilder();   
        char[] inputArray = chineseStr.toCharArray();   
        try {
	        for (int i = 0; i < inputArray.length; i++) {   
	            String[] pinYin;
				pinYin = PinyinHelper.toHanyuPinyinStringArray(inputArray[i], getDefaultOutputFormat());
	            if (pinYin != null)  // ��ת����ƴ��    
	                zhongWenPinYin.append(pinYin[0]);   
	            else {  // û��ת����ƴ����˵���Ƿ������ַ�������Ӣ�ġ�������ŵ�  
	                zhongWenPinYin.append(inputArray[i]);
	            }
	        }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        return zhongWenPinYin.toString();
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        // ��д
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // �����������硰���ġ���ƴ��Ϊ��zhong wen��
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // ���̡���ƴ��Ϊ lu
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);

        return format;
    }
	
}
