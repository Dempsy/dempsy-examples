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

package com.nokia.dempsy.example.simplewordcount.adaptor;

import org.apache.log4j.Logger;

import com.nokia.dempsy.Adaptor;
import com.nokia.dempsy.Dispatcher;
import com.nokia.dempsy.example.simplewordcount.message.Word;

/**
 * <p>Adaptor class to send messages {@link Word} inside
 * the cluster.</p>
 * 
 * <p>The container invokes methods <b>setDispatcher(Dispatcher)</b> first and then <b>start()</b>
 * Use of post-construct should be avoided</p> 
 *
 */
public class WordMessageAdaptor implements Adaptor
{
   Logger logger = Logger.getLogger(WordMessageAdaptor.class);
   Dispatcher dispatcher ;

   @Override
   public void setDispatcher(Dispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
   }

   @Override
   public void start()
   {
      if(dispatcher != null)
      {
         Word one = new  Word("one");
         dispatcher.dispatch(one);
         for(int i =0 ; i<10; i++)
         {
            dispatcher.dispatch(one);
         }
         Word two = new Word("two");
         for(int i =0 ; i<15; i++)
         {
            dispatcher.dispatch(two);
         }
      }
      else
      {
         logger.error("Dispatcher is not set. The frameword should be doing that");
      }

   }
   
   @Override
   public void stop() {}

}
