package org.wing4j.common.utils;

import org.junit.Test;
import org.wing4j.common.utils.domains.Request;
import org.wing4j.common.utils.service.Interface1;
import org.wing4j.common.utils.service.Interface2;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/6/27.
 */
public class GenericUtilTest {

    @Test
    public void testExtractClass() throws Exception {
        Class[] classes = GenericUtil.extractInterface(Interface2.class, Interface1.class);
        System.out.println(classes[0]);
        System.out.println(classes[1]);

//        Class[] classes1 = GenericUtil.extractInterface(Interface1.class, Interface0.class);
//        System.out.println(classes1[0]);
//        System.out.println(classes1[1]);
//        System.out.println(classes1[2]);
    }

    @Test
    public void testExtractParams() throws Exception {
        Class[] classes = GenericUtil.extractInterfaceMethodParams(Interface2.class, "doing", new Class[]{Request.class}, 0);
        System.out.println(classes[0]);
        System.out.println(classes[1]);
        Class[] classes1 = GenericUtil.extractInterfaceMethodParams(Interface2.class, "work", new Class[]{Request.class}, 0);
        System.out.println(classes1[0]);
        System.out.println(classes1[1]);
    }

    @Test
    public void testExtractReturn() throws Exception {
        Class[] classes = GenericUtil.extractInterfaceMethodReturn(Interface2.class, "doing", new Class[]{Request.class});
        System.out.println(classes[0]);
        System.out.println(classes[1]);
        Class[] classes1 = GenericUtil.extractInterfaceMethodReturn(Interface2.class, "work", new Class[]{Request.class});
        System.out.println(classes1[0]);
        System.out.println(classes1[1]);
    }

}