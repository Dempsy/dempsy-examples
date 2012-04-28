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

package com.nokia.dempsy.example.userguide.wordcount;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.nokia.dempsy.Adaptor;
import com.nokia.dempsy.Dispatcher;

/**
 * This class is from the user guide though it has an actual source of data. The words
 * it sources are stored in the jar inside a file called AV1611Bible.txt which contains the
 * test of the King James Bible.
 */
public class WordAdaptor implements Adaptor
{
   private static Logger logger = Logger.getLogger(WordAdaptor.class);
   private Dispatcher dempsy;
   private AtomicBoolean running = new AtomicBoolean(false);
   
   /**
    * This method is called by the framework to provide a handle to the
    * Dempsy message bus.
    */
   @Override
   public void setDispatcher(Dispatcher dispatcher)
   {
      this.dempsy = dispatcher; 
   }

   @Override
   public void start()
   {
      try { setupStream(); } 
      catch (IOException ioe)
      {
         logger.fatal("ERROR");
         
         // throwing an exception from an adaptor start will halt the Vm
         throw new RuntimeException(ioe);
      }
      
      running.set(true);
      while (running.get())
      {
         // obtain data from an external source
         String wordString = getNextWordFromSoucre();
         if (wordString == null)
            running.set(false);
         else
            dempsy.dispatch(new Word(wordString));
      }
   }
   
   @Override
   public void stop()
   {
      running.set(false);
   }
   
   private String[] strings;
   int curCount = 0;
   
   private void setupStream() throws IOException
   {
      InputStream is = WordAdaptor.class.getClassLoader().getResourceAsStream("AV1611Bible.txt");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is,writer);
      strings = writer.toString().split("\\s+");
   }
   
   private String getNextWordFromSoucre()
   {
      String ret = strings[curCount];
      curCount++;
      if (curCount >= strings.length)
         return null;
      return ret;
   }

}
