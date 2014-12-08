/**
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package railo.runtime.functions.struct;

import railo.runtime.PageContext;
import railo.runtime.exp.FunctionException;
import railo.runtime.exp.PageException;
import railo.runtime.functions.BIF;
import railo.runtime.functions.closure.Some;
import railo.runtime.op.Caster;
import railo.runtime.type.Struct;
import railo.runtime.type.UDF;

public class StructSome extends BIF {

	private static final long serialVersionUID = -7449844630816343951L;

	public static boolean call(PageContext pc , Struct sct, UDF udf) throws PageException {
		return _call(pc, sct, udf, false, 20);
	}
	public static boolean call(PageContext pc , Struct sct, UDF udf, boolean parallel) throws PageException {
		return _call(pc, sct, udf, parallel, 20);
	}

	public static boolean call(PageContext pc , Struct sct, UDF udf, boolean parallel, double maxThreads) throws PageException {
		return _call(pc, sct, udf, parallel, (int)maxThreads);
	}
	private static boolean _call(PageContext pc , Struct sct, UDF udf, boolean parallel, int maxThreads) throws PageException {
		return Some._call(pc, sct, udf, parallel, maxThreads);
	}

	@Override
	public Object invoke(PageContext pc, Object[] args) throws PageException {
		if(args.length==2)
			return call(pc, Caster.toStruct(args[0]), Caster.toFunction(args[1]));
		if(args.length==3)
			return call(pc, Caster.toStruct(args[0]), Caster.toFunction(args[1]), Caster.toBooleanValue(args[2]));
		if(args.length==4)
			return call(pc, Caster.toStruct(args[0]), Caster.toFunction(args[1]), Caster.toBooleanValue(args[2]), Caster.toDoubleValue(args[3]));
		
		throw new FunctionException(pc,"StructSome",2,4,args.length);
		
	}

}