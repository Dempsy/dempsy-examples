package net.dempsy.example.simplewordcount;

import static net.dempsy.util.Functional.uncheck;

import java.util.concurrent.ArrayBlockingQueue;

import net.dempsy.NodeManager;
import net.dempsy.cluster.local.LocalClusterSessionFactory;
import net.dempsy.config.Cluster;
import net.dempsy.config.Node;
import net.dempsy.lifecycle.annotation.MessageProcessor;
import net.dempsy.monitoring.dummy.DummyNodeStatsCollector;
import net.dempsy.transport.blockingqueue.BlockingQueueReceiver;

public class SimpleWordCount {

    public static void main(final String[] args) {

        final WordAdaptor wordAdaptor = new WordAdaptor();

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
                            .adaptor(wordAdaptor),
                        // and a cluster that contains the WordCount message processor
                        new Cluster("counter")
                            .mp(new MessageProcessor<WordCount>(new WordCount()))
                            // with the following routing strategy
                            .routingStrategyId("net.dempsy.router.simple")

                    )
                    // this will basically disable monitoring for the example
                    .nodeStatsCollector(new DummyNodeStatsCollector())
                    // use a blocking queue as the transport mechanism since this is all running in the same process
                    .receiver(new BlockingQueueReceiver(new ArrayBlockingQueue<>(100000)))
                    .build()

            )

            // define the infrastructure to be used. Since we're in a single process
            // we're going to use a local collaborator. Alternatively we'd specify
            // using zookeeper to coordinate across processes and machines.
            .collaborator(new LocalClusterSessionFactory().createSession());

        // start dempsy processing for this node in the background.
        nodeManager.start();

        // wait for the node manager to be started.
        while(!nodeManager.isReady())
            Thread.yield();

        // wait for all of the words to be sent.
        while(!wordAdaptor.haveSentAllWords())
            Thread.yield();

        // wait some amount of time for the words to be processed. Obvious this is not
        // an appropriate technique for anything but this simple example
        uncheck(() -> Thread.sleep(1000));

        // shut down the processing in this node.
        nodeManager.stop();

        System.out.println("Exiting Main");
    }
}
