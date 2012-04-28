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

package com.nokia.dempsy.example.simplewordcount.message;

import java.io.Serializable;

import com.nokia.dempsy.annotations.MessageKey;

public class WordCount implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private Word word;
   private int count;
   
   private String key = "SINGLE_INSTANCE";
   
   public WordCount(Word word, int count)
   {
      this.word = word;
      this.count = count;
   }
   
   @MessageKey
   public String key()
   {
      return key;
   }

   public Word getWord()
   {
      return word;
   }

   public void setWord(Word word)
   {
      this.word = word;
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
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if(this == obj)
         return true;
      if(obj == null)
         return false;
      if(getClass() != obj.getClass())
         return false;
      WordCount other = (WordCount)obj;
      if(key == null)
      {
         if(other.key != null)
            return false;
      }
      else if(!key.equals(other.key))
         return false;
      return true;
   }
   
   @Override
   public String toString()
   {
      return word+":"+count;
   }

}
