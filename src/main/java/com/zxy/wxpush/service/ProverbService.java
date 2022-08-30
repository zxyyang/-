package com.zxy.wxpush.service;

/**
 * @Author: zx.yang
 * @DateTime: 2022/8/23 14:50
 * @Description: TODO
 */
public interface ProverbService {
    /**
     * 这个接口很奇怪，可能会返回奇奇怪怪的句子
     * @return
     */
    String getOneProverbRandom();

    String translateToEnglish(String sentence);

    /**
     * 得到正常的句子
     * @return
     */
    String[] getOneNormalProverb();
}
