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


package com.nokia.dempsy.example.simplewordcount.mp;

import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nokia.dempsy.example.simplewordcount.message.Word;
import com.nokia.dempsy.example.simplewordcount.message.WordCount;

public class WordRankMPTest
{
   
   private WordRankMP mp;
   
   @Before
   public void init()
   {
      mp = new WordRankMP();
   }
   
   @Test
   public void cloneTest() throws CloneNotSupportedException
   {
      WordRankMP clone = mp.clone();
      Assert.assertNotNull(clone);
      Assert.assertNotSame(mp, clone);
      clone.map = new ConcurrentHashMap<Word, WordCount>();
      Assert.assertNull(mp.map);
      Assert.assertNotNull(clone.map);
   }
   
   @Test
   public void handleWordCountTest2() throws CloneNotSupportedException
   {
      WordCount wordCount = new WordCount(new Word("Test"), 1);
      WordRankMP clone1 = mp.clone();
      for(int i=0; i<11; i++)
         clone1.handleWordCount(wordCount);
      
      Assert.assertNotNull(clone1.map);
      Assert.assertEquals(1, clone1.map.size());
      Assert.assertEquals(1, clone1.map.get(wordCount.getWord()).getCount());
   }
   
   @Test
   public void handleWordCountTest3() throws CloneNotSupportedException
   {
      WordRankMP clone1 = mp.clone();
      clone1.handleWordCount(new WordCount(new Word("Test1"), 1));
      clone1.handleWordCount(new WordCount(new Word("Test2"), 17));
      clone1.handleWordCount(new WordCount(new Word("Test3"), 1));
      clone1.handleWordCount(new WordCount(new Word("Test4"), 12));
      clone1.handleWordCount(new WordCount(new Word("Test5"), 3));
      clone1.handleWordCount(new WordCount(new Word("Test6"), 5));
      clone1.handleWordCount(new WordCount(new Word("Test7"), 2));
      clone1.handleWordCount(new WordCount(new Word("Test8"), 3));
      clone1.handleWordCount(new WordCount(new Word("Test9"), 5));
      clone1.handleWordCount(new WordCount(new Word("Test10"), 2));
      clone1.handleWordCount(new WordCount(new Word("Test11"), 3));
      clone1.handleWordCount(new WordCount(new Word("Test12"), 5));
      clone1.handleWordCount(new WordCount(new Word("Test3"), 2));
      
      Assert.assertNotNull(clone1.map);
      Assert.assertEquals(12, clone1.map.size());
      Assert.assertEquals(2, clone1.map.get(new Word("Test3")).getCount());
      
      Assert.assertNotNull(clone1.sortedset);
      Assert.assertEquals(10, clone1.sortedset.size());
      Assert.assertEquals(new Word("Test2"), clone1.sortedset.first().getWord());
   }
}
