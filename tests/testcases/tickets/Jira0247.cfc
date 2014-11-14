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

	public function setUp(){
	}

	public void function test(){
		local.second = createobject( "component", "Jira0247.second");
		assertEquals("default",second.check());

		second.init();
		assertEquals("inited",second.check());

		createObject( "component", "Jira0247.second" );
		assertEquals("inited",second.check());
		
		sct=getmetaData(second.check);
		assertEquals("check",sct.name);
	}
} 
</cfscript>