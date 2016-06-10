package org.henry;

import org.apache.zookeeper.KeeperException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MasterTest {

	@Test(expectedExceptions=KeeperException.NodeExistsException.class)
	public void testRunForMaster() throws Exception
	{
		Master m = new Master("localhost:2181");
		m.startZK();
		m.runForMaster();
		
		try {
			m.runForMaster();
			Assert.fail("should have thrown exception.");
		} catch(Exception e) {
			throw e;
		}
	}
}
