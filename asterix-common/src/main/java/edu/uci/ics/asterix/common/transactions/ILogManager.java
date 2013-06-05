/*
 * Copyright 2009-2010 by The Regents of the University of California
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License from
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.uci.ics.asterix.common.transactions;

import java.io.IOException;

import edu.uci.ics.asterix.common.exceptions.ACIDException;

public interface ILogManager {

    /**
     * @param logType
     * @param context
     * @param datasetId
     * @param PKHashValue
     * @param resourceId
     * @param resourceMgrId
     * @param logContentSize
     * @param reusableLogContentObject
     * @param logger
     * @param logicalLogLocator
     * @throws ACIDException
     */
    void log(byte logType, ITransactionContext context, int datasetId, int PKHashValue, long resourceId,
            byte resourceMgrId, int logContentSize, ReusableLogContentObject reusableLogContentObject, ILogger logger,
            LogicalLogLocator logicalLogLocator) throws ACIDException;

    /**
     * @param physicalLogLocator
     *            specifies the physical location from where the logs need to be
     *            read
     * @param logFilter
     *            specifies the filtering criteria for the retrieved logs
     * @return LogCursor an iterator for the retrieved logs
     * @throws ACIDException
     */
    public ILogCursor readLog(PhysicalLogLocator physicalLogLocator, ILogFilter logFilter) throws IOException,
            ACIDException;

    /**
     * @param lsnValue
     *            TODO
     * @param logicalLogLocator
     *            TODO
     * @throws ACIDException
     */
    public void readLog(long lsnValue, LogicalLogLocator logicalLogLocator) throws ACIDException;

    /**
     * Retrieves the configuration parameters of the ILogManager
     * 
     * @return LogManagerProperties: the configuration parameters for the
     *         ILogManager
     */
    public LogManagerProperties getLogManagerProperties();

    /**
     * Returns the ILogRecordHelper instance associated with this ILogManager
     * instance
     * 
     * @return ILogRecordHelper: the utility (class) for writing/reading log
     *         header.
     */
    public ILogRecordHelper getLogRecordHelper();

    /**
     * Returns the Transaction Provider associated with this ILogManager
     * instance
     * 
     * @return TransactionSubsystem
     */
    public ITransactionSubsystem getTransactionSubsystem();

}
