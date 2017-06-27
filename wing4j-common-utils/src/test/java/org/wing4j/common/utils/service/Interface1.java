package org.wing4j.common.utils.service;

import org.wing4j.common.utils.domains.FetchInterfaceRequest;
import org.wing4j.common.utils.domains.FetchInterfaceResponse;
import org.wing4j.common.utils.domains.Request;
import org.wing4j.common.utils.domains.Response;

public interface Interface1<T, K> extends Interface0<T, K, String> {
    Response<FetchInterfaceResponse, String> doing(Request<FetchInterfaceRequest, Integer> request);
}
