package railo.runtime.db;


import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

import net.sourceforge.jtds.jdbc.ConnectionJDBC2;
import net.sourceforge.jtds.jdbc.ConnectionJDBC3;
import railo.runtime.PageContext;
import railo.runtime.PageContextImpl;
import railo.runtime.config.ConfigImpl;
import railo.runtime.engine.ThreadLocalPageContext;
import railo.runtime.exp.DatabaseException;
import railo.runtime.exp.ExceptionHandler;
import railo.runtime.exp.PageException;
import railo.runtime.functions.system.SystemCacheClear;
import railo.runtime.orm.ORMDatasourceConnection;
import railo.runtime.orm.ORMSession;

/**
 * this class handle multible db connection, transaction and logging
 */
public final class DatasourceManagerImpl implements DataSourceManager {
	
	private ConfigImpl config;
	
	boolean autoCommit=true;
	private int isolation=Connection.TRANSACTION_NONE;
	private DatasourceConnection transConn;
    

	/**
	 * constructor of the class
	 * @param pc
	 */
	public DatasourceManagerImpl(ConfigImpl c) {
		this.config=c;
	}

	/**
	 * FUTURE deprecated
	 * @see DataSourceManager#getConnection(PageContext pc,java.lang.String, java.lang.String, java.lang.String)
	 */
	public DatasourceConnection getConnection(PageContext pc,String _datasource, String user, String pass) throws PageException {
		return getConnection(pc,pc.getConfig().getDataSource(_datasource), user, pass);
	}

	// FUTURE add to interface
	public DatasourceConnection getConnection(PageContext pc,DataSource ds, String user, String pass) throws PageException {
		if(autoCommit)
			return config.getDatasourceConnectionPool().getDatasourceConnection(pc,ds,user,pass);

		
		pc=ThreadLocalPageContext.get(pc);
		DatasourceConnection dc=((PageContextImpl)pc)._getConnection(ds,user,pass);
		
		// transaction
		//if(!autoCommit) {
            try {
                if(transConn==null) {
                    ConnectionJDBC2 con = (ConnectionJDBC2)dc.getConnection();

                    try {
                        Field socket = ConnectionJDBC2.class.getDeclaredField("socket");
                        socket.setAccessible(true);
                        Object sock = socket.get(con);
                        socket = sock.getClass().getDeclaredField("socket");
                        socket.setAccessible(true);
                        Socket s = (Socket)socket.get(sock);
                        int depth = Thread.currentThread().getStackTrace().length;
                        System.out.println("con=" + System.identityHashCode(dc.getConnection()) +
                                " DatasourceManagerImpl=" + System.identityHashCode(this) +
                                " getConnection(false)");
                        new Exception(""+s.getLocalPort()).printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                	dc.getConnection().setAutoCommit(false);
					
                    if(isolation!=Connection.TRANSACTION_NONE)
					    dc.getConnection().setTransactionIsolation(isolation);
                    System.out.println("" + System.identityHashCode(this) + " setting con=" + System.identityHashCode(dc.getConnection()));
                    transConn=dc;
    			}
    			else if(!transConn.equals(dc)) {
                	if("_queryofquerydb".equalsIgnoreCase(ds.getName())) return dc;
    				throw new DatabaseException(
    						"can't use different connections inside a transaction",null,null,dc);
    			}
                else if(dc.getConnection().getAutoCommit()) {
                    ConnectionJDBC2 con = (ConnectionJDBC2)dc.getConnection();
                    try {
                        Field socket = ConnectionJDBC2.class.getDeclaredField("socket");
                        socket.setAccessible(true);
                        Object sock = socket.get(con);
                        socket = sock.getClass().getDeclaredField("socket");
                        socket.setAccessible(true);
                        Socket s = (Socket)socket.get(sock);
                        int depth = Thread.currentThread().getStackTrace().length;
                        System.out.println("con=" + System.identityHashCode(dc.getConnection()) +
                                " DatasourceManagerImpl=" + System.identityHashCode(this) +
                                " getConnection(false)");
                        new Exception(""+s.getLocalPort()).printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dc.getConnection().setAutoCommit(false);
                }
            } catch (SQLException e) {
               ExceptionHandler.printStackTrace(e);
            }
		//}
		return dc;
	}
	

	public void add(PageContext pc,ORMSession session) throws PageException {
		
		// transaction
		if(!autoCommit) {
            try {
                if(transConn==null) {
                	ORMDatasourceConnection dc=new ORMDatasourceConnection(pc,session);
                	
                    if(isolation!=Connection.TRANSACTION_NONE)
					    dc.getConnection().setTransactionIsolation(isolation);
                    System.out.println("" + System.identityHashCode(this) + " setting con=" + System.identityHashCode(dc.getConnection()));
                    transConn=dc;
    			}
    			else if(!(transConn instanceof ORMDatasourceConnection)){
    				/*if(transConn.getDatasource().equals(session.getEngine().getDataSource())){
    					ORMDatasourceConnection dc=new ORMDatasourceConnection(pc,session);
                    	
                        if(isolation!=Connection.TRANSACTION_NONE)
    					    dc.getConnection().setTransactionIsolation(isolation);
                        transConn=dc;
    				}
    				else*/
    					throw new DatabaseException(
    						"can't use transaction for datasource and orm at the same time",null,null,null);
    			}
            } catch (SQLException e) {
               ExceptionHandler.printStackTrace(e);
            }
		}
	}
	
	/**
	 * @see railo.runtime.db.DataSourceManager#releaseConnection(railo.runtime.db.DatasourceConnection)
	 */
	public void releaseConnection(PageContext pc,DatasourceConnection dc) {
		if(autoCommit) config.getDatasourceConnectionPool().releaseDatasourceConnection(dc);
	}
	
	/*private void releaseConnection(int pid,DatasourceConnection dc) {
		config.getDatasourceConnectionPool().releaseDatasourceConnection(pid,dc);
	}*/
	
	/**
	 *
	 * @see DataSourceManager#begin()
	 */
	public void begin() {
        System.out.println("" + System.identityHashCode(this) + " Starting transaction...");
		this.autoCommit=false;
		this.isolation=Connection.TRANSACTION_NONE;		
	}
	
	/**
	 *
	 * @see DataSourceManager#begin(java.lang.String)
	 */
	public void begin(String isolation) {
        System.out.println("" + System.identityHashCode(this) + " Starting transaction...");
		this.autoCommit=false;
    	
		if(isolation.equalsIgnoreCase("read_uncommitted"))
		    this.isolation=Connection.TRANSACTION_READ_UNCOMMITTED;
		else if(isolation.equalsIgnoreCase("read_committed"))
		    this.isolation=Connection.TRANSACTION_READ_COMMITTED;
		else if(isolation.equalsIgnoreCase("repeatable_read"))
		    this.isolation=Connection.TRANSACTION_REPEATABLE_READ;
		else if(isolation.equalsIgnoreCase("serializable"))
		    this.isolation=Connection.TRANSACTION_SERIALIZABLE;
		else 
		    this.isolation=Connection.TRANSACTION_NONE;
        
	}
    /**
	 *
	 * @see DataSourceManager#begin(int)
	 */
    public void begin(int isolation) {
    	//print.out("begin:"+autoCommit);
        System.out.println("" + System.identityHashCode(this) + " Starting transaction...");
    	this.autoCommit=false;
        this.isolation=isolation;
    }

	/**
	 *
	 * @see DataSourceManager#rollback()
	 */
	public void rollback() throws DatabaseException {
		if(autoCommit)return;
        //autoCommit=true;
		if(transConn!=null) {
			try {
                System.out.println("" + System.identityHashCode(transConn.getConnection()) + " DatasourceManagerImpl.rollback");
				transConn.getConnection().rollback();
				//transConn.setAutoCommit(true);
			} 
			catch (SQLException e) {
				throw new DatabaseException(e,transConn);
			}
			//transConn=null;
		}
	}
	
	// FUTURE add to interface
	public void savepoint() throws DatabaseException {
		if(autoCommit)return;
        //autoCommit=true;
		if(transConn!=null) {
			try {
				transConn.getConnection().setSavepoint();
			} 
			catch (SQLException e) {
				throw new DatabaseException(e,transConn);
			}
		}
	}

	/**
	 *
	 * @see DataSourceManager#commit()
	 */
	public void commit() throws DatabaseException {
        //print.out("commit:"+autoCommit);
        if(autoCommit)return ;
        //autoCommit=true;
		if(transConn!=null) {
			try {
                System.out.println("" + System.identityHashCode(transConn.getConnection()) + " DatasourceManagerImpl.commit");
				transConn.getConnection().commit();
				//transConn.setAutoCommit(true);
			} 
			catch (SQLException e) {
				throw new DatabaseException(e,transConn);
			}
			//transConn=null;
		}
	}
	
    /**
	 *
	 * @see DataSourceManager#isAutoCommit()
	 */
    public boolean isAutoCommit() {
        return autoCommit;
    }

    /**
	 *
	 * @see DataSourceManager#end()
	 */
    public void end() {
        System.out.println("" + System.identityHashCode(this) + " Ending transaction...");
        autoCommit=true;
        if(transConn!=null) {
        	try {
                int code = System.identityHashCode(transConn.getConnection());
                System.out.println("" + code + " DatasourceManagerImpl.end(true)");
                new Exception("" + code).printStackTrace();
            	transConn.getConnection().setAutoCommit(true);
            } 
            catch (SQLException e) {
                ExceptionHandler.printStackTrace(e);
            }
            transConn=null;
        }
    }

	public void remove(String datasource) {
		config.getDatasourceConnectionPool().remove(datasource);
	}

	public void release() {
		this.transConn=null;
		this.isolation=Connection.TRANSACTION_NONE;
	}

}