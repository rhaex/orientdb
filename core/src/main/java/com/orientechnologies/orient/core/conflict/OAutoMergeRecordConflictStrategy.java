/*
 *
 *  *  Copyright 2014 Orient Technologies LTD (info(at)orientechnologies.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://www.orientechnologies.com
 *  
 */

package com.orientechnologies.orient.core.conflict;

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.version.ORecordVersion;

/**
 * Auto merges new record with the existent. Collections are also merged, item by item.
 * 
 * @author Luca Garulli
 */
public class OAutoMergeRecordConflictStrategy implements ORecordConflictStrategy {
  public static final String NAME = "automerge";

  @Override
  public byte[] onUpdate(final ORecordId rid, final ORecordVersion iRecordVersion, final byte[] iRecordContent,
      final ORecordVersion iDatabaseVersion) {
    final ODocument storedRecord = rid.getRecord();
    final ODocument newRecord = new ODocument().fromStream(iRecordContent);

    storedRecord.merge(newRecord, false, true);

    iDatabaseVersion.setCounter(Math.max(iDatabaseVersion.getCounter(), iRecordVersion.getCounter()));

    return storedRecord.toStream();
  }
}