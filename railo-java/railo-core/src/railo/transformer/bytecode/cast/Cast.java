package railo.transformer.bytecode.cast;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import railo.commons.lang.ClassException;
import railo.commons.lang.ClassUtil;
import railo.commons.lang.StringUtil;
import railo.transformer.bytecode.BytecodeContext;
import railo.transformer.bytecode.BytecodeException;
import railo.transformer.bytecode.expression.Expression;
import railo.transformer.bytecode.expression.ExpressionBase;
import railo.transformer.bytecode.expression.var.VariableString;
import railo.transformer.bytecode.util.Methods_Caster;
import railo.transformer.bytecode.util.Types;

/**
 * cast a Expression to a Double
 */
public final class Cast extends ExpressionBase {
    // TODO support short type
    private Expression expr;
    private String type;
    private String lcType;
    
    private Cast(Expression expr, String type, String lcType) {
        super(expr.getLine());
        this.expr=expr;
        this.type=type;
        this.lcType=lcType;
        
    }

    public static Expression toExpression(Expression expr, String type) {
    	if(type==null) return expr;
    	
    	String lcType=StringUtil.toLowerCase(type);
    	switch(lcType.charAt(0)) {
    	case 'a':
            if("any".equals(lcType)) 							{
            	return expr;
            }
    	break;
    	case 'b':
            if("boolean".equals(type) || "bool".equals(lcType)) return CastBoolean.toExprBoolean(expr);
        break;
    	case 'd':
    		if("double".equals(type))							return CastDouble.toExprDouble(expr);
    	break;
    	case 'n':
        	if("number".equals(lcType) || "numeric".equals(lcType))return CastDouble.toExprDouble(expr);
        break;
    	case 'o':
        	if("object".equals(lcType))							{
        		return expr;
        	}
        break;
        case 's':
        	if("string".equals(lcType))							return CastString.toExprString(expr);
        	//if("string_array".equals(lcType))					return StringArray.toExpr(expr);     
        break;
        case 'u':
        	if("uuid".equals(lcType)) 							return  CastString.toExprString(expr);
        break;
        case 'v':
        	if("variablename".equals(lcType))					return VariableString.toExprString(expr);
        	if("variable_name".equals(lcType))					return VariableString.toExprString(expr);
        	if("variablestring".equals(lcType))					return VariableString.toExprString(expr);
        	if("variable_string".equals(lcType))				return VariableString.toExprString(expr);
        	if("void".equals(lcType))							return expr;
        break;
        
        
        
        
    	}
        return new Cast(expr,type,lcType);
    }

    // Array toArray(Object)
    final public static Method TO_ARRAY = new Method("toArray",
    		Types.ARRAY,
    		new Type[]{Types.OBJECT});
    
    // String toBase64 (Object);
    final public static Method TO_BASE64 = new Method("toBase64",
			Types.STRING,
			new Type[]{Types.OBJECT}); 
    
    // byte[] toBinary (Object)
    final public static Method TO_BINARY = new Method("toBinary",
			Types.BYTE_VALUE_ARRAY,
			new Type[]{Types.OBJECT}); 
    
    // byte[] toCollection (Object)
    final public static Method TO_COLLECTION = new Method("toCollection",
			Types.BYTE_VALUE_ARRAY,
			new Type[]{Types.OBJECT});
    
    // railo.runtime.Component toComponent (Object)
    final public static Method TO_COMPONENT = new Method("toComponent",
			Types.COMPONENT,
			new Type[]{Types.OBJECT});
	
    
    // String toDecimal (Object)
    final public static Method TO_DECIMAL = new Method("toDecimal",
			Types.STRING,
			new Type[]{Types.OBJECT});
    
    // railo.runtime.config.Config getConfig ()
    final public static Method GET_CONFIG = new Method("getConfig",
			Types.CONFIG_WEB,
			new Type[]{}); 
    
    //java.util.TimeZone getTimeZone ()
    final public static Method GET_TIMEZONE = new Method("getTimeZone",
    			Types.TIMEZONE,
    			new Type[]{});
    
    
    /**
     * @see railo.transformer.bytecode.expression.Expression#_writeOut(org.objectweb.asm.commons.GeneratorAdapter, int)
     */
    public Type _writeOut(BytecodeContext bc, int mode) throws BytecodeException {
//Caster.toDecimal(null);
    	GeneratorAdapter adapter = bc.getAdapter();
        char first=lcType.charAt(0);
        Type rtn;
        
        switch(first) {
        case 'a':
            if("array".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.ARRAY)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_ARRAY);
                return Types.ARRAY;
            }
        break;
        case 'b':
            if("base64".equals(lcType)) {
                expr.writeOut(bc,MODE_REF);
                adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_BASE64);
                return Types.STRING;
            }
            if("binary".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.BYTE_VALUE_ARRAY)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_BINARY);
                return Types.BYTE_VALUE_ARRAY;
            }
            if("byte".equals(type)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
            	if(!rtn.equals(Types.BYTE_VALUE)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_BYTE_VALUE[Types.getType(rtn)]);
            	return Types.BYTE_VALUE;
            }
            if("byte".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.BYTE)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_BYTE[Types.getType(rtn)]);
                return Types.BYTE;
            }
            if("boolean".equals(lcType)) {
            	return CastBoolean.toExprBoolean(expr).writeOut(bc, MODE_REF);
            }
        break;
        case 'c':
            if("char".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
            	if(!rtn.equals(Types.CHAR)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_CHAR_VALUE[Types.getType(rtn)]);
                return Types.CHAR;
            }
            if("character".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.CHARACTER)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_CHARACTER[Types.getType(rtn)]);
                return Types.CHARACTER;
            }
            if("collection".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.COLLECTION)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_COLLECTION);
                return Types.COLLECTION;
            }
            if("component".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.COMPONENT)) 
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_COMPONENT);
                return Types.COMPONENT;
            }
        break;
        case 'd':
            if("double".equals(lcType))	{
            	return CastDouble.toExprDouble(expr).writeOut(bc, MODE_REF);
            }
            if("date".equals(lcType) || "datetime".equals(lcType)) {
                // First Arg
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(rtn.equals(Types.DATE_TIME)) return Types.DATE_TIME;
                
            	int type=Types.getType(rtn);
                
                // Second Arg
                adapter.loadArg(0);
                adapter.invokeVirtual(Types.PAGE_CONTEXT,GET_CONFIG);
                adapter.invokeInterface(Types.CONFIG_WEB,GET_TIMEZONE);
                adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_DATE[type]);
                return Types.DATE_TIME;
            }
            if("decimal".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_DECIMAL[Types.getType(rtn)]);
                return Types.STRING;
            }
        break;
        case 'f':
            if("file".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.FILE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_FILE);
                return Types.FILE;
            }
            if("float".equals(type)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
            	if(!rtn.equals(Types.FLOAT_VALUE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_FLOAT_VALUE[Types.getType(rtn)]);
                return Types.FLOAT_VALUE;
            }
            if("float".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.FLOAT))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_FLOAT[Types.getType(rtn)]);
                return Types.FLOAT;
            }
        break;
        case 'i':
            if("int".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
            	if(!rtn.equals(Types.INT_VALUE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_INT_VALUE[Types.getType(rtn)]);
                return Types.INT_VALUE;
            }
            if("integer".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.INTEGER))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_INTEGER[Types.getType(rtn)]);
                return Types.INTEGER;
            }
        break;
        case 'j':

            if("java.lang.boolean".equals(lcType))	{
            	return CastBoolean.toExprBoolean(expr).writeOut(bc, MODE_REF);
            }
            if("java.lang.double".equals(lcType))	{
            	return CastDouble.toExprDouble(expr).writeOut(bc, MODE_REF);
            }
            if("java.lang.string".equals(lcType))	{
            	return CastString.toExprString(expr).writeOut(bc, MODE_REF);
            }
            if("java.lang.stringbuffer".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.STRING_BUFFER))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_STRING_BUFFER);
                return Types.STRING_BUFFER;
            }
            if("java.lang.byte".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.BYTE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_BYTE[Types.getType(rtn)]);
                return Types.BYTE;
            }
            if("java.lang.character".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.CHARACTER))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_CHARACTER[Types.getType(rtn)]);
                return Types.CHARACTER;
            }
            if("java.lang.short".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.SHORT))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_SHORT[Types.getType(rtn)]);
                return Types.SHORT;
            }
            if("java.lang.integer".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.INTEGER))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_INTEGER[Types.getType(rtn)]);
                return Types.INTEGER;
            }
            if("java.lang.long".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.LONG))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_LONG[Types.getType(rtn)]);
                return Types.LONG;
            }
            if("java.lang.float".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.FLOAT))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_FLOAT[Types.getType(rtn)]);
                return Types.FLOAT;
            }
            if("java.io.file".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.FILE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_FILE);
                return Types.FILE;
            }
            if("java.lang.object".equals(lcType)) {
            	return expr.writeOut(bc,MODE_REF);
            }
            else if("java.util.date".equals(lcType)) {
                // First Arg
                rtn=expr.writeOut(bc,MODE_REF);
                if(rtn.equals(Types.DATE)) return Types.DATE;
                if(rtn.equals(Types.DATE_TIME)) return Types.DATE_TIME;

                // Second Arg
                adapter.loadArg(0);
                adapter.invokeVirtual(Types.PAGE_CONTEXT,GET_CONFIG);
                adapter.invokeVirtual(Types.CONFIG_WEB,GET_TIMEZONE);
                adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_DATE[Types.getType(rtn)]);
                return Types.DATE_TIME;
            }
        break;
        case 'l':
            if("long".equals(type)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
                if(!rtn.equals(Types.LONG_VALUE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_LONG_VALUE[Types.getType(rtn)]);
                return Types.LONG_VALUE;
            }
            else if("long".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.LONG))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_LONG[Types.getType(rtn)]);
                return Types.LONG;
            }
        break;
        case 'n':
            if("node".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.NODE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_NODE);
                return Types.NODE;
            }
            else if("null".equals(lcType)) {
                expr.writeOut(bc,MODE_REF);
                adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_NULL);
                // TODO gibt es einen null typ?
                return Types.OBJECT;
            }
        break;
        case 'o':
            if("object".equals(lcType) || "other".equals(lcType)) {
                expr.writeOut(bc,MODE_REF);
                return Types.OBJECT;
            }
        break;
        case 's':
            if("struct".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.STRUCT))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_STRUCT);
                return Types.STRUCT;
            }
            if("short".equals(type)) {
            	rtn=expr.writeOut(bc,MODE_VALUE);
                if(!rtn.equals(Types.SHORT_VALUE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_SHORT_VALUE[Types.getType(rtn)]);
                return Types.SHORT_VALUE;
            }
            if("short".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.SHORT))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_SHORT[Types.getType(rtn)]);
                return Types.SHORT;
            }
            if("stringbuffer".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
            	if(!rtn.equals(Types.STRING_BUFFER))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_STRING_BUFFER);
                return Types.STRING_BUFFER;
            }
        break;

        case 'x':
            if("xml".equals(lcType)) {
                rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.NODE))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_NODE);
                return Types.NODE;
            }
        break;
        default:
            if("query".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.QUERY))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_QUERY);
                return Types.QUERY;
            }
            else if("timespan".equals(lcType)) {
            	rtn=expr.writeOut(bc,MODE_REF);
                if(!rtn.equals(Types.TIMESPAN))
                	adapter.invokeStatic(Types.CASTER,Methods_Caster.TO_TIMESPAN);
                return Types.TIMESPAN;
            }
        }
        Type t=getType(type);
        
        expr.writeOut(bc,MODE_REF);
        adapter.checkCast(t);
        return t;
    }

    /*
    private int writeExprx(BytecodeContext bc) throws TemplateException {
    	return Types.getType(expr.writeOut(bc,MODE_VALUE));
	}*/

	

	public static Type getType(String type) throws BytecodeException {
		if(StringUtil.isEmpty(type)) return Types.OBJECT;
    	
    	
    	String lcType=StringUtil.toLowerCase(type);
    	switch(lcType.charAt(0)) {
    	case 'a':
    		if("any".equals(lcType)) 							return Types.OBJECT;
    		if("array".equals(lcType)) 							return Types.ARRAY;
    	break;
    	case 'b':
    		if("bool".equals(lcType) || "boolean".equals(type))	return Types.BOOLEAN_VALUE;
            if("boolean".equals(lcType)) 						return Types.BOOLEAN;
            if("base64".equals(lcType)) 						return Types.STRING;
            
            if("binary".equals(lcType))							return Types.BYTE_VALUE_ARRAY;
            if("byte".equals(type))								return Types.BYTE_VALUE;
            if("byte".equals(lcType))							return Types.BYTE;
            
    	break;
    	
    	case 'c':
            if("char".equals(lcType)) 							return Types.CHAR;
            if("character".equals(lcType))						return Types.CHARACTER;
            if("collection".equals(lcType))						return Types.COLLECTION;
            if("component".equals(lcType))						return Types.COMPONENT;
    	break;
    	
        case 'd':
            if("double".equals(type))							return Types.DOUBLE_VALUE;
            if("double".equals(lcType))							return Types.DOUBLE;
            
            if("date".equals(lcType) || "datetime".equals(lcType))return Types.DATE_TIME;
            if("decimal".equals(lcType))						return Types.STRING;
            
        break;
        
        case 'f':
        	if("file".equals(lcType)) 							return Types.FILE;
            if("float".equals(type)) 							return Types.FLOAT_VALUE;
            if("float".equals(lcType))							return Types.FLOAT;
        break;

        case 'i':
            if("int".equals(lcType)) 							return Types.INT_VALUE;
            if("integer".equals(lcType))						return Types.INTEGER;
        break;

        case 'l':
            if("long".equals(type))								return Types.LONG_VALUE;
            if("long".equals(lcType))							return Types.LONG;
        break;
        case 'n':
            if("node".equals(lcType))							return Types.NODE;
            if("null".equals(lcType))							return Types.OBJECT;
            if("number".equals(lcType))							return Types.DOUBLE_VALUE;
            if("numeric".equals(lcType))						return Types.DOUBLE_VALUE;
        break;
        case 's':
            if("string".equals(lcType))							return Types.STRING;
            if("struct".equals(lcType))							return Types.STRUCT;
            if("short".equals(type))							return Types.SHORT_VALUE;
            if("short".equals(lcType))							return Types.SHORT;
        break;
        case 'o':
        	if("other".equals(lcType)) 							return Types.OBJECT;
        	if("object".equals(lcType)) 						return Types.OBJECT;
        break;
        case 'u':
        	if("uuid".equals(lcType)) 							return Types.STRING;
        break;
        case 'q':
        	if("query".equals(lcType)) 							return Types.QUERY;
        break;
        case 't':
        	if("timespan".equals(lcType))						return Types.TIMESPAN;
        break;
        case 'v':
        	if("variablename".equals(lcType))					return Types.STRING;
        	if("variable_name".equals(lcType))					return Types.STRING;
        	if("variablestring".equals(lcType))					return Types.STRING;
        	if("variable_string".equals(lcType))				return Types.STRING;
        	if("void".equals(lcType))							return Types.VOID;
        break;
        case 'x':
        	if("xml".equals(lcType))							return Types.NODE;
        break;
    	}
        try {
			return Type.getType(ClassUtil.loadClass(type));
		} 
        catch (ClassException e) {
			throw new BytecodeException(e.getMessage(),-1);
		}
		
	}

	/**
	 * @return the expr
	 */
	public Expression getExpr() {
		return expr;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}




