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

package com.nokia.dempsy.example.wordcount.adaptor;

import java.util.List;

import org.apache.log4j.Logger;

import com.nokia.dempsy.Adaptor;
import com.nokia.dempsy.Dispatcher;
import com.nokia.dempsy.example.simplewordcount.message.Word;
import com.nokia.dempsy.example.wordcount.factory.WordListFileReaderWordFactory;

public class WordCountAdaptor implements Adaptor
{
   
   private Logger logger = Logger.getLogger(WordCountAdaptor.class);
   private Dispatcher dispatcher;
   private WordListFileReaderWordFactory wordFactory;

   @Override
   public void setDispatcher(Dispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
      if(this.wordFactory != null)
      {
         this.wordFactory.readData();
      }
   }

   @Override
   public void start()
   {
      if(this.dispatcher != null && this.wordFactory != null)
      {
         final List<String> wordList = wordFactory.getWordList();
         Runnable r = new Runnable()
         {
            
            @Override
            public void run()
            {
               int i =0;
               for(String word:wordList)
               {
                  dispatcher.dispatch(new Word(word));
                  try
                  {
                     if(++i %100 == 0) Thread.sleep(100);
                  }
                  catch(InterruptedException e){/* Do Nothing */}
               }
            }
         };
         
         Thread thread1 = new Thread(r);
         Thread thread2 = new Thread(r);
         
         thread1.start();
         thread2.start();
         try
         {
            thread1.join();
            thread2.join();
         }
         catch(InterruptedException e)
         {
            logger.error("Interrupted . . . ", e);
         }
      }
      else
      {
         logger.error("Dispatcher or the word factory is null");
      }
   }
   
   @Override
   public void stop() {}

   public WordListFileReaderWordFactory getWordFactory()
   {
      return wordFactory;
   }

   public void setWordFactory(WordListFileReaderWordFactory wordFactory)
   {
      this.wordFactory = wordFactory;
   }

}
