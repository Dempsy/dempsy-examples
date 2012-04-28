/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nokia.dempsy.example.simplewordcount.mp;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.example.simplewordcount.message.Word;
import com.nokia.dempsy.example.simplewordcount.message.WordCount;

/**
 * <p>
 * Message Processor for storing counts for the all wordcount messages. 
 * For every 10th message the cache is sorted to determine words with highest count.
 * </p>
 * 
 * <p>
 * The class has to be annotated with {@link MessageProcessor} annotation for the container to process it.
 * </p>
 * 
 * <p>
 * The message processor class needs to implement interface {@link Cloneable}. 
 * One instance of this class will be provided to the container as a prototype. The
 * container calls on the overridden clone method to create additional 
 * instances needs to process each unique message.
 * </p>
 * 
 */

@MessageProcessor
public class WordRankMP implements Cloneable
{
   private Logger logger = Logger.getLogger(WordRankMP.class);

   protected ConcurrentHashMap<Word, WordCount> map = null;

   protected AtomicLong counter = null;

   protected TreeSet<WordCount> sortedset;

   /**
    * <p>
    * Message Handler to process incoming messages {@link WordCount}
    * </p>
    * 
    * <p>
    * The method has to be marked as Message Handler using annotation 
    * {@link MessageHandler} annotation.
    * 
    * @param word
    * @return
    */
   @MessageHandler
   public void handleWordCount(WordCount wordCount)
   {
      if(counter == null)
      {
         synchronized(this)
         {
            if(counter == null)
            {
               counter = new AtomicLong();
               map = new ConcurrentHashMap<Word, WordCount>();
            }
         }
      }
      map.put(wordCount.getWord(), wordCount);
      long count = counter.incrementAndGet();
      logger.info("received message for " + wordCount);
      if(count % 10 == 0)
      {
         sortedset = new TreeSet<WordCount>(new Comparator<WordCount>()
         {
            @Override
            public int compare(WordCount o1, WordCount o2)
            {
               if(o1 == o2)
               {
                  return 0;
               }
               if(o1 == null)
               {
                  return 2;
               }
               if(o2 == null)
               {
                  return -2;
               }
               if(o1.getCount() < o2.getCount())
               {
                  return 2;
               }
               else if(o1.getCount() > o2.getCount())
               {
                  return -2;
               }
               else if(o1.getWord().equals(o2.getWord()))
               {
                  return 0;
               }
               else
                  return 1;
            }
         });
         sortedset.addAll(map.values());
         int i = 0;
         for(WordCount word: sortedset)
         {
            logger.debug("Word:" + word.getWord() + " count:" + word.getCount());
            if(++i > 9)
            {
               break;
            }
         }
      }
   }

   /**
    * Clone method provided for the container to create additional instances. 
    * This method needs to be <b>public</b> and should return the <b>Message Processor
    * instance</b>.
    * 
    */
   @Override
   public WordRankMP clone() throws CloneNotSupportedException
   {
      WordRankMP mp = (WordRankMP)super.clone();
      return mp;
   };
}
