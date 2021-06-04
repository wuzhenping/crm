package com.msy.plus.core.key;

import com.msy.plus.util.DateUtils;

/**
 * @author wzp
 * @version 2021-06-03
 */
public class CodeGenerator {
    private static CodeGenerator instance = null;

    public static final String CUSTOMER_MANAGER_SING = "CM";//客户管理

    private CodeGenerator() {
    }

    public static CodeGenerator getInstance() {
        if (instance == null) {
            instance = new CodeGenerator();
        }
        return instance;
    }

    //客户管理
    public String getCustomerManagerNo() {
        return CUSTOMER_MANAGER_SING + DateUtils.getSubYear() + DateUtils.getMonth() +DateUtils.getDay() + GeneratorId.getInstance().nextCode(7);
    }

}
