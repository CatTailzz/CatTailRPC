package com.cattail.common.constants;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public enum MsgType {

    REQUEST,
    RESPONSE,
    HEARTBEAT;


    public static MsgType findByType(int type) {
        return MsgType.values()[type];
    }
}
