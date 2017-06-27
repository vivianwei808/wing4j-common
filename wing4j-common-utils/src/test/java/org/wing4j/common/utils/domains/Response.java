/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wing4j.common.utils.domains;

import lombok.*;

import java.io.Serializable;

/**
 * Created by wing4j on 2017/6/25.
 * 请求对象
 */
@ToString
@EqualsAndHashCode
@Builder
public class Response<T, K> implements Serializable {
    /**
     * 通道编号
     */
    @Getter
    @Setter
    String channelNo;
    /**
     * 接口名称
     */
    @Getter
    @Setter
    String name;
    /**
     * 接口版本号
     */
    @Getter
    @Setter
    String version;
    /**
     * 错误码
     */
    @Getter
    @Setter
    String code;
    /**
     * 错误信息
     */
    @Getter
    @Setter
    String desc;
    /**
     * 应答包装的类名
     */
    @Getter
    String className;
    /**
     * 签名
     */
    @Getter
    @Setter
    String sign;
    /**
     * 签名方式
     */
    @Getter
    @Setter
    String signType;
    /**
     * 加密方式
     */
    @Getter
    @Setter
    String cipherType;
    /**
     * 加密数据
     */
    @Getter
    @Setter
    String data;
}
