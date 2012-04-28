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


package com.nokia.dempsy.example.wordcount.mp;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nokia.dempsy.example.simplewordcount.message.Letter;
import com.nokia.dempsy.example.wordcount.messages.LetterCount;

public class StartLetterRankMPTest
{
   
   private StartLetterRankMP mp = null;
   
   @Before
   public void init()
   {
      mp = new StartLetterRankMP();
   }
   
   @Test
   public void handleWordCountTest() throws CloneNotSupportedException, InterruptedException
   {
      final StartLetterRankMP clone1 = mp.clone();
      
      Thread t1 = new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            for(int j=0; j<20; j++)
            {
               for(int i =65; i<85; i++)
                  clone1.handleWordCount(new LetterCount(new Letter((char)i), j));
            }
         }
      });
      Thread t2 = new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            for(int j=0; j<20; j++)
            {
               for(int i=90; i>75; i--)
                  clone1.handleWordCount(new LetterCount(new Letter((char)i), j));
            }
         }
      });
      
      t1.start();
      t2.start();
      
      t1.join();
      t2.join();
      
      Assert.assertNotNull(clone1.map);
      Assert.assertEquals(26, clone1.map.size());
   }
   
   @Test
   public void outputTest() throws CloneNotSupportedException, InterruptedException
   {
      StartLetterRankMP clone1 = mp.clone();
      Assert.assertNull(clone1.map);
      Assert.assertNull(clone1.sortedset);
      LetterCount T1 = new LetterCount(new Letter('T'), 10);
      clone1.handleWordCount(T1);
      LetterCount A = new LetterCount(new Letter('A'), 20);
      clone1.handleWordCount(A);
      LetterCount S = new LetterCount(new Letter('S'), 30);
      clone1.handleWordCount(S);
      LetterCount B = new LetterCount(new Letter('B'), 27); 
      clone1.handleWordCount(B);
      LetterCount D = new LetterCount(new Letter('D'), 2);
      clone1.handleWordCount(D);
      LetterCount K = new LetterCount(new Letter('K'), 5);
      clone1.handleWordCount(K);
      LetterCount P = new LetterCount(new Letter('P'), 11);
      clone1.handleWordCount(P);
      LetterCount T2 = new LetterCount(new Letter('T'), 19);
      clone1.handleWordCount(T2);
      
      clone1.output();
      Assert.assertNotNull(clone1.sortedset);
      Assert.assertEquals(7, clone1.sortedset.size());
      Assert.assertEquals(S, clone1.sortedset.pollFirst());
      Assert.assertEquals(B, clone1.sortedset.pollFirst());
      Assert.assertEquals(A, clone1.sortedset.pollFirst());
      Assert.assertEquals(T2, clone1.sortedset.pollFirst());
      Assert.assertEquals(P, clone1.sortedset.pollFirst());
      Assert.assertEquals(K, clone1.sortedset.pollFirst());
      Assert.assertEquals(D, clone1.sortedset.pollFirst());
      
   }

   
}
