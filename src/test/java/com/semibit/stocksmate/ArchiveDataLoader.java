package com.semibit.stocksmate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

@RunWith(JUnit4.class)
public class ArchiveDataLoader {

    @Test
    public void loadData(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Mar_01_22.json").getFile());
        String path = file.getAbsolutePath();
        var x = StocksMateApplication.loadFromArchive(path);
        Assert.assertTrue(x.size() > 0);
    }
}
