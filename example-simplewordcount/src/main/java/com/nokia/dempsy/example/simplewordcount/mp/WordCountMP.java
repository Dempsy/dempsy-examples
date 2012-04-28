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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.example.simplewordcount.message.Letter;
import com.nokia.dempsy.example.simplewordcount.message.Word;
import com.nokia.dempsy.example.simplewordcount.message.WordCount;

/**
 * <p>Message Processor for Counting re-occurrence of words.
 * For every word received count is incremented and 
 * new message {@link WordCount} is forwarded to next 
 * stage.</p>
 * 
 * <p>The class has to be annotated with
 * {@link MessageProcessor} annotation for the container to process it.</p>
 * 
 * <p>The message processor class needs to implement interface {@link Cloneable}.
 * One instance of this class will be provided to the container as a prototype.
 * The container calls on the overridden clone method to create additional 
 * instances needs to process each unique message.</p>
 * 
 */
@MessageProcessor
public class WordCountMP implements Cloneable
{
   private Logger logger = Logger.getLogger(WordCountMP.class);
   protected Word word;
   protected int count;
   protected List<Object> forward;
   protected WordCount wordCount;
   
   /**
    * <p>Message Handler to process incoming messages {@link Word}</p>
    * 
    * <p>The method has to be marked as Message Handler using annotation
    * {@link MessageHandler} annotation.
    * 
    * @param word
    * @return
    */
   @MessageHandler
   public List<?> handleWord(Word word)
   {
      if(this.word != null)
      {
         if(!this.word.equals(word))
         {
            logger.error("Expected "+this.word+" actually received "+word);
            return null;
         }
      }
      else
      {
         this.word = word;
      }
      this.count++;
      if(wordCount == null)
      {
         wordCount = new WordCount(this.word, count);
      }
      else
      {
         wordCount.setCount(this.count);
      }
      if(forward == null)
      {
         forward = new ArrayList<Object>();
         forward.add(wordCount);
         forward.add(new Letter(this.word.getData().charAt(0)));
      }

      logger.info("received word \""+word+"\" count: "+count);
      return forward;
   }

   /**
    * Clone method provided for the container to create additional instances.
    * This method needs to be <b>public</b> and should return the 
    * <b>Message Processor instance</b>.
    *  
    */
   @Override
   public WordCountMP clone() throws CloneNotSupportedException
   {
      WordCountMP mp = (WordCountMP)super.clone();
      return mp;
   }
}
