package org.henry;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.testng.annotations.Test;

public class ZKClientTest implements Watcher {
	@Test
	public void f() throws IOException, KeeperException, InterruptedException {
		ZooKeeper zk = new ZooKeeper("localhost:2183,localhost:2182,localhost:2181", 15000, this);
		System.out.println(zk.getChildren("/", false));
		zk.close();
	}

	public void process(WatchedEvent e) {
		System.out.println(e); 
	}
}
