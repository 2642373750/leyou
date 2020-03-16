package com.zs.user.service;

import com.zs.user.utils.CodecUtils;

public class aa {

    public static void main(String[] args) {
        String s = CodecUtils.md5Hex("123456", "b968b171dc5944a9b204f4888d555f52");
        System.out.println(s);
    }
}
