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

package com.nokia.dempsy.example.wordcount.messages;

import java.io.Serializable;

import com.nokia.dempsy.annotations.MessageKey;
import com.nokia.dempsy.example.simplewordcount.message.Letter;

public class LetterCount implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private Letter letter;
   
   private int count;
   
   public LetterCount(Letter letter, int count)
   {
      this.letter = letter;
      this.count = count;
   }
   
   @MessageKey
   public String getKey()
   {
      return "SINGLE_KEY";
   }

   public Letter getLetter()
   {
      return letter;
   }

   public void setLetter(Letter letter)
   {
      this.letter = letter;
   }

   public int getCount()
   {
      return count;
   }

   public void setCount(int count)
   {
      this.count = count;
   }
   
   @Override
   public String toString()
   {
      return letter.toString()+":"+this.count;
   }
   
}
