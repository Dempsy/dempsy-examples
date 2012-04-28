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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Factory to create words from provided file in classpath.
 * Each line of file is passed in as single word
 *
 */
public class WordListFileReaderWordFactory
{
   private Logger logger = Logger.getLogger(WordListFileReaderWordFactory.class);
   private String fileName = "/words.txt";
   private List<String> wordList;
   
   /**
    * <p>Populates list with words read from the specified file.
    * Each line of the file is one entry in the list.</p>
    */
   public void readData()
   {
      InputStream is = this.getClass().getResourceAsStream(fileName);
      if(is == null)
      {
         logger.error("Unable to locate file "+fileName);
         return;
      }
      BufferedReader reader = null;
      try
      {
         reader = new BufferedReader(new InputStreamReader(is));
         String line = null;
         wordList = new ArrayList<String>();
         while (( line = reader.readLine()) != null){
            if(line.trim().length()>0) { wordList.add(line); }
         }
      }
      catch(IOException e)
      {
         logger.error("error reading file "+fileName, e);
      }
      finally
      {
         try
         {
            if(reader != null) {reader.close(); }
         }
         catch(Exception e2){ /* do nothing */}
      }
   }
   
   public List<String> getWordList()
   {
      return this.wordList;
   }
   
   public void setFileName(String fileName)
   {
      this.fileName = fileName;
   }

}
