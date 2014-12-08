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
<cfscript>
component extends="org.railo.cfml.test.RailoTestCase"	{
	

	public void function testSuperByCurrent(){
		savecontent variable="local.c" {
			new JIra2902.Test().testDirect();
		}
		assertEquals("{before:test.cfc}{before:abs.cfc}{absabs.cfc}{after:abs.cfc}{after:test.cfc}",c);
	}

	public void function testSuperByBase(){
		savecontent variable="local.c" {
			new JIra2902.Test().testIndirect();
		}
		assertEquals("{before:test.cfc}{before:abs.cfc}{absabs.cfc}{after:abs.cfc}{after:test.cfc}",c);
	}
} 
</cfscript>