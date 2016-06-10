package org.henry;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestLockImpl {

	private  ZooKeeper zk;
	@BeforeClass
	public void setUpZK() throws IOException
	{
		 zk = new ZooKeeper("localhost:2183", 15000, new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				
			}
			 
		 });
	}
	
	@Test
	public void testTryLock()
	{
		LockImpl lock_1 = new LockImpl(zk);
		LockImpl lock_2 = new LockImpl(zk);
		LockImpl lock_3 = new LockImpl(zk);
		Assert.assertEquals(lock_1.tryLock(), true);
		Assert.assertEquals(lock_2.tryLock(), false);
		Assert.assertEquals(lock_3.tryLock(), false);
	}
}
