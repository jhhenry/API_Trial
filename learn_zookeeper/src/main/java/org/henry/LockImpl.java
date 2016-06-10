package org.henry;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class LockImpl {
	private static final String LOCK_IMPL_PATH = "/LockImpl";
	private final ZooKeeper zk;

	public LockImpl(ZooKeeper zk) {
		this.zk = zk;
	}
	
	public boolean tryLock()
	{
		try {
			zk.create(LOCK_IMPL_PATH, "lockIt".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			return true;
		} catch (KeeperException | InterruptedException e) {
			return false;
			
		}
	}
	
	public boolean unLock()
	{
		try {
			zk.delete(LOCK_IMPL_PATH, -1);
			return true;
		} catch (InterruptedException | KeeperException e) {
			return false;
		}
	}
}
