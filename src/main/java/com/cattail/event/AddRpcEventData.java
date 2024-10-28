package com.cattail.event;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class AddRpcEventData implements RpcEventData{

    private Object data;

    public AddRpcEventData(Object data) {
        this.data = data;
    }

    @Override
    public void setData(Object o) {
        this.data = o;
    }

    @Override
    public Object getData() {
        return data;
    }
}
