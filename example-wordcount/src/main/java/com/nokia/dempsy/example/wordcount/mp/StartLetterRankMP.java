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

package com.nokia.dempsy.example.wordcount.mp;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.annotations.Output;
import com.nokia.dempsy.config.OutputSchedule;
import com.nokia.dempsy.example.simplewordcount.message.Letter;
import com.nokia.dempsy.example.wordcount.messages.LetterCount;

/**
 * <p>
 * Message Processor for storing counts for the all LetterCount messages. For every 10th message the cache is sorted to determine Letters with highest count.
 * </p>
 * 
 * <p>
 * The class has to be annotated with {@link MessageProcessor} annotation for the container to process it.
 * </p>
 * 
 * <p>
 * The message processor class needs to implement interface {@link Cloneable}. One instance of this class will be provided to the container as a prototype. The
 * container calls on the overridden clone method to create additional instances needs to process each unique message.
 * </p>
 * 
 */
@MessageProcessor
public class StartLetterRankMP implements Cloneable
{
   private Logger logger = Logger.getLogger(StartLetterRankMP.class);

   protected ConcurrentHashMap<Letter, LetterCount> map = null;

   protected TreeSet<LetterCount> sortedset;

   /**
    * <p>
    * Message Handler to process incoming messages {@link LetterCount}
    * </p>
    * 
    * <p>
    * The method has to be marked as Message Handler using annotation {@link MessageHandler} annotation.
    * 
    * @param word
    * @return
    */
   @MessageHandler
   public void handleWordCount(LetterCount letterCount)
   {
      if(map == null)
      {
         synchronized(this)
         {
            if(map == null)
            {
               map = new ConcurrentHashMap<Letter, LetterCount>();
            }
         }
      }
      map.put(letterCount.getLetter(), letterCount);
      logger.info("received message for " + letterCount);
   }

   /**
    * Clone method provided for the container to create additional instances. This method needs to be <b>public</b> and should return the <b>Message Processor
    * instance</b>.
    * 
    */
   @Override
   public StartLetterRankMP clone() throws CloneNotSupportedException
   {
      return (StartLetterRankMP)super.clone();
   }

   /**
    * Output method is called on a timer with scheduled specified by {@link OutputSchedule} in the configuration
    */
   @Output
   public void output()
   {
      sortedset = new TreeSet<LetterCount>(new Comparator<LetterCount>()
      {
         @Override
         public int compare(LetterCount o1, LetterCount o2)
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
            else if(o1.getLetter().equals(o2.getLetter()))
            {
               return 0;
            }
            else
               return 1;
         }
      });
      sortedset.addAll(map.values());
      StringBuilder sb = new StringBuilder("Sorted output \n");
      for(LetterCount letter: sortedset)
      {
         sb.append("   Letter:" + letter.getLetter() + " count:" + letter.getCount() +"\n");
      }
      
      logger.info(sb.toString());
   }
}
