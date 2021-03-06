package railo.runtime.orm.naming;

import railo.runtime.Component;
import railo.runtime.engine.ThreadLocalPageContext;
import railo.runtime.exp.PageException;
import railo.runtime.exp.PageRuntimeException;
import railo.runtime.op.Caster;
import railo.runtime.type.UDF;

public class CFCNamingStrategy implements NamingStrategy {
	
	Component cfc;
	
	
	public CFCNamingStrategy(String cfcName) throws PageException{
		this.cfc=ThreadLocalPageContext.get().loadComponent(cfcName);
	}
	
	
	/**
	 * @see railo.runtime.orm.naming.NamingStrategy#convertTableName(java.lang.String)
	 */
	public String convertTableName(String tableName) {
		return call("getTableName",tableName);
	}

	/**
	 * @see railo.runtime.orm.naming.NamingStrategy#convertColumnName(java.lang.String)
	 */
	public String convertColumnName(String columnName) {
		return call("getColumnName",columnName);
	}

	private String call(String functionName, String name) {
		Object res = cfc.get(functionName,null);
		if(!(res instanceof UDF)) return name;
		
		try {
			return Caster.toString(cfc.call(ThreadLocalPageContext.get(), functionName, new Object[]{name}));
		} catch (PageException pe) {
			throw new PageRuntimeException(pe);
		}
	}

	/**
	 * @see railo.runtime.orm.naming.NamingStrategy#getType()
	 */
	public String getType() {
		return "cfc";
	}

}
