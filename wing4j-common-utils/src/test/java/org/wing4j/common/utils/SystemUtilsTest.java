package org.wing4j.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/3/7.
 * 8:15
 */
public class SystemUtilsTest {

    @Test
    public void testPid() throws Exception {
        String pid = SystemUtils.pid();
        System.out.println(pid);
    }
}