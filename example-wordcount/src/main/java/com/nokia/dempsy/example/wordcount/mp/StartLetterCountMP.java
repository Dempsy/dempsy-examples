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

import org.apache.log4j.Logger;

import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.example.simplewordcount.message.Letter;
import com.nokia.dempsy.example.wordcount.messages.LetterCount;

@MessageProcessor
public class StartLetterCountMP implements Cloneable
{
   private Logger logger = Logger.getLogger(StartLetterCountMP.class);
   protected Character character;
   protected int count;
   private LetterCount letterCount;
   
   @MessageHandler
   public LetterCount handleFirstCharacter(Letter letter)
   {
      if(character != null)
      {
         if(!character.equals(letter.getLetterKey()))
         {
            logger.error("Expected letter "+character+" actually received "+letter.getLetterKey());
            return null;
         }
      }
      else
      {
         this.character = letter.getLetterKey();
      }
      if(letterCount == null)
      {
         this.letterCount = new LetterCount(letter, count);
      }
      count++;
      this.letterCount.setCount(count);
      logger.info("received letter "+character+" count "+count);
      return letterCount;
   }
   
   @Override
   public StartLetterCountMP clone() throws CloneNotSupportedException
   {
      return (StartLetterCountMP)super.clone();
   }
}
