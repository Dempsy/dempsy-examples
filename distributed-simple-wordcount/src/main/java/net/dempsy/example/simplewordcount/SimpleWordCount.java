package net.dempsy.example.simplewordcount;

import net.dempsy.NodeManager;
import net.dempsy.cluster.ClusterInfoException;
import net.dempsy.cluster.zookeeper.ZookeeperSessionFactory;
import net.dempsy.config.Cluster;
import net.dempsy.config.Node;
import net.dempsy.lifecycle.annotation.MessageProcessor;
import net.dempsy.monitoring.dummy.DummyNodeStatsCollector;
import net.dempsy.serialization.jackson.JsonSerializer;
import net.dempsy.serialization.java.JavaSerializer;
import net.dempsy.transport.tcp.nio.NioReceiver;

public class SimpleWordCount {

    public static void main(final String[] args) throws InterruptedException, IllegalStateException, IllegalArgumentException, ClusterInfoException {

        final WordAdaptor wordAdaptor = new WordAdaptor();

        @SuppressWarnings("resource")
        final NodeManager nodeManager = new NodeManager()
            // add a node
            .node(
                // a node in an application called word-count
                new Node.Builder("word-count")
                    // with the following clusters
                    .clusters(
                        // a cluster that has the adaptor
                        new Cluster("adaptor")
                            .adaptor(wordAdaptor),
                        // and a cluster that contains the WordCount message processor
                        new Cluster("counter")
                            .mp(new MessageProcessor<WordCount>(new WordCount()))
                            // with the following routing strategy
                            .routingStrategyId("net.dempsy.router.managed")

                    )
                    // this will basically disable monitoring for the example
                    .nodeStatsCollector(new DummyNodeStatsCollector())
                    // use a blocking queue as the transport mechanism since this is all running in the same process
                    .receiver(new NioReceiver<Object>(new JavaSerializer()))
                    .build()

            )

            // define the infrastructure to be used.

            // we want to connect to a zookeeper instance running on this machine.
            .collaborator(new ZookeeperSessionFactory("localhost:2181", 3000, new JsonSerializer()).createSession());

        // start dempsy processing for this node in the background.
        nodeManager.start();

        // wait for the node manager to be started.
        while(!nodeManager.isReady())
            Thread.yield();

        // we're just going to wait *forever*
        Thread.sleep(999999);
    }
}
