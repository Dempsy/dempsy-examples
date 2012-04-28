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

import java.io.Serializable;

import com.nokia.dempsy.annotations.MessageKey;

/**
 * <p>See the Dempsy User Guide for a walkthrough of this class</p>
 */
public class Word implements Serializable
{
   private static final long serialVersionUID = 1L;
   private String wordText;
   
   public Word(String data)
   {
      this.wordText = data;
   }
   
   @MessageKey
   public String getWordText()
   {
      return this.wordText;
   }

}
