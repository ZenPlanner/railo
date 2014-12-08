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
package railo.runtime.type.it;

import java.util.Enumeration;
import java.util.Iterator;

import railo.runtime.type.Collection;

public class ItAsEnum implements Enumeration<String> {
	
	private Iterator<Collection.Key> it;

	private ItAsEnum(Iterator<Collection.Key> it){
		this.it=it;
	}

	@Override
	public boolean hasMoreElements() {
		return it.hasNext();
	}

	@Override
	public String nextElement() {
		return it.next().getString();
	}
	
	public static Enumeration<String> toStringEnumeration(Iterator<Collection.Key> it){
		return new ItAsEnum(it);
	}

}