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
		ZooKeeper zk = new ZooKeeper("localhost:9983", 15000, this);
		System.out.println(zk.getChildren("/", false));
	}

	public void process(WatchedEvent e) {
		System.out.println(e); 
	}
}
