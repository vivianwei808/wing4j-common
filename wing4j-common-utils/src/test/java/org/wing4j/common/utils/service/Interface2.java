package org.wing4j.common.utils.service;

import org.wing4j.common.utils.domains.Request;
import org.wing4j.common.utils.domains.Response;

import java.math.BigDecimal;

public interface Interface2 extends Interface1<String,Integer> {
    Response<String, Integer> work(Request<BigDecimal, Double> request);
}
