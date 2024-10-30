package com.cattail.tolerant;

import com.cattail.common.constants.FaultTolerant;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/30
 * @Copyright: https://github.com/CatTailzz
 */
public class FaultTolerantFactory {

    private static Map<FaultTolerant, IFaultTolerantStrategy> FAULT_TOLERANT_STRATEGY_MAP = new HashMap<>();

    static {
        FAULT_TOLERANT_STRATEGY_MAP.put(FaultTolerant.Failover, new FailoverIFaultTorelrantStrategy());
        FAULT_TOLERANT_STRATEGY_MAP.put(FaultTolerant.FailFast, new FailFastIFaultTolerantStrategy());
    }

    public static IFaultTolerantStrategy get(FaultTolerant faultTolerant) {
        return FAULT_TOLERANT_STRATEGY_MAP.get(faultTolerant);
    }
}
