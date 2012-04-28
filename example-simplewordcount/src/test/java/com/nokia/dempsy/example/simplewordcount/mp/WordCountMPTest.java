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

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nokia.dempsy.example.simplewordcount.message.Letter;
import com.nokia.dempsy.example.simplewordcount.message.Word;
import com.nokia.dempsy.example.simplewordcount.message.WordCount;

public class WordCountMPTest
{
   
   private WordCountMP mp = null;
   
   @Before
   public void init()
   {
      mp = new WordCountMP();
   }
   
   @Test
   public void cloneTest() throws CloneNotSupportedException
   {
      WordCountMP clone1 = mp.clone();
      WordCountMP clone2 = mp.clone();

      Assert.assertNotNull(clone1);
      Assert.assertNotNull(clone2);
      Assert.assertNull(clone1.word);
      Assert.assertNull(clone2.word);
      
      clone1.word = new Word("Test");
      Assert.assertNotNull(clone1.word);
      Assert.assertNull(clone2.word);
      Assert.assertNotSame(clone2.word, clone1.word);
   }
   
   @Test
   public void handleWordTest1()
   {
      Word word = new Word("Test1");
      List<?> list = mp.handleWord(word);
      
      Assert.assertNotNull(list);
      Assert.assertEquals(2, list.size());
      for(Object data : list)
      {
         if(data instanceof WordCount)
         {
            WordCount wordCount = (WordCount)data;
            Assert.assertEquals(word, wordCount.getWord());
            Assert.assertEquals(1, wordCount.getCount());
         }
         else
         {
            Assert.assertEquals(new Character('T'), ((Letter)data).getLetterKey());
         }
      }
   }

   @Test
   public void handleWordTest2()
   {
      handleWordTest1();
      Word word = new Word("Test2");
      List<?> count = mp.handleWord(word);
      Assert.assertNull(count);
   }

   @Test
   public void handleWordTest3()
   {
      handleWordTest2();
      Word word = new Word("Test1");
      List<?> list = mp.handleWord(word);
      Assert.assertNotNull(list);
      for(Object data : list)
      {
         if(data instanceof WordCount)
         {
            WordCount wordCount = (WordCount)data;
            Assert.assertEquals(word, wordCount.getWord());
            Assert.assertEquals(2, wordCount.getCount());
         }
         else
         {
            Assert.assertEquals(new Character('T'), ((Letter)data).getLetterKey());
         }
      }
   }

}
