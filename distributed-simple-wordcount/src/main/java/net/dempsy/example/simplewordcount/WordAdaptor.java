package net.dempsy.example.simplewordcount;

import java.util.concurrent.atomic.AtomicBoolean;

import net.dempsy.messages.Adaptor;
import net.dempsy.messages.Dispatcher;

public class WordAdaptor implements Adaptor {
    private Dispatcher dempsy;
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * This method is called by the framework to provide a handle to the
     * Dempsy message bus. It's called prior to start()
     */
    @Override
    public void setDispatcher(final Dispatcher dispatcher) {
        this.dempsy = dispatcher;
    }

    @Override
    public void start() {
        // ... set up the source for the words.
        running.set(true);
        while(running.get()) {
            // obtain data from an external source
            final String wordString = getNextWordFromSoucre();
            if(wordString == null) // the first null ends the stream.
                running.set(false);
            else {
                // Create a "Word" message and send it into the processing stream.
                try {
                    dempsy.dispatchAnnotated(new Word(wordString));
                } catch(final Exception e) {
                    e.printStackTrace(); // we want to keep going
                }
            }
        }
    }

    @Override
    public void stop() {
        running.set(false);
    }

    private static final String[] wordSource = {"it","was","the","best","of","times","it","was","the","worst","of","times"};
    private int wordSourceIndex = 0;

    public boolean haveSentAllWords() {
        return wordSourceIndex >= wordSource.length;
    }

    private String getNextWordFromSoucre() {
        if(wordSourceIndex >= wordSource.length)
            wordSourceIndex = 0;
        return wordSource[wordSourceIndex++];
    }
}
