<!--- 
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
 ---><cfcomponent extends="org.railo.cfml.test.RailoTestCase">
	<!---
	<cffunction name="beforeTests"></cffunction>
	<cffunction name="afterTests"></cffunction>
	<cffunction name="setUp"></cffunction>
	--->
	<cffunction name="testXMLNodes">
		<cfxml variable="local.MyDoc">
		<MyDoc>this is a test<test>test 2</test>
		<myTag>
		<tagTest>tag</tagTest>
		</myTag>
		</MyDoc>
		</cfxml>

		<cfset assertEquals(5,arrayLen(MyDoc.Mydoc.xmlNodes))>

		<cfset assertEquals('x?xml version="1.0" encoding="utf-8"?>this is a test',replace(trim(MyDoc.Mydoc.xmlNodes[1]&" "),'<','x','all'))>
		<cfset assertEquals("this is a test",MyDoc.Mydoc.xmlNodes[1].xmlText)>

	</cffunction>

	<cffunction name="testXMLChildren">
		<cfxml variable="local.MyDoc">
		<MyDoc>this is a test<test>test 2</test>
		<myTag>
		<tagTest>tag</tagTest>
		</myTag>
		</MyDoc>
		</cfxml>

		<cfset assertEquals(2,arrayLen(MyDoc.Mydoc.xmlChildren))>

		<cfset assertEquals('x?xml version="1.0" encoding="utf-8"?>xtest>test 2x/test>',replace(trim(MyDoc.Mydoc.xmlChildren[1]&" "),'<','x','all'))>

	</cffunction>

</cfcomponent>