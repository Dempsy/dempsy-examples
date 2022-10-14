package net.dempsy.example.simplewordcount;

import net.dempsy.lifecycle.annotation.MessageHandler;
import net.dempsy.lifecycle.annotation.Mp;

@Mp
public class WordCount implements Cloneable {
    private long count = 0;

    @MessageHandler
    public void countWord(final Word word) {
        count++;
        if((count % 10000) == 0)
            System.out.println("The word \"" + word.getWordText() + "\" has a count of " + count);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
