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


package com.nokia.dempsy.example.userguide.wordcount;

import com.nokia.dempsy.annotations.Activation;
import com.nokia.dempsy.annotations.MessageHandler;
import com.nokia.dempsy.annotations.MessageProcessor;
import com.nokia.dempsy.annotations.Output;

/**
 * <p>See the Dempsy User Guide for a walkthrough of this class</p>
 */
@MessageProcessor
public class WordCount implements Cloneable
{
  private long count = 0;
  private String myword = null;

  @MessageHandler
  public void countWord(Word word)
  {
     count++;
  }
  
  @Activation
  public void activate(String key)
  {
     myword = key;
  }
  
  @Output
  public CountedWord outputResults()
  {
     return new CountedWord(myword,count);
  }

  public Object clone() throws CloneNotSupportedException
  {
     return (WordCount)super.clone();
  }
}
