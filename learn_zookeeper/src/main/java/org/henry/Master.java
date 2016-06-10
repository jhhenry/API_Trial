package org.henry;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;
import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Master implements Watcher {
    ZooKeeper zk;
    String hostPort;
    String serverId = Integer.toHexString(new Random().nextInt());
   	private boolean isLeader;
   	
   	
    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws IOException {
        zk = new ZooKeeper(hostPort, 15000, this); 
    }

    public void process(WatchedEvent e) {
        System.out.println(e); 
    }
    
    void stopZK() throws Exception { zk.close(); }
    
   
    boolean checkMaster() throws KeeperException, InterruptedException {
        while (true) {
            try {
                Stat stat = new Stat();
                byte data[] = zk.getData("/master", false, stat); 
                setLeader(new String(data).equals(serverId)); 
                return true;
            } catch (NoNodeException e) {
                // no master, so try create again
                return false;
            } catch (ConnectionLossException e) {
            }
        }
    }
    void runForMaster() throws KeeperException, InterruptedException 
    {
    	   while (true) {
               try { 
                   zk.create("/master", serverId.getBytes(),
                             OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL); 
                   setLeader(true);
                   break;
               } catch (NodeExistsException e) {
            	   setLeader(false);
            	   throw e;
               } catch (ConnectionLossException e) { 
               }
               if (checkMaster()) break; 
           }
    }

    public static void main(String args[])
        throws Exception {
        Master m = new Master(args[0]);

        m.startZK();

        // wait for a bit
        Thread.sleep(60000); 
    }

	public boolean isLeader() {
		return isLeader;
	}

	private void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
}