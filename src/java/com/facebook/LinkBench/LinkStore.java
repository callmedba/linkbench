package com.facebook.LinkBench;

import java.io.*;
import java.util.List;
import java.util.Properties;

public abstract class LinkStore {
  // void createLinkTable();
  public static final long LINK_TYPE = 123456789;
  public static final long MAX_ID2 = Long.MAX_VALUE;
  public static final int ID1_TYPE = 2048;
  public static final int ID2_TYPE = 2048;

  // visibility
  public static final byte VISIBILITY_HIDDEN = 0;
  public static final byte VISIBILITY_DEFAULT = 1;
  
  public static final int MAX_OPTYPES = LinkBenchOp.values().length;
  public static final int DEFAULT_LIMIT = 10000;

  /** Controls the current setting for range limit */
  protected int rangeLimit;

  /** The default constructor */
  public LinkStore() {
    this.rangeLimit = DEFAULT_LIMIT;
  } 
  
  public int getRangeLimit() {
    return rangeLimit;
  }

  public void setRangeLimit(int rangeLimit) {
    this.rangeLimit = rangeLimit;
  }

  /** initialize the store object */
  public abstract void initialize(Properties p,
      Phase currentPhase, int threadId) throws IOException, Exception;

  /**
   * Do any cleanup.  After this is called, store won't be reused
   */
  public abstract void close();

  // this is invoked when an error happens in case connection needs to be
  // cleaned up, reset, reopened, whatever 
  public abstract void clearErrors(int threadID);

  public abstract void addLink(String dbid, Link a, boolean noinverse) throws Exception;
  public abstract void deleteLink(String dbid, long id1, long link_type, long id2, 
                   boolean noinverse, boolean expunge) throws Exception;
  public abstract void updateLink(String dbid, Link a, boolean noinverse) 
    throws Exception;

  /**
   *  lookup using id1, type, id2
   *  Returns hidden links.
   * @param dbid
   * @param id1
   * @param link_type
   * @param id2
   * @return
   * @throws Exception
   */
  public abstract Link getLink(String dbid, long id1, long link_type, long id2) 
    throws Exception;

  /**
   * lookup using just id1, type
   * Does not return hidden links
   * @param dbid
   * @param id1
   * @param link_type
   * @return list of links in descending order of time, or null
   *                                       if no matching links
   * @throws Exception
   */
  public abstract Link[] getLinkList(String dbid, long id1, long link_type) 
    throws Exception;


  /**
   * lookup using just id1, type
   * Does not return hidden links
   * @param dbid
   * @param id1
   * @param link_type
   * @param minTimestamp
   * @param maxTimestamp
   * @param offset
   * @param limit
   * @return list of links in descending order of time, or null
   *                                       if no matching links
   * @throws Exception
   */
  public abstract Link[] getLinkList(String dbid, long id1, long link_type, 
                            long minTimestamp, long maxTimestamp, 
                            int offset, int limit)
    throws Exception;

  // count the #links
  public abstract long countLinks(String dbid, long id1, long link_type) throws Exception;
  
  /** 
   * @return 0 if it doesn't support addBulkLinks and recalculateCounts methods
   *         If it does support them, return the maximum number of links that 
   *         can be added at a time */
  public int bulkLoadBatchSize() {
    return 0;
  }
  
  /** Add a batch of links without updating counts */
  public void addBulkLinks(String dbid, List<Link> a, boolean noinverse)
             throws Exception {
    throw new UnsupportedOperationException("addBulkLinks not supported for " +
    		"LinkStore subclass " + this.getClass().getName());
  }
  
  /** Add a batch of counts */
  public void addBulkCounts(String dbid, List<LinkCount> a)
      throws Exception {
    throw new UnsupportedOperationException("addBulkCounts not supported for " +
      "LinkStore subclass " + this.getClass().getName());
  }
}
