package net.dempsy.example.simplewordcount;

import java.util.concurrent.ArrayBlockingQueue;

import net.dempsy.NodeManager;
import net.dempsy.cluster.ClusterInfoException;
import net.dempsy.cluster.local.LocalClusterSessionFactory;
import net.dempsy.config.Cluster;
import net.dempsy.config.Node;
import net.dempsy.lifecycle.annotation.MessageProcessor;
import net.dempsy.monitoring.dummy.DummyNodeStatsCollector;
import net.dempsy.transport.blockingqueue.BlockingQueueReceiver;

public class SimpleWordCount {

    public static void main(final String[] args) throws ClusterInfoException {

        @SuppressWarnings("resource")
        final NodeManager nodeManager = new NodeManager()
            // add a node
            .node(
                // a node called word-count
                new Node.Builder("word-count")
                    // with the following clusters
                    .clusters(
                        // a cluster that has the adaptor
                        new Cluster("adaptor")
                            .adaptor(new WordAdaptor()),
                        // and a cluster that contains the WordCount message processor
                        new Cluster("counter")
                            .mp(new MessageProcessor<WordCount>(new WordCount()))
                            // with the following routing strategy
                            .routingStrategyId("net.dempsy.router.simple")

                    )
                    // this will basically disable monitoring for the example
                    .nodeStatsCollector(new DummyNodeStatsCollector())
                    .receiver(new BlockingQueueReceiver(new ArrayBlockingQueue<>(100000)))
                    .build()

            )

            // define the infrastructure to be used.
            .collaborator(new LocalClusterSessionFactory().createSession());

        nodeManager.start();

        System.out.println("Exiting Main");
    }
}
