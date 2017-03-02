package org.wing4j.common.utils;

import org.apache.commons.io.*;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by wing4j on 2017/3/2.
 */
public class DynamicFileTest {

    @Test
    public void testSave() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.save("this is a test".getBytes());
    }

    @Test
    public void testRead() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.save("this is a test".getBytes());
        InputStream is = file.read();
        byte[] data = new byte[is.available()];
        is.read(data);
        is.close();
        System.out.println(new String(data));
    }
    @Test(expected = DynamicFile.NotExistsCommitOrRollbackFileException.class)
    public void testRollback1() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.mkdirs();
        file.rollback();
    }
    @Test
    public void testRollback2() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.begin();
        file.rollback();
    }
    @Test(expected = DynamicFile.NotExistsCommitOrRollbackFileException.class)
    public void testCommit1() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.mkdirs();
        file.commit();
    }
    @Test
    public void testCommit2() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.begin();
        file.commit();
    }

    @Test
    public void testCreate() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.begin();
    }

    @Test(expected = DynamicFile.NotSuchFieldException.class)
    public void testGetRealFile1() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.begin();
        file.getRealFile();
    }

    @Test
    public void testGetRealFile2() throws Exception {
        DynamicFile file = new DynamicFile("./target/"+ UUID.randomUUID().toString()+"/test.txt", 2, 10);
        file.begin();
        file.commit();
        file.begin();
        file.commit();
        file.begin();
        file.commit();
        File file1 = file.getRealFile();
        System.out.println(file1);
    }
}