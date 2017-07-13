package com.raysmond.artirest.service.functions;

import java.math.BigDecimal;

/**
 * Created by Raysmond on 2017/7/14.
 */
public interface FunctionService {
    BigDecimal calcFunc(String func, Object... args);
}
