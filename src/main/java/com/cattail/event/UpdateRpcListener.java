package com.cattail.event;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class UpdateRpcListener implements IRpcListener<UpdateRpcEventData> {

    @Override
    public void exec(UpdateRpcEventData updateRpcEventData) {
        System.out.println("触发修改" + updateRpcEventData);
    }
}
