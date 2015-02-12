/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.marfcat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.marfcat.Marfcat;

/**
 *
 * @author c_bode
 */
public class MarfcatTest {
    
    Marfcat marf;
    
    public MarfcatTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void test1() {
        marf = new Marfcat();
        try {
            marf.train("lib/marfcat/apache-tomcat-5.5.13-src_train.xml");
        } catch (Exception e) {
            
        }
    }
}
