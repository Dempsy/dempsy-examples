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


package com.nokia.dempsy.example.userguide.wordcount;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.annotations.Output;

/**
 * This message processor will recieve incoming {@link CountedWord} instances and 
 * keep track of the top 10.
 */
@MessageProcessor
public class WordRank implements Cloneable
{
   private Comparator<CountedWord> comparator = new Comparator<CountedWord>()
   {
      @Override
      public int compare(CountedWord o1, CountedWord o2)
      {
         long o1c = o1.getCount();
         long o2c = o2.getCount();
         return o1c < o2c ? -1 : (o1c > o2c ? 1 : 0);
      }
   };
   
   private TreeSet<CountedWord> topTen = new TreeSet<CountedWord>(comparator);
   
   @MessageHandler
   public void handleCount(CountedWord countedWord)
   {
      topTen.add(countedWord);
      
      if (topTen.size() > 100)
         trim();
   }
   
   @Output
   public void outputResults()
   {
      trim();
      
      for (Iterator<CountedWord> iter = topTen.descendingIterator(); iter.hasNext();)
      {
         CountedWord cur = iter.next();
         System.out.println(cur.getWordText() + ":" + cur.getCount());
      }
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      return (WordRank)super.clone();
   }
   
   private void trim()
   {
      TreeSet<CountedWord> newTopTen = new TreeSet<CountedWord>(comparator);
      Iterator<CountedWord> iter = topTen.descendingIterator();
      for (int i=0; i < 10; i++)
         newTopTen.add(iter.next());
      topTen = newTopTen;
   }

}
