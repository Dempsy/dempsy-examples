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

package com.nokia.dempsy.example.wordcount.factory;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class WordListFileReaderWordFactoryTest
{
   private WordListFileReaderWordFactory factory;
   
   @Before
   public void init()
   {
      factory = new WordListFileReaderWordFactory();
   }
   
   @Test
   public void readDataTest1()
   {
      factory.readData();
      List<String> wordList = factory.getWordList();
      Assert.assertNotNull(wordList);
      Assert.assertFalse(wordList.size()==0);
   }

   @Test
   public void readDataTest2()
   {
      factory.setFileName("DOESNOTEXISTS");
      factory.readData();
      List<String> wordList = factory.getWordList();
      Assert.assertNull(wordList);
   }

   @Test
   public void readDataTest3()
   {
      factory.setFileName("/test.txt");
      factory.readData();
      List<String> wordList = factory.getWordList();
      Assert.assertNotNull(wordList);
      Assert.assertEquals(4, wordList.size());
   }

   @Test
   public void readDataTest4()
   {
      factory.setFileName("/test-emptyvalues.txt");
      factory.readData();
      List<String> wordList = factory.getWordList();
      Assert.assertNotNull(wordList);
      Assert.assertEquals(4, wordList.size());
   }

}
