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

public class StartLetterCountMPTest
{
   private StartLetterCountMP mp ;
   
   @Before
   public void init()
   {
      mp = new StartLetterCountMP();
   }

   @Test
   public void cloneTest() throws CloneNotSupportedException
   {
      StartLetterCountMP clone1 = mp.clone();
      StartLetterCountMP clone2 = mp.clone();

      Assert.assertNotNull(clone1);
      Assert.assertNotNull(clone2);
      Assert.assertNull(clone1.character);
      Assert.assertNull(clone2.character);
      
      clone1.character = new Character('T');
      Assert.assertNotNull(clone1.character);
      Assert.assertNull(clone2.character);
      Assert.assertNotSame(clone2.character, clone1.character);
   }
   
   @Test
   public void handleLetterTest1()
   {
      Letter letter = new Letter('T');
      LetterCount letterCount = mp.handleFirstCharacter(letter);
      
      Assert.assertNotNull(letterCount);
      Assert.assertEquals(1, letterCount.getCount());
      Assert.assertNotNull(letterCount.getLetter());
      Assert.assertEquals(letter, letterCount.getLetter());
   }

   @Test
   public void handleLetterTest2()
   {
      handleLetterTest1();
      Letter letter = new Letter('A');
      LetterCount letterCount = mp.handleFirstCharacter(letter);
      Assert.assertNull(letterCount);
   }

   @Test
   public void handleLetterTest3()
   {
      handleLetterTest2();
      Letter letter = new Letter('T');
      LetterCount letterCount = mp.handleFirstCharacter(letter);
      Assert.assertNotNull(letterCount);
      Assert.assertEquals(2, letterCount.getCount());
      Assert.assertNotNull(letterCount.getLetter());
      Assert.assertEquals(letter, letterCount.getLetter());
   }
}
