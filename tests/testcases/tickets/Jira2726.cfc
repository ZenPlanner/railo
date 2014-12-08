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
	
	//public function beforeTests(){}
	
	//public function afterTests(){}
	
	//public function setUp(){}

	public void function testCallStackGetStaticNames(){
		var res=a();
		loop from ="1" to="#res.len()-1#" index="local.i" {
		     assertEquals(true,len(res[i].function)>0);	
		}
	}
	
	private array function a(){return b();}
	private array function b(){return c();}
	private array function c(){return CallStackGet();}
	
} 
</cfscript>